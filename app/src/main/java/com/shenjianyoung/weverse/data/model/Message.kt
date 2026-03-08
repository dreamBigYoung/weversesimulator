package com.shenjianyoung.weverse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.cert.CertPath

@Entity(tableName = "message")
data class Message(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val memberId: Long,

    val originalText: String,

    val translatedText: String,

    val imgPath: String,

    val timestamp: String?

)
