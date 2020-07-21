package com.example.demo.DataModel.Api

import com.example.demo.Interface.ApiInterface
import com.example.demo.Model.CoronaModel
import com.lifeprint.digitalframe.Login.Interface.ApiCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiModel : ApiInterface {

    override fun getData(callBack: ApiCallBack<CoronaModel>) {
            val apiClient = RetrofitInstance.client.create(RetrofitService::class.java)
            val call = apiClient?.getData()
            call?.enqueue(object : Callback<CoronaModel> {

                override fun onFailure(call: Call<CoronaModel>?, t: Throwable?) {
                    callBack.onError(t.toString()) }
                override fun onResponse(
                    call: Call<CoronaModel>?,
                    response: Response<CoronaModel>
                ) {
                    if (response.isSuccessful) {
                        callBack.onSuccess(response.body()!!)
                    } else {
                        callBack.onError(response.message())
                    }
                }
            })
        }
    }

