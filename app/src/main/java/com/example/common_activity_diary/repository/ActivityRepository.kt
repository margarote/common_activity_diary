package com.example.common_activity_diary.repository

import android.content.Context
import android.os.AsyncTask
import com.example.common_activity_diary.bd.AcitivityDiaryBD
import com.example.common_activity_diary.dao.ActivityDiaryDao
import com.example.common_activity_diary.models.ActivityDiaryModel

class ActivityRepository(context: Context) {
    var db = AcitivityDiaryBD.getInstance(context)?.activityDao()!!


    fun gelAllActivitiesDiary(): List<ActivityDiaryModel> {
        return db.gelAllActivitiesDiary()
    }

    fun insertActivityDiary(activityDiaryModel: ActivityDiaryModel) {
        InsertAsyncTask(db).execute(activityDiaryModel)
    }

    fun updateActivityDiary(activityDiaryModel: ActivityDiaryModel) {
        db.updateActivityDiary(activityDiaryModel)
    }

    fun deleteActivityDiary(activityDiaryModel: ActivityDiaryModel) {
        db.deleteActivityDiary(activityDiaryModel)
    }

    private class InsertAsyncTask internal constructor(private val activityDiaryDao: ActivityDiaryDao) :
        AsyncTask<ActivityDiaryModel, Void, Void>() {

        override fun doInBackground(vararg params: ActivityDiaryModel): Void? {
            activityDiaryDao.insertActivityDiary(params[0])
            return null
        }
    }
}