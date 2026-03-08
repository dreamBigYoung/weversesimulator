package com.shenjianyoung.weverse

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.shenjianyoung.weverse.db.dao.MemberDao
import com.shenjianyoung.weverse.db.dao.MessageDao
import com.shenjianyoung.weverse.db.database.AppDatabase

class MyApplication : Application() {
    companion object {
        lateinit var context: Context
            private set

        lateinit var db: AppDatabase
            private set

        lateinit var memberDao: MemberDao
            private set

        lateinit var messageDao: MessageDao
            private set
    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weverse_db"
        ).build()

        memberDao = db.memberDao()

        messageDao = db.messageDao()
    }
}