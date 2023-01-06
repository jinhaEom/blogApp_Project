package bu.ac.kr.blogapp.data

import android.os.Parcelable
import android.renderscript.Long2
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName="tb_blog")
data class BlogModel(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "userId")
    var userId: String,

    @ColumnInfo(name = "seq")
    var imageUrl: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "content")
    var content: String,

    @ColumnInfo(name = "createDate")
    var createDate: Long
): Parcelable{
    constructor(): this(0,"","","","",0)
}