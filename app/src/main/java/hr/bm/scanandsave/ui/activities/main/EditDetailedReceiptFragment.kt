package hr.bm.scanandsave.ui.activities.main

import android.os.Bundle
import android.view.View
import hr.bm.scanandsave.R
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.enums.Currency
import hr.bm.scanandsave.enums.ReceiptType
import hr.bm.scanandsave.utils.parsePrice
import java.util.*

class EditDetailedReceiptFragment(private val parentViewModel: MainViewModel, private val receiptId: Long): DetailedReceiptFragment(parentViewModel, null) {

    override fun setObservers() {
        super.setObservers()

        receiptViewModel.getReceipt().observe(this, { receiptWithItems ->
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
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    init {
        receiptViewModel.fetchReceipt(receiptId)
    }
}
