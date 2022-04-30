package hr.bm.scanandsave.ui.activities.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.support.AndroidSupportInjection
import hr.bm.scanandsave.R
import hr.bm.scanandsave.database.entities.Category
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.databinding.DialogAddCategoryBinding
import hr.bm.scanandsave.databinding.DialogSimpleReceiptBinding
import hr.bm.scanandsave.enums.Currency
import hr.bm.scanandsave.enums.ReceiptType
import hr.bm.scanandsave.utils.ViewModelFactory
import hr.bm.scanandsave.utils.showToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.NumberFormatException
import java.util.*
import javax.inject.Inject

class AddCategoryDialogFragment(private val parentViewModel: MainViewModel): DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogAddCategoryBinding

//    override fun getTheme() = R.style.DialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddCategoryBinding.inflate(inflater, container, false)
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

        binding.nameLayout.setRequired()
    }

//    private fun createViewModel() {
//        parentViewModel = viewModelFactory.let {
//            ViewModelProvider(this, it)[MainViewModel::class.java]
//        }
//    }

    private fun setOnClickListener() {
        binding.btnAdd.setOnClickListener(this)
    }

    @SuppressLint("CheckResult")
    private fun setObservers() {
        // TODO add to disposable - create BaseDialogFragment ??
        RxTextView.textChanges(binding.name).subscribeOn(Schedulers.io()).subscribe { str: CharSequence ->
            binding.btnAdd.isEnabled = str.isNotEmpty()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAdd -> addCategory()
        }
    }

    private fun addCategory() {
        try {
            parentViewModel.addCategory(
                Category(0, binding.name.text.toString())
            )
            dismiss()
        } catch (exception : Exception) {
            context?.let { showToast(it, it.getString(R.string.category_not_valid)) }
        }
    }
}