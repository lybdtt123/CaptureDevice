package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.LikeListener
import app.jietuqi.cn.ui.activity.OverallRemoveWatermarkActivity
import app.jietuqi.cn.ui.activity.OverallWebViewActivity
import app.jietuqi.cn.ui.activity.WechatSimulatorActivity
import app.jietuqi.cn.ui.entity.OverallDynamicEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.util.WechatTimeUtil
import app.jietuqi.cn.widget.ninegrid.NineGridView
import app.jietuqi.cn.widget.ninegrid.preview.NineGridViewClickAdapter
import com.sackcentury.shinebuttonlib.ShineButton
import com.youth.banner.Banner

/**
 * 作者： liuyuanbo on 2018/10/24 15:46.
 * 时间： 2018/10/24 15:46
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class HomeAdapter(val mList: ArrayList<OverallDynamicEntity>, val mLikeListener: LikeListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE1 = 0
    private val TYPE2 = 1
    private val MAX_LINE_COUNT = 3//最大显示行数
    private val STATE_UNKNOW = -1//未知状态
    private val STATE_NOT_OVERFLOW = 1//文本行数小于最大可显示行数
    private val STATE_COLLAPSED = 2//折叠状态
    private val STATE_EXPANDED = 3//展开状态
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE1
            else -> TYPE2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE1 -> Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_home_1, parent, false))
            else -> Holder2(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_communicate, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE1 -> {
            }
            TYPE2 -> if (holder is Holder2) {
                var entity = mList[position - 1]
                val state = entity.showAllStatus
                if (state == STATE_UNKNOW) {//未知状态
                    holder.content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            //这个回掉会调用多次，获取完行数后记得注销监听
                            holder.content.viewTreeObserver.removeOnPreDrawListener(this)
                            //holder.content.getViewTreeObserver().addOnPreDrawListener(null);
                            //如果内容显示的行数大于最大显示行数
                            if (holder.content.lineCount > MAX_LINE_COUNT) {
                                holder.content.maxLines = MAX_LINE_COUNT//设置最大显示行数
                                holder.showAll.visibility = View.VISIBLE//显示“全文”
                                holder.showAll.text = "全文"
                                entity.showAllStatus = STATE_COLLAPSED//保存状态
                            } else {
                                holder.showAll.visibility = View.GONE
                                entity.showAllStatus = STATE_NOT_OVERFLOW//保存状态
                            }
                            return true
                        }
                    })
                    holder.content.maxLines = Integer.MAX_VALUE//设置文本的最大行数，为整数的最大数值
                } else {
                    //如果之前已经初始化过了，则使用保存的状态。
                    when (state) {
                        STATE_NOT_OVERFLOW -> {
                            holder.showAll.visibility = View.GONE
                        }
                        STATE_COLLAPSED -> {
                            holder.content.maxLines = MAX_LINE_COUNT
                            holder.showAll.visibility = View.VISIBLE
                            holder.showAll.text = "全文"
                        }
                        STATE_EXPANDED -> {
                            holder.content.maxLines = Integer.MAX_VALUE
                            holder.showAll.visibility = View.VISIBLE
                            holder.showAll.text = "收起"
                        }
                    }
                }
                holder.bind(mList[position - 1])
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size + 1
    }

    internal inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.wechatFunLayout -> {
                    LaunchUtil.launch(itemView.context, WechatSimulatorActivity::class.java)
                }
                R.id.removeWaterMarkFunLayout -> {
                    LaunchUtil.launch(itemView.context, OverallRemoveWatermarkActivity::class.java)
                }
                R.id.kuaiDiChaXunFunLayout -> {
                    LaunchUtil.launch(itemView.context, OverallWebViewActivity::class.java)
                }
            }
        }

        private var banner: Banner = itemView.findViewById(R.id.mBanner)
//        private var tabLayout: TabLayout = itemView.findViewById(R.id.mTabLayout)

        init {
            itemView.findViewById<LinearLayout>(R.id.wechatFunLayout).setOnClickListener(this)
            itemView.findViewById<LinearLayout>(R.id.removeWaterMarkFunLayout).setOnClickListener(this)
            itemView.findViewById<LinearLayout>(R.id.kuaiDiChaXunFunLayout).setOnClickListener(this)
//            tabLayout.addTab(tabLayout.newTab().setText("最新"))
//            tabLayout.addTab(tabLayout.newTab().setText("最热"))
//            tabLayout.addTab(tabLayout.newTab().setText("已购买"))

        }

        fun bind() {

        }
    }

    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var entity = mList[adapterPosition - 1]
            when (v?.id) {
                R.id.overallCommunicateShowAllContentTv -> {
                    val status = entity.showAllStatus
                    if (status == STATE_COLLAPSED) {
                        content.maxLines = Integer.MAX_VALUE
                        showAll.text = "收起"
                        entity.showAllStatus = STATE_EXPANDED
                    } else if (status == STATE_EXPANDED) {
                        content.maxLines = MAX_LINE_COUNT
                        showAll.text = "全文"
                        entity.showAllStatus = STATE_COLLAPSED
                    }
                }
                R.id.overallCommunicateLikeBtn -> {
                    if (UserOperateUtil.isCurrentLoginDirectlyLogin(itemView.context)){
                        mLikeListener.like(entity)
                    }
                }
                else -> {
                    entity.position = adapterPosition - 1
                    LaunchUtil.startOverallCommunicateDetailsActivity(itemView.context, entity)
                }
            }
        }

        var avatar: ImageView = itemView.findViewById(R.id.overallCommunicateAvatarIv)
        private var nickName: TextView = itemView.findViewById(R.id.overallCommunicateNickNameTv)
        var content: TextView = itemView.findViewById(R.id.overallCommunicateContentTv)
        var showAll: TextView = itemView.findViewById(R.id.overallCommunicateShowAllContentTv)//展开/全文按钮
        var pics: NineGridView = itemView.findViewById(R.id.overallCommunicateNineGridView)
        var createTime: TextView = itemView.findViewById(R.id.overallCommunicateTimeTv)//创建时间
        var likeCount: TextView = itemView.findViewById(R.id.overallCommunicateLikeCountTv)//点赞人数
        var pingLunCount: TextView = itemView.findViewById(R.id.overallCommunicatePingLunTv)//评论人数
        private var likeBtn: ShineButton = itemView.findViewById(R.id.overallCommunicateLikeBtn)//点赞按钮

        init {
            likeBtn.setOnClickListener(this)
            showAll.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        fun bind(entity: OverallDynamicEntity) {
            GlideUtil.displayHead(itemView.context, entity.headimgurl, avatar)
            nickName.text = entity.nickname
            content.text = entity.content
            if (null != entity.infoList && entity.infoList.size != 0){
                pics.visibility = View.VISIBLE
                pics.setAdapter(NineGridViewClickAdapter(itemView.context, entity.infoList))
            }else{
                pics.visibility = View.GONE
            }
            likeCount.text = entity.favour.toString()
            pingLunCount.text = entity.comment_number.toString()
            createTime.text = WechatTimeUtil.getStandardDate(entity.create_time * 1000)
            if (entity.is_favour == 1) {
                likeBtn.setChecked(true, false)
            } else {
                likeBtn.setChecked(false, false)
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

        when (holder.itemViewType) {
            TYPE2 -> if (holder is Holder2) {//不添加该判断会导致数据重复
                GlideUtil.clearViews(holder.avatar.context, holder.avatar)
                holder.avatar.setImageBitmap(null)
            }
            else -> {
            }
        }
    }
}
