package com.cc.bassam.repositry


import com.cc.bassam.retrofit.ApiService
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val retrofitService: ApiService) {

    suspend fun getMembersList() = retrofitService.getMembersList()


    suspend fun getMemberDetail(id:Int) = retrofitService.getMemberDetail(id)

}