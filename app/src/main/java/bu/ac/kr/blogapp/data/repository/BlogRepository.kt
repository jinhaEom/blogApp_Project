package bu.ac.kr.blogapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import bu.ac.kr.blogapp.data.BlogDAO
import bu.ac.kr.blogapp.data.BlogDatabase
import bu.ac.kr.blogapp.data.BlogModel

class BlogRepository(application: Application) {
    private var mBlogDatabase : BlogDatabase
    private var mBlogDAO : BlogDAO
    private var mBlogItems : LiveData<List<BlogModel>>

    init {
        mBlogDatabase= BlogDatabase.getInstance(application)
        mBlogDAO = mBlogDatabase.blogDao()
        mBlogItems = mBlogDAO.getBlogList()
    }
    fun getBlogList() : LiveData<List<BlogModel>> {
        return mBlogItems
    }
    fun insertBlog(blogModel: BlogModel){
        Thread(Runnable{
            mBlogDAO.insertBlog(blogModel)
        }).start()
    }
}
// viewmodel에서 이 repo를 통해 데이터를 얻음