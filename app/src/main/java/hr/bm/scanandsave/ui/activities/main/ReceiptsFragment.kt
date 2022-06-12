package hr.bm.scanandsave.ui.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseFragment
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.databinding.FragmentMainBinding
import hr.bm.scanandsave.databinding.FragmentReceiptsBinding
import hr.bm.scanandsave.utils.Resource
import hr.bm.scanandsave.utils.ResourceStatus
import hr.bm.scanandsave.utils.ViewModelFactory
import hr.bm.scanandsave.utils.dateToString
import javax.inject.Inject

class ReceiptsFragment(private val parentViewModel: MainViewModel) : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentReceiptsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var receiptsViewModel: ReceiptsViewModel
//    private lateinit var parentViewModel: MainViewModel

    private lateinit var adapter: ReceiptsListAdapter

    private var mScrollRecyclerViewY = 0

    override fun layoutRes(): Int {
        return R.layout.fragment_receipts
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentReceiptsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()

        initViews()

        setObservers()

        setOnClickListeners()

        setTitleToToolbarListener()
    }

    private fun setObservers() {
//        parentViewModel.getReceipts().observe(viewLifecycleOwner) { receipts ->
//            //TODO update recyclerview
//        }
//        parentViewModel.getReloadReceipts().observe(viewLifecycleOwner) { reload ->
////            if (reload) {
//            receiptsViewModel.fetchReceipts()
////            }
//        }

//        receiptsViewModel.getListLoading().observe(viewLifecycleOwner) { loading ->
//            binding.recyclerView.setLoading(loading)
//        }
    }

    private fun setOnClickListeners() {

    }

    private fun createViewModel() {
        receiptsViewModel = viewModelFactory.let {
            ViewModelProvider(this, it)[ReceiptsViewModel::class.java]
        }
//        parentViewModel = viewModelFactory.let {
//            ViewModelProvider(this, it)[MainViewModel::class.java]
//        }
    }

    private fun initViews() {
        adapter = ReceiptsListAdapter(receiptsViewModel, this, activity!!, parentViewModel)
        binding.recyclerView.setAdapter(adapter)
        binding.recyclerView.setLayoutManager(LinearLayoutManager(context))
        binding.recyclerView.setProgressLayout(R.layout.view_receipt_placeholder)

        val recyclerItemDecoration = RecyclerItemDecoration(
            context!!,
            resources.getDimensionPixelSize(R.dimen.header_height),
            true,
            getSectionCallback(receiptsViewModel, this)
        )
        binding.recyclerView.addItemDecoration(recyclerItemDecoration)
    }

    //TODO testirat na vise uredaja razlicite velicine
    private fun setTitleToToolbarListener() {
        val tv = TypedValue()
        var actionBarHeight = 0
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight =
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        val displayMetrics = DisplayMetrics()
        baseActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val diffCompensation = height / DIFF_COMPENSATION_DIVIDER
        val scrollingOffset = height / SCROLLING_OFFSET_DIVIDER
        val scrollingRange = height.toFloat() / SCROLLING_RANGE_DIVIDER

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollRecyclerViewY += dy
                val diff = mScrollRecyclerViewY - actionBarHeight + diffCompensation
                val percentage = diff.toFloat() / scrollingOffset

                val titleView =
                    baseActivity!!.supportActionBar!!.customView.findViewById<TextView>(R.id.title)
                when {
                    diff in 0..scrollingOffset -> titleView.translationY =
                        scrollingRange * (1 - percentage)
                    diff < 0 -> titleView.translationY = scrollingRange
                    diff > scrollingOffset -> titleView.translationY = 0f
                }
            }
        })
    }

    private fun getSectionCallback(
        viewModel: ReceiptsViewModel,
        lifecycleOwner: LifecycleOwner
    ): RecyclerItemDecoration.SectionCallback {
        return object : RecyclerItemDecoration.SectionCallback {

            private val data: MutableList<Receipt> = mutableListOf()

            override fun isSection(pos: Int): Boolean {
                return pos != 0 && (pos == 1 || data[pos - 1].date?.date != data[pos - 2].date?.date)
            }

            override fun getSectionHeaderName(pos: Int): String {
                if (pos <= 0 || pos > data.size || data[pos - 1].date == null)
                    return ""

                val dataMap = data[pos - 1]
                return dateToString(dataMap.date!!)
            }

            init {
                // Set observer for updating data
                viewModel.getReceipts().observe(
                    lifecycleOwner, { result ->
//                        if (result?.status == Resource.Status.SUCCESS) {
//                            //result.data?.removeObservers(lifecycleOwner)
////                            result.data?.observe(lifecycleOwner, { receipts ->
////                                data.clear()
////                                if (receipts != null) {
////                                    data.addAll(receipts)
////                                }
////                            })
//                        }
                        data.clear()
                        data.addAll(result)
                    })

                viewModel.getStatus().observe(
                    lifecycleOwner, {
                        binding.recyclerView.setLoading(it.status == ResourceStatus.Status.LOADING)
                    }
                )
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {

        }
    }

    companion object {
        //diff_compensator -> lower value means earlier text pull up
        //scrolling_offset -> lower value means slower pulling up
        //scrolling_range -> lower value means bigger scrolling offset of textView
        private const val DIFF_COMPENSATION_DIVIDER = 20
        private const val SCROLLING_OFFSET_DIVIDER = 18
        private const val SCROLLING_RANGE_DIVIDER = 15
    }
}