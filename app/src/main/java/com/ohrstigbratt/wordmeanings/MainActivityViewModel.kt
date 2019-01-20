package com.ohrstigbratt.wordmeanings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val selectedTabTag = MutableLiveData<String>()

    fun setSelectedTabTag(tabTag: String) {
        selectedTabTag.value = tabTag
    }
}