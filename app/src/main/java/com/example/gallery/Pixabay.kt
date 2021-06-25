
package com.example.gallery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class Pixabay(
    val total:Int,
    val hits:Array<PhotoItem>,
    val totalHits:Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pixabay

        if (total != other.total) return false
        if (!hits.contentEquals(other.hits)) return false
        if (totalHits != other.totalHits) return false

        return true
    }

    override fun hashCode(): Int {
        var result = total
        result = 31 * result + hits.contentHashCode()
        result = 31 * result + totalHits
        return result
    }
}

 @Parcelize
 data class PhotoItem(
    @SerializedName("webformatURL")val previewUrl:String,//序列化名称
    @SerializedName("id")val photoId:Int,
    @SerializedName("largeImageURL")val fullUrl:String,
    @SerializedName("webformatHeight")val photoHeight:Int,
    @SerializedName("user")val photoUser:String,
    @SerializedName("likes")val photoLikes:Int,
    @SerializedName("favorites")val photoFavorites:Int
): Parcelable