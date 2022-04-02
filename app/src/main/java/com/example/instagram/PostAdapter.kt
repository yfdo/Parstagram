package com.example.instagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(val context: Context, val posts: List<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        // specify layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPostUsername: TextView
        val ivPostImage: ImageView
        val tvPostContent: TextView

        init {
            tvPostUsername = itemView.findViewById(R.id.tvPostUsername)
            ivPostImage = itemView.findViewById(R.id.ivPostImage)
            tvPostContent = itemView.findViewById(R.id.tvPostContent)
        }

        fun bind(post: Post) {
            tvPostUsername.text = post.getUser()?.username
            tvPostContent.text = post.getDescription()

            // populate image with Glider
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivPostImage)
        }

    }
}