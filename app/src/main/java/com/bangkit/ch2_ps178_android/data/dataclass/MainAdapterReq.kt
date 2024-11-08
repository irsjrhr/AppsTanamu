package com.bangkit.ch2_ps178_android.data.dataclass


data class MainAdapterReq(
    val page: Int,
    val pageSize: Int,
    val lat: Double,
    val lon: Double,
    val input_field: String
)