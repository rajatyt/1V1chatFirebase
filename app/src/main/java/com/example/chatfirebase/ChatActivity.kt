package com.example.chatfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
	private lateinit var messageRcy: RecyclerView
	private lateinit var messageBox: EditText
	private lateinit var sentButton: ImageView
	private lateinit var messageAdapter: MessageAdapter
	private lateinit var messageList: ArrayList<Message>
	private lateinit var mDbRef: DatabaseReference

	var receiverRoom: String? = null
	var sentRoom: String? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat)

		val name = intent.getStringExtra("name")
		val receiverUid = intent.getStringExtra("uid")

		val senderUid = FirebaseAuth.getInstance().currentUser?.uid

		mDbRef = FirebaseDatabase.getInstance().reference
		sentRoom = receiverUid + senderUid
		receiverRoom = senderUid + receiverUid

		supportActionBar?.title = name

		messageRcy = findViewById(R.id.chatRcy)
		messageBox = findViewById(R.id.messageBox)
		sentButton = findViewById(R.id.sendBtn)
		messageList = ArrayList()
		messageAdapter = MessageAdapter(this, messageList)
		messageRcy.layoutManager=LinearLayoutManager(this)
		messageRcy.adapter=messageAdapter

		mDbRef.child("chats").child(sentRoom!!).child(
			"messages"
		).addValueEventListener(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {

				messageList.clear()
				for (postSnapshot in snapshot.children) {
					val msg = postSnapshot.getValue(Message::class.java)
					messageList.add(msg!!)
				}
			}

			override fun onCancelled(error: DatabaseError) {
				TODO("Not yet implemented")
			}

		})

		sentButton.setOnClickListener {
			val message = messageBox.text.toString()
			val messageObject = Message(message, senderUid)

			mDbRef.child("chats").child(sentRoom!!).child("messages").push()
				.setValue(messageObject).addOnSuccessListener {
					mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
						.setValue(messageObject)
				}

			messageBox.setText("")


		}
	}
}