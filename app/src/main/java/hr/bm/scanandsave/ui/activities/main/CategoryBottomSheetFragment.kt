package hr.bm.scanandsave.ui.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import hr.bm.scanandsave.R
import hr.bm.scanandsave.databinding.FragmentCategoryBottomSheetBinding
import hr.bm.scanandsave.databinding.FragmentReceiptBottomSheetBinding

class CategoryBottomSheetFragment(private val parentViewModel: MainViewModel) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentCategoryBottomSheetBinding

    private lateinit var adapter: CategoryListAdapter

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCategoryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListener()

        setObservers()

        initRecyclerView()
    }

    fun setObservers() {
//        parentViewModel.getSelectedCategory().observe(this, {
//            dismiss()
//        })
    }

    private fun setOnClickListener() {
        binding.addBtn.setOnClickListener(this)
    }

    private fun initRecyclerView() {
        adapter = CategoryListAdapter(this, this, parentViewModel)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.addBtn -> showAddCategoryDialog()
        }
    }

    private fun showAddCategoryDialog() {
        AddCategoryDialogFragment(parentViewModel).show(activity!!.supportFragmentManager, "")
    }
}