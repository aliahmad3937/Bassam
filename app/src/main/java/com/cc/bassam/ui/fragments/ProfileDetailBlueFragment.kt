package com.cc.bassam.ui.fragments

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cc.bassam.R
import com.cc.bassam.adapters.*
import com.cc.bassam.databinding.FragmentProfileDetailBlueBinding
import com.cc.bassam.databinding.SocialDialogLayoutBinding
import com.cc.bassam.models.APIResponse
import com.cc.bassam.models.MemberDetail
import com.cc.bassam.utils.MemberDetailData
import com.cc.bassam.utils.isNetworkConnected
import com.cc.bassam.utils.showToast
import com.cc.bassam.viewModel.ProfileDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileDetailBlueFragment : Fragment() {
    private val viewModel: ProfileDetailViewModel by viewModels()
    private lateinit var binding: FragmentProfileDetailBlueBinding
    private var model: MemberDetail.Detail? = null
    private lateinit var childrenAdapter: ChildrenAdapter
    private lateinit var siblingAdapter: SiblingAdapter


    // get the arguments from the Registration fragment
    private val args: ProfileDetailBlueFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile_detail_blue,
            container,
            false
        )

        observeData()
        //   getData()
        updateViews()
        setUpListeners()


        return binding.root
    }

    private fun getData() {
        if (viewModel.memberId != args.id) {
            if (requireActivity().isNetworkConnected()) {
                binding.noNetwork.visibility = View.GONE
                viewModel.getDetail(args.id)
            } else {
                binding.noNetwork.visibility = View.VISIBLE
            }
        } else {
            updateViews()
        }
    }


    private fun updateViews() {
        model = MemberDetailData.map.get(args.id)

        model?.let { model ->
            binding.textView6.text = model.nodeID!!.substring(0, 2)
            binding.textView5.text = model.nodeID!!.substring(2, model.nodeID!!.lastIndex + 1)
            binding.textView3.text =
                "${model.name}\n${model.fatherName} ${model.grandFatherName} ${model.gGrandFatherName}"
            binding.groupDob.visibility = if (model.dob!!.isEmpty()) View.GONE else View.VISIBLE
            binding.groupAddress.visibility =
                if (model.country!!.isEmpty() && model.city!!.isEmpty()) View.GONE else View.VISIBLE
            binding.groupDesc.visibility =
                if (model.briefDescription!!.isEmpty()) View.GONE else View.VISIBLE
            binding.textView10.text = model.dob
            binding.textView11.text = model.briefDescription
            binding.textView4.text = "${model.country}\n${model.city}"

            binding.recJob.visibility = if (model.workplaces.isEmpty()) View.GONE else View.VISIBLE
            binding.recEducation.visibility =
                if (model.education.isEmpty()) View.GONE else View.VISIBLE

            val jobAdapter = JobAdapter(mList = model.workplaces, context = requireContext())
            binding.recJob.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.recJob.adapter = jobAdapter

            val educationAdapter =
                EducationAdapter(mList = model.education, context = requireContext())
            binding.recEducation.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.recEducation.adapter = educationAdapter

            binding.imageView17.setImageDrawable(
                if (model.email!!.isEmpty()) requireContext().getDrawable(R.drawable.email_icon_3x) else requireContext().getDrawable(
                    R.drawable.email_icon_blue_3x
                )
            )

            binding.imageView15.setImageDrawable(
                if (model.twitter!!.isEmpty() && model.instagram!!.isEmpty() && model.snapchat!!.isEmpty()) requireContext().getDrawable(
                    R.drawable.social_3x
                ) else requireContext().getDrawable(R.drawable.socialiconblue_3x)
            )
            binding.imageView16.setImageDrawable(
                if (model.mobile!!.isEmpty()) requireContext().getDrawable(R.drawable.phone) else requireContext().getDrawable(
                    R.drawable.phone1
                )
            )

            Glide.with(requireContext())
                .load(model.profilePictureSquare)
                .error(R.drawable.listing_placeholder_3x)
                .placeholder(R.drawable.node1_3x)
                .into(binding.imageView12)

            if (model.children.isEmpty()) {
                binding.imageView18.visibility = View.GONE
            } else {
                binding.imageView18.visibility = View.VISIBLE
                if (model.children.size == 1) {
                    binding.layoutChildren.rec.visibility = View.GONE
                    binding.layoutChildren.textView7.text = model.children[0].name
                    Glide.with(requireContext())
                        .load(model.profilePictureSquare)
                        .error(R.drawable.listing_placeholder_3x)
                        .placeholder(R.drawable.node1_3x)
                        .into(binding.layoutChildren.imageView20)

                } else {
                    val list:ArrayList<MemberDetail.Children> = arrayListOf()
                    list.addAll(model.children.subList(1,model.children.size))

                    Glide.with(requireContext())
                        .load(model.siblings[0].profilePictureSquare)
                        .error(R.drawable.listing_placeholder_3x)
                        .placeholder(R.drawable.node1_3x)
                        .into(binding.layoutSibling.imageView20)
                    binding.layoutSibling.textView7.text = model.siblings[0].name
                    if(model.siblings[0].id == model.id){
                        binding.layoutSibling.imageView24.visibility = View.VISIBLE
                    }else{
                        binding.layoutSibling.imageView24.visibility = View.GONE
                    }

                    binding.layoutChildren.rec.visibility = View.VISIBLE
                    childrenAdapter =
                        ChildrenAdapter(mList =list, context = requireContext(),model.id!!)
                    binding.layoutChildren.rec.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.layoutChildren.rec.adapter = childrenAdapter
                }
            }

            if (model.siblings.isEmpty()) {
                binding.imageView19.visibility = View.GONE
            } else {
                binding.imageView19.visibility = View.VISIBLE
                if (model.siblings.size == 1) {
                    binding.layoutSibling.rec.visibility = View.GONE
                    binding.layoutSibling.textView7.text = model.siblings[0].name
                    Glide.with(requireContext())
                        .load(model.profilePictureSquare)
                        .error(R.drawable.listing_placeholder_3x)
                        .placeholder(R.drawable.node1_3x)
                        .into(binding.layoutSibling.imageView20)
                } else {
                       val list:ArrayList<MemberDetail.Siblings> = arrayListOf()
                    list.addAll(model.siblings.subList(1,model.siblings.size))
                    Glide.with(requireContext())
                        .load(model.siblings[0].profilePictureSquare)
                        .error(R.drawable.listing_placeholder_3x)
                        .placeholder(R.drawable.node1_3x)
                        .into(binding.layoutSibling.imageView20)
                    binding.layoutSibling.textView7.text = model.siblings[0].name
                    if(model.siblings[0].id == model.id){
                        binding.layoutSibling.imageView24.visibility = View.VISIBLE
                    }else{
                        binding.layoutSibling.imageView24.visibility = View.GONE
                    }


                    binding.layoutSibling.rec.visibility = View.VISIBLE
                    siblingAdapter =
                        SiblingAdapter(mList = list , context = requireContext(),model.id!!)
                    binding.layoutSibling.rec.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.layoutSibling.rec.adapter = siblingAdapter
                }
            }


        } ?: getData()


    }

    private fun setUpListeners() {
        binding.noNetwork.setOnClickListener {
            getData()
        }
        binding.imageView9.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageView15.setOnClickListener {
            if (model?.twitter!!.isNotEmpty() || model?.instagram!!.isNotEmpty() || model?.snapchat!!.isNotEmpty())
                openSocialDialogue()
        }
        binding.imageView16.setOnClickListener {
            if (model?.mobile!!.isNotEmpty()) {
                val u: Uri = Uri.parse("tel:" + model?.mobile)

                // Create the intent and set the data for the
                // intent as the phone number.

                // Create the intent and set the data for the
                // intent as the phone number.
                val i = Intent(Intent.ACTION_DIAL, u)

                try {
                    // Launch the Phone app's dialer with a phone
                    // number to dial a call.
                    startActivity(i)
                } catch (s: SecurityException) {
                    // show() method display the toast with
                    // exception message.
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        binding.imageView17.setOnClickListener {
            if (model?.email!!.isNotEmpty())
                sendEmail()
        }
        binding.imageView18.setOnClickListener {
            // children
            if (binding.imageView18.visibility == View.VISIBLE) {
                binding.layoutChildren.layoutTree.visibility =
                    if (binding.layoutChildren.layoutTree.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            binding.layoutSibling.layoutTree.visibility = View.GONE

        }
        binding.imageView19.setOnClickListener {
            // sibling

            binding.layoutSibling.layoutTree.visibility =
                if (binding.layoutSibling.layoutTree.visibility == View.VISIBLE) View.GONE else View.VISIBLE

            binding.layoutChildren.layoutTree.visibility = View.GONE
        }
    }

    private fun sendEmail() {
        Log.i("Send email", "")
        val TO = arrayOf(model?.email)
        //   val CC = arrayOf("")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.setPackage("com.google.android.gm");
        // emailIntent.putExtra(Intent.EXTRA_CC, CC)
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject")
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here")
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            //  requireActivity().finish()
            Log.i("Finished sending email...", "")
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "There is no email client installed.",
                Toast.LENGTH_SHORT
            ).show()
        }

//        try {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + model?.email))
//            intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject")
//            intent.putExtra(Intent.EXTRA_TEXT, "your_text")
//            startActivity(intent)
//        } catch (e: ActivityNotFoundException) {
//
//        }
    }

    private fun openSocialDialogue() {
        val dialog = Dialog(requireContext())
        val clipboard: ClipboardManager? =
            requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        var clip = ClipData.newPlainText("label", "Text to copy")
        val mBinding = SocialDialogLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(mBinding.root)

        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        // val height = (resources.displayMetrics.heightPixels * 0.90).toInt()
        //    dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)
        dialog.window!!.attributes.windowAnimations = R.style.animation

        mBinding.layoutTwitter.visibility =
            if (model?.twitter!!.isEmpty()) View.GONE else View.VISIBLE
        mBinding.layoutInsta.visibility =
            if (model?.instagram!!.isEmpty()) View.GONE else View.VISIBLE
        mBinding.layoutSnap.visibility =
            if (model?.snapchat!!.isEmpty()) View.GONE else View.VISIBLE

        mBinding.textView17.text = model?.twitter
        mBinding.textView20.text = model?.instagram
        mBinding.textView23.text = model?.snapchat


        mBinding.textView18.setOnClickListener {
            dialog.dismiss()
        }
        mBinding.textView19.setOnClickListener {
            clip = ClipData.newPlainText("label", model?.twitter)
            clipboard?.setPrimaryClip(clip)
            dialog.dismiss()
            requireContext().showToast("Copy to Clipboard Successfully!")
        }


        mBinding.textView21.setOnClickListener {
            dialog.dismiss()
        }
        mBinding.textView22.setOnClickListener {
            clip = ClipData.newPlainText("label", model?.instagram)
            clipboard?.setPrimaryClip(clip)
            dialog.dismiss()
            requireContext().showToast("Copy to Clipboard Successfully!")
        }


        mBinding.textView24.setOnClickListener {
            dialog.dismiss()
        }
        mBinding.textView25.setOnClickListener {
            clip = ClipData.newPlainText("label", model?.snapchat)
            clipboard?.setPrimaryClip(clip)
            dialog.dismiss()
            requireContext().showToast("Copy to Clipboard Successfully!")
        }



        dialog.show()
    }

    private fun observeData() {
        viewModel.memberDetail.observe(viewLifecycleOwner) {
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
                    updateViews()
                }
                else -> {}
            }
        }
    }

}