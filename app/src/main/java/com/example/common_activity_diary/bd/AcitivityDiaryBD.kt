package com.example.common_activity_diary.bd

import android.content.Context
import androidx.room.*
import com.example.common_activity_diary.converters.Converters
import com.example.common_activity_diary.dao.ActivityDiaryDao
import com.example.common_activity_diary.models.ActivityDiaryModel

@Database(entities = [ActivityDiaryModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AcitivityDiaryBD : RoomDatabase() {

    abstract fun activityDao() : ActivityDiaryDao

    companion object {
        private var INSTANCE: AcitivityDiaryBD? = null

        fun getInstance(context: Context): AcitivityDiaryBD? {
            if(INSTANCE == null){
                synchronized(AcitivityDiaryBD::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AcitivityDiaryBD::class.java, "activityDiary.db").allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
