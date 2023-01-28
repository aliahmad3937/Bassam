package com.cc.bassam.retrofit

import com.cc.bassam.models.FamilyMember
import com.cc.bassam.models.MemberDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("familymembers/v2")
    suspend fun getMembersList(): Response<FamilyMember>

    @GET("familymembers/{member_id}")
    suspend fun getMemberDetail(@Path(value = "member_id", encoded = true) id:Int): Response<MemberDetail>


}