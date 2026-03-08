package com.shenjianyoung.weverse.ui.chatroom

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.shenjianyoung.weverse.R
import com.shenjianyoung.weverse.data.model.Message


class MessageAdapter(
    private val avatar: String,
    private val onItemLongClick: (Long, Long, String, String, String, String) -> Unit
) : RecyclerView.Adapter<MessageAdapter.VH>() {
    private val msgList = mutableListOf<Message>()

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvOri = view.findViewById<TextView>(R.id.tv_original)
        val tvTrans = view.findViewById<TextView>(R.id.tv_translated)
        val tvTime = view.findViewById<TextView>(R.id.tv_time)
        val ivAvatarImage = view.findViewById<ImageView>(R.id.ivAvatarImage)
        val msgLayout = view.findViewById<LinearLayout>(R.id.layout_msg)
        val textLayout = view.findViewById<LinearLayout>(R.id.layout_text)
        val ivMsg = view.findViewById<ImageView>(R.id.iv_msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_msg, parent, false)
        return VH(inflate)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val msg = msgList.get(position)
        Glide.with(holder.ivAvatarImage)
            .load(avatar)
            .circleCrop() // 核心：直接裁剪为圆形
            .placeholder(R.drawable.bg_circle) // 占位图
            .into(holder.ivAvatarImage)

        holder.tvTime.text = ""
        var hourStr = ""
        var minuteStr = ""
        if (msg.timestamp != null && msg.timestamp.isNotEmpty()) {
            val split = msg.timestamp.split("_")
            if (split.size > 1) {
                holder.tvTime.text = "${split[0]}：${split[1]}"
                hourStr = split[0]
                minuteStr = split[1]
            }
        }

        if (msg.imgPath != null && msg.imgPath.isNotEmpty()) {
            holder.ivMsg.isVisible = true
            holder.textLayout.isVisible = false
            Log.i("imgPath", msg.imgPath)
            Glide.with(holder.ivMsg)
                .load(msg.imgPath)
                .transform(CenterCrop(), RoundedCorners(16 * 3)) // 先裁剪中心，再切圆角
                .into(holder.ivMsg)

            holder.msgLayout.setOnLongClickListener(null)

        } else {
            holder.ivMsg.isVisible = false
            holder.textLayout.isVisible = true

            holder.tvOri.setText(msg.originalText)
            holder.tvTrans.setText(msg.translatedText)

            holder.msgLayout.setOnLongClickListener {
                onItemLongClick(
                    msg.id,
                    msg.memberId,
                    msg.originalText,
                    msg.translatedText,
                    hourStr,
                    minuteStr
                )
                true
            }
        }

    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    fun addLastestMsg(msg: Message) {
        msgList.add(msg)
        notifyDataSetChanged()
    }

    fun modifyMsg(msg: Message) {
        val find = msgList.find {
            it.id == msg.id
        }
        val indexOfFirst = msgList.indexOfFirst {
            it.id == msg.id
        }
        msgList.removeAt(indexOfFirst)
        msgList.add(indexOfFirst, msg)

        notifyItemChanged(indexOfFirst)
    }

    fun resetDataList(list: List<Message>) {
        msgList.clear()
        msgList.addAll(list)
        notifyDataSetChanged()
    }

    fun addNextPageMsg(list: List<Message>) {
        msgList.addAll(list)
        notifyDataSetChanged()
    }
}