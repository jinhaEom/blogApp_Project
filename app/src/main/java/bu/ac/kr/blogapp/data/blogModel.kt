package bu.ac.kr.blogapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="tb_blog")
data class BlogModel(

    @PrimaryKey(autoGenerate = true)
    val userId : Long?,

    @ColumnInfo(name = "seq")
    var imageUrl : String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "content")
    var content: String,

    @ColumnInfo(name = "createDate")
    var createDate: Long
){
    constructor(): this(null,"","","",-1)
}
