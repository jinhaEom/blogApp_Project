package bu.ac.kr.blogapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import bu.ac.kr.blogapp.data.repository.BlogRepository

class BlogViewModel(application: Application) :
    AndroidViewModel(application) {
    private val mBlogRepository: BlogRepository
    private var mBlogItems: LiveData<List<BlogModel>>

    init{
        mBlogRepository = BlogRepository(application)
        mBlogItems = mBlogRepository.getBlogList()
    }
    fun insertBlog(blogModel: BlogModel){
        mBlogRepository.insertBlog(blogModel)
    }
    fun deleteBlog(blogModel: BlogModel){
        mBlogRepository.deleteBlog(blogModel)
    }
    fun getBlogList() : LiveData<List<BlogModel>> {
        return mBlogItems
    }
}