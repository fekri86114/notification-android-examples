package info.fekri.androidxml.ui.features.firstScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import info.fekri.androidxml.R
import info.fekri.androidxml.databinding.ItemRecyclerFirstScreenBinding
import info.fekri.androidxml.model.data.TvMazeItem

class FirstRecyclerAdapter(
    private val data: ArrayList<TvMazeItem>,
    private val personEvent: PersonEvent
) : RecyclerView.Adapter<FirstRecyclerAdapter.FirstRecyclerViewHolder>() {
    private lateinit var binding: ItemRecyclerFirstScreenBinding

    inner class FirstRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            binding.apply {

                txtFirstName.text = data[position].person.name
                txtFirstGender.text = data[position].person.gender
                txtFirstBirthday.text = data[position].person.birthday
                txtFirstNationality.text = data[position].person.country.name

                Picasso.get()
                    .load(data[position].character.image.original)
                    .error(R.drawable.broken_img)
                    .into(imgFirstPerson)

            }

            itemView.setOnClickListener {
                personEvent.onPersonClicked(data[adapterPosition], adapterPosition)
            }
            itemView.setOnLongClickListener {
                personEvent.onPersonLongClicked(data[adapterPosition], adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerFirstScreenBinding.inflate(inflater, parent, false)
        return FirstRecyclerViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FirstRecyclerViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = data.size

    fun deleteItem(oldPerson: TvMazeItem, oldPosition: Int) {
        data.remove(oldPerson)
        notifyItemRemoved( oldPosition )
    }


    interface PersonEvent {
        fun onPersonClicked(personItem: TvMazeItem, position: Int)
        fun onPersonLongClicked(personItem: TvMazeItem, position: Int)
    }

}