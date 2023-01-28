package com.cc.bassam.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc.bassam.models.APIResponse
import com.cc.bassam.models.FamilyMember
import com.cc.bassam.models.MemberDetail
import com.cc.bassam.repositry.NetworkRepository
import com.cc.bassam.utils.MemberDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val repository: NetworkRepository,
) : ViewModel() {

    private val _memberDetail = MutableLiveData<APIResponse>()
    val memberDetail: LiveData<APIResponse> = _memberDetail


    var detail: MemberDetail.Detail? = null
    var memberId: Int = 0


    fun getDetail(id:Int) {
        memberId = id
        _memberDetail.postValue(APIResponse.Loading)
        viewModelScope.launch {
            val response = repository.getMemberDetail(id)
            if (response.isSuccessful) {
                  detail = response.body()?.Data
                MemberDetailData.updateMap(id,response.body()?.Data!!)
                _memberDetail.postValue(APIResponse.Success(response.body()))
            } else {
                _memberDetail.postValue(APIResponse.Error("Error : ${response.message()} "))
            }
        }
    }


}