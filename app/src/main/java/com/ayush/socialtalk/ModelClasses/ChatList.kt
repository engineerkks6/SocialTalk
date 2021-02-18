package com.ayush.socialtalk.ModelClasses

class ChatList {
    private var id: String = ""


    constructor()

    constructor(id: String) {
        this.id = id
    }

    //1
    fun getId():String?{
        return id
    }
    fun seId(id: String?){
        this.id = id!!
    }

}