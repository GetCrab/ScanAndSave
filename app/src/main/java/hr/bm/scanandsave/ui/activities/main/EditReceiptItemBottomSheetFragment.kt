package hr.bm.scanandsave.ui.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import hr.bm.scanandsave.R
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.databinding.FragmentEditReceiptItemBinding

class EditReceiptItemBottomSheetFragment(val item: ReceiptItem, private val position: Int) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentEditReceiptItemBinding

    private val parentViewModel: DetailedReceiptViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentEditReceiptItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.delete.setOnClickListener(this)
        binding.edit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.delete -> deleteReceiptItem()
            binding.edit -> editReceiptItem()
        }
        dismiss()
    }

    private fun deleteReceiptItem() {
        (parentFragment as DetailedReceiptFragment).deleteReceiptItem(item)
    }

    private fun editReceiptItem() {
        val fragment = AddReceiptItemFragment()
        fragment.setEditFragment(position)
        fragment.show(activity!!.supportFragmentManager.findFragmentByTag("detailed_receipt")!!.childFragmentManager, "edit_item")
    }
}