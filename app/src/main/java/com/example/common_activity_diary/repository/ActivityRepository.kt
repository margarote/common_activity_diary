package com.example.common_activity_diary.repository

import android.content.Context
import com.example.common_activity_diary.bd.AcitivityDiaryBD
import com.example.common_activity_diary.models.ActivityDiaryModel

class ActivityRepository(context: Context) {
    var db = AcitivityDiaryBD.getInstance(context)?.activityDao()!!


    suspend fun gelAllActivitiesDiary(): List<ActivityDiaryModel> {
        return db.gelAllActivitiesDiary()
    }

    suspend fun insertActivityDiary(activityDiaryModel: ActivityDiaryModel) {
        db.insertActivityDiary(activityDiaryModel)
    }

    suspend fun updateActivityDiary(activityDiaryModel: ActivityDiaryModel) {
        db.updateActivityDiary(activityDiaryModel)
    }

    suspend fun deleteActivityDiary(activityDiaryModel: ActivityDiaryModel) {
        db.deleteActivityDiary(activityDiaryModel)
    }
}