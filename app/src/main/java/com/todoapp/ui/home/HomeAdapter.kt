package com.todoapp.ui.home

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.todoapp.R
import com.todoapp.app.App
import com.todoapp.database.entity.Task
import com.todoapp.helper.ItemClickListener
import java.io.File
import javax.inject.Inject

class HomeAdapter(
    private val application: Application,
    private var tasks: List<Task>,
    private var itemClickListener: ItemClickListener
) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {


    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        (application as App).appComponent.inject(this)

        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        val pvh = MyViewHolder(itemView)

        itemView.setOnClickListener { v -> itemClickListener.onItemClick(v, pvh.adapterPosition) }

        return pvh
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = tasks[position]

        holder.summary.text = task.content
        holder.url.text = task.url

        if (task.image.isNotEmpty()) {
            Glide.with(application.baseContext).asBitmap().placeholder(R.drawable.placeholder)
                .load(File(task.image))
                .into(holder.thumbnail)
        } else {
            Glide.with(application.baseContext).asBitmap().placeholder(R.drawable.placeholder)
                .load(R.drawable.placeholder)
                .into(holder.thumbnail)
        }

        try {
            holder.taskLayout.setOnClickListener { itemClickListener.onItemClick(task, position) }
        } catch (ignore: Exception) {
        }

    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setItems(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val summary: TextView = view.findViewById(R.id.content)
        val url: TextView = view.findViewById(R.id.url)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        val taskLayout: ConstraintLayout = view.findViewById(R.id.taskLayout)
    }

}
