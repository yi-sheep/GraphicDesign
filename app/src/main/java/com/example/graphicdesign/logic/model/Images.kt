package com.example.graphicdesign.logic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * 图片数据共同类
 */
@Parcelize data class Images(
    val url: String,
    val thumbnailUrl: String,
    val thumbnailHeight: Int,
    val thumbnailWidth: Int,
    val likes: @RawValue Likes,
    val createdAt: String
):Parcelable
@Parcelize data class Likes(val users:@RawValue Array<String>, val number: Int):Parcelable