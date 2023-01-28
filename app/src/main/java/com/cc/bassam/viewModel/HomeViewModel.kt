package com.cc.bassam.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc.bassam.models.APIResponse
import com.cc.bassam.models.FamilyMember
import com.cc.bassam.repositry.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NetworkRepository,
) : ViewModel() {

    private val _membersList = MutableLiveData<APIResponse>()
    val membersList: LiveData<APIResponse> = _membersList

    val mList: ArrayList<FamilyMember.Member> = arrayListOf()
    var genderBaseFilter: Int = 0
    var isWorthy = false
    var isMale = false
    var isFemale = false


    fun getMembersList() {
        _membersList.postValue(APIResponse.Loading)
        viewModelScope.launch {
            val response = repository.getMembersList()
            if (response.isSuccessful) {
                mList.addAll(response.body()!!.Data)
                _membersList.postValue(APIResponse.Success(response.body()))
            } else {
                _membersList.postValue(APIResponse.Error("Error : ${response.message()} "))
            }
        }
    }



}