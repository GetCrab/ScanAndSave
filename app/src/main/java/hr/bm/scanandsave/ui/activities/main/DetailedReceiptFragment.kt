package hr.bm.scanandsave.ui.activities.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.microblink.BlinkReceiptSdk
import com.microblink.EdgeDetectionConfiguration
//import com.microblink.CameraScanActivity
import com.microblink.FrameCharacteristics
import com.microblink.ScanOptions
import com.microblink.core.ScanResults
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog
import dagger.android.support.AndroidSupportInjection
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseFragment
import hr.bm.scanandsave.enums.Category as CategoryEnum
import hr.bm.scanandsave.database.entities.Receipt
import hr.bm.scanandsave.database.entities.ReceiptItem
import hr.bm.scanandsave.databinding.FragmentDetailedReceiptBinding
import hr.bm.scanandsave.enums.Currency
import hr.bm.scanandsave.enums.ReceiptType
import hr.bm.scanandsave.utils.ViewModelFactory
import hr.bm.scanandsave.utils.parsePrice
import hr.bm.scanandsave.utils.showToast
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

open class DetailedReceiptFragment(private val parentViewModel: MainViewModel, private var data: Intent?)
    : BaseFragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener, EasyPermissions.PermissionCallbacks {

    protected lateinit var binding: FragmentDetailedReceiptBinding

    private lateinit var adapter: ReceiptItemsListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var receiptViewModel: DetailedReceiptViewModel

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val data = result.data
        if (data != null && result.resultCode == Activity.RESULT_OK) {
//            val scanResult: ScanResults? = data.getParcelableExtra(ScanActivity.DATA_EXTRA)
//            if (scanResult != null) {
//                val media: Media? = data.getParcelableExtra(ScanActivity.MEDIA_EXTRA)
            this.data = data
            initViews()
//            } else {
//                showToast(context!!, "Fetching data from receipt failed.")
//            }
        }
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_detailed_receipt
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentDetailedReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()

        setRecyclerView()

        initViews()

        setObservers()

        setOnClickListeners()
    }

    protected open fun setObservers() {
        receiptViewModel.getReceiptItems().observe(this, { items ->
            var total = 0.0
            items.forEach { item -> total += item.price }
            receiptViewModel.setTotalPrice(total)
            binding.total.text = parsePrice(total)
        })

        receiptViewModel.getReceiptSaved().observe(this, { saved ->
            if (saved) {
                parentViewModel.setReloadReceipts()
                activity!!.supportFragmentManager.popBackStack()
            }
        })

        parentViewModel.getSelectedCategory().observe(this, { category ->
            if (category != null) {
                binding.category.text = category.name
            }
        })

        receiptViewModel.getDate().observe(this, { date ->
            if (date != null) {
                val formatter = SimpleDateFormat.getDateInstance()
                binding.date.text = formatter.format(date)
            }
        })
    }

    private fun setOnClickListeners() {
        binding.btnAdd.setOnClickListener(this)
        binding.btnRetake.setOnClickListener(this)
        binding.date.setOnClickListener(this)
    }

    protected open fun createViewModel() {
        receiptViewModel = viewModelFactory.let {
            ViewModelProvider(this, it)[DetailedReceiptViewModel::class.java]
        }
    }

    private fun initViews() {
        binding.total.text = parsePrice(0.0)
        binding.category.text = CategoryEnum.FOOD.name
        binding.category.setOnClickListener(this)
        binding.toolbar.setNavigationOnClickListener { activity!!.supportFragmentManager.popBackStack() }

        data?.let {
            val brScanResults: ScanResults? = data!!.getParcelableExtra(ScanActivity.DATA_EXTRA)
//            val media: Media? = data.getParcelableExtra(CameraScanActivity.MEDIA_EXTRA)

            val merchantName = brScanResults?.merchantName()?.value()
            if (merchantName == null) {
                binding.storeName.setText(brScanResults?.merchantMatchGuess()?.value())
            } else {
                binding.storeName.setText(brScanResults.merchantName()?.value())
            }

            val itemList = arrayListOf<ReceiptItem>()
            var totalPrice = 0.0
            brScanResults?.products()?.forEach {
                itemList.add(ReceiptItem(0, 0, it.productName() ?: it.description()?.value() ?: "" , it.description()?.value() ?: "", it.totalPrice()?.value()?.toDouble() ?: 0.0, it.quantity()?.value()?.toInt() ?: 1))

                if (it.totalPrice()?.value() != null) {
                    totalPrice += it.totalPrice()?.value()!!
                }
            }
            receiptViewModel.setItems(itemList)

            if (brScanResults?.total()?.value() != null && brScanResults.total()?.value()!! > totalPrice) {
                showToast(context!!, context!!.getString(R.string.missing_items))
            }

            binding.btnRetake.visibility = View.VISIBLE
        }
    }

    private fun setRecyclerView() {
        adapter = ReceiptItemsListAdapter(receiptViewModel, this, activity!!)
        binding.receiptsRecyclerView.adapter = adapter
        binding.receiptsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAdd -> addReceipt()
            binding.category -> showCategoryFragment()
            binding.btnRetake -> openScanActivity()
            binding.date -> showDatePicker()
        }
    }

    protected open fun addReceipt() {
        updateReceiptItems()
        receiptViewModel.addReceipt(Receipt(0, binding.storeName.text.toString(), null,
            receiptViewModel.getTotalPrice(), Currency.HRK, ReceiptType.DETAILED, 0), binding.category.text.toString())
    }

    protected fun updateReceiptItems() {
        for (i in 0..(adapter.itemCount - 2)) {
            (binding.receiptsRecyclerView.findViewHolderForAdapterPosition(i) as ReceiptItemsListAdapter.ItemsViewHolder?)?.updateItem()
        }
    }

    fun deleteReceiptItem(item: ReceiptItem) {
        val itemView = binding.receiptsRecyclerView.findViewHolderForAdapterPosition(receiptViewModel.getReceiptItems().value!!.indexOf(item))
        adapter.deleteItem(itemView?.itemView, item)
    }

    private fun showCategoryFragment() {
        CategoryBottomSheetFragment(parentViewModel).show(activity!!.supportFragmentManager, "")
    }

    private fun showAddReceiptFragment() {
        AddReceiptBottomSheetFragment(parentViewModel).show(activity!!.supportFragmentManager, "add_receipt")
    }

    private fun showDatePicker() {
        val date = receiptViewModel.getDate().value ?: return
        val calendarDate = Calendar.getInstance()
        calendarDate.time = date

        DatePickerDialog.Builder(
            this,
            calendarDate.get(Calendar.YEAR),
            calendarDate.get(Calendar.MONTH),
            calendarDate.get(Calendar.DAY_OF_MONTH)
        ).build().show(activity!!.supportFragmentManager, "DATE_DIALOG")
    }

    private fun openScanActivity() {
        if (EasyPermissions.hasPermissions(context!!, Manifest.permission.CAMERA)) {
//            val scanOptions: ScanOptions = ScanOptions.newBuilder()
//                .frameCharacteristics(
//                    FrameCharacteristics.newBuilder()
//                        .storeFrames(true)
//                        .compressionQuality(100)
//                        .externalStorage(false)
//                        .build()
//                )
//                .countryCode("HR")
//                .logoDetection(true)
//                .build()
//
//            val bundle = Bundle()
//            bundle.putParcelable(CameraScanActivity.SCAN_OPTIONS_EXTRA, scanOptions)
//            val intent = Intent(context, CameraScanActivity::class.java)
//                .putExtra(CameraScanActivity.BUNDLE_EXTRA, bundle)
//
//            startActivityForResult(intent, AddReceiptBottomSheetFragment.SCAN_RECEIPT_REQUEST)


            launcher.launch(
                Intent(context, ScanActivity::class.java)
                    .putExtra(
                        ScanActivity.SCAN_OPTIONS_EXTRA, ScanOptions.newBuilder()
                        .frameCharacteristics(
                            FrameCharacteristics.newBuilder()
                                .storeFrames(true)
                                .compressionQuality(100)
                                .externalStorage(false)
                                .build()
                        )
                        .countryCode("HR")
                        .detectDuplicates(true)
                        .logoDetection(true)
                        .edgeDetectionConfiguration(EdgeDetectionConfiguration.newBuilder().build())
                        .build()
                    )
            )
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.camera_permission_rationale),
                1, Manifest.permission.CAMERA
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        openScanActivity()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // Do nothing
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == AddReceiptBottomSheetFragment.SCAN_RECEIPT_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
//            this.data = data
//            initViews()
//        }
//    }

    override fun onDateSet(
        dialog: DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        val cal: Calendar = GregorianCalendar()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = monthOfYear
        cal[Calendar.DAY_OF_MONTH] = dayOfMonth

        receiptViewModel.setDate(cal.time)
    }

    companion object {
        const val TAG: String = "DetailedReceipt"
    }
}