package com.example.graphicdesign.logic.model

data class UserResponse(val result:Boolean, val content:UserRelationship)
data class UserRelationship(val id:Int,val follow:UserData,val fans:UserData,val likes_images:LikesImages)
data class UserData(val users:List<UserId>,val number:Int)
data class UserId(val userid:String)
data class LikesImages(val images:List<ImageId>,val number: Int)
data class ImageId(val imageid:String)