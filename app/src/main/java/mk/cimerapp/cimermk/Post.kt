package mk.cimerapp.cimermk

data class Post(

    var documentId: String = "",

    val title: String = "",

    val city: String = "",

    val price: String = "",

    val description: String = "",

    val userId: String = "",

    var isFavorite: Boolean = false,

    var gender: String = "",

    var authorName: String = "",

    val createdAt: Long = 0L,

    var anonymousSessionId: String = "",

    var lookingForRoommate: Boolean = false,

    var lookingForApartment: Boolean = false,

    var offeringApartment: Boolean = false
)