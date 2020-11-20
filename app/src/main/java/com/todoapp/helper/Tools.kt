package com.todoapp.helper

import android.content.Context
import android.graphics.Bitmap
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.todoapp.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object Tools {

    fun onFinishWithMenu(activity: FragmentActivity?, fragment: Fragment) {
        activity?.onBackPressedDispatcher?.addCallback(
            fragment,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBack(activity)
                }
            })
    }

    fun saveImage(
        bitmap: Bitmap,
        fileName: String,
        fileTitle: String,
        imageExt: String = "",
        imagePath: String = ""
    ): String {

        val myDir = File(fileName)
        if (!myDir.exists()) {
            myDir.mkdirs()
        }

        val resultFile = File(myDir, fileTitle)
        if (resultFile.exists()) {
            resultFile.delete()
        }

        val fileOutputStream = FileOutputStream(resultFile)

        try {
            if (imageExt.contains(".jpeg", true) or imageExt.contains(
                    ".jpg",
                    true
                ) or imageExt.contains(".png", true) or imageExt.isEmpty()
            ) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
                copy(FileInputStream(File(imagePath)), fileOutputStream)
            } else if (imageExt.contains(".gif", true) and !imagePath.isBlank()) {
                copy(FileInputStream(File(imagePath)), fileOutputStream)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return resultFile.absolutePath
    }

    private fun copy(src: FileInputStream, dst: FileOutputStream) {

        try {
            val buf = ByteArray(1024)
            var len: Int
            while (src.read(buf).also { len = it } > 0) {
                dst.write(buf, 0, len)
            }
            dst.flush()
            dst.close()

        } catch (e: Exception) {

        }

    }


    fun goBack(activity: FragmentActivity) {
        activity.supportFragmentManager.popBackStack()
        activity.findViewById<Toolbar>(R.id.toolbar)?.navigationIcon = null
    }

    fun closeKeyboard(activity: FragmentActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    fun gotoFragment(activity: FragmentActivity, fragment: Fragment, tag: String = "") {
        activity.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.container_body, fragment, tag).addToBackStack("")
            .commit()
    }

    fun gotoFragmentOnly(activity: FragmentActivity, fragment: Fragment, tag: String = "") {
        activity.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.container_body, fragment, tag)
            .commit()
    }

}
