package mk.cimerapp.cimermk

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostAdapter(
    private val postList: ArrayList<Post>,
    private val isFavoritesScreen: Boolean = false
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val title: TextView =
            itemView.findViewById(R.id.tvTitle)

        val city: TextView =
            itemView.findViewById(R.id.tvCity)

        val price: TextView =
            itemView.findViewById(R.id.tvPrice)

        val gender: TextView =
            itemView.findViewById(R.id.tvGender)

        val postTypes: TextView =
            itemView.findViewById(R.id.tvPostTypes)

        val description: TextView =
            itemView.findViewById(R.id.tvDescription)

        val author: TextView =
            itemView.findViewById(R.id.tvAuthor)

        val createdAt: TextView =
            itemView.findViewById(R.id.tvCreatedAt)

        val deleteButton: Button =
            itemView.findViewById(R.id.btnDelete)

        val favoriteButton: Button =
            itemView.findViewById(R.id.btnFavorite)

        val editButton: Button =
            itemView.findViewById(R.id.btnEdit)

        val avatar: TextView = itemView.findViewById(R.id.tvAvatar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_post,
                parent,
                false
            )

        return PostViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {

        val context =
            holder.itemView.context

        val post = postList[position]

        holder.itemView.setOnClickListener {

            val intent =
                Intent(
                    context,
                    PostDetailsActivity::class.java
                )

            intent.putExtra("title", post.title)
            intent.putExtra("authorName", post.authorName)
            intent.putExtra("city", post.city)
            intent.putExtra("price", post.price)
            intent.putExtra("gender", post.gender)
            intent.putExtra("description", post.description)
            intent.putExtra("lookingForRoommate", post.lookingForRoommate)
            intent.putExtra("lookingForApartment", post.lookingForApartment)
            intent.putExtra("offeringApartment", post.offeringApartment)

            context.startActivity(intent)
        }

        holder.title.text = post.title
        holder.city.text = post.city
        holder.price.text = post.price + " €"

        val genderText =
            if (post.gender == "female") {
                context.getString(R.string.female)
            } else {
                context.getString(R.string.male)
            }

        holder.gender.text =
            context.getString(R.string.gender) + ": " + genderText

        val types =
            mutableListOf<String>()

        if (post.lookingForRoommate) {
            types.add(context.getString(R.string.looking_for_roommate))
        }

        if (post.lookingForApartment) {
            types.add(context.getString(R.string.looking_for_apartment))
        }

        if (post.offeringApartment) {
            types.add(context.getString(R.string.offering_apartment))
        }

        holder.postTypes.text =
            "• " + types.joinToString(" • ")

        holder.postTypes.visibility =
            if (types.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }

        holder.description.text = post.description

        holder.author.text =
             post.authorName

        val initials =
            if (post.authorName.isBlank()) {
                "AN"
            } else {
                val parts =
                    post.authorName.trim().split(" ")

                if (parts.size >= 2) {
                    "${parts[0].first()}${parts[1].first()}"
                } else {
                    post.authorName.take(2)
                }
            }

        holder.avatar.text =
            initials.uppercase()

        val formattedDate =
            if (post.createdAt > 0) {

                SimpleDateFormat(
                    "dd.MM.yyyy HH:mm",
                    Locale.getDefault()
                ).format(
                    Date(post.createdAt)
                )

            } else {

                ""
            }

        holder.createdAt.text =
            formattedDate

        holder.favoriteButton.text =
            if (post.isFavorite) {
                context.getString(R.string.saved)
            } else {
                context.getString(R.string.favorite)
            }

        holder.favoriteButton.setOnClickListener {

            post.isFavorite = !post.isFavorite

          /*  FirebaseFirestore.getInstance()
                .collection("posts")
                .document(post.documentId)
                .update(
                    "favorite",
                    post.isFavorite
                )
           */

            val database =
                DatabaseProvider.getDatabase(context)

            CoroutineScope(Dispatchers.IO).launch {

                if (post.isFavorite) {

                    val currentUserId =
                        FirebaseAuth.getInstance()
                            .currentUser
                            ?.uid ?: return@launch

                    val savedPost =
                        SavedPostEntity(
                            documentId = post.documentId,
                            userId = currentUserId,
                            title = post.title,
                            city = post.city,
                            price = post.price,
                            gender = post.gender,
                            description = post.description
                        )

                    database.savedPostDao()
                        .insertPost(savedPost)

                } else {

                    val currentUserId =
                        FirebaseAuth.getInstance()
                            .currentUser
                            ?.uid ?: return@launch

                    database.savedPostDao()
                        .deletePost(
                            post.documentId,
                            currentUserId
                        )
                }
            }

            holder.favoriteButton.text =
                if (post.isFavorite) {
                    context.getString(R.string.saved)
                } else {
                    context.getString(R.string.favorite)
                }

            if (isFavoritesScreen && !post.isFavorite) {

                val adapterPosition =
                    holder.adapterPosition

                if (adapterPosition != RecyclerView.NO_POSITION) {

                    postList.removeAt(adapterPosition)

                    notifyItemRemoved(adapterPosition)

                    notifyItemRangeChanged(
                        adapterPosition,
                        postList.size
                    )
                }
            }
        }
        val currentUserId =
            FirebaseAuth.getInstance()
                .currentUser
                ?.uid

        val currentUser =
            FirebaseAuth.getInstance()
                .currentUser

        val currentSessionId =
            context.getSharedPreferences(
                "app_settings",
                android.content.Context.MODE_PRIVATE
            ).getString(
                "anonymousSessionId",
                ""
            )

        if (

            post.userId == currentUserId

            &&

            (
                    currentUser?.isAnonymous == false

                            ||

                            post.anonymousSessionId == currentSessionId
                    )

        ) {

            holder.deleteButton.visibility =
                View.VISIBLE

            holder.editButton.visibility =
                View.VISIBLE

            holder.deleteButton.setOnClickListener {

                AlertDialog.Builder(context)
                    .setTitle(
                        context.getString(R.string.delete)
                    )

                    .setMessage(
                        context.getString(R.string.confirm_delete)
                    )

                    .setPositiveButton(
                        context.getString(R.string.yes)
                    ) { _, _ ->

                        FirebaseFirestore.getInstance()
                            .collection("posts")
                            .document(post.documentId)
                            .delete()

                        postList.removeAt(position)

                        notifyItemRemoved(position)

                        notifyItemRangeChanged(
                            position,
                            postList.size
                        )
                    }

                    .setNegativeButton(
                        context.getString(R.string.cancel),
                        null
                    )

                    .show()
            }

            holder.editButton.setOnClickListener {

                val intent = Intent(
                    context,
                    EditPostActivity::class.java
                )

                intent.putExtra(
                    "documentId",
                    post.documentId
                )

                intent.putExtra(
                    "title",
                    post.title
                )

                intent.putExtra(
                    "city",
                    post.city
                )

                intent.putExtra(
                    "price",
                    post.price
                )

                intent.putExtra(
                    "gender",
                    post.gender
                )

                intent.putExtra(
                    "description",
                    post.description
                )

                intent.putExtra(
                    "postTypes",
                    holder.postTypes.text.toString()
                )

                intent.putExtra("lookingForRoommate", post.lookingForRoommate)
                intent.putExtra("lookingForApartment", post.lookingForApartment)
                intent.putExtra("offeringApartment", post.offeringApartment)

                context.startActivity(intent)
            }

        } else {

            holder.deleteButton.visibility =
                View.GONE

            holder.editButton.visibility =
                View.GONE
        }
    }

    override fun getItemCount(): Int {

        return postList.size
    }
}