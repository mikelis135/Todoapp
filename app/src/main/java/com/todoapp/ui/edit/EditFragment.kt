package com.todoapp.ui.edit

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.todoapp.R
import com.todoapp.app.App
import com.todoapp.database.entity.Task
import com.todoapp.helper.Keys
import com.todoapp.helper.Tools
import kotlinx.android.synthetic.main.edit_fragment.view.*
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

class EditFragment : Fragment() {

    @Inject
    lateinit var editViewModel: EditViewModel

    private var tasks: Task? = null
    var newTask: Boolean = false
    var savedImagePath: String = ""
    var taskId: String = UUID.randomUUID().toString()

    private lateinit var rootView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        arguments?.let {
            newTask = it.getBoolean(Keys.NEW, false)
            it.getParcelable<Task>(Keys.TASK)?.let { tasks ->
                this.tasks = tasks
                taskId = tasks.id
                savedImagePath = tasks.image
            }
        }

        super.onCreate(savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.delete -> {

                val dialogItems = arrayOf("Don't", "Yes, delete")
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete?")
                builder.setItems(dialogItems) { dialog, option ->
                    when (option) {
                        0 -> dialog.dismiss()
                        1 -> {
                            editViewModel.deleteTask(taskId)
                            activity?.let { Tools.goBack(it) }
                        }
                    }
                }.show()

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.edit_fragment, container, false)

        setHasOptionsMenu(true)

        setupNavigation()

        rootView.insertImageImg.setOnClickListener {
            pickImage()
        }

        if (!newTask) {

            tasks?.let {
                rootView.urlTxt.setText(it.url)
                rootView.contentTxt.setText(it.content)
                rootView.contentTxt.setSelection(it.content.length)

                Glide.with(requireContext()).asBitmap().placeholder(R.drawable.placeholder)
                    .load(File(it.image)).into(rootView.image)
                rootView.image.visibility = View.VISIBLE

                rootView.image.setOnClickListener {
                    pickImage()
                }

            }
        }

        return rootView
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.select_picture)),
            3
        )
    }

    override fun onPause() {

        activity?.let { Tools.closeKeyboard(it) }

        try {
            saveTask()
        } catch (e: Exception) {
            Log.e("okh", e.message.toString())
        }
        super.onPause()
    }

    private fun saveTask() {

        val url = rootView.urlTxt.text.toString()
        val content = rootView.contentTxt.text.toString()

        if (validated()) {
            if (newTask) {
                val task = Task(taskId, content, url, savedImagePath)
                editViewModel.insertTask(task)

            } else {

                val image = if (savedImagePath.isNotBlank()) {
                    savedImagePath
                } else {
                    tasks?.image ?: ""
                }

                val task = Task(taskId, content, url, image)
                editViewModel.updateTask(task)

            }
        } else {
            Toast.makeText(
                requireContext(), getString(R.string.add_url_and_content),
                Toast.LENGTH_LONG
            ).show()
        }


    }

    private fun validated(): Boolean {
        return when {
            rootView.contentTxt.text.isBlank() -> {
                false
            }
            rootView.urlTxt.text.isBlank() -> {
                false
            }
            else -> true
        }
    }

    private fun setupNavigation() {
        activity?.let {

            it.findViewById<Toolbar>(R.id.toolbar)?.apply {
                setNavigationIcon(R.drawable.ic_back)
                title = ""
                setNavigationOnClickListener {
                    activity?.let { Tools.goBack(it) }
                }
            }

            it.let { Tools.onFinishWithMenu(it, this) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        data.let {

            if (requestCode == 3) {

                if (resultCode == Activity.RESULT_OK) {

                    val cursor = it?.data?.let { it1 -> getCursor(it1) }

                    val imageName = getFileName(cursor).replace(" ", "")
                    val imagePath = getFilePath(cursor)
                    val imageExt = imageName.substring((imageName.lastIndexOf(".")))

                    cursor?.close()

                    try {
                        val bmp = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            it?.data
                        )
                        val savedImagePath = Tools.saveImage(
                            bmp,
                            requireContext().getExternalFilesDir(null)?.absolutePath + Keys.TASK_DIR + "/" + taskId,
                            imageName,
                            imageExt,
                            imagePath
                        )

                        Glide.with(requireContext()).asBitmap().placeholder(R.drawable.placeholder)
                            .load(bmp).into(rootView.image)

                        this.savedImagePath = savedImagePath

                    } catch (ignored: IOException) {

                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.could_not_get_picture,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), R.string.could_not_get_that, Toast.LENGTH_LONG)
                    .show()
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getFileName(cursor: Cursor?): String {

        var name = ""
        cursor?.let {
            val columnIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
            try {
                it.moveToFirst()
                name = it.getString(columnIndex)

            } catch (e: java.lang.Exception) {
                Log.d("okh", e.message.toString())
            }
        }
        return name
    }

    private fun getFilePath(cursor: Cursor?): String {

        var imagePath = ""

        cursor?.let {
            try {
                val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                it.moveToFirst()
                imagePath = it.getString(columnIndex)

            } catch (e: java.lang.Exception) {

                Log.d("okh", e.message.toString())

            }
        }

        return imagePath
    }

    private fun getCursor(uri: Uri): Cursor? {

        var cursor: Cursor? = null
        try {
            cursor = requireContext().contentResolver.query(uri, null, null, null, null)

        } catch (ignore: Exception) {

        }
        return cursor
    }

}