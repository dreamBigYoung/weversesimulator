package com.shenjianyoung.weverse.ui.chatroom

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shenjianyoung.weverse.R
import com.shenjianyoung.weverse.databinding.FragmentChatroomBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatRoomFragment : Fragment(R.layout.fragment_chatroom) {
    private val TAG = "ChatRoomFragment"
    private var __binding: FragmentChatroomBinding? = null
    private val binding get() = __binding!!

    private val viewModel by viewModels<ChatViewModel>()

    private var member_id: Long? = -1L
    private var member_name: String? = ""

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

            uri?.let {
                val absolutePath = it.toString()
                var timeStr = getTimeStr()
                viewModel.addMessage(
                    oriMsg = "",
                    transMsg = "",
                    timeStr = timeStr,
                    memberId = member_id!!,
                    imgPath = absolutePath
                )
            }

        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        __binding = FragmentChatroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        member_id = arguments?.getLong("member_id")
        member_name = arguments?.getString("member_name")
        val member_avatar = arguments?.getString("member_avatar")

        val adapter =
            MessageAdapter(member_avatar!!) { msg_id, member_id, oriMsg, transMsg, hourStr, minuteStr ->


                showCustomPop(
                    anchorView = binding.rvChatList.rootView,
                    msg_id = msg_id,
                    member_id = member_id,
                    oriStr = oriMsg,
                    tranStr = transMsg,
                    hourStr = hourStr,
                    minuteStr = minuteStr
                )
            }

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.stackFromEnd = true
        binding.rvChatList.layoutManager = linearLayoutManager

        binding.rvChatList.adapter = adapter

        binding.layoutWork.layoutWorkArea.isVisible = false

        viewModel.latestMsg.observe(viewLifecycleOwner) {
            adapter.addLastestMsg(it)
        }

        viewModel.modifyMsg.observe(viewLifecycleOwner) {
            adapter.modifyMsg(it)
        }

        viewModel.messageList.observe(viewLifecycleOwner) {
            adapter.resetDataList(it)
        }

        binding.layoutWork.btnImg.setOnClickListener() {
            pickImageLauncher.launch("image/*")
        }

        binding.btnWorking.setOnClickListener() {
            binding.layoutWork.layoutWorkArea.isVisible = true
        }
        binding.btnCancle.setOnClickListener {
            binding.layoutWork.layoutWorkArea.isVisible = false
        }
        binding.btnClearAll.setOnClickListener() {
            binding.layoutWork.etHours.setText("")
            binding.layoutWork.etMinutes.setText("")
            binding.layoutWork.etOriginal.setText("")
            binding.layoutWork.etTranslate.setText("")
        }

        binding.layoutWork.btnEnter.setOnClickListener() {
            val originalText = binding.layoutWork.etOriginal.text.toString().trim()
            val transText = binding.layoutWork.etTranslate.text.toString().trim()
            if (originalText.isEmpty() && transText.isEmpty()) {
                Toast.makeText(requireContext(), "原文或者译文至少输入一项", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            var timeStr = getTimeStr()
            viewModel.addMessage(
                oriMsg = originalText,
                transMsg = transText,
                timeStr = timeStr,
                memberId = member_id!!,
                imgPath = ""
            )
        }

        binding.btnScreen.setOnClickListener() {
            lifecycleScope.launch {

                val bitmap = captureRecyclerView(binding.rvChatList)

                val path = saveBitmapToGallery(
                    requireContext(),
                    bitmap,
                    "chat_${System.currentTimeMillis()}"
                )

                Log.d("screenshot", "保存路径: $path")
                Toast.makeText(requireContext(), "保存路径: Pictures/WeverseShots", Toast.LENGTH_SHORT).show()

            }

        }

        viewModel.fetchMessage(member_id!!)
    }

    private fun getTimeStr(): String {
        val hourStr = binding.layoutWork.etHours.text.toString().trim()
        val minutesStr = binding.layoutWork.etMinutes.text.toString().trim()
        var timeStr = ""
        if (hourStr.isNotEmpty() && minutesStr.isNotEmpty()) {
            timeStr = "${hourStr}_${minutesStr}"
        }
        return timeStr
    }

    override fun onResume() {
        super.onResume()
    }


    fun captureRecyclerView(rv: RecyclerView): Bitmap {

        val bitmap = Bitmap.createBitmap(
            rv.width,
            rv.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        rv.draw(canvas)

        return bitmap
    }

    suspend fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        fileName: String
    ): Uri? {

        return withContext(Dispatchers.IO) {

            val resolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/WeverseShots")
            }

            val uri = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let {

                val out = resolver.openOutputStream(it)

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)

                out?.flush()
                out?.close()

            }

            uri

        }
    }


    fun showCustomPop(
        anchorView: View?,
        msg_id: Long,
        member_id: Long,
        oriStr: String,
        tranStr: String,
        hourStr: String,
        minuteStr: String
    ) {
        // 1. 加载布局
        val contentView: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.popwin_custom_layout, null)

        // 2. 创建 PopupWindow 实例
        // 参数：View, 宽, 高, 是否获取焦点
        val popupWindow = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, true
        )

        // 3. 设置属性
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = true // 点击外部消失
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 必须设置背景，外部点击才有效
        // 背景变暗
        setBackgroundAlpha(0.5f)

        popupWindow.setOnDismissListener {
            // 恢复亮度
            setBackgroundAlpha(1f)
        }

        // 设置动画
        // popupWindow.setAnimationStyle(R.style.PopupAnimation);

        // 4. 处理布局内的点击事件
//        contentView.findViewById<View>(R.id.btn_action).setOnClickListener { v: View? ->
//            // 执行逻辑
//            popupWindow.dismiss()
//        }
        val etOri = contentView.findViewById<EditText>(R.id.et_original)
        val etTrans = contentView.findViewById<EditText>(R.id.et_translate)
        val etHour = contentView.findViewById<EditText>(R.id.et_hours)
        val etMinute = contentView.findViewById<EditText>(R.id.et_minutes)
        val btEdit = contentView.findViewById<Button>(R.id.btn_enter)
        etOri.setText(oriStr)
        etTrans.setText(tranStr)
        etHour.setText(hourStr)
        etMinute.setText(minuteStr)
        btEdit.setText("修改")
        btEdit.setOnClickListener() {
            val originalLatest = etOri.text.toString().trim()
            val transLatest = etTrans.text.toString().trim()
            if (originalLatest.isEmpty() && transLatest.isEmpty()) {
                Toast.makeText(requireContext(), "原文或者译文至少输入一项", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val hourStr = etHour.text.toString().trim()
            val minutesStr = etMinute.text.toString().trim()
            var timeStr = ""
            if (hourStr.isNotEmpty() && minutesStr.isNotEmpty()) {
                timeStr = "${hourStr}_${minutesStr}"
            }

            viewModel.updateMessage(
                msgId = msg_id,
                oriMsg = originalLatest,
                transMsg = transLatest,
                timeStr = timeStr,
                memberId = member_id,
                ""
            )
            popupWindow.dismiss()
        }

        // 5. 显示位置
        // 显示在屏幕中心：

        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        Log.i(TAG, anchorView.toString())
    }


    fun setBackgroundAlpha(alpha: Float) {
        val lp = requireActivity().window.attributes
        lp.alpha = alpha
        requireActivity().window.attributes = lp
    }

    override fun onDestroy() {
        super.onDestroy()
        __binding = null
    }

}