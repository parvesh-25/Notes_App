package com.altaf.notesapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.altaf.notesapp.R
import com.altaf.notesapp.data.entity.Notes
import com.altaf.notesapp.data.entity.Priority
import com.altaf.notesapp.databinding.RowItemNotesBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    // semua data disimpan di variabel listNotes
    val listNotes = ArrayList<Notes>()

    inner class MyViewHolder(val binding: RowItemNotesBinding):
        RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder (
        RowItemNotesBinding.inflate(LayoutInflater.from(parent.context),parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listNotes[position]
        holder.binding.apply {
            mNotes = data
            executePendingBindings()

        }
    }

    override fun getItemCount() = listNotes.size

    fun setData(data: List<Notes>?){
        if (data == null) return
        val diffCallback = DiffCallback(listNotes,data)
        val diffCallbackResult = DiffUtil.calculateDiff(diffCallback)

        listNotes.clear()
        // addAll untuk memasukkan semua data
        listNotes.addAll(data)
        diffCallbackResult.dispatchUpdatesTo(this)
    }
}