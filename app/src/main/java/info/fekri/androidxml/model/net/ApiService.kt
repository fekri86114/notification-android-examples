package info.fekri.androidxml.model.net

import info.fekri.androidxml.model.data.TvMaze
import info.fekri.androidxml.model.data.TvMazeItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/shows/1/cast")
    fun getPersons(): Call<TvMazeItem>

}