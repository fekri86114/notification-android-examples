package info.fekri.androidxml.model.net

import info.fekri.androidxml.ext.MyConstants
import info.fekri.androidxml.model.data.TvMazeItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getPersons(apiCallback: ApiCallback<ArrayList<TvMazeItem>>) {
        apiService.getPersons().enqueue(object : Callback<TvMazeItem> {

            override fun onResponse(call: Call<TvMazeItem>, response: Response<TvMazeItem>) {
                val data = response.body()!!
                apiCallback.onSuccess(arrayListOf(data))
            }

            override fun onFailure(call: Call<TvMazeItem>, t: Throwable) {
                apiCallback.onError(t.message ?: "null message")
            }

        })
    }


    interface ApiCallback<T> {
        fun onSuccess(data: T)
        fun onError(message: String)
    }

}