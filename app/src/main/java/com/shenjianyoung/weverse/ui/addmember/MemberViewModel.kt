package com.shenjianyoung.weverse.ui.addmember

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shenjianyoung.weverse.MyApplication
import com.shenjianyoung.weverse.MyApplication.Companion.context
import com.shenjianyoung.weverse.data.model.Member
import com.shenjianyoung.weverse.data.repository.MemberRepository
import com.shenjianyoung.weverse.utils.IMGUtils
import kotlinx.coroutines.launch

class MemberViewModel(

) : ViewModel() {
    private val repository = MemberRepository(MyApplication.memberDao)

    val avatarPath = MutableLiveData<String>()

    val addResult = MutableLiveData<Boolean>()

    fun addAvatar(uri: Uri, memberName: String) {
        viewModelScope.launch {

            val path = IMGUtils.saveUriToInternalWebP(
                context,
                uri,
                "member_${memberName}"
            )

            if (path != null) {
                avatarPath.postValue(path!!)
            }

        }
    }

    fun addMember(name: String, avatar: String) {

        viewModelScope.launch {
            repository.addMember(name, avatar)
            addResult.postValue(true)
        }

    }

}
