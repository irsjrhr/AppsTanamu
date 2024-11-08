package com.bangkit.ch2_ps178_android.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.bangkit.ch2_ps178_android.data.api.MainApi
import com.bangkit.ch2_ps178_android.data.dataclass.MainAdapterRow
import com.bangkit.ch2_ps178_android.data.adapter.MainPagingSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainModel : ViewModel() {

    //Konfig API
    val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl( BaseModel.BASE_URL ) // Ganti URL sesuai kebutuhan Anda
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient) // Tambahkan OkHttpClient yang sudah disetel dengan logging interceptor
        .build()
    var MainApi = retrofit.create(MainApi::class.java)
    //=====================================================================

    lateinit var Data_mainPaging: LiveData<PagingData<MainAdapterRow>>//Data story dalam jumlah yang banyak

    fun set_data()  {
        //Mengisi live data dengan data paging
        this.Data_mainPaging = get_data().cachedIn(viewModelScope)
    }

    fun get_data() : LiveData<PagingData<MainAdapterRow>>{
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                MainPagingSource(MainApi)
            }
        ).liveData
    }
}