package com.altaf.notesapp.utils

import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.altaf.notesapp.MainActivity
import com.altaf.notesapp.R
import com.google.android.material.appbar.MaterialToolbar

object ExtensionsFunctions {

    fun MaterialToolbar.setActionBar(requireActivity: FragmentActivity) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        (requireActivity as MainActivity).setSupportActionBar(this)
        setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.updateFragment -> setNavigationIcon(R.drawable.ic_left_arrow) //nambah icon yang tadinya bawaan jadi kita ganti pake drawable
                R.id.detailFragment -> setNavigationIcon(R.drawable.ic_left_arrow)
                R.id.addFragment -> setNavigationIcon(R.drawable.ic_left_arrow)

            }
        }
    }
}