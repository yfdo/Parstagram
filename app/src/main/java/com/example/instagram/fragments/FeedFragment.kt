package com.example.instagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.instagram.MainActivity
import com.example.instagram.Post
import com.example.instagram.PostAdapter
import com.example.instagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout

    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up views and click listeners
        postsRecyclerView = view.findViewById(R.id.postRecyclerView)
        swipeContainer = view.findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            Log.i(TAG, "swipe listener")
            queryPosts()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );


        // populate recycler view
        // 1. Create layout for each row in list (item_post.xml)
        // 2. Create data source for each row (Post class)
        // 3. Create adapter that will bridge data and row layout (PostAdapter)
        // 4. Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        postsRecyclerView.adapter = adapter

        // 5. Set layout manager on RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }


    open fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        //Query for all posts in our server
        query.include(Post.KEY_USER)//also give back the user information
        // return posts in descending order, newer posts appear first on top
        query.addDescendingOrder("createdAt")
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    //something went wrong
                    Log.e(TAG, "Error fetching posts")
                } else {
                    //successful
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(
                                MainActivity.TAG,
                                "Post: " + post.getDescription() + ", username: " + post.getUser()?.username
                            )
                        }
                        adapter.clear()
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "FeedFragment"
    }

}