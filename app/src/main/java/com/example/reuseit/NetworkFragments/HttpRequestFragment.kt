package com.example.reuseit.NetworkFragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.reuseit.Network.HttpService
import com.example.reuseit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HttpRequestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_http_requests, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnGet = view.findViewById<Button>(R.id.btnGet)
        val btnPost = view.findViewById<Button>(R.id.btnPost)
        val tvResult = view.findViewById<TextView>(R.id.tvResult)

        btnGet.setOnClickListener {
            lifecycleScope.launch {
                val result = withContext(Dispatchers.IO) { HttpService.performGetRequest() }
                tvResult.text = "GET Response:\n$result"
            }
        }

        btnPost.setOnClickListener {
            lifecycleScope.launch {
                val result = withContext(Dispatchers.IO) { HttpService.performPostRequest() }
                tvResult.text = "POST Response:\n$result"
            }
        }
    }
}
