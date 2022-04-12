package com.altaf.notesapp.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.altaf.notesapp.data.entity.Notes


// class ini digunakan jika terjadi perubahan data
class DiffCallback(val oldList: List<Notes>, val newList: List<Notes>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // mengecek apakah posisi item nya sama atau tidak
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.id == newData.id &&
                oldData.title == newData.title &&
                oldData.description == newData.description &&
                oldData.date == newData.date &&
                oldData.priority == newData.priority
    }
}