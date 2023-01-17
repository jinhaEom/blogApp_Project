package bu.ac.kr.blogapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BlogDAO {
    @Query("SELECT * from tb_blog ORDER BY createDate ASC")
    fun getBlogList() : LiveData<List<BlogModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlog(blogModel : BlogModel)

    @Delete
    fun deleteBlog(blogModel: BlogModel)

}