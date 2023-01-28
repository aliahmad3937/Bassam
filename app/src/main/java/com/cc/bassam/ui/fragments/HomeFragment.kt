package com.cc.bassam.ui.fragments


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cc.bassam.R
import com.cc.bassam.adapters.HomeAdapter
import com.cc.bassam.databinding.FragmentHomeBinding
import com.cc.bassam.models.APIResponse
import com.cc.bassam.models.FamilyMember
import com.cc.bassam.repositry.NetworkRepository
import com.cc.bassam.utils.MemberDetailData
import com.cc.bassam.utils.isNetworkConnected
import com.cc.bassam.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var mAdapter: HomeAdapter? = null
    var membersList: ArrayList<FamilyMember.Member>? = null

    @Inject
    lateinit var networkRepository: NetworkRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )


        setUpRecyclerView()
        observeData()
        getData()
        setUpWatcher()
        setUpListeners()


       var a:A =A()
    //    a.show()

        return binding.root
    }

    private fun getData() {
        if (viewModel.mList.isEmpty()) {
            if (requireActivity().isNetworkConnected()) {
                binding.noNetwork.visibility = View.GONE
                viewModel.getMembersList()
            } else {
                binding.noNetwork.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpListeners() {
        binding.noNetwork.setOnClickListener {
            getData()
        }
        binding.imageView3.setOnClickListener {
            viewModel.isWorthy = !viewModel.isWorthy
            if(viewModel.isWorthy){
                binding.imageView3.setImageDrawable(requireContext().getDrawable(R.drawable.worthyx))
            }else{
                binding.imageView3.setImageDrawable(requireContext().getDrawable(R.drawable.worthy))
            }
            filterGenderBaseRecords()
        }
        binding.imageView4.setOnClickListener {
            viewModel.isFemale = !viewModel.isFemale
            if(viewModel.isFemale){
                binding.imageView4.setImageDrawable(requireContext().getDrawable(R.drawable.empty_node_3x))
            }else{
                binding.imageView4.setImageDrawable(requireContext().getDrawable(R.drawable.female_new_3x))
            }
            filterGenderBaseRecords()
        }
        binding.imageView5.setOnClickListener {
            viewModel.isMale = !viewModel.isMale
            if(viewModel.isMale){
                binding.imageView5.setImageDrawable(requireContext().getDrawable(R.drawable.male_new_3x))
            }else{
                binding.imageView5.setImageDrawable(requireContext().getDrawable(R.drawable.listing_placeholder_3x))
            }
            filterGenderBaseRecords()
        }
    }

    private fun filterGenderBaseRecords() {
        membersList?.clear()

        if (viewModel.isWorthy && viewModel.isMale && viewModel.isFemale) {
            membersList?.addAll(viewModel.mList)
        } else {
            if (viewModel.isWorthy && viewModel.isMale) {
                Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                var filterList = viewModel.mList.filter { model ->
                    model.gender == "0" || model.is_worthy == "Yes"
                }
                if (filterList != null && filterList.size > 0) {
                    membersList?.addAll(filterList)
                }
            }
            else if (viewModel.isWorthy && viewModel.isFemale) {
                Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                var filterList = viewModel.mList.filter { model ->
                    model.gender == "1" || model.is_worthy == "Yes"
                }
                if (filterList != null && filterList.size > 0) {
                    membersList?.addAll(filterList)
                }
            }
            else if (viewModel.isFemale && viewModel.isMale) {
                Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                var filterList = viewModel.mList.filter { model ->
                    (model.gender == "0" &&  model.is_worthy == "No") || model.gender == "1"
                }
                if (filterList != null && filterList.size > 0) {
                    membersList?.addAll(filterList)
                }
            }
            else {
                if (viewModel.isWorthy) {
                    var filterList = viewModel.mList.filter { model ->

                        model.is_worthy == "Yes"
                    }
                    Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                    if (filterList != null && filterList.size > 0) {
                        membersList?.addAll(filterList)
                    }
                } else if (viewModel.isFemale) {
                    var filterList = viewModel.mList.filter { model ->
                        model.gender == "1"
                    }
                    Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                    if (filterList != null && filterList.size > 0) {
                        membersList?.addAll(filterList)
                    }
                } else if (viewModel.isMale) {
                    Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                    var filterList = viewModel.mList.filter { model ->
                        model.gender == "0" &&  model.is_worthy == "No"
                    }
                    if (filterList != null && filterList.size > 0) {
                        membersList?.addAll(filterList)
                    }
                }else{
                    membersList?.addAll(viewModel.mList)
                }

            }
        }


        mAdapter?.notifyDataSetChanged()
        binding.textView2.text = membersList?.size.toString()
    }

    private fun filterGenderBaseRecords(i: Int) {
        viewModel.genderBaseFilter = i
        membersList?.clear()
        when (i) {
            1 -> {
                Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                var filterList = viewModel.mList.filter { model ->
                    model.is_worthy == "Yes"
                }
                Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                if (filterList != null && filterList.size > 0) {
                    membersList?.addAll(filterList)
                }
            }
            2 -> {
                var filterList = viewModel.mList.filter { model ->
                    model.gender == "1"
                }
                Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                if (filterList != null && filterList.size > 0) {
                    membersList?.addAll(filterList)
                }
            }
            3 -> {
                Log.v("TAG", "value :${viewModel.genderBaseFilter}")
                var filterList = viewModel.mList.filter { model ->
                    model.gender == "0"
                }
                if (filterList != null && filterList.size > 0) {
                    membersList?.addAll(filterList)
                }
            }
            else -> {
                membersList?.addAll(viewModel.mList)
            }
        }
        mAdapter?.notifyDataSetChanged()
        binding.textView2.text = membersList?.size.toString()
    }


    private fun filterRecords(query: String?) {
        membersList?.clear()
        if (query != null && query.isNotEmpty()) {

            var filterList = viewModel.mList.filter { model ->
                val name =
                    "${model.name} ${model.father_name} ${model.grand_father_name} ${model.g_grand_father_name}"
                name.toLowerCase().contains(query)
            }
            if (filterList != null && filterList.size > 0) {
                binding.recycler.visibility = View.VISIBLE
                binding.noData.visibility = View.GONE
                membersList?.addAll(filterList)
            } else {
                binding.recycler.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
            }
        } else {
            binding.recycler.visibility = View.VISIBLE
            binding.noData.visibility = View.GONE
            membersList?.addAll(viewModel.mList)
        }
        mAdapter?.notifyDataSetChanged()
        binding.textView2.text = membersList?.size.toString()
    }


    private fun setUpWatcher() {

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecords(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    private fun observeData() {
        viewModel.membersList.observe(viewLifecycleOwner) {
            when (it) {
                is APIResponse.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is APIResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    Log.v("TAG6", "${it.message}")

                }
                is APIResponse.Success<*> -> {
                    Log.v("TAG", "observer")
                    binding.progressBar.visibility = View.GONE
                   // MemberDetailData.getMemberDetail(viewModel.mList,networkRepository)
                    filterGenderBaseRecords()
                }
                else -> {}
            }
        }
    }

    private fun setUpRecyclerView() {
        if (membersList == null)
            membersList = arrayListOf()

        mAdapter = HomeAdapter(mList = membersList!!, context = requireContext(), networkRepository = networkRepository) {
        //   Toast.makeText(requireContext(),"$it ",Toast.LENGTH_SHORT).show()
            val action = HomeFragmentDirections.actionHomeFragmentToProfileDetailBlueFragment(it)
            findNavController().navigate(action)
        }
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycler.layoutManager = linearLayoutManager
        binding.recycler.adapter = mAdapter
    }
    open class A{
        constructor(
        ){
            Log.v("TAG","constructor A")
        }
        init {
            Log.v("TAG","init A")
            show()
        }
        open fun show(){
            Log.v("TAG","show A")
            add()
        }
        open fun add(){
            Log.v("TAG","add A")

        }
    }
    class B: A {
        constructor(
        ){
            Log.v("TAG","constructor B")

        }

        init {
            Log.v("TAG","init B")
            show()
        }

        override fun show() {
         Log.v("TAG","show B")
            super.show()
        }
        override fun add(){
            Log.v("TAG","add B")
        }
    }


}




