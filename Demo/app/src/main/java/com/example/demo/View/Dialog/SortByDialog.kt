package com.example.demo.View.Dialog

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.demo.R

class SortByDialog(var mSortByName: String,var mSortByOrder: String) : DialogFragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private lateinit var checkboxAsc: CheckBox
    private lateinit var checkboxDesc: CheckBox
    private lateinit var close: TextView
    private lateinit var btnSave: Button
    private lateinit var tvSortByCountry: TextView
    private lateinit var tvSortByTotalCase: TextView
    private lateinit var tvSortByDeath: TextView
    private lateinit var tvSortByRecovered: TextView
    private var sortByListner: SortBy? = null

    interface SortBy{
        fun sortBy(sortByName:String,sortByOrder:String)
    }

    override fun show(
        manager: FragmentManager,
        tag: String?
    ) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
        }
    }

    override fun onResume() {
        super.onResume()
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) handleDismiss()
            false
        }
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window?.setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT)
    }

    override fun onPause() {
        super.onPause()
        handleDismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        val view: View = inflater.inflate(
            R.layout.dialog_sort_by,
            container,
            false
        )
        initView(view)
        setClickEvent()
        handleCheckBoxUi()
        habldleSortByUi()
        return view
    }

    private fun habldleSortByUi() {
        if (mSortByName.equals("Sort by Total cases")){
            sortByTotalCase()
        }else if(mSortByName.equals(this.resources.getString(R.string.sort_by_country))){
            sortByCountry()
        }else if(mSortByName.equals(this.resources.getString(R.string.sort_by_deaths))){
            sortByDeath()
        }else if(mSortByName.equals(this.resources.getString(R.string.sort_by_recovered))){
            sortByRecovered()
        }
    }

     fun sortByRecovered() {
        tvSortByTotalCase.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByCountry.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByDeath.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByRecovered.setTextColor(this.resources.getColor(R.color.color_sky_blue))
        mSortByName = this.resources.getString(R.string.sort_by_recovered)
    }

     fun sortByDeath() {
        tvSortByTotalCase.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByCountry.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByDeath.setTextColor(this.resources.getColor(R.color.color_sky_blue))
        tvSortByRecovered.setTextColor(this.resources.getColor(R.color.color_black))
        mSortByName = this.resources.getString(R.string.sort_by_deaths)
    }

     fun sortByCountry() {
        tvSortByTotalCase.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByCountry.setTextColor(this.resources.getColor(R.color.color_sky_blue))
        tvSortByDeath.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByRecovered.setTextColor(this.resources.getColor(R.color.color_black))
        mSortByName = this.resources.getString(R.string.sort_by_country)
    }

     fun sortByTotalCase() {
        tvSortByTotalCase.setTextColor(this.resources.getColor(R.color.color_sky_blue))
        tvSortByCountry.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByDeath.setTextColor(this.resources.getColor(R.color.color_black))
        tvSortByRecovered.setTextColor(this.resources.getColor(R.color.color_black))
        mSortByName = this.resources.getString(R.string.sort_by_total_cases)
    }

    private fun setClickEvent() {
        tvSortByTotalCase.setOnClickListener(this)
        tvSortByCountry.setOnClickListener(this)
        tvSortByDeath.setOnClickListener(this)
        tvSortByRecovered.setOnClickListener(this)
        checkboxDesc.setOnCheckedChangeListener(this)
        checkboxAsc.setOnCheckedChangeListener(this)
        btnSave!!.setOnClickListener(this)
        close!!.setOnClickListener(this)
    }

    private fun initView(view: View) {
        tvSortByCountry = view.findViewById<View>(R.id.tv_country) as TextView
        tvSortByTotalCase = view.findViewById<View>(R.id.tv_total_case) as TextView
        tvSortByDeath = view.findViewById<View>(R.id.tv_death) as TextView
        tvSortByRecovered = view.findViewById<View>(R.id.tv_recovered) as TextView
        close = view.findViewById<View>(R.id.cancle) as TextView
        btnSave = view.findViewById<View>(R.id.btn_save) as Button
        checkboxAsc = view.findViewById<View>(R.id.asc) as CheckBox
        checkboxDesc = view.findViewById<View>(R.id.desc) as CheckBox
    }

    private fun handleCheckBoxUi() {
        if (mSortByOrder.equals("DESC")) {
            checkboxDesc.isChecked = true
            checkboxAsc.isChecked = false
        } else {
            checkboxAsc.isChecked = true
            checkboxDesc.isChecked = false
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancle -> handleDismiss()
            R.id.tv_recovered -> sortByRecovered()
            R.id.tv_death -> sortByDeath()
            R.id.tv_total_case -> sortByTotalCase()
            R.id.tv_country -> sortByCountry()
            R.id.btn_save -> handleSave()

        }
    }

    private fun handleSave() {
      sortByListner?.sortBy(mSortByName,mSortByOrder)
        dismiss()
    }

    private fun handleDismiss() {
        dismiss()
    }

    fun getInstance(sortByClickListner: SortBy): SortByDialog {
        val deleteDialog = SortByDialog(mSortByName, mSortByOrder)
        deleteDialog.sortByListner = sortByClickListner
        return deleteDialog
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.asc -> {
                if (isChecked){
                    mSortByOrder = "ASC"
                    checkboxDesc.isChecked =false
                }
            }
            R.id.desc -> {
                if (isChecked){
                    mSortByOrder = "DESC"
                    checkboxAsc.isChecked =false
                }

            }
        }
    }

}
