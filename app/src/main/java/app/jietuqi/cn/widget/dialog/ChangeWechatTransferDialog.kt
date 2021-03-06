package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R

import kotlinx.android.synthetic.main.dialog_change_wechat_transfer.*

/**
 * 作者： liuyuanbo on 2018/10/15 13:40.
 * 时间： 2018/10/15 13:40
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class ChangeWechatTransferDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private var mItemSelectionListener: OnItemSelectListener? = null
    override fun onClick(v: View) {
        mItemSelectionListener?.click(v.tag.toString())
        dismiss()
    }

    fun setOnItemSelectListener(itemSelection: OnItemSelectListener){
        mItemSelectionListener = itemSelection
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_change_wechat_transfer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMoney.setOnClickListener(this)
        notGetMoney.setOnClickListener(this)
        giveMoneyBack.setOnClickListener(this)
        cancel.setOnClickListener(this)
    }

    interface OnItemSelectListener{
        fun click(type: String)
    }
}
