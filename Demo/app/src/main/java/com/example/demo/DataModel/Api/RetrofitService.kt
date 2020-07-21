package com.example.demo.DataModel.Api

import com.example.demo.Model.CoronaModel
import retrofit2.http.GET

interface RetrofitService{

    @GET("/summary")
    fun getData()  : retrofit2.Call<CoronaModel>

}