package com.example.diary.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary.R
import com.example.diary.models.Post
import com.example.diary.ui.post.PostActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    companion object{
        val CREATE_ACTION = "create"
    }

    private lateinit var adapter: PostAdapter
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var mReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        adapter = PostAdapter()
        main_recyclerView.adapter = adapter
        main_recyclerView.layoutManager = LinearLayoutManager(this)

        main_swipeRefresh.setOnRefreshListener {

        }


        auth = FirebaseAuth.getInstance()
        mReference = FirebaseDatabase.getInstance().reference.child("posts")
        mReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@MainActivity, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                scope.launch {
                    val task = async(Dispatchers.IO){
                        try {
                            val posts = mutableListOf<Post>()
                            for (item in p0.children){
                                val post = item.getValue(Post::class.java)
                                if (post != null) {
                                    posts.add(post)
                                }
                            }
                            Result<List<Post>>(posts, null)
                        }catch (t: Throwable){
                            Result(null, t)
                        }
                    }
                    val result = task.await()
                    if (result.success != null){
                        adapter.addPosts(result.success)
                    } else if(result.error != null){
                        Log.e("TAG", result.error.message)
                    }
                }
            }

        })

        main_floatButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.action = CREATE_ACTION
            startActivity(intent)
        }
    }

    fun loadData(p0: DataSnapshot) = scope.launch {
        main_swipeRefresh.isRefreshing = false
    }
}
