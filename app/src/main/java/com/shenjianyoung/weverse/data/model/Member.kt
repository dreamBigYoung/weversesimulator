package com.shenjianyoung.weverse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val avatar: String, // 本地路径
    val clickCount: Long = 0 // 点击次数
)
