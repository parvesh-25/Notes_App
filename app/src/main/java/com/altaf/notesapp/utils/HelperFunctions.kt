package com.altaf.notesapp.utils

import android.content.Context
import android.view.View
import android.view.ViewParent
import android.widget.AdapterView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.altaf.notesapp.R
import com.altaf.notesapp.data.entity.Notes
import com.altaf.notesapp.data.entity.Priority

object HelperFunctions {

    // priorityIndicator: CardView, artinya priorityIndicator adlah argumen yg menampung untuk merubah CardView
    fun spinnerListener(context: Context?, priorityIndicator: CardView): AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            context?.let{
                when(position){
                    // 0 adalah indeks high dari array di prioritySpinner
                    0 -> {
                        // untuk menentukan warna    ContextCompat berfungsi untuk memanggil warna di class / object  R = resource , color = file warna , pink = warnanya
                        val pink = ContextCompat.getColor(it, R.color.pink)
                        // priorityIndicator adalah id dari cardview nya, setCardBackGroundColor untuk menetapkan warna backgrounnya
                        priorityIndicator.setCardBackgroundColor(pink)
                    }
                    // 1 adalah indeks dari medium
                    1 -> {
                        val yellow = ContextCompat.getColor(it,R.color.yellow)
                        priorityIndicator.setCardBackgroundColor(yellow)

                    }
                    // 2 adalah indeks dari low
                    2 -> {
                        val green = ContextCompat.getColor(it, R.color.green)
                        priorityIndicator.setCardBackgroundColor(green)
                    }
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
     fun parsetoPriority(priority: String, context: Context?): Priority {
        // ngambil dari array priorities
        val expectedPriority = context?.resources?.getStringArray(R.array.priorities)
        return when(priority){
            expectedPriority?.get(0) -> Priority.HIGH
            expectedPriority?.get(1) -> Priority.MEDIUM
            expectedPriority?.get(2) -> Priority.LOW
            else -> Priority.HIGH
        }
    }

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIsDataEmpty(data: List<Notes>) {
        emptyDatabase.value = data.isEmpty()
    }

}