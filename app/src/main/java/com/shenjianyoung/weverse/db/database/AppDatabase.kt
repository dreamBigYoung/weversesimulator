package com.shenjianyoung.weverse.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shenjianyoung.weverse.data.model.Member
import com.shenjianyoung.weverse.data.model.Message
import com.shenjianyoung.weverse.db.dao.MemberDao
import com.shenjianyoung.weverse.db.dao.MessageDao

@Database(
    entities = [Member::class, Message::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao

    abstract fun messageDao(): MessageDao

}