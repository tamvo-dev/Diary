package com.example.diary.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diary.R
import com.example.diary.models.Post
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.time.Period
import java.util.*
import java.util.concurrent.TimeUnit

class PostAdapter : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    private val posts = mutableListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bindItem(post = posts[position])

    inner class MyViewHolder(val item : View) : RecyclerView.ViewHolder(item){

        val postDateLable = item.findViewById<TextView>(R.id.post_date_label)
        val postTimeLable = item.findViewById<TextView>(R.id.post_time_label)
        val postTitle = item.findViewById<TextView>(R.id.post_title)
        val postContent = item.findViewById<TextView>(R.id.post_content)
        val postContainer = item.findViewById<MaterialCardView>(R.id.post_card_container)

        fun bindItem(post : Post){
            postTitle.setText(post.title)
            postContent.setText(post.content)
            postContainer.setCardBackgroundColor(post.background)
            val date = Date(post.timestamp)

            val simpleDateFormat = SimpleDateFormat("HH:mm\na")
            postTimeLable.setText(simpleDateFormat.format(date))

            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis - date.time
            val day = TimeUnit.MILLISECONDS.toDays(now)
            Log.e("TAG", calendar.get(Calendar.DAY_OF_MONTH).toString())
            postDateLable.setText(day.toString() + " days ago")
        }
    }

    fun addPost(post: Post){
        posts.add(post)
        notifyDataSetChanged()
    }

    fun addPosts(posts : List<Post>){
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    fun deletePost(position : Int){
        posts.removeAt(position)
        notifyDataSetChanged()
    }
}