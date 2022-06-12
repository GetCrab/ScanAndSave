package hr.bm.scanandsave.ui.activities.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import hr.bm.scanandsave.R
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.databinding.*
import hr.bm.scanandsave.utils.Resource
import hr.bm.scanandsave.utils.getStoreIconName
import hr.bm.scanandsave.utils.parseDate
import hr.bm.scanandsave.utils.parsePrice
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class ReceiptsListAdapter
    internal constructor(private val viewModel: ReceiptsViewModel, lifecycleOwner: LifecycleOwner,
                         private val context: FragmentActivity, private val parentViewModel: MainViewModel)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: MutableList<Receipt> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        val viewHolder: RecyclerView.ViewHolder

        if (viewType == HEADER_VIEW) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.view_receipts_header, parent, false)
            viewHolder = HeaderViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.view_receipt, parent, false)
            viewHolder = ReceiptViewHolder(context, view, this, viewModel, parentViewModel)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ReceiptViewHolder) {
            holder.bind(data[position - 1])
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return HEADER_VIEW
        }

        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun deleteItem(rowView: View?, receipt: Receipt) {
        if (rowView == null) {
            viewModel.deleteReceipt(receipt)
        } else {
            val anim: Animation = AnimationUtils.loadAnimation(rowView.context,
                android.R.anim.slide_out_right)
            anim.duration = 300
            rowView.startAnimation(anim)

            Completable.complete()
                .delay(300, TimeUnit.MILLISECONDS)
                .doOnComplete {
                    viewModel.deleteReceipt(receipt)
                }
                .subscribe()
        }
    }

    class ReceiptViewHolder(private val context: FragmentActivity, itemView: View, private val adapter: ReceiptsListAdapter,
                            private val viewModel: ReceiptsViewModel, private val parentViewModel: MainViewModel) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var binding: ViewReceiptBinding
        private lateinit var mItem: Receipt

        fun bind(item: Receipt) {
            mItem = item

            binding = ViewReceiptBinding.bind(itemView)

            binding.storeName.text = item.merchant
            binding.price.text = parsePrice(item.totalPrice)
            binding.receiptDate.text = parseDate(item.date)
            //TODO check for different loading
            binding.storeIcon.setImageDrawable(context.getDrawable(context.resources.getIdentifier(getStoreIconName(item.merchant), "drawable", context.packageName)))

//            binding.container.viewTreeObserver.addOnGlobalLayoutListener {
//                binding.btnRepeat.height = binding.container.height
//                binding.btnEdit.height = binding.container.height
//                binding.btnDelete.height = binding.container.height
//            }

            binding.container.setOnClickListener(this)
            binding.btnRepeat.setOnClickListener(this)
            binding.btnEdit.setOnClickListener(this)
            binding.btnDelete.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v) {
                binding.container -> showEditFragment()
                binding.btnRepeat -> repeatReceipt()
                binding.btnEdit -> editReceipt()
                binding.btnDelete -> deleteReceipt()
            }
            binding.root.close(true)
        }

        private fun repeatReceipt() {
            viewModel.repeatReceipt(mItem.receiptId)
        }

        private fun editReceipt() {
            showEditFragment()
        }

        private fun deleteReceipt() {
            adapter.deleteItem(itemView, mItem)
        }

        private fun showEditFragment() {
            (context as MainActivity).hideToolbar()
            context.supportFragmentManager.beginTransaction()
                .replace(R.id.screenContainer, EditDetailedReceiptFragment(parentViewModel, mItem.receiptId), "detailed_receipt")
                .addToBackStack("detailed_receipt").commit()
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding: ViewReceiptsHeaderBinding = ViewReceiptsHeaderBinding.bind(itemView)

    }

    init {
        viewModel.getReceipts().observe(lifecycleOwner, { result ->
//            if (result?.status == Resource.Status.SUCCESS) {
//                //result.data?.removeObservers(lifecycleOwner)
//                result.data?.observe(lifecycleOwner, { receipts ->
//                    data.clear()
//                    if (receipts != null) {
//                        data.addAll(receipts)
//                        notifyDataSetChanged()
//                    }
//                })
//            }
            data.clear()
            data.addAll(result)
            notifyDataSetChanged()
        })
        setHasStableIds(true)
    }

    companion object {
        private const val HEADER_VIEW = 1
    }
}