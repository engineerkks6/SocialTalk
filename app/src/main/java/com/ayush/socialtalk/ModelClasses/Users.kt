package com.ayush.socialtalk.ModelClasses

class Users
{
    private  var uid:String = ""
    private  var username:String = ""
    private  var profile:String = ""
    private  var cover:String = ""
    private  var status:String = ""
    private  var search:String = ""
    private  var facebook:String = ""
    private  var instagram:String = ""
    private  var website:String = ""

    constructor()

    constructor(
        uid: String,
        username: String,
        profile: String,
        cover: String,
        status: String,
        search: String,
        facebook: String,
        instagram: String,
        website: String
    ) {
        this.uid = uid
        this.username = username
        this.profile = profile
        this.cover = cover
        this.status = status
        this.search = search
        this.facebook = facebook
        this.instagram = instagram
        this.website = website
    }


    //1
    fun getUID():String?{
        return uid
    }
    fun setUID(uid:String){
        this.uid = uid
    }
    //2
    fun getUserName():String?{
        return username
    }
    fun setUserName(username:String){
        this.username = username
    }
    //3
    fun getProfile():String?{
        return profile
    }
    fun setProfile(profile:String){
        this.profile = profile
    }

    //4
    fun getCover():String?{
        return cover
    }
    fun setCover(cover:String){
        this.cover = cover
    }
    //5
    fun getStatus():String?{
        return status
    }
    fun setStatus(status:String){
        this.status = status
    }
    //6
    fun getSearch():String?{
        return search
    }
    fun setSearch(search:String){
        this.search = search
    }
    //7
    fun getFacebook():String?{
        return facebook
    }
    fun setFacebook(facebook:String){
        this.facebook = facebook
    }
    //8
    fun getInstagram():String?{
        return instagram
    }
    fun setInstagram(instagram:String){
        this.instagram = instagram
    }
    //9
    fun getWebsite():String?{
        return website
    }
    fun setWebsite(website:String){
        this.website = website
    }


}