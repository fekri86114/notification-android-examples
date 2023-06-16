package info.fekri.androidxml.model.net

import info.fekri.androidxml.ext.MyConstants
import info.fekri.androidxml.ext.asyncRequest
import info.fekri.androidxml.model.data.TvMazeItem
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(MyConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getPersons(
        serverEvent: ServerEvent<List<TvMazeItem>>,
        disposable: CompositeDisposable
    ) {
        apiService.getPersons()
            .asyncRequest()
            .subscribe(object : SingleObserver<List<TvMazeItem>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onError(e: Throwable) {
                    serverEvent.onError(e.message ?: "null-message")
                }

                override fun onSuccess(t: List<TvMazeItem>) {
                    serverEvent.onSuccess(t)
                }
            })
    }

    interface ApiCallback<T> {
        fun onSuccess(data: T)
        fun onError(message: String)
    }

    interface ServerEvent<T> {
        fun onError(msg: String)
        fun onSuccess(data: T)
    }

}