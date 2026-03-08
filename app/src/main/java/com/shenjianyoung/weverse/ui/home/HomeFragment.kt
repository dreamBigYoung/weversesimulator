package com.shenjianyoung.weverse.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shenjianyoung.weverse.MyApplication
import com.shenjianyoung.weverse.R
import com.shenjianyoung.weverse.data.repository.MemberRepository
import com.shenjianyoung.weverse.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val TAG = "HomeFragment"

    private var __binding: FragmentHomeBinding? = null
    private val binding get() = __binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private val adapter = MemberAdapter { id, name, avatar ->
        val apply = Bundle().apply {
            putLong("member_id", id)
            putString("member_name", name)
            putString("member_avatar", avatar)
        }
        findNavController().navigate(R.id.action_home_to_chatroom, apply)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        __binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addMember.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_add_member)
        }
        binding.rvMemberList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMemberList.adapter = adapter

        viewModel.memberList.observe(viewLifecycleOwner) {
            adapter.resetList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchMemberList()
    }

    override fun onDestroy() {
        super.onDestroy()
        __binding = null
    }
}