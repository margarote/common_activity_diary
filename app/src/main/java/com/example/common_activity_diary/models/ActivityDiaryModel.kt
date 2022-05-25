package com.example.common_activity_diary.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activityDiary")
data class ActivityDiaryModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String,
    var date: String,
    var hour: String,
    var description: String,
    var isAllDay: Boolean,
    var dateRegistry: String
)