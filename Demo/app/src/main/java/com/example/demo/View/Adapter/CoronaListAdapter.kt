package com.example.demo.View.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.Model.Country
import com.example.demo.R
import kotlinx.android.synthetic.main.adapter_item.view.*


class CoronaListAdapter(
    private val context: Context,
    private var mList: ArrayList<Country>
) :
    RecyclerView.Adapter<CoronaListAdapter.ColorViewHolder>() {

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        var view : ColorViewHolder? =null
        view = ColorViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false))
        return view!!
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val country =  mList.get(position)
        holder.mTvCountry.text = country.country
        holder.mTvTotalCas.text = country.totalConfirmed.toString()
        holder.mTvDeath.text = country.totalDeaths.toString()
        holder.mTvRecoverd.text = country.totalRecovered.toString()
        if (country.countryCode.equals("IN")){
            holder.mLlView.setBackgroundColor(context.resources.getColor(R.color.theme_color))
        }else{
            holder.mLlView.setBackgroundColor(context.resources.getColor(R.color.color_white))
        }
    }



    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvCountry = view.tv_country as TextView
        val mTvTotalCas = view.tv_total_case as TextView
        val mTvDeath = view.tv_death as TextView
        val mTvRecoverd = view.tv_recoverd as TextView
        val mLlView = view.ll_view as LinearLayout
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun notifyData(list: ArrayList<Country>) {
         mList = list
        notifyDataSetChanged()
    }

}