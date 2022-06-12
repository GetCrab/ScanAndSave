package hr.bm.scanandsave.ui.activities.main

import android.os.Bundle
import android.view.View
import hr.bm.scanandsave.R
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.enums.Currency
import hr.bm.scanandsave.enums.ReceiptType
import hr.bm.scanandsave.utils.Resource
import hr.bm.scanandsave.utils.parsePrice
import java.util.*

class EditDetailedReceiptFragment(private val parentViewModel: MainViewModel, private val receiptId: Long): DetailedReceiptFragment(parentViewModel, null) {

    override fun setObservers() {
        super.setObservers()

        /*receiptViewModel.getReceipt().observe(this, { receiptWithItems ->
            receiptWithItems.let {
                binding.btnAdd.text = context?.getString(R.string.edit)
                binding.storeName.setText(receiptWithItems.receipt.merchant)
                binding.total.text = parsePrice(receiptWithItems.receipt.totalPrice)
                binding.category.text = receiptWithItems.category.name

                receiptViewModel.setItems(receiptWithItems.items)

                binding.btnRepeat.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnRepeat.setOnClickListener(this)
                binding.btnDelete.setOnClickListener(this)

                receiptViewModel.setDate(receiptWithItems.receipt.date)
            }
        })*/

        receiptViewModel.getActionResult().observe(this, { result ->
            when(result.status) {
                Resource.Status.LOADING -> {}
                Resource.Status.ERROR -> {}
                Resource.Status.SUCCESS -> {
                    activity!!.supportFragmentManager.popBackStack()
                }
            }
        })
    }

    override fun createViewModel() {
        super.createViewModel()
        receiptViewModel.initReceiptData(receiptId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiptViewModel.fetchReceipt(receiptId)

//        receiptViewModel.initReceiptData(receiptId)
        receiptViewModel.getReceipt().observe(this, { result ->
//            when(result.status) {
//                Resource.Status.LOADING -> {}
//                Resource.Status.ERROR -> {}
//                Resource.Status.SUCCESS -> {
//                    result.data?.removeObservers(this)
//                    result.data?.observe(this, { receiptWithItems ->
//                        receiptWithItems?.let {
//                            binding.btnAdd.text = context?.getString(R.string.edit)
//                            binding.storeName.setText(it.receipt.merchant)
//                            binding.total.text = parsePrice(it.receipt.totalPrice)
//                            binding.category.text = it.category.name
//
//                            receiptViewModel.setItems(it.items)
//
//                            binding.btnRepeat.visibility = View.VISIBLE
//                            binding.btnDelete.visibility = View.VISIBLE
//                            binding.btnRepeat.setOnClickListener(this)
//                            binding.btnDelete.setOnClickListener(this)
//
//                            receiptViewModel.setDate(it.receipt.date)
//                        }
//                    })
//                }
//            }
            result?.let {
                binding.btnAdd.text = context?.getString(R.string.edit)
                binding.storeName.setText(it.receipt.merchant)
                binding.total.text = parsePrice(it.receipt.totalPrice)
                binding.category.text = it.category.name

                receiptViewModel.setItems(it.items)

                binding.btnRepeat.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnRepeat.setOnClickListener(this)
                binding.btnDelete.setOnClickListener(this)

                receiptViewModel.setDate(it.receipt.date)
            }
        })

        receiptViewModel.getStatus().observe(
            this, { result ->
                when(result.status) {
                    //TODO handle this
                }
            }
        )
    }

    override fun addReceipt() {
        updateReceiptItems()
        receiptViewModel.updateReceipt(
            Receipt(receiptId, binding.storeName.text.toString(), null,
            receiptViewModel.getTotalPrice(), Currency.HRK, ReceiptType.DETAILED, 0),
            binding.category.text.toString()
        )
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.btnRepeat -> repeatReceipt()
            binding.btnDelete -> deleteReceipt()
        }
    }

    private fun repeatReceipt() {
        receiptViewModel.setupRepeatReceipt()
        super.addReceipt()
    }

    private fun deleteReceipt() {
        receiptViewModel.deleteReceipt(receiptId.toInt())
    }

    /*init {
        createViewModel()
        receiptViewModel.fetchReceipt(receiptId)
    }*/
}
