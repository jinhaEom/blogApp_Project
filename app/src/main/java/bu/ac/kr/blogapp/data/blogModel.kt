package bu.ac.kr.blogapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="tb_blog")
data class BlogModel(

    @PrimaryKey(autoGenerate = true)
    val id : Long?,
    @ColumnInfo(name="userId")
    val userId : String,

    @ColumnInfo(name = "seq")
    var seq : Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "content")
    var content: String,

    @ColumnInfo(name= "image")
    var image : String,

    @ColumnInfo(name = "createDate")
    var createDate: Long
){
    constructor(): this(null,"",0,"","","",-1)
}
