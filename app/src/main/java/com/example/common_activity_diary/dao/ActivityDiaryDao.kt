package com.example.common_activity_diary.dao

import androidx.room.*
import com.example.common_activity_diary.models.ActivityDiaryModel


@Dao
interface ActivityDiaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertActivityDiary(users: ActivityDiaryModel)

    @Query("Select * from activityDiary")
    fun gelAllActivitiesDiary(): List<ActivityDiaryModel>

    @Update
    fun updateActivityDiary(acitivityDiary: ActivityDiaryModel)

    @Delete
    fun deleteActivityDiary(acitivityDiary: ActivityDiaryModel)

}