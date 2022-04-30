package hr.bm.scanandsave.ui.activities.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseFragment
import hr.bm.scanandsave.database.entities.Category
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.databinding.ViewCategoryBinding
import hr.bm.scanandsave.utils.parseDate
import hr.bm.scanandsave.utils.parsePrice
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class CategoryListAdapter
    internal constructor(private val parentFragment: BottomSheetDialogFragment,
                         lifecycleOwner: LifecycleOwner, private val parentViewModel: MainViewModel)
    : RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    private val data: MutableList<Category> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_category, parent, false)
        return CategoryViewHolder(parentFragment, view, parentViewModel)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CategoryViewHolder(private val parentFragment: BottomSheetDialogFragment, itemView: View, private val parentViewModel: MainViewModel)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var binding: ViewCategoryBinding
        private lateinit var mItem: Category

        fun bind(item: Category) {
            mItem = item

            binding = ViewCategoryBinding.bind(itemView)

            binding.category.text = item.name
            binding.container.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            setCategory()
        }

        private fun setCategory() {
            parentViewModel.setCategory(mItem)
            parentFragment.dismiss()
        }
    }

    init {
        parentViewModel.getCategories().observe(lifecycleOwner, { categories ->
            data.clear()
            if (categories != null) {
                data.addAll(categories)
                notifyDataSetChanged()
            }
        })
        setHasStableIds(true)
    }
}