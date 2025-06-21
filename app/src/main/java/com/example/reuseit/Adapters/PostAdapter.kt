package com.example.reuseit.Adapters


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reuseit.Adapters.CommentAdapter
import com.example.reuseit.Application.Database.Entity.CommentEntity
import com.example.reuseit.Application.Database.Entity.PostEntity
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.DatabaseInstance
import com.example.reuseit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(
    private val posts: List<PostEntity>,
    private val activity: FragmentActivity
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val ivPostImage: ImageView = view.findViewById(R.id.ivPostImage)
        val etComment: EditText = view.findViewById(R.id.etAddComment)
        val btnSendComment: Button = view.findViewById(R.id.btnSendComment)
        val rvComments: RecyclerView = view.findViewById(R.id.commentRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        holder.tvUserName.text = post.userName
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        holder.tvTimestamp.text = sdf.format(Date(post.timestamp))
        holder.tvTitle.text = post.title

        if (post.imagePath.isNotBlank()) {
            val bitmap = BitmapFactory.decodeFile(post.imagePath)
            holder.ivPostImage.setImageBitmap(bitmap)
            holder.ivPostImage.visibility = View.VISIBLE
        } else {
            holder.ivPostImage.setImageDrawable(null)
            holder.ivPostImage.visibility = View.GONE
        }

        // Load comments
        holder.rvComments.layoutManager = LinearLayoutManager(holder.itemView.context)
        activity.lifecycleScope.launch {
            val comments = withContext(Dispatchers.IO) {
                DatabaseInstance.Access.commentDAO().getCommentsForPost(post.postId)
            }
            holder.rvComments.adapter = CommentAdapter(comments)
        }

        // Add comment
        holder.btnSendComment.setOnClickListener {
            val text = holder.etComment.text.toString()
            if (text.isNotBlank()) {
                val comment = CommentEntity(
                    postId = post.postId,
                    userId = CurrentUser.Data.UserID,
                    userName = "${CurrentUser.Data.FirstName} ${CurrentUser.Data.LastName}",
                    content = text
                )

                activity.lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        DatabaseInstance.Access.commentDAO().insert(comment)
                    }
                    withContext(Dispatchers.Main) {
                        holder.etComment.text.clear()
                        val updatedComments = withContext(Dispatchers.IO) {
                            DatabaseInstance.Access.commentDAO().getCommentsForPost(post.postId)
                        }
                        holder.rvComments.adapter = CommentAdapter(updatedComments)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = posts.size
}