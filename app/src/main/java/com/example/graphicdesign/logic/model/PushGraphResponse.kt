package com.example.graphicdesign.logic.model

/**
 * 推图数据类
 */


data class PushGraphResponse(val result: Boolean, val number: Int, val content: List<Images>)

data class Content(
    val url: String,
    val thumbnailUrl: String,
    val thumbnailHeight: Int,
    val thumbnailWidth: Int,
    val likes: Likes,
    val createdAt: String
)


