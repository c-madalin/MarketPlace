package com.example.reuseit.Application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.R
import com.example.reuseit.Adapters.PostAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reuseit.DatabaseInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.reuseit.Application.Database.Entity.PostEntity
import com.example.reuseit.Application.Database.DAO.PostDAO

class ApplicationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_application, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.postRecyclerView)
        val addPostButton = view.findViewById<Button>(R.id.addPostButton)
        val httpRequest = view.findViewById<Button>(R.id.httpRequest)
        httpRequest.setOnClickListener {
            findNavController().navigate(R.id.action_applicationFragment_to_requestsFragment)
        }




        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            val posts = withContext(Dispatchers.IO) {
                DatabaseInstance.Access.postDAO().getAllPosts()
            }
            recyclerView.adapter = PostAdapter(posts, requireActivity())
        }

        addPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_applicationFragment_to_addPostFragment)
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApplicationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
