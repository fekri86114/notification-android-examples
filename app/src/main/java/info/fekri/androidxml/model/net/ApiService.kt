package info.fekri.androidxml.model.net

import info.fekri.androidxml.model.data.TvMazeItem
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("/shows/1/cast")
    fun getPersons(): Single<List<TvMazeItem>>

}