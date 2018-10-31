package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.provider.ContactsContract
import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.constant.RequestCode
import dasheng.com.capturedevice.database.table.WechatSingleTalkEntity
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.util.*
import dasheng.com.capturedevice.widget.dialog.ChangeRoleDialog
import kotlinx.android.synthetic.main.activity_wechat_role.*
import kotlinx.android.synthetic.main.activity_wechat_singlechat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * 作者： liuyuanbo on 2018/10/18 14:24.
 * 时间： 2018/10/18 14:24
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 修改角色
 */

class WechatRoleActivity : BaseWechatActivity() {
    private var mEntity: WechatUserTable = WechatUserTable()
    override fun setLayoutResourceId() = R.layout.activity_wechat_role

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setWechatViewTitle("修改角色", 1)
        EventBusUtil.register(this)
    }

    override fun initViewsListener() {
        mAvatarLayout.setOnClickListener(this)
        mNickNameLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAvatarLayout ->{
                callAlbum(1, true)
            }
            R.id.mNickNameLayout ->{
                ChangeRoleDialog().show(supportFragmentManager, "changeRole")
            }
            R.id.sureTv ->{
                var intent: Intent = Intent()
                intent.putExtra(IntentKey.ENTITY, mEntity)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    mEntity.avatar = mFinalCropFile
                    GlideUtil.display(this, mFinalCropFile, mAvatarIv)
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(nickname: String) {
        mSenderNameTv.text = nickname
        mEntity.wechatUserNickName = nickname
    }

    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}