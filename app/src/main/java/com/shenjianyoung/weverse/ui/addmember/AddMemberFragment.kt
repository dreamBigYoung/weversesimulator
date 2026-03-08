package com.shenjianyoung.weverse.ui.addmember

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shenjianyoung.weverse.R
import com.shenjianyoung.weverse.databinding.FragmentAddMemberBinding
import com.shenjianyoung.weverse.utils.IMGUtils

class AddMemberFragment : Fragment(R.layout.fragment_add_member) {
    private val TAG = "AddMemberFragment"
    private var __binding: FragmentAddMemberBinding? = null
    private val binding get() = __binding!!

    private val viewModel by viewModels<MemberViewModel>()

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val memberName = binding.memberName.text.toString().trim()
            uri?.let {
                viewModel.addAvatar(uri, memberName)
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        __binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.avatarPath.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(it)
                    .skipMemoryCache(true) // 跳过内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 跳过磁盘缓存
                    .circleCrop() // 直接裁剪为圆形
                    .placeholder(R.drawable.bg_circle) // 占位图
                    .into(binding.ivAvatarImage)

            }
        }

        viewModel.addResult.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(requireContext(), "添加成功", Toast.LENGTH_SHORT).show()
            }
            findNavController().popBackStack()
        }

        viewModel.modifyResult.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(requireContext(), "修改成功", Toast.LENGTH_SHORT).show()
            }
            findNavController().popBackStack()
        }

        binding.btnAddImg.setOnClickListener {
            val memberName = binding.memberName.text.toString().trim()
            if (memberName.isEmpty()) {

                Toast.makeText(requireContext(), "请先输入成员姓名", Toast.LENGTH_SHORT).show()
            } else {
                pickImageLauncher.launch("image/*")
            }

        }
        binding.saveBtn.setOnClickListener {
            val memberName = binding.memberName.text.toString().trim()
            val path = viewModel.avatarPath.value
            if (memberName.isEmpty()) {
                Toast.makeText(requireContext(), "请先输入成员姓名", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (path == null || path.isEmpty()) {
                Toast.makeText(requireContext(), "请先选择成员头像", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addMember(name = memberName, avatar = path)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        __binding = null
    }

}