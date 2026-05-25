package mk.cimerapp.cimermk

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PostAdapter(
    private val postList: ArrayList<Post>
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val title: TextView =
            itemView.findViewById(R.id.tvTitle)

        val city: TextView =
            itemView.findViewById(R.id.tvCity)

        val price: TextView =
            itemView.findViewById(R.id.tvPrice)

        val description: TextView =
            itemView.findViewById(R.id.tvDescription)

        val deleteButton: Button =
            itemView.findViewById(R.id.btnDelete)

        val favoriteButton: Button =
            itemView.findViewById(R.id.btnFavorite)

        val editButton: Button =
            itemView.findViewById(R.id.btnEdit)
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

        val post = postList[position]

        holder.title.text = post.title
        holder.city.text = post.city
        holder.price.text = post.price + " €"
        holder.description.text = post.description

        if (post.isFavorite) {

            holder.favoriteButton.text =
                "❤ Saved"

        } else {

            holder.favoriteButton.text =
                "❤ Favorite"
        }

        holder.favoriteButton.setOnClickListener {

            post.isFavorite = !post.isFavorite

            FirebaseFirestore.getInstance()
                .collection("posts")
                .document(post.documentId)
                .update(
                    "favorite",
                    post.isFavorite
                )

            if (post.isFavorite) {

                holder.favoriteButton.text =
                    "❤ Saved"

            } else {

                holder.favoriteButton.text =
                    "❤ Favorite"

                postList.removeAt(position)

                notifyItemRemoved(position)

                notifyItemRangeChanged(
                    position,
                    postList.size
                )
            }
        }

        val currentUserId =
            FirebaseAuth.getInstance()
                .currentUser
                ?.uid

        if (post.userId == currentUserId) {

            holder.deleteButton.visibility =
                View.VISIBLE

            holder.editButton.visibility =
                View.VISIBLE

            holder.deleteButton.setOnClickListener {

                AlertDialog.Builder(
                    holder.itemView.context
                )
                    .setTitle("Delete Post")

                    .setMessage(
                        "Are you sure you want to delete this post?"
                    )

                    .setPositiveButton(
                        "Yes"
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
                        "Cancel",
                        null
                    )

                    .show()
            }

            holder.editButton.setOnClickListener {

                val intent = Intent(
                    holder.itemView.context,
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
                    "description",
                    post.description
                )

                holder.itemView.context
                    .startActivity(intent)
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