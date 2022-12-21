package bu.ac.kr.blogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BlogModel::class], version = 1)
abstract class BlogDatabase : RoomDatabase() {
    abstract fun blogDao() : BlogDAO

    companion object{
        @Volatile private var INSTANCE : BlogDatabase? = null

        fun getInstance(context: Context) : BlogDatabase?{
            if(INSTANCE == null){
                synchronized(BlogDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    BlogDatabase::class.java, "Blog")
                        .fallbackToDestructiveMigration() // 데이터 갱신시 테이블 새로 사용(기존꺼 버리고)
                        .build()
                }
            }
            return INSTANCE
        }

    }
}