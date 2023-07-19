package com.example.chatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatfirebase.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

	private lateinit var userRecyclerView: RecyclerView
	private lateinit var userList: ArrayList<User>
	private lateinit var userAdapter: UserAdapter
	private lateinit var mAuth: FirebaseAuth
	private lateinit var mDatabaseReference: DatabaseReference
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		mAuth = FirebaseAuth.getInstance()
		userList = ArrayList()
		userAdapter = UserAdapter(this, userList)
		mDatabaseReference = FirebaseDatabase.getInstance().reference

		userRecyclerView = findViewById(R.id.userRecyclerView)
		userRecyclerView.layoutManager = LinearLayoutManager(this)
		userRecyclerView.adapter = userAdapter

		mDatabaseReference.child("User").addValueEventListener(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				userList.clear()
				for (postSnapshot in snapshot.children) {
					val currentUser = postSnapshot.getValue(User::class.java)
					if (mAuth.currentUser?.uid!=currentUser?.uid){
						userList.add(currentUser!!)
					}
				}
				userAdapter.notifyDataSetChanged()
			}

			override fun onCancelled(error: DatabaseError) {
			}

		})


	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.logout) {
			mAuth.signOut()
			finish()
			return true
		}
		return true
	}
}