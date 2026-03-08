package com.shenjianyoung.weverse.db.dao

import androidx.room.*
import com.shenjianyoung.weverse.data.model.Message

@Dao
interface MessageDao {

    // 新增
    @Insert
    suspend fun insert(message: Message): Long

    // 批量新增
    @Insert
    suspend fun insertAll(list: List<Message>)

    // 查询全部
    @Query("SELECT * FROM message ORDER BY id DESC")
    suspend fun getAll(): List<Message>

    // 根据ID查询
    @Query("SELECT * FROM message WHERE memberId = :memberId")
    suspend fun getByMemberId(memberId: Long): List<Message>

    // 更新
    @Update
    suspend fun update(message: Message)

    // 删除
    @Delete
    suspend fun delete(message: Message)

    // 根据ID删除
    @Query("DELETE FROM message WHERE id = :id")
    suspend fun deleteById(id: Long)

    // 删除全部
    @Query("DELETE FROM message")
    suspend fun deleteAll()
}