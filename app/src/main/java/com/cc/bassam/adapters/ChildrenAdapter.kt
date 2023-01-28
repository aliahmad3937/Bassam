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
import com.cc.bassam.databinding.ItemEduBinding
import com.cc.bassam.databinding.ItemHomeBinding
import com.cc.bassam.databinding.ItemTreeBinding
import com.cc.bassam.models.FamilyMember
import com.cc.bassam.models.MemberDetail
import com.cc.bassam.repositry.NetworkRepository
import com.cc.bassam.utils.MemberDetailData
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject



class ChildrenAdapter(
    private var mList: ArrayList<MemberDetail.Children>,
    val context: Context,
    var id:Int,
) : RecyclerView.Adapter<ChildrenAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTreeBinding.inflate(
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



   inner class ViewHolder(val mBinding: ItemTreeBinding) :
        RecyclerView.ViewHolder(mBinding.root) {


        fun bind(model: MemberDetail.Children) {
            if(model.id == id){
                mBinding.imageView24.visibility = View.VISIBLE
            }else{
                mBinding.imageView24.visibility = View.GONE
            }
            mBinding.textView8.text = model.name

            Glide.with(context)
                .load(model.profilePictureSquare)
                .error(R.drawable.listing_placeholder_3x)
                .placeholder(R.drawable.node1_3x)
                .into(mBinding.imageView21)



        }

    }

}

