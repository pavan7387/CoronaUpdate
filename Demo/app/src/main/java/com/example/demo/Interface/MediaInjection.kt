package com.example.demo.Interface

import androidx.lifecycle.ViewModelProvider
import com.example.demo.DataModel.Api.ApiModel
import com.example.demo.ViewModel.ViewModelFactory

object MediaInjection {
    private val mDataSource : ApiInterface = ApiModel()
    private val mViewModelFactory = ViewModelFactory(mDataSource)

    fun provideViewModelFactory() : ViewModelProvider.Factory{
        return mViewModelFactory
    }
}