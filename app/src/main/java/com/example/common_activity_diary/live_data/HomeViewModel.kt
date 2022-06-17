package com.example.common_activity_diary.live_data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common_activity_diary.models.ActivityDiaryModel
import com.example.common_activity_diary.repository.ActivityRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val currentListItems: MutableLiveData<List<ActivityDiaryModel>> by lazy {
        MutableLiveData<List<ActivityDiaryModel>>()
    }

     fun fetchActivityDiary(activityRepository: ActivityRepository) {
        viewModelScope.launch {
            try {
                val activityDiaryModel = activityRepository.gelAllActivitiesDiary()
                currentListItems.value = activityDiaryModel
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun insertActivityDiary(activityDiaryModel: ActivityDiaryModel, activityRepository: ActivityRepository) {
        viewModelScope.launch {
            try {
                activityRepository.insertActivityDiary(activityDiaryModel)
                fetchActivityDiary(activityRepository)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

     fun updateActivityDiary(activityDiaryModel: ActivityDiaryModel, activityRepository: ActivityRepository) {
        viewModelScope.launch {
            try {
                activityRepository.updateActivityDiary(activityDiaryModel)
                fetchActivityDiary(activityRepository)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

     fun deleteActivityDiary(activityDiaryModel: ActivityDiaryModel, activityRepository: ActivityRepository) {
         viewModelScope.launch {
             try {
                 activityRepository.deleteActivityDiary(activityDiaryModel)
                 fetchActivityDiary(activityRepository)
             } catch (e: Exception) {
                 println(e.message)
             }
         }
    }
}