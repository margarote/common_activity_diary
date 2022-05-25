package com.example.common_activity_diary.live_data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common_activity_diary.models.ActivityDiaryModel

class HomeViewModel : ViewModel() {
    val currentListItems: MutableLiveData<List<ActivityDiaryModel>> by lazy {
        MutableLiveData<List<ActivityDiaryModel>>()
    }
}