package hr.bm.scanandsave.ui.activities.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.microblink.CameraScanActivity
import com.microblink.FrameCharacteristics
import com.microblink.ScanOptions
import hr.bm.scanandsave.R
import hr.bm.scanandsave.databinding.FragmentReceiptBottomSheetBinding
import pub.devrel.easypermissions.EasyPermissions
import androidx.activity.result.ActivityResult

import androidx.activity.result.contract.ActivityResultContracts
import hr.bm.scanandsave.ui.activities.main.ScanActivity.Companion.SCAN_OPTIONS_EXTRA
import com.microblink.EdgeDetectionConfiguration


class AddReceiptBottomSheetFragment(private val parentViewModel: MainViewModel) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentReceiptBottomSheetBinding

    @LayoutRes
    private fun layoutRes() : Int {
        return R.layout.fragment_receipt_bottom_sheet
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val data = result.data
        if (data != null && result.resultCode == Activity.RESULT_OK) {
//            val scanResult: ScanResults? = data.getParcelableExtra(ScanActivity.DATA_EXTRA)
//            if (scanResult != null) {
//                val media: Media? = data.getParcelableExtra(ScanActivity.MEDIA_EXTRA)
                (context as MainActivity).hideToolbar()
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.screenContainer, DetailedReceiptFragment(parentViewModel, data), DetailedReceiptFragment.TAG)
                    .addToBackStack(DetailedReceiptFragment.TAG)
                    .commit()
                dismiss()
//            } else {
//                showToast(context!!, "Fetching data from receipt failed.")
//            }
        }
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentReceiptBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.simpleReceipt.setOnClickListener(this)
        binding.detailedReceipt.setOnClickListener(this)
        binding.scanReceipt.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.simpleReceipt -> openSimpleReceiptDialog()
            binding.detailedReceipt -> openDetailedReceiptFragment()
            binding.scanReceipt -> openScanActivity()
        }
    }

    private fun openSimpleReceiptDialog() {
        SimpleReceiptDialogFragment(parentViewModel).show(parentFragmentManager, "simple_receipt")
        dismiss()
    }

    private fun openDetailedReceiptFragment() {
        if (activity == null)
            return

        (context as MainActivity).hideToolbar()
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.screenContainer, DetailedReceiptFragment(parentViewModel, null), "detailed_receipt")
            .addToBackStack("detailed_receipt")
            .commit()
        dismiss()
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
//                .detectDuplicates(true)
//                .logoDetection(true)
//                .build()
//
//            val bundle = Bundle()
//
//            bundle.putParcelable(CameraScanActivity.SCAN_OPTIONS_EXTRA, scanOptions)
//
//            val intent = Intent(context, CameraScanActivity::class.java)
//                .putExtra(CameraScanActivity.BUNDLE_EXTRA, bundle)
//
//            startActivityForResult(intent, SCAN_RECEIPT_REQUEST)

            launcher.launch(
                //TODO move scan options to viewmodel?
                Intent(context, ScanActivity::class.java)
                    .putExtra(SCAN_OPTIONS_EXTRA, ScanOptions.newBuilder()
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
}