package com.shenjianyoung.weverse.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shenjianyoung.weverse.MyApplication
import com.shenjianyoung.weverse.data.model.Member
import com.shenjianyoung.weverse.data.repository.MemberRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = MemberRepository(MyApplication.memberDao)

    val memberList = MutableLiveData<List<Member>>()

    fun fetchMemberList() {
        viewModelScope.launch {
            val members = repository.getMembers()
            memberList.postValue(members)
        }
    }
}