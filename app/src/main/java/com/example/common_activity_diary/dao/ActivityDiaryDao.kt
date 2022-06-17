package com.example.common_activity_diary.dao

import androidx.room.*
import com.example.common_activity_diary.models.ActivityDiaryModel


@Dao
interface ActivityDiaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActivityDiary(users: ActivityDiaryModel)

    @Query("Select * from activityDiary")
    suspend fun gelAllActivitiesDiary(): List<ActivityDiaryModel>

    @Update
    suspend fun updateActivityDiary(acitivityDiary: ActivityDiaryModel)

    @Delete
    suspend fun deleteActivityDiary(acitivityDiary: ActivityDiaryModel)

}