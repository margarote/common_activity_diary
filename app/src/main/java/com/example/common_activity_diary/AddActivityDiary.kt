package com.example.common_activity_diary

import android.app.TimePickerDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.common_activity_diary.converters.ConvertersDate
import com.example.common_activity_diary.converters.IConvertersDate
import com.example.common_activity_diary.live_data.AddActivityDiaryViewModel
import com.example.common_activity_diary.models.ActivityDiaryModel
import com.example.common_activity_diary.repository.ActivityRepository
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddActivityDiary : Fragment(), TimePickerDialog.OnTimeSetListener {

    var activityDiaryModel: ActivityDiaryModel? = null
    var addActivityDiaryViewModel: AddActivityDiaryViewModel? = null
    var activityRepository: ActivityRepository? = null
    var convertersDate: IConvertersDate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivityDiaryViewModel =
            ViewModelProvider(this).get(AddActivityDiaryViewModel::class.java)
        activityDiaryModel = ActivityDiaryModel(null, "", "", "00:00:00", "", false, "")
        activityRepository = ActivityRepository(context!!)
        convertersDate = ConvertersDate()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val screen = inflater.inflate(R.layout.fragment_add_activity_diary, container, false)

        selectDate(screen)
        buttonSave(screen)
        selectHour(screen)
        buttonCheckRepeatAllDays(screen)

        addActivityDiaryViewModel?.currentHour?.observe(this, {
            screen.findViewById<Button>(R.id.button_select_hour).text = it
        })

        addActivityDiaryViewModel?.isAllToday?.observe(this, {
            if (it) {
                screen.findViewById<CalendarView>(R.id.calendario_id).visibility = GONE
            } else {
                screen.findViewById<CalendarView>(R.id.calendario_id).visibility = VISIBLE
            }
        })
        return screen
    }


    fun selectDate(view: View) {
        val buttonSelectDate = view.findViewById<CalendarView>(R.id.calendario_id)
        buttonSelectDate?.setOnDateChangeListener { v, year, month, dayOfMonth ->
            activityDiaryModel?.date = "${year}-${month}-${dayOfMonth}"
        }
    }

    private fun selectHour(view: View) {
        val selectHour = view.findViewById<Button>(R.id.button_select_hour)
        selectHour.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timerPicker =
                TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
            timerPicker.show()
        }
    }

    private fun buttonCheckRepeatAllDays(view: View) {
        val button = view.findViewById<CheckBox>(R.id.checkRepeatAllDays)

        button.setOnCheckedChangeListener { _, isChecked ->
            if (activityDiaryModel != null) {
                addActivityDiaryViewModel?.isAllToday?.value = isChecked
                activityDiaryModel?.isAllDay = isChecked
                val date =
                    "${convertersDate?.convertDateInString(Date())} ${activityDiaryModel?.hour}"
                activityDiaryModel?.date = date
            }
        }
    }


    private fun buttonSave(view: View) {
        val button = view.findViewById<Button>(R.id.button_save)
        button?.setOnClickListener {
            val resultValidation = validationButtonSave(view)

            if (resultValidation) {
                activityRepository?.insertActivityDiary(activityDiaryModel!!)
                findNavController().navigate(R.id.action_addActivityDiaryDialog_to_home)
            }
        }
    }

    private fun validationButtonSave(view: View): Boolean {
        val title = view.findViewById<EditText>(R.id.titulo_field)
        val description = view.findViewById<EditText>(R.id.descricao_field)
        val titleIsNotEmpty = isEmpty(title.text.toString())
        val descriptionIsNotEmpty = isEmpty(description.text.toString())

        activityDiaryModel?.title = title.text.toString()
        activityDiaryModel?.description = description.text.toString()

        when {
            titleIsNotEmpty || descriptionIsNotEmpty -> {
                showSnackbar(
                    view,
                    "O campo Título ou Descrição não estão preenchidos corretamente!"
                )
            }
            activityDiaryModel?.date.isNullOrEmpty() ||
                    (activityDiaryModel?.date?.length ?: 0) <= 10 -> {
                showSnackbar(view, "Informe a data e o horário da atividade!")
            }
            else -> {
                return true
            }
        }
        return false
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val date = if ((activityDiaryModel?.date?.length
                ?: 0) > 10 || activityDiaryModel?.date.isNullOrEmpty()
        ) {
            convertersDate?.convertDateInString(Date())
        } else {
            activityDiaryModel?.date
        }
        activityDiaryModel?.date = "$date ${hourOfDay}:${minute}:00"
        activityDiaryModel?.hour = "${hourOfDay}:${minute}:00"
        addActivityDiaryViewModel?.currentHour?.value = activityDiaryModel?.hour
    }

    private fun showSnackbar(view: View, text: String) {
        val sandbar = Snackbar.make(
            view, text,
            Snackbar.ANIMATION_MODE_SLIDE
        )
        sandbar.setBackgroundTintMode(PorterDuff.Mode.SCREEN)
            .show()
    }
}