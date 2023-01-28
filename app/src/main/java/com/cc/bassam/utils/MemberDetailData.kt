package com.cc.bassam.utils

import android.util.Log
import com.cc.bassam.models.FamilyMember
import com.cc.bassam.models.MemberDetail
import com.cc.bassam.repositry.NetworkRepository
import kotlinx.coroutines.*
import javax.inject.Inject


object MemberDetailData {
//    fun getMemberDetail(
//        mList: ArrayList<FamilyMember.Member>,
//        networkRepository: NetworkRepository
//    ) {
//        for (i in mList) {
//            Log.v("MemberDetailData", i.id.toString())
//            GlobalScope.launch {
//                if (!map.containsKey(i.id)) {
//
//                    val response =
//                        withContext(Dispatchers.IO) { networkRepository.getMemberDetail(i.id) }
//                    if (response.isSuccessful)
//                        Log.v("MemberDetailData", "responsevc:${i.id}")
//                    map.put(i.id, response.body()?.Data!!)
//                }
//            }
//        }
//    }

    fun updateMap(id: Int, data: MemberDetail.Detail) {
        if (!map.containsKey(id)) {
            map.put(id, data)
        }
    }

    val map = mutableMapOf<Int, MemberDetail.Detail>()


    fun getMemberDetail(id: Int , networkRepository:NetworkRepository) {
        Log.v("MemberDetailData",id.toString())
        if (!map.containsKey(id)) {
           GlobalScope.launch {
                val response = withContext(Dispatchers.IO) { networkRepository.getMemberDetail(id) }
                if (response.isSuccessful)
                    Log.v("MemberDetailData","responsevc:$id")
                    map.put(id, response.body()?.Data!!)
            }
        }
    }

}