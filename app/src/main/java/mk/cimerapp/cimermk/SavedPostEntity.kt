package mk.cimerapp.cimermk

import androidx.room.Entity

@Entity(
    tableName = "saved_posts",
    primaryKeys = ["documentId", "userId"]
)
data class SavedPostEntity(

    val documentId: String,

    val userId: String,

    val title: String,

    val city: String,

    val price: String,

    val gender: String,

    val description: String
)