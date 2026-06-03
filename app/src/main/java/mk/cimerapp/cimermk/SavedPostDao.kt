package mk.cimerapp.cimermk

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(
        post: SavedPostEntity
    )

    @Query("SELECT * FROM saved_posts WHERE userId = :userId")
    suspend fun getPostsForUser(
        userId: String
    ): List<SavedPostEntity>

    @Query("DELETE FROM saved_posts WHERE documentId = :id AND userId = :userId")
    suspend fun deletePost(
        id: String,
        userId: String
    )

    @Query("SELECT EXISTS(SELECT 1 FROM saved_posts WHERE documentId = :postId AND userId = :userId)")
    suspend fun isPostSaved(
        postId: String,
        userId: String
    ): Boolean

    @Query("DELETE FROM saved_posts WHERE userId = :userId")
    suspend fun deletePostsForUser(
        userId: String
    )
}