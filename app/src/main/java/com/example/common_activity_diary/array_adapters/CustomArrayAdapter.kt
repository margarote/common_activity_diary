package com.example.common_activity_diary.array_adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.common_activity_diary.R
import com.example.common_activity_diary.models.ActivityDiaryModel
import com.example.common_activity_diary.repository.ActivityRepository

class CustomArrayAdapter(
    private val currentListItems: MutableLiveData<List<ActivityDiaryModel>>,
    private val mContext: Context?,
    private val repository: ActivityRepository?
) : BaseAdapter() {

    override fun getCount(): Int {
        return currentListItems.value?.count() ?: 0
    }

    override fun getItem(position: Int): Any {
        return (currentListItems.value ?: listOf())[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var listItem = convertView
        if (listItem == null) {
            listItem =
                LayoutInflater.from(mContext).inflate(R.layout.card_view_checkbox, parent, false)
        }
        val currentActivityDiaryModel = (currentListItems.value ?: listOf())[position]

        val title = listItem?.findViewById<TextView>(R.id.card_view_title_id)
        val description = listItem?.findViewById<TextView>(R.id.card_view_description_id)
        val buttonDelete = listItem?.findViewById<ImageButton>(R.id.button_delete_id)

        title?.text = currentActivityDiaryModel.title
        description?.text = currentActivityDiaryModel.description



        buttonDelete?.setOnClickListener {
            onClickButtonDelete(currentActivityDiaryModel)
        }
        return listItem!!
    }


    private fun onClickButtonDelete(currentActivityDiaryModel: ActivityDiaryModel) {
        val alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setTitle("Excluir")
        alertDialog.setMessage("Deseja deletar este item?")

        alertDialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setPositiveButton("Confirmar") { _, _ ->
            repository?.deleteActivityDiary(currentActivityDiaryModel)
            currentListItems.value = repository?.gelAllActivitiesDiary()
        }
        alertDialog.show()
    }


}