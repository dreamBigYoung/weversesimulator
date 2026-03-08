package com.shenjianyoung.weverse.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object IMGUtils {

    suspend fun saveUriToInternalWebP(
        context: Context,
        uri: Uri,
        fileName: String
    ): String? {

        return withContext(Dispatchers.IO) {

            try {

                // Glide 同步获取 Bitmap
                val bitmap: Bitmap = Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .submit()
                    .get()

                val dir = File(context.filesDir, "memberimgs")

                if (!dir.exists()) {
                    dir.mkdirs()
                }

                val file = File(dir, "$fileName.webp")

                val out = FileOutputStream(file)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    bitmap.compress(
                        Bitmap.CompressFormat.WEBP_LOSSY,
                        85,
                        out
                    )
                } else {
                    @Suppress("DEPRECATION")
                    bitmap.compress(
                        Bitmap.CompressFormat.WEBP,
                        85,
                        out
                    )
                }

                out.flush()
                out.close()

                Log.d("IMGUtils", "保存成功: ${file.absolutePath}")

                file.absolutePath

            } catch (e: Exception) {

                Log.e("IMGUtils", "保存失败", e)
                null

            }

        }

    }
}