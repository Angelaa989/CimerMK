package mk.cimerapp.cimermk

data class Post(

    var documentId: String = "",

    val title: String = "",

    val city: String = "",

    val price: String = "",

    val description: String = "",

    val userId: String = "",

    var isFavorite: Boolean = false
)