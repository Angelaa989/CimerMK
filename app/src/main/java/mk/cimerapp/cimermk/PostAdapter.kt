package mk.cimerapp.cimermk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private val postList: List<Post>
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
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}