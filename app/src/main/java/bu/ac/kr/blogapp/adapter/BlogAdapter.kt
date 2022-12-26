package bu.ac.kr.blogapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bu.ac.kr.blogapp.R
import bu.ac.kr.blogapp.data.BlogModel
import bu.ac.kr.blogapp.databinding.ActivityMainBinding
import com.bumptech.glide.Glide

class BlogAdapter(val BlogItemClick: (BlogModel) -> Unit, val BlogItemLongClick: (BlogModel) -> Unit):
    RecyclerView.Adapter<BlogAdapter.ViewHolder>() {
        private var blogs: List<BlogModel> = listOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return blogs.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(blogs[position])
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val coverImageView = itemView.findViewById<ImageView>(R.id.coverImageView)
        private val title = itemView.findViewById<TextView>(R.id.descriptionTextView)
        private val reviewEditText = itemView.findViewById<TextView>(R.id.reviewEditText)
        fun bind(blogModel: BlogModel){
            Glide.with(itemView)
                .load(R.drawable.ic_launcher_foreground)
            title.text = blogModel.title
            reviewEditText.text = blogModel.content

            itemView.setOnClickListener {
                BlogItemClick(blogModel)
            }
            itemView.setOnLongClickListener {
                BlogItemLongClick(blogModel)
                true
            }
        }
    }
    fun setBlogs(blogs:List<BlogModel>){  // 화면 갱신할때 사용할 함수
        this.blogs = blogs
        notifyDataSetChanged()
    }

}