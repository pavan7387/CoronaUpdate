package com.example.demo.View.Dialog

import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.size
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.demo.R
import kotlinx.android.synthetic.main.dialog_filter_by.*

class FilterByDialog(var mFilterByName: String,var mMinValue: String,var mMaxValue:String) : DialogFragment(),
    View.OnClickListener {

    private lateinit var tvCancle: TextView
    private lateinit var btnApply: Button
    private lateinit var btnClear: Button
    private lateinit var etMinValue: EditText
    private lateinit var etMaxValue: EditText
    private lateinit var spinner: AppCompatSpinner
    var data = "Total Cases,Deaths,Recovered"
    var mFilterList = data.split(",")
    private var filterByListner: FilterBy? = null

    interface FilterBy {
        fun filterBy(filterByName: String, min: String, max: String)
        fun clearAll()
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
        dialog!!.window?.setLayout(
            ViewPager.LayoutParams.MATCH_PARENT,
            ViewPager.LayoutParams.WRAP_CONTENT
        )
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
            R.layout.dialog_filter_by,
            container,
            false
        )
        initView(view)
        setClickEvent()
        setSpinner()
        return view
    }

    private fun setSpinner() {
        spinner?.setAdapter(
            ArrayAdapter<String>(
                this.activity!!.applicationContext,
                android.R.layout.select_dialog_item,
                mFilterList
            )
        )
        if (mFilterByName.equals("Total Cases")) {
            spinner.setSelection(0)
        } else if (mFilterByName.equals("Deaths")) {
            spinner.setSelection(1)
        } else if (mFilterByName.equals("Recovered")) {
            spinner.setSelection(2)
        } else {
            spinner.setSelection(0)
        }
        etMaxValue.setText(mMaxValue)
        etMinValue.setText(mMinValue)

    }

    private fun setClickEvent() {
        btnApply.setOnClickListener(this)
        btnClear.setOnClickListener(this)
        tvCancle.setOnClickListener(this)
    }

    private fun initView(view: View) {
        etMinValue = view.findViewById<View>(R.id.et_min) as EditText
        etMaxValue = view.findViewById<View>(R.id.et_max) as EditText
        btnApply = view.findViewById<View>(R.id.apply) as Button
        btnClear = view.findViewById<View>(R.id.clear) as Button
        spinner = view.findViewById<View>(R.id.sp_filter_list) as AppCompatSpinner
        tvCancle = view.findViewById<View>(R.id.cancle) as TextView

    }

    private fun handleDismiss() {
        dismiss()
    }

    fun getInstance(filterByClickListner: FilterBy): FilterByDialog {
        val filterByDialog = FilterByDialog(mFilterByName, mMinValue, mMaxValue)
        filterByDialog.filterByListner = filterByClickListner
        return filterByDialog
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.apply -> handleApply()
            R.id.clear -> handleClearAll()
            R.id.cancle -> handleDismiss()
        }
    }

    private fun handleClearAll() {
        filterByListner?.clearAll()
        dismiss()
    }

    private fun handleApply() {
       if (etMinValue.text.toString().trim().equals("") || etMaxValue.text.toString().trim().equals("")){
           Toast.makeText(this.activity,"Please enter min and max value",Toast.LENGTH_LONG).show()
       }else{
           if (etMinValue.text.toString().trim().toInt() < etMaxValue.text.toString().trim().toInt()) {
               filterByListner?.filterBy(
                   spinner.selectedItem.toString().trim(),
                   etMinValue.text.toString().trim(),
                   etMaxValue.text.toString().trim()
               )
               dismiss()
           }else{
               Toast.makeText(this.activity,"max value should be greater than min value",Toast.LENGTH_LONG).show()
           }
       }
    }


}
