package com.example.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

/**
 * Let user create a post by taking a photo within their camera
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //1. setting the description of the post
        //2. a button to launch the camera to take a picture
        //3. an ImageView to show the picture the user has taken
        //4. a button to save and send the post to our Parse server

        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            //send post to server

            //get the description user inputted
            val description = findViewById<EditText>(R.id.etDescription).text.toString()
            val user = ParseUser.getCurrentUser()
            submitPost(description, user)
        }

        findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            //launch camera to let user take picture
        }


        queryPosts()

    }

    private fun submitPost(description: String, user: ParseUser) {
//create the post object to send to the parser
        val post = Post()
        //set the field values
        post.setDescription(description)
        post.setUser(user)
        post.saveInBackground { exception ->
            if (exception != null) {
                //something went wrong
                Log.e(TAG, "Error while saving post")
                exception.printStackTrace()
                Toast.makeText(this, "Saving post failed :(", Toast.LENGTH_SHORT)
            } else {
                Log.i(TAG, "Save post successful!")
                //todo:reset the edittext field to be empty
                //todo:reset the imageview to be empty
            }
        }
    }

    private fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        //Query for all posts in our server
        query.include(Post.KEY_USER)//also give back the user information
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
                                TAG,
                                "Post: " + post.getDescription() + ", username: " + post.getUser()?.username
                            )
                        }
                    }
                }
            }


        })
    }

    companion object {
        const val TAG = "MainActivity"
    }

}