package com.example.newsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.data.local.converters.Converter
import com.example.newsapp.data.local.dao.ArticleDao
import com.example.newsapp.data.model.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(
    Converter::class
)
abstract class ArticleDatabase : RoomDatabase() {

    // is used to retrieve the DAO (Data Access Object) for accessing articles in the database.
    abstract fun getArticleDao(): ArticleDao  // room will do this for us

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()


        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context)!!

        }

        private fun createDatabase(context: Context): ArticleDatabase? {
            Room.databaseBuilder(
                context.applicationContext,
                //This specifies the class of the database (ArticleDatabase) that Room will create.
                ArticleDatabase::class.java,
                name = "article_db.db"
            ).build().also {
                instance= it
                return instance
            }
        //This method call finalizes the database configuration and constructs the Room database instance.
        }

    }
}
//Application Context:
//


//The application context represents the entire application and is not tied to any specific activity or
// component within the application.
//It persists throughout the entire lifecycle of the application and remains unchanged even if
// activities are destroyed and recreated.
//It's primarily used for operations that require a long-lived context,
// such as creating singletons, accessing resources, and performing database operations.



//Usage in Room Database Configuration:
//
//When configuring a Room database, it's essential to use the application context
// instead of an activity context
// to avoid memory leaks.
//Using an activity context can lead to memory leaks because it holds a reference to the activity,
// preventing it from being garbage collected when the activity is destroyed.



//the getArticleDao() method in the ArticleDatabase class is necessary because it defines
// the contract for obtaining the DAO instance used for accessing the Article entity in the Room database.
// It enables Room to generate the concrete implementation of the ArticleDao interface
// and facilitates clean and structured database interactions within the application.