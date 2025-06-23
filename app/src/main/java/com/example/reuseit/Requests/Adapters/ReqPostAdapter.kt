package com.example.reuseit.Requests.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reuseit.Adapters.PostAdapter
import com.example.reuseit.R
import com.example.reuseit.Requests.Models.ReqPostModel

class ReqPostAdapter(private val posts: List<ReqPostModel>)
    :RecyclerView.Adapter<ReqPostAdapter.PostViewHolder>(){

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView =    itemView.findViewById(R.id.textUserName)
        val address: TextView =     itemView.findViewById(R.id.textAdress)
        val title: TextView =       itemView.findViewById(R.id.textTitle)
        val message: TextView =     itemView.findViewById(R.id.textBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.req_post_layout, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.userName.text = post.userName
        holder.address.text = post.userAddress
        holder.title.text = post.postTitle
        holder.message.text = post.postMessage
    }

    override fun getItemCount(): Int = posts.size
}