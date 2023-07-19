package com.example.chatfirebase

class Message {
	var message:String?=null
	var sender:String?=null

	constructor(){}
	constructor(message: String?, sender: String?) {
		this.message = message
		this.sender = sender
	}


}