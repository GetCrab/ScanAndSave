package hr.bm.scanandsave.ui.activities.main

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.StringUtil
import butterknife.BindView
import butterknife.ButterKnife
import hr.bm.scanandsave.R
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.databinding.DialogSimpleReceiptBinding
import hr.bm.scanandsave.databinding.ViewAddItemFooterBinding
import hr.bm.scanandsave.databinding.ViewReceiptItemBinding
import hr.bm.scanandsave.utils.getPrice
import hr.bm.scanandsave.utils.parsePrice
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class ReceiptItemsListAdapter
    internal constructor(private val viewModel: DetailedReceiptViewModel, lifecycleOwner: LifecycleOwner, context: FragmentActivity)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: FragmentActivity = context
    private val data: MutableList<ReceiptItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        val viewHolder: RecyclerView.ViewHolder

        if (viewType == FOOTER_VIEW) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.view_add_item_footer, parent, false)
            viewHolder = FooterViewHolder(mContext, view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.view_receipt_item, parent, false)
            viewHolder = ItemsViewHolder(view, this, viewModel)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemsViewHolder) {
            holder.bind(data[position], position)
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == data.size) {
            return FOOTER_VIEW
        }

        return super.getItemViewType(position)
    }

    fun deleteItem(rowView: View?, item: ReceiptItem) {
        if (rowView == null) {
            viewModel.deleteReceiptItem(item)
        } else {
            val anim: Animation = AnimationUtils.loadAnimation(rowView.context,
                android.R.anim.slide_out_right)
            anim.duration = 300
            rowView.startAnimation(anim)

            Completable.complete()
                .delay(300, TimeUnit.MILLISECONDS)
                .doOnComplete {
                    viewModel.deleteReceiptItem(item)
                }
                .subscribe()
        }
    }

    class ItemsViewHolder(itemView: View, private val adapter: ReceiptItemsListAdapter,private val viewModel: DetailedReceiptViewModel)
        : RecyclerView.ViewHolder(itemView), View.OnLongClickListener, View.OnFocusChangeListener {

        private lateinit var binding: ViewReceiptItemBinding
        private lateinit var mItem: ReceiptItem
        private var mPosition = 0

        fun bind(item: ReceiptItem, position: Int) {
            mItem = item
            mPosition = position

            binding = ViewReceiptItemBinding.bind(itemView)

            binding.itemName.setText(item.name)
            binding.itemPrice.setText(parsePrice(item.price))
            binding.container.setOnLongClickListener(this)
            binding.itemName.setOnLongClickListener(this)
            binding.itemPrice.setOnLongClickListener(this)
            binding.itemName.onFocusChangeListener = this
            binding.itemPrice.onFocusChangeListener = this
        }

        override fun onLongClick(v: View?): Boolean {
            showEditFragment()
            return true
        }

        private fun showEditFragment() {
            updateItem()
            //TODO check for better solution (we want to set AddReceiptItemFragment as child of DetailedReceiptFragment)
            EditReceiptItemBottomSheetFragment(mItem, mPosition).show(adapter.mContext.supportFragmentManager.findFragmentByTag("detailed_receipt")!!.childFragmentManager, "")
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (!hasFocus) {
                updateItem()
            } else {
                if (v == binding.itemPrice) {
                    binding.itemPrice.setText(mItem.price.toString())
                }
            }
        }

        fun updateItem() {
            mItem.name = binding.itemName.text.toString()
            mItem.price = getPrice(binding.itemPrice.text.toString())
            viewModel.editReceiptItem(mPosition, mItem)
        }
    }

    class FooterViewHolder(context: FragmentActivity, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var mContext: FragmentActivity = context

        private var binding: ViewAddItemFooterBinding = ViewAddItemFooterBinding.bind(itemView)

        init {
            binding.btnAdd.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v) {
                binding.btnAdd -> addNewItem()
            }
        }

        private fun addNewItem() {
            //TODO check for better solution (we want to set AddReceiptItemFragment as child of DetailedReceiptFragment)
            AddReceiptItemFragment().show(mContext.supportFragmentManager.findFragmentByTag("detailed_receipt")!!.childFragmentManager, "add_item")
        }
    }

    init {
        viewModel.getReceiptItems().observe(lifecycleOwner, { items ->
            data.clear()
            if (items != null) {
                data.addAll(items)
                notifyDataSetChanged()
            }
        })
        setHasStableIds(true)
    }

    companion object {
        private const val FOOTER_VIEW = 1
    }
}