package bu.ac.kr.blogapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Blog(
    @SerializedName("title") val title : String,
    @SerializedName("imageUrl") val imageUrl : String,
    @SerializedName("content") val content : String
) : Parcelable