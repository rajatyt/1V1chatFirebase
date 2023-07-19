package com.example.chatfirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chatfirebase.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {

	lateinit var mAuth: FirebaseAuth
	lateinit var binding: ActivitySignupBinding
	lateinit var databaseRef: DatabaseReference

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.hide()
		binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

		mAuth = FirebaseAuth.getInstance()

		binding.signUpBtn.setOnClickListener {
			val name = binding.signUpName.text.toString()
			val email = binding.signEmail.text.toString()
			val pass = binding.signPass.text.toString()

			signUp(name, email, pass)
			hideKeybaord(it)
		}


	}

	private fun signUp(name: String, email: String, pass: String) {
		mAuth.createUserWithEmailAndPassword(email, pass)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
					val intent = Intent(this@Signup, Login::class.java)
					finish()
					startActivity(intent)
				} else {
					Toast.makeText(this@Signup, "Some Error occurred", Toast.LENGTH_SHORT).show()

				}
			}

	}

	private fun addUserToDatabase(name: String, email: String, uid: String?) {
		databaseRef = FirebaseDatabase.getInstance().getReference()
		databaseRef.child("User").child(uid!!).setValue(User(name, email, uid))
	}

	private fun hideKeybaord(v: View) {
		val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
		inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0)
	}

}