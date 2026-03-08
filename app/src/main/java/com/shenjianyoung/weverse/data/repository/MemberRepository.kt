package com.shenjianyoung.weverse.data.repository

import com.shenjianyoung.weverse.data.model.Member
import com.shenjianyoung.weverse.db.dao.MemberDao

class MemberRepository(private val dao: MemberDao) {

    suspend fun addMember(name: String, avatar: String) {
        dao.insert(
            Member(
                name = name,
                avatar = avatar
            )
        )
    }

    suspend fun getMembers(): List<Member> {
        return dao.getAll()
    }

    suspend fun deleteMember(member: Member) {
        dao.delete(member)
    }

    suspend fun updateMember(member: Member) {
        dao.update(member)
    }

    suspend fun clickMember(id: Long) {
        dao.increaseClick(id)
    }
}