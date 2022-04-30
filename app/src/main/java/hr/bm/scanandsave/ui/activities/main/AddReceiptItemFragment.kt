package hr.bm.scanandsave.ui.activities.main

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import hr.bm.scanandsave.R
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.databinding.FragmentAddReceiptItemBinding
import hr.bm.scanandsave.databinding.FragmentReceiptBottomSheetBinding
import hr.bm.scanandsave.utils.showToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.lang.NumberFormatException

class AddReceiptItemFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddReceiptItemBinding

    private val parentViewModel: DetailedReceiptViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var mIsEdit = false
    private var mPosition = 0

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentAddReceiptItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        setObservers()
        setOnClickListener()
    }

    private fun initViews() {
        binding.nameLayout.setRequired()
        binding.priceLayout.setRequired()

        if (mIsEdit) {
            binding.title.text = activity!!.getString(R.string.edit_receipt_item)
            binding.btnAdd.text = activity!!.getString(R.string.edit)

            binding.name.setText(parentViewModel.getReceiptItems().value?.get(mPosition)?.name)
            binding.price.setText(parentViewModel.getReceiptItems().value?.get(mPosition)?.price.toString())
        }
    }

    private fun setObservers() {
        val storeTextChange = RxTextView.textChanges(binding.name)
        val amountTextChange = RxTextView.textChanges(binding.price)
        // TODO add to disposable - create BaseDialogFragment ??
        Observable.combineLatest(listOf(storeTextChange, amountTextChange)) {
            val store = it[0].toString()
            val amount = it[1].toString()

            //TODO add check for number validity
            binding.btnAdd.isEnabled = store.length in 1..30 && amount.isNotEmpty()
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    private fun setOnClickListener() {
        binding.btnAdd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAdd -> addReceiptItem()
        }
        dismiss()
    }

    private fun addReceiptItem() {
        try {
            val item = ReceiptItem(0, 0, binding.name.text.toString(), "", binding.price.text.toString().toDouble(), 1)
            if (mIsEdit)
                parentViewModel.editReceiptItem(mPosition, item)
            else
                parentViewModel.addReceiptItem(item)
        } catch (exception : NumberFormatException) {
            context?.let { showToast(it, it.getString(R.string.price_not_valid)) }
        }
    }

    fun setEditFragment(position: Int) {
        mPosition = position
        mIsEdit = true
    }
}