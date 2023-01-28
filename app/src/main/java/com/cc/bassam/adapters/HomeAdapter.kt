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
import com.cc.bassam.models.FamilyMember
import com.cc.bassam.repositry.NetworkRepository
import com.cc.bassam.utils.MemberDetailData
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject



class HomeAdapter(
    private var mList: ArrayList<FamilyMember.Member>,
    val context: Context,
    val networkRepository: NetworkRepository,
    val callBack: (Int) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ) { pos ->
            callBack(mList[pos].id)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])

    }

    override fun getItemCount(): Int {
        return mList.size
    }



   inner class ViewHolder(val mBinding: ItemHomeBinding, listener: (Int) -> Unit) :
        RecyclerView.ViewHolder(mBinding.root) {


        fun bind(model: FamilyMember.Member) {
            Log.v("HomeAdapter","posi :${model.id}")
            MemberDetailData.getMemberDetail(model.id,networkRepository)

            if(model.gender == "0"){
                mBinding.textView8.text = if(model.alive == "No") "رحمه الله" else ""
                mBinding.textView3.text = Html.fromHtml("<font color='#1870D5'>${model.name}</font> ${model.father_name} ${model.grand_father_name} ${model.g_grand_father_name}")
            }else{
                mBinding.textView8.text = if(model.alive == "No") "رحمها الله" else ""
                mBinding.textView3.text = Html.fromHtml("<font color='#BD10E0'>${model.name}</font> ${model.father_name} ${model.grand_father_name} ${model.g_grand_father_name}")
            }

            mBinding.textView4.text = model.nodeID
            var tex = model.nodeID
            val textToBeColored = model.nodeID.substring(0,2)
            val htmlText = tex.replaceFirst(textToBeColored,"<font color='#AD2641'>"+textToBeColored +"</font>")
            mBinding.textView4.text = Html.fromHtml(htmlText)


            mBinding.imageView14.visibility = if(model.is_worthy == "Yes") View.VISIBLE else View.GONE
            Glide.with(context)
                .load(model.profile_picture_square)
                .error(R.drawable.listing_placeholder_3x)
                .placeholder(R.drawable.node1_3x)
                .into(mBinding.imageView6)

        }

        init {
//            Log.v("HomeAdapter","pos :$adapterPosition")
            mBinding.root.setOnClickListener {
                listener(adapterPosition)
            }
        }
    }

}

