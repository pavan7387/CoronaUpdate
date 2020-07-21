package com.example.demo.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demo.Interface.ApiInterface
import com.example.demo.Model.CoronaModel
import com.example.demo.Model.Country
import com.lifeprint.digitalframe.Login.Interface.ApiCallBack


class DataViewModel(private val mViewModel : ApiInterface) : ViewModel() {

    private val mDatauccess = MutableLiveData<CoronaModel>()
    private val mDataFaild = MutableLiveData<String>()
    private var mList = ArrayList<Country>()

    fun getCoronaData():LiveData<CoronaModel>{
        return mDatauccess
    }

    fun getDataFaild():LiveData<String>{
        return mDataFaild
    }

    fun getData() {
      mViewModel.getData(object :ApiCallBack<CoronaModel>{
          override fun onError(error: String?) {
              mDataFaild.postValue(error.toString())
          }

          override fun onSuccess(data: CoronaModel) {
              mList.addAll(data?.countries as ArrayList<Country>)
              mDatauccess.postValue(data)
          }
      })
    }
}