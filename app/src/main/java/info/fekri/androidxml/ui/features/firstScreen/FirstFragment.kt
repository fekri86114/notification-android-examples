package info.fekri.androidxml.ui.features.firstScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.fekri.androidxml.R
import info.fekri.androidxml.databinding.FragmentFirstBinding
import info.fekri.androidxml.model.data.TvMazeItem
import info.fekri.androidxml.model.net.ApiManager

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private val apiManager: ApiManager = ApiManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiManager.getPersons(object : ApiManager.ApiCallback<ArrayList<TvMazeItem>>{
            override fun onSuccess(data: ArrayList<TvMazeItem>) {
                setupData(data)
            }
            @SuppressLint("SetTextI18n")
            override fun onError(message: String) {
                Log.v("FirstFragment", message)
                binding.recyclerMainFirstScreen.visibility = View.GONE
                binding.txtFirstShowError.visibility = View.VISIBLE
                binding.txtFirstShowError.text = "Something went wrong!"
                Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun setupData(data: ArrayList<TvMazeItem>) {
        val adapter = FirstRecyclerAdapter(data)
        val layoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.recyclerMainFirstScreen.adapter = adapter
        binding.recyclerMainFirstScreen.layoutManager = layoutManager
    }

}