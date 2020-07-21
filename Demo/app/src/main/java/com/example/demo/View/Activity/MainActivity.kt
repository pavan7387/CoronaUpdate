package com.example.demo

import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.Interface.MediaInjection
import com.example.demo.Model.CoronaModel
import com.example.demo.Model.Country
import com.example.demo.Util.AppUtil
import com.example.demo.Util.DeviceManager
import com.example.demo.Util.GPSTracker
import com.example.demo.Util.ManagePermissions
import com.example.demo.View.Adapter.CoronaListAdapter
import com.example.demo.View.Dialog.FilterByDialog
import com.example.demo.View.Dialog.SortByDialog
import com.example.demo.ViewModel.DataViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(),SortByDialog.SortBy ,FilterByDialog.FilterBy{

    private var isApiCalled: Boolean=false
    private lateinit var managePermissions: ManagePermissions
    private var mFilterByName: String=""
    private var mMinValue: String=""
    private var mMaxValue: String=""
    private  var mCountryName: String=""
    private  var mSortByName: String="Sort by Total cases"
    private  var mSortByOrder: String="DESC"
    private var mList: ArrayList<Country> = ArrayList()
    private var mGlobalList: ArrayList<Country> = ArrayList()
    private lateinit var wifiReceiver: BroadcastReceiver
    private lateinit var mCoronaListAdapter: CoronaListAdapter
    private lateinit var mCoronaViewModel: DataViewModel
    private  var mDialog: Dialog? = null
    private val PermissionsRequestCode = 123
    val list = listOf<String>(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerBroadcast()
        setUpViewModel()
        setCoronaListAdapter()
    }

    /**
     * get current position using GPS
     */
    private fun getCurentposition() {
        val permission = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        val res = this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            managePermissions = ManagePermissions(this, list, PermissionsRequestCode)
            managePermissions.checkPermissions()
        }
    }

    /**
     * get lat long and using this finding the country name
     */
    private fun getLocation() {
        val gpsTracker =  GPSTracker(this);
        if(gpsTracker.canGetLocation()){
            val  latitude = gpsTracker.getLatitude();
            val  longitude = gpsTracker.getLongitude();
            val geocoder =  Geocoder(this, Locale.getDefault()) as Geocoder
            var  addresses:List<Address> = ArrayList();
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                mCountryName = addresses.get(0).countryCode.trim()
            }
            arrangeDataCountryWise()
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    private fun registerBroadcast() {
        wifiReceiver = WifiReceiver()
        try {
            registerReceiver(wifiReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
        }catch (e: Exception){}
    }

    inner class WifiReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val conMan: ConnectivityManager? =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo: NetworkInfo? = conMan?.getActiveNetworkInfo()
            if (netInfo != null){
                getCurentposition()
                tv_no_data.visibility = View.GONE
                rv_image.visibility = View.VISIBLE
                rl_report.visibility = View.VISIBLE
                header.visibility = View.VISIBLE
                if (!isApiCalled) {
                    isApiCalled = true
                    getCoronaList(true)
                }
            }else{
                tv_no_data.text = getString(R.string.no_internet_nplease_connect_to_internet)
                tv_no_data.visibility = View.VISIBLE
                rv_image.visibility = View.GONE
                rl_report.visibility = View.GONE
                header.visibility = View.GONE
            }
        }
    }

    /**
     * set up view model using DI
     */
    private fun setUpViewModel() {
        mCoronaViewModel = ViewModelProvider(this,MediaInjection.provideViewModelFactory()).get(DataViewModel::class.java)
        setObserver()
    }

    /**
     * this method observe data change
     */
    private fun setObserver() {
        mCoronaViewModel.getCoronaData().observe(this, Observer { it ->
            handleApiResponse(it);
        })
        mCoronaViewModel.getDataFaild().observe(this,Observer{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            mDialog?.dismiss()
        })
    }

    /**
     * this method handle api response and update list
     */
    private fun handleApiResponse(it: CoronaModel) {
        if (it.countries?.size != 0)
            tv_no_data.visibility = View.GONE

        mList.clear()
        val orderedData = it.countries!!.sortedByDescending { it.totalConfirmed }
        mList.addAll(orderedData)
        mGlobalList.addAll(orderedData)
        handleTotal()
        arrangeDataCountryWise()
        mDialog?.dismiss()
        isApiCalled = false
    }

    private fun arrangeDataCountryWise() {
        val data: Country? = mList.find { it.countryCode == mCountryName }
        if (data != null) {
            mList.remove(data)
            mList.add(0, data!!)
        }
        mCoronaListAdapter.notifyData(mList)
    }

    /**
     * get corona list
     */
    private fun getCoronaList(isLoaderShow: Boolean) {
        if (isLoaderShow ){
            mDialog = AppUtil.showProgressDialog(this)
        }
        mCoronaViewModel.getData()
    }

    /**
     * set adapter to show list
     */
    private fun setCoronaListAdapter() {
        val manager = LinearLayoutManager(this)
        rv_image.setLayoutManager(manager)
        mCoronaListAdapter = CoronaListAdapter(this,mList)
        rv_image.adapter = mCoronaListAdapter
        rv_image.itemAnimator = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.filterby -> handleFilterBy()
            R.id.sortby -> handleSortBy()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * show dialog for sort list
     */
    private fun handleSortBy() {
        if (DeviceManager.isNetworkAvailable()) {
            val sortDialog: SortByDialog = SortByDialog(mSortByName, mSortByOrder).getInstance(this)
            sortDialog.show(supportFragmentManager, "dialog")
        }else{
            Toast.makeText(this,"Please connect to internet",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * show dialog for filter list
     */
    private fun handleFilterBy() {
        if (DeviceManager.isNetworkAvailable()) {
            val filterDialog: FilterByDialog = FilterByDialog(mFilterByName,mMinValue,mMaxValue).getInstance(this)
            filterDialog.show(supportFragmentManager, "dialog")
        }else{
            Toast.makeText(this,"Please connect to internet",Toast.LENGTH_SHORT).show()
        }
    }

    override fun sortBy(sortByName: String,sortByOrder:String) {
        runOnUiThread {
            mSortByName = sortByName
            mSortByOrder = sortByOrder
            sortList()
        }
    }

    /**
     * this method sort the list as per selection
     */
    private fun sortList() {
        if (mSortByName.equals("Sort by country")) {
            handleSortByCountry()
        } else if (mSortByName.equals("Sort by Deaths")) {
            handleSortByDeath()
        } else if (mSortByName.equals("Sort by Total cases")) {
            handleSortByTotalCase()
        } else if (mSortByName.equals("Sort by Recovered")) {
            handleSortByRecovered()
        }
        arrangeDataCountryWise()
    }

    /**
     * sort list by recovered cases
     */
    private fun handleSortByRecovered() {
        if (mSortByOrder.equals("DESC")){
            val list = mList.sortedByDescending { it.totalRecovered }
            mList.clear()
            mList.addAll(list)
        }else{
            val list = mList.sortedWith(compareBy({ it.totalRecovered }))
            mList.clear()
            mList.addAll(list)
        }
    }

    /**
     * sort list by total cases
     */
    private fun handleSortByTotalCase() {
        if (mSortByOrder.equals("DESC")){
            val list = mList.sortedByDescending { it.totalConfirmed }
            mList.clear()
            mList.addAll(list)
        }else{
            val list = mList.sortedWith(compareBy({ it.totalConfirmed }))
            mList.clear()
            mList.addAll(list)
        }
    }

    /**
     * sort list by death cases
     */
    private fun handleSortByDeath() {
        if (mSortByOrder.equals("DESC")){
            val list = mList.sortedByDescending { it.totalDeaths }
            mList.clear()
            mList.addAll(list)
        }else{
            val list = mList.sortedWith(compareBy({ it.totalDeaths }))
            mList.clear()
            mList.addAll(list)
        }
    }

    /**
     * sort list by country
     */
    private fun handleSortByCountry() {
        if (mSortByOrder.equals("DESC")){
            val list = mList.sortedByDescending { it.country }
            mList.clear()
            mList.addAll(list)
        }else{
            val list = mList.sortedWith(compareBy({ it.country }))
            mList.clear()
            mList.addAll(list)
        }
    }

    override fun filterBy(filterByName: String, min: String, max: String) {
        mList.clear()
        mList.addAll(mGlobalList)
        mMaxValue = max
        mMinValue = min
        mFilterByName = filterByName
        filterList(filterByName, min, max)
    }

    /**
     * this method apply filter as per the selection and create new filtered data
     */
    private fun filterList(filterByName: String, min: String, max: String) {
        if (filterByName.equals("Total Cases")) {
            var list =
                mList.filter { it.totalConfirmed!! > min.toInt() && it.totalConfirmed!! < max.toInt() }
            mList.clear()
            mList.addAll(list)
        } else if (filterByName.equals("Deaths")) {
            var list =
                mList.filter { it.totalDeaths!! > min.toInt() && it.totalDeaths!! < max.toInt() }
            mList.clear()
            mList.addAll(list)
        } else if (filterByName.equals("Recovered")) {
            var list =
                mList.filter { it.totalRecovered!! > min.toInt() && it.totalRecovered!! < max.toInt() }
            mList.clear()
            mList.addAll(list)
        }
        handleTotal()
        sortList()
        handleVisibility()
    }

    /**
     * this method handle visinility for list
     */
    private fun handleVisibility() {
        if (mList.size > 0) {
            tv_no_data.visibility = View.GONE
            rv_image.visibility = View.VISIBLE
        } else {
            tv_no_data.text = getString(R.string.no_data)
            tv_no_data.visibility = View.VISIBLE
            rv_image.visibility = View.GONE
        }
    }

    /**
     * calculate report of cases and show in Ui
     */
    private fun handleTotal() {
        tv_total_case.text =
            getString(R.string.total_casees) + "\n" + mList.map { it.totalConfirmed!! }.sum()
        tv_total_death.text =
            getString(R.string.total_death) + "\n" + mList.map { it.totalDeaths!! }.sum()
        tv_total_recoverd.text =
            getString(R.string.total_recoverd) + "\n" + mList.map { it.totalRecovered!! }.sum()
    }

    /**
     * clear all filter
     */
    override fun clearAll() {
        mFilterByName=""
        mMaxValue=""
        mMinValue=""
        mList.clear()
        mList.addAll(mGlobalList)
        handleTotal()
        sortList()
        handleVisibility()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions.processPermissionsResult(
                    requestCode,
                    permissions,
                    grantResults
                )
                if (!isPermissionsGranted) {
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(this, list.get(0))
                    if (!showRationale) {
                        DeviceManager.openSettings(this, PermissionsRequestCode)
                    }
                }else{
                }
                getCurentposition()
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(wifiReceiver)
        }catch (e:java.lang.Exception){}
    }

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            unregisterReceiver(wifiReceiver)
        }catch (e:java.lang.Exception){}
    }


}
