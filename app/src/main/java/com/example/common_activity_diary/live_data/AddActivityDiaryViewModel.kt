package com.example.common_activity_diary.live_data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddActivityDiaryViewModel : ViewModel() {
    val currentHour: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isAllToday: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}