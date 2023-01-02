package bu.ac.kr.blogapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bu.ac.kr.blogapp.data.BlogModel

import bu.ac.kr.blogapp.databinding.ItemListBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class BlogAdapter(val onItemClicked: (BlogModel) -> Unit) :
    ListAdapter<BlogModel, BlogAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(blogModel: BlogModel) {
            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(blogModel.createDate)

            binding.DiaryTitle.text = blogModel.title
            binding.DiaryDate.text = format.format(date).toString()

            if (blogModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.DiaryImage)
                    .load(blogModel.imageUrl)
                    .into(binding.DiaryImage)
            }
            binding.root.setOnClickListener {
                onItemClicked(blogModel)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BlogModel>() {
            override fun areItemsTheSame(oldItem: BlogModel, newItem: BlogModel): Boolean {
                return oldItem.createDate == newItem.createDate
            }

            override fun areContentsTheSame(oldItem: BlogModel, newItem: BlogModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}

