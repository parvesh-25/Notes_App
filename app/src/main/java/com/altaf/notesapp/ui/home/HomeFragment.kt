package com.altaf.notesapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.altaf.notesapp.MainActivity
import com.altaf.notesapp.R
import com.altaf.notesapp.data.NotesViewModel
import com.altaf.notesapp.data.entity.Notes
import com.altaf.notesapp.databinding.FragmentHomeBinding
import com.altaf.notesapp.utils.HelperFunctions
import com.altaf.notesapp.utils.HelperFunctions.checkIsDataEmpty
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding as FragmentHomeBinding

    private val homeViewModel by viewModels<NotesViewModel>()

    private val homeAdapter by lazy { HomeAdapter() }

    private var _currentData: List<Notes>? = null

    private val currentData get() = _currentData as List<Notes>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    // setting tombol back setiap fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inisialisasi binding
        binding.mHelperFunctions = HelperFunctions

        setHasOptionsMenu(true) // memberitahu bahwa fragment punya menu sendiri

        // bikin navController, appBArConfiguration. panggil di toolbarHome
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.apply {
            toolbarHome.apply {
                // requireActivity untuk memanggil setSupportActionBar, fragment tidka bisa memanggil maka dia dijadikan sebagai MainActivity
                (requireActivity() as MainActivity).setSupportActionBar(this)
                setupWithNavController(navController, appBarConfiguration) // toolbar
            }
            fabAdd.setOnClickListener{
                findNavController().navigate(R.id.action_homeFragment_to_addFragment)
            }
        }

        setupRecyclerView()

    }

    // rvnotes
private fun setupRecyclerView(){
    binding.rvNotes.apply{
        homeViewModel.getAllData().observe(viewLifecycleOwner) {
            // agar tulisan no notes hilang saat data ada
            checkIsDataEmpty(it)
            checkIsDataEmpty(it)
            homeAdapter.setData(it)
            _currentData = it
        }
        adapter = homeAdapter
        // StaggeredGridLayoutManager . bentukan nya sama seperti grid berbentuk kolom, namun StaggeredGridLayoutManager tidak sejajar (kyk tampilannya pinterest)
        layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL) // spanCount = 2 agar yg ditampilkan tiap baris 2, LinearLayoutManager.VERTICAL = agar posisi nya kebawah bukan samping (horizopntal)
        swipeToDelete(this)
    }
}



    // nge cek apakah data kosong

    private fun checkIsDataEmpty(data: List<Notes>) {
        binding.apply {
            if (data.isEmpty()){
                // jika data kosong maka gambar tanpa data akan tampil
                imgNoData.visibility = View.VISIBLE
                // jika data kosong rvNotes tidak tampil
                rvNotes.visibility = View.INVISIBLE
            } else{
                // jika ada data maka gambar tidak tampil
                imgNoData.visibility = View.INVISIBLE
                // jika ada data maka rvNotes tampil
                rvNotes.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val search = menu.findItem(R.id.menu_search)
        val searchAction = search.actionView as? SearchView
        searchAction?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_priority_high -> homeViewModel.sortByHighPriority().observe(this){ dataHigh ->
                // dataHigh mereferensi dari sortByHighPriority
                homeAdapter.setData(dataHigh)
            }
            // observe untuk memantau live data, data terbaru / perubahan data
            R.id.menu_priority_low -> homeViewModel.sortByLowPriority().observe(this){ dataLow ->
                homeAdapter.setData(dataLow)
            }
            R.id.menu_delete_all -> confrimDeletaAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confrimDeletaAll(){
        if (currentData.isEmpty()){
            AlertDialog.Builder(requireContext())
                .setTitle("No Notes")
                .setMessage("G ada data")
                .setPositiveButton("close"){dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }else{
            AlertDialog.Builder(requireContext())
                .setTitle("Delete all your notes?")
                .setMessage("Are u sure want to clear all of these data ?")
                .setPositiveButton("Yes"){_, _ ->
                    homeViewModel.deleteAllData()
                    Toast.makeText(requireContext(), "Succesfully delete data", Toast.LENGTH_SHORT)
                        .show()
                }
                .setNegativeButton("No"){dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }



    // dijalankan ketika user mengisi text di search
    override fun onQueryTextSubmit(query: String?): Boolean {
        val querySearch = "%$query%"
        query?.let {
            homeViewModel.searchByQuery(query).observe(this){
                homeAdapter.setData(it)


            }
        }
        return true
    }

    // akan dijalankan setiap ada perubahan di textsubmit (pas ngetik) di tempat search
    // dipanggil ketika query berubah
    override fun onQueryTextChange(newText: String?): Boolean {
        // artinya, nyari kata yg tengahnya ada $newText
        val querySearch = "%$newText%"
        newText?.let {
            homeViewModel.searchByQuery(newText).observe(this){
                homeAdapter.setData(it)

            }
        }
        return true
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = homeAdapter.listNotes[viewHolder.adapterPosition]
                homeViewModel.deleteNote(deletedItem)
                restoredData(viewHolder.itemView, deletedItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoredData(view: View, deletedItem: Notes) {
        val snackBar = Snackbar.make(
            view, "Deleted: ${deletedItem.title}", Snackbar.LENGTH_LONG
        )
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        snackBar.setAction("Undo") {
            homeViewModel.insertData(deletedItem)
        }
        snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        snackBar.show()
    }

    // onDestroy view untuk
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}