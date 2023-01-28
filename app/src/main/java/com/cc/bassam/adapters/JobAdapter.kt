package com.cc.bassam.adapters


import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cc.bassam.R
import com.cc.bassam.databinding.ItemHomeBinding
import com.cc.bassam.databinding.ItemJobBinding
import com.cc.bassam.models.FamilyMember
import com.cc.bassam.models.MemberDetail
import com.cc.bassam.repositry.NetworkRepository
import com.cc.bassam.utils.MemberDetailData
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject



class JobAdapter(
    private var mList: ArrayList<MemberDetail.Workplaces>,
    val context: Context,
) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemJobBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])

    }

    override fun getItemCount(): Int {
        return mList.size
    }



   inner class ViewHolder(val mBinding: ItemJobBinding) :
        RecyclerView.ViewHolder(mBinding.root) {


        fun bind(model: MemberDetail.Workplaces) {
             mBinding.textView12.text = model.jobTitle
             mBinding.textView13.text = model.location
        }

    }

}

