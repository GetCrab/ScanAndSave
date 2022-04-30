package hr.bm.scanandsave.ui.activities.main

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.support.AndroidSupportInjection
import hr.bm.scanandsave.R
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.databinding.DialogSimpleReceiptBinding
import hr.bm.scanandsave.enums.Category
import hr.bm.scanandsave.enums.Currency
import hr.bm.scanandsave.enums.ReceiptType
import hr.bm.scanandsave.utils.ViewModelFactory
import hr.bm.scanandsave.utils.showToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.lang.NumberFormatException
import java.util.*
import javax.inject.Inject

class SimpleReceiptDialogFragment(private val parentViewModel: MainViewModel): DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogSimpleReceiptBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val receiptViewModel: ReceiptsViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

//    override fun getTheme() = R.style.DialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSimpleReceiptBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_dialog);
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        createViewModel()

        setOnClickListener()

        setObservers()

        binding.storeNameLayout.setRequired()
        binding.amountLayout.setRequired()

        binding.category.text = Category.FOOD.name
        binding.category.setOnClickListener(this)
    }

//    private fun createViewModel() {
//        parentViewModel = viewModelFactory.let {
//            ViewModelProvider(this, it)[MainViewModel::class.java]
//        }
//    }

    private fun setOnClickListener() {
        binding.btnAdd.setOnClickListener(this)
    }

    private fun setObservers() {
        val storeTextChange = RxTextView.textChanges(binding.storeName)
        val amountTextChange = RxTextView.textChanges(binding.amount)
        // TODO add to disposable - create BaseDialogFragment ??
        Observable.combineLatest(listOf(storeTextChange, amountTextChange)) {
            val store = it[0].toString()
            val amount = it[1].toString()

            //TODO add check for number validity
            binding.btnAdd.isEnabled = store.length in 1..30 && amount.isNotEmpty()
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe()

        parentViewModel.getSelectedCategory().observe(this, { category ->
            if (category != null) {
                binding.category.text = category.name
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAdd -> addReceipt()
            binding.category -> showCategoryFragment()
        }
    }

    private fun addReceipt() {
        try {
            receiptViewModel.addSimpleReceipt(Receipt(0, binding.storeName.text.toString(),
                Calendar.getInstance().time, binding.amount.text.toString().toDouble(), Currency.HRK, ReceiptType.SIMPLE, 0),
                arrayListOf(ReceiptItem(0, 0, binding.description.text.toString(), "", binding.amount.text.toString().toDouble(), 1)),
                binding.category.text.toString())
            parentViewModel.setReloadReceipts()
            dismiss()
        } catch (exception : NumberFormatException) {
            context?.let { showToast(it, it.getString(R.string.price_not_valid)) }
        }
    }

    private fun showCategoryFragment() {
        CategoryBottomSheetFragment(parentViewModel).show(activity!!.supportFragmentManager, "")
    }
}