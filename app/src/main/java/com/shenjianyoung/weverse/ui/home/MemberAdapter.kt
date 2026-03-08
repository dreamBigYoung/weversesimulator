package com.shenjianyoung.weverse.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shenjianyoung.weverse.R
import com.shenjianyoung.weverse.data.model.Member

class MemberAdapter(private val onItemClick: (Long, String, String) -> Unit) :
    RecyclerView.Adapter<MemberAdapter.VH>() {
    val memberList = mutableListOf<Member>()

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatarImage)
        val tvMember = view.findViewById<TextView>(R.id.tv_member)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val member = memberList.get(position)
        holder.tvMember.text = member.name
        Glide.with(holder.ivAvatar)
            .load(member.avatar)
            .skipMemoryCache(true) // 跳过内存缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 跳过磁盘缓存
            .circleCrop() // 核心：直接裁剪为圆形
            .placeholder(R.drawable.bg_circle) // 占位图
            .into(holder.ivAvatar)

        holder.itemView.setOnClickListener() {
            onItemClick(member.id, member.name, member.avatar)
        }
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    fun resetList(mbs: List<Member>) {
        memberList.clear()
        memberList.addAll(mbs)
        notifyDataSetChanged()
    }
}