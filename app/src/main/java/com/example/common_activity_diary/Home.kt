package com.example.common_activity_diary

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.work.*
import com.example.common_activity_diary.array_adapters.CustomArrayAdapter
import com.example.common_activity_diary.converters.ConvertersDate
import com.example.common_activity_diary.converters.IConvertersDate
import com.example.common_activity_diary.live_data.HomeViewModel
import com.example.common_activity_diary.notification.NotificationWork
import com.example.common_activity_diary.notification.NotificationWork.Companion.NOTIFICATION_DESCRIPTION
import com.example.common_activity_diary.notification.NotificationWork.Companion.NOTIFICATION_TITLE
import com.example.common_activity_diary.repository.ActivityRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.concurrent.TimeUnit

class Home : Fragment() {

    var listViewWidget: ListView? = null
    lateinit var homeViewModel: HomeViewModel
    lateinit var activityRepository: ActivityRepository
    private var convertersDate: IConvertersDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRepository = ActivityRepository(context!!)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.fetchActivityDiary(activityRepository)
        convertersDate = ConvertersDate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val screen = inflater.inflate(R.layout.home, container, false)

        listViewWidget = screen.findViewById(R.id.listviewPrimary)

        clickButtonMore(screen)
        observeListActivityDiary()
        scheduleNotification(context)

        return screen
    }


    private fun observeListActivityDiary() {
        var arrayAdapter = CustomArrayAdapter(
            homeViewModel.currentListItems,
            context,
            homeViewModel,
            activityRepository,
        )

        listViewWidget?.adapter = arrayAdapter

        homeViewModel.currentListItems.observe(this, {
            arrayAdapter = CustomArrayAdapter(
                homeViewModel.currentListItems,
                context,
                homeViewModel,
                activityRepository,
            )
            listViewWidget?.adapter = arrayAdapter
        })
    }


    private fun clickButtonMore(view: View) {
        val button = view.findViewById<FloatingActionButton>(R.id.floatingButtonPrimary)
        button?.setOnClickListener {
            it.findNavController().navigate(R.id.addActivityDiaryDialog)
        }
    }


    private  fun scheduleNotification(context: Context?) {
        if (context != null) {
            val listActivityDiary = homeViewModel.currentListItems.value ?: listOf()


            listActivityDiary.forEach { data ->
                val dataBuilder = Data.Builder().putInt(NotificationWork.NOTIFICATION_ID, 0)
                    .putString(NOTIFICATION_TITLE, data.title)
                    .putString(NOTIFICATION_DESCRIPTION, data.description)
                    .build()
                val date = convertersDate?.convertStringInDate(data.date)

                val timeHoje = Calendar.getInstance().get(Calendar.MINUTE)
                val currentTimeMilis = ((date?.minutes ?: 0) - timeHoje).toLong()
                if (date != null) {
                    val isTodayRegistred = validationDateIsToday(data.date)
                    when {
                        !data.isAllDay && data.dateRegistry.isEmpty() -> {

                            val notificationWork =
                                OneTimeWorkRequest.Builder(NotificationWork::class.java)
                                    .setInitialDelay(currentTimeMilis, TimeUnit.MINUTES)
                                    .setInputData(dataBuilder).build()

                            val instanceWorkManager = WorkManager.getInstance(context)
                            instanceWorkManager.beginUniqueWork(
                                NotificationWork.NOTIFICATION_WORK,
                                ExistingWorkPolicy.APPEND,
                                notificationWork
                            ).enqueue()
                            data.dateRegistry = Date().toString()
                            homeViewModel.updateActivityDiary(data, activityRepository)
                        }
                        data.isAllDay && !isTodayRegistred -> {

                            val notificationWork =
                                OneTimeWorkRequest.Builder(NotificationWork::class.java)
                                    .setInitialDelay(currentTimeMilis, TimeUnit.MINUTES)
                                    .setInputData(dataBuilder).build()

                            val instanceWorkManager = WorkManager.getInstance(context)
                            instanceWorkManager.beginUniqueWork(
                                NotificationWork.NOTIFICATION_WORK,
                                ExistingWorkPolicy.APPEND,
                                notificationWork
                            ).enqueue()
                            data.dateRegistry = Date().toString()
                            homeViewModel.updateActivityDiary(data, activityRepository)
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun validationDateIsToday(date: String?): Boolean {
        if (!date.isNullOrEmpty()) {
            val dateToday = Calendar.getInstance().time
            val date = convertersDate?.convertStringInDate(date)
            return dateToday.compareTo(date) == 0
        }
        return false
    }

}

