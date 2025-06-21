package com.example.reuseit.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reuseit.Application.Database.Entity.CommentEntity
import com.example.reuseit.R

class CommentAdapter(private val comments: List<CommentEntity>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.commentContent)
        val user: TextView = view.findViewById(R.id.commentUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.content.text = comment.content
        holder.user.text = comment.userName
    }

    override fun getItemCount(): Int = comments.size
}
