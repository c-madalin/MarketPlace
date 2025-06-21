package com.example.reuseit.Adapters
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reuseit.R
import com.example.reuseit.Application.Database.Entity.PostEntity
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(private val posts: List<PostEntity>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val ivPostImage: ImageView = view.findViewById(R.id.ivPostImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.tvUserName.text = "Posted by: ${post.userName}"

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


    }
}
