package com.shenjianyoung.weverse.db.dao

import androidx.room.*
import com.shenjianyoung.weverse.data.model.Member

@Dao
interface MemberDao {

    // 新增
    @Insert
    suspend fun insert(member: Member): Long

    // 批量新增
    @Insert
    suspend fun insertAll(list: List<Member>)

    // 查询全部
    @Query("SELECT * FROM member ORDER BY clickCount DESC")
    suspend fun getAll(): List<Member>

    // 根据ID查询
    @Query("SELECT * FROM member WHERE id = :id")
    suspend fun getById(id: Long): Member?

    // 更新
    @Update
    suspend fun update(member: Member)

    // 删除
    @Delete
    suspend fun delete(member: Member)

    // 删除全部
    @Query("DELETE FROM member")
    suspend fun deleteAll()

    // 点击次数+1
    @Query("UPDATE member SET clickCount = clickCount + 1 WHERE id = :id")
    suspend fun increaseClick(id: Long)
}