package bu.ac.kr.blogapp.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface BlogDAO {
    @Query("SELECT * from tb_blog ORDER BY createDate ASC")
    fun getBlogList() : LiveData<List<BlogModel>>

    @Insert
    fun insertBlog(blogModel : BlogModel)

    @Delete
    fun deleteBlog(blogModel: BlogModel)

}