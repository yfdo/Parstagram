package com.example.instagram.fragments

import android.util.Log
import com.example.instagram.MainActivity
import com.example.instagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : FeedFragment() {

    override fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Query for all posts in our server
        query.include(Post.KEY_USER)//also give back the user information
        // return posts in descending order, newer posts appear first on top
        query.addDescendingOrder("createdAt")
        // only include posts from the assigned user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())

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

                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}