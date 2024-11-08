package com.bangkit.ch2_ps178_android.data.dataclass

data class MainAPIResponse(
    val data: List<MainAdapterRow>, // Gantilah YourDataModel dengan model data sesuai kebutuhan
    val total: Int,
    val page: Int,
    val pageSize: Int
)
