package com.example.graphicdesign.logic.model

data class InfoResponse(
    val content: InfoContent,
    val result: Boolean
)
data class InfoContent(
    val UserId: Int,
    val age: Int,
    val contact: String,
    val createdAt: String,
    val id: Int,
    val name: String,
    val portrait: String,
    val sex: String,
    val updatedAt: String
)