package com.example.chatfirebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MessageAdapter(val context: Context, val message: ArrayList<Message>) :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	val ITEM_RECEIVE = 1
	val ITEM_SENT = 2

	class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		val sent = itemView.findViewById<TextView>(R.id.textSent)
	}

	class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val receive = itemView.findViewById<TextView>(R.id.textReceive)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		if (viewType == 1) {
			val view: View = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false)
			return ReceiveViewHolder(view)
		} else {
			val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
			return SentViewHolder(view)
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val currentMessage = message[position]

		if (holder.javaClass == SentViewHolder::class.java) {
			val viewHolder = holder as SentViewHolder
			holder.sent.text = currentMessage.message
		} else {
			val viewHolder = holder as ReceiveViewHolder
			holder.receive.text = currentMessage.message
		}

	}

	override fun getItemViewType(position: Int): Int {
		val currentMessage = message[position]
		if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sender)) {
			return ITEM_SENT
		} else {
			return ITEM_RECEIVE
		}
	}

	override fun getItemCount(): Int {
		return message.size
	}
}