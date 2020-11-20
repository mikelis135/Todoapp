package com.todoapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.todoapp.R
import com.todoapp.app.App
import com.todoapp.database.entity.Task
import com.todoapp.helper.ItemClickListener
import com.todoapp.helper.Keys
import com.todoapp.helper.Tools
import com.todoapp.ui.edit.EditFragment
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var rootView: View
    private var appDelegate: Int = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setUpRecyclerView()
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.home_fragment, container, false)

        rootView.taskRecycler.adapter = homeAdapter
        activity?.findViewById<Toolbar>(R.id.toolbar)?.apply {
            title = getString(R.string.app_name)
        }

        rootView.newTaskFab.setOnClickListener {

            val editFragment = EditFragment()

            val bundle = Bundle()
            bundle.putBoolean(Keys.NEW, true)

            editFragment.arguments = bundle
            activity?.let { Tools.gotoFragment(it, editFragment) }
        }

        return rootView
    }

    private fun setUpRecyclerView() {

        activity?.let {

            homeAdapter = HomeAdapter(it.application, mutableListOf(), object : ItemClickListener {

                override fun <T> onItemClick(item: T, position: Int) {

                    val tasks = item as Task

                    val bundle = Bundle()
                    bundle.putParcelable(Keys.TASK, tasks)

                    val editFragment = EditFragment()
                    editFragment.arguments = bundle

                    Tools.gotoFragment(it, editFragment)

                }

                override fun onItemLongClick(v: View, position: Int) {

                }
            })

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        homeViewModel.todoLiveData.observe(this, Observer {

            it?.let {
                homeAdapter.setItems(it)
                homeViewModel.checkEmpty(it)
            }
        })

        homeViewModel.emptyTaskLiveData.observe(this, Observer {

            it?.let {
                when (it) {
                    TRUE -> {
                        rootView.taskRecycler.visibility = View.GONE
                        rootView.newTaskTxt.visibility = View.VISIBLE
                    }
                    FALSE -> {
                        rootView.newTaskTxt.visibility = View.GONE
                        rootView.taskRecycler.visibility = View.VISIBLE
                    }
                }

            }
        })

        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.theme -> {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

            }
        }

        return super.onOptionsItemSelected(item)
    }

}