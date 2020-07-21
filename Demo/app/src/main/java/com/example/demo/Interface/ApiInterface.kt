package com.example.demo.Interface

import com.example.demo.Model.CoronaModel
import com.lifeprint.digitalframe.Login.Interface.ApiCallBack

interface ApiInterface {
   fun getData(callBack: ApiCallBack<CoronaModel>)
}