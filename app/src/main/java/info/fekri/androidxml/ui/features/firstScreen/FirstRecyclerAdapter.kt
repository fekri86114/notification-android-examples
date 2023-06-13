package info.fekri.androidxml.ui.features.firstScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import info.fekri.androidxml.R
import info.fekri.androidxml.databinding.ItemRecyclerFirstScreenBinding
import info.fekri.androidxml.model.data.TvMazeItem

class FirstRecyclerAdapter(private val data: ArrayList<TvMazeItem>):
    RecyclerView.Adapter<FirstRecyclerAdapter.FirstRecyclerViewHolder>() {
    private lateinit var binding: ItemRecyclerFirstScreenBinding

    inner class FirstRecyclerViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        fun bind(data: TvMazeItem) {
            binding.apply {

                txtFirstName.text = data.person.name
                txtFirstGender.text = data.person.gender
                txtFirstBirthday.text = data.person.birthday
                txtFirstNationality.text = data.person.country.name

                Picasso.get()
                    .load(data.character.image.original)
                    .error(R.drawable.broken_img)
                    .into(imgFirstPerson)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerFirstScreenBinding.inflate(inflater, parent, false)
        return FirstRecyclerViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FirstRecyclerViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}