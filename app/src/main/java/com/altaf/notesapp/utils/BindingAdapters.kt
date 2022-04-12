package com.altaf.notesapp.utils

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.altaf.notesapp.R
import com.altaf.notesapp.data.entity.Notes
import com.altaf.notesapp.data.entity.Priority
import com.altaf.notesapp.ui.home.HomeFragmentDirections
import com.google.android.material.card.MaterialCardView

object BindingAdapters {

    // ini ("android:parsePriorityColor") akan dipanggil di dalam XMl
    @RequiresApi(Build.VERSION_CODES.M)
    @BindingAdapter("android:parsePriorityColor")
    @JvmStatic
    fun parsePriorityColor(cardView: MaterialCardView, priority: Priority) {
        when(priority){
            Priority.HIGH ->{
                cardView.setCardBackgroundColor(cardView.context.getColor(R.color.pink))
            }
            Priority.MEDIUM ->{
                cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))
            }
            Priority.LOW ->{
                cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
            }
        }
    }

    @BindingAdapter("android:sendDataToDetail")
    @JvmStatic
    fun sendDataToDetail(view: ConstraintLayout, currentItem: Notes) {
        view.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(currentItem)
            view.findNavController().navigate(action)
        }
    }

    @BindingAdapter("android:parsePriorityToInt")
    @JvmStatic
    fun parsePriorityToInt(view: AppCompatSpinner, priority: Priority){
        when(priority){
            Priority.LOW -> view.setSelection(2)
            Priority.MEDIUM -> view.setSelection(1)
            Priority.HIGH -> view.setSelection(0)
        }
    }

//    @BindingAdapter("android:emptyDatabase")
//    @JvmStatic
//    fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
//        when (emptyDatabase.value){
//            // jika value dari MutableLiveData<Boolean> = true maka akan menampilkan imageView
//            true -> view.visibility = View.VISIBLE
//            // jika value dari MutableLiveData<Boolean> = false maka tidak akan menampilkan imageView
//            else -> view.visibility = View.GONE
//        }
//    }
}