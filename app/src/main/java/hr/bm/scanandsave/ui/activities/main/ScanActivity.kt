package hr.bm.scanandsave.ui.activities.main

import android.content.Intent
import android.content.res.Configuration
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.microblink.*
import com.microblink.camera.hardware.SuccessCallback
import com.microblink.core.ScanResults
import com.microblink.core.Timberland
import hr.bm.scanandsave.R
import java.io.File
import java.lang.Exception
import java.util.*

class ScanActivity : AppCompatActivity(), CameraRecognizerCallback {

    private var recognizerView: RecognizerView? = null
    private var finishScan: Button? = null
    private var torch: View? = null
    private var isTorchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_scan)
        recognizerView = findViewById(R.id.recognizer)
        finishScan = findViewById(R.id.finish_scan)
        finishScan!!.setOnClickListener { view: View ->
            try {
                Toast.makeText(applicationContext, "Finishing", Toast.LENGTH_SHORT)
                    .show()
                view.isEnabled = false
                recognizerView!!.finishedScanning()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        torch = findViewById(R.id.torch)
        torch!!.setOnClickListener {
            recognizerView!!.setTorchState(!isTorchOn
            ) { success: Boolean ->
                if (success) {
                    isTorchOn = !isTorchOn
                }
            }
        }
        val captureFrame = findViewById<Button>(R.id.capture_photo)
        captureFrame.setOnClickListener { v: View? ->
            recognizerView!!.takePicture(object : CameraCaptureListener {
                override fun onCaptured(results: BitmapResult) {
                    recognizerView!!.confirmPicture(results)
                    Toast.makeText(
                        applicationContext,
                        "Captured photo",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onException(e: Throwable) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }
        val regionOfInterest = RectF(.05f, .10f, .95f, .90f)
        recognizerView!!.recognizerCallback(this)
        recognizerView!!.scanRegion(regionOfInterest)
        recognizerView!!.setMeteringAreas(
            arrayOf(
                regionOfInterest
            ), true
        )
        try {
            recognizerView!!.initialize(
                Objects.requireNonNull(
                    intent
                        .getParcelableExtra(SCAN_OPTIONS_EXTRA)
                )
            )
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
            finish()
        }
        recognizerView!!.create()
    }

    public override fun onStart() {
        super.onStart()
        if (recognizerView != null) {
            recognizerView!!.start()
        }
    }

    public override fun onResume() {
        super.onResume()
        if (recognizerView != null) {
            recognizerView!!.resume()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (recognizerView != null) {
            recognizerView!!.pause()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (recognizerView != null) {
            recognizerView!!.stop()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (recognizerView != null) {
            try {
                recognizerView!!.destroy()
            } catch (e: Exception) {
                Timberland.e(e)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (recognizerView != null) {
            recognizerView!!.changeConfiguration(newConfig)
        }
    }

    override fun onRecognizerDone(results: ScanResults, media: Media) {
        finishScan!!.isEnabled = true
        setResult(
            RESULT_OK, Intent()
                .putExtra(DATA_EXTRA, results)
                .putExtra(MEDIA_EXTRA, media)
        )
        finish()
    }

    override fun onRecognizerException(throwable: Throwable) {
        finishScan!!.isEnabled = true
        Toast.makeText(applicationContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onRecognizerResultsChanged(result: RecognizerResult) {
        Timberland.d("results: $result")
    }

    override fun onConfirmPicture(file: File) {
        Timberland.d(file.toString())
    }

    override fun onPermissionDenied() {}
    override fun onPreviewStarted() {
        if (recognizerView!!.isCameraTorchSupported) {
            torch!!.visibility = View.VISIBLE
        } else {
            torch!!.visibility = View.GONE
        }
    }

    override fun onPreviewStopped() {}
    override fun onException(throwable: Throwable) {
        Timberland.e(throwable)
        Toast.makeText(applicationContext, throwable.toString(), Toast.LENGTH_LONG).show()
    }

    companion object {
        const val DATA_EXTRA = "dataExtra"
        const val MEDIA_EXTRA = "mediaExtra"
        const val SCAN_OPTIONS_EXTRA = "scanOptionsExtra"
    }
}