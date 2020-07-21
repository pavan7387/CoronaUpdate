package com.example.demo.Model

import com.google.gson.annotations.SerializedName

class CoronaModel {
    @SerializedName("Global")
    var global: Global? = null
    @SerializedName("Countries")
    var countries: List<Country>? = null
    @SerializedName("Date")
    var date: String? = null
}

class Global {
    @SerializedName("NewConfirmed")
    var newConfirmed: Int? = null
    @SerializedName("TotalConfirmed")
    var totalConfirmed: Int? = null
    @SerializedName("NewDeaths")
    var newDeaths: Int? = null
    @SerializedName("TotalDeaths")
    var totalDeaths: Int? = null
    @SerializedName("NewRecovered")
    var newRecovered: Int? = null
    @SerializedName("TotalRecovered")
    var totalRecovered: Int? = null
}

class Country {
    @SerializedName("Country")
    var country: String? = null
    @SerializedName("CountryCode")
    var countryCode: String? = null
    @SerializedName("Slug")
    var slug: String? = null
    @SerializedName("NewConfirmed")
    var newConfirmed: Int? = null
    @SerializedName("TotalConfirmed")
    var totalConfirmed: Int? = null
    @SerializedName("NewDeaths")
    var newDeaths: Int? = null
    @SerializedName("TotalDeaths")
    var totalDeaths: Int? = null
    @SerializedName("NewRecovered")
    var newRecovered: Int? = null
    @SerializedName("TotalRecovered")
    var totalRecovered: Int? = null
    @SerializedName("Date")
    var date: String? = null
}