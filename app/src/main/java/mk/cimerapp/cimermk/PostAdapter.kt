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

        val gender: TextView =
            itemView.findViewById(R.id.tvGender)

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

        val context =
            holder.itemView.context

        val post = postList[position]

        holder.title.text = post.title
        holder.city.text = post.city
        holder.price.text = post.price + " €"

        holder.gender.text =
            context.getString(R.string.gender) + ": " + post.gender

        holder.description.text = post.description

        holder.favoriteButton.text =
            if (post.isFavorite) {
                context.getString(R.string.saved)
            } else {
                context.getString(R.string.favorite)
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

            holder.favoriteButton.text =
                if (post.isFavorite) {
                    context.getString(R.string.saved)
                } else {
                    context.getString(R.string.favorite)
                }

            if (!post.isFavorite) {

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