package com.bangkit.ch2_ps178_android.data.api


import com.bangkit.ch2_ps178_android.data.dataclass.MainAPIResponse
import com.bangkit.ch2_ps178_android.data.dataclass.MainAdapterReq
import com.bangkit.ch2_ps178_android.data.dataclass.MainAdapterRow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MainApi {
    @GET("api/data")
    fun get_data(): Call<List<MainAdapterRow>>

    @POST("api/data-paging")
    suspend fun getDataPaging(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("input_field") input_field: String,

    ): Response<MainAPIResponse>

}


