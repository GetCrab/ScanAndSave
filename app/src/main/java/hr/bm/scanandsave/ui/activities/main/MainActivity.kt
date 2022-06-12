package hr.bm.scanandsave.ui.activities.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.microblink.BlinkReceiptSdk
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseActivity
import hr.bm.scanandsave.base.BaseFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_ActionBar_Grey)
        super.onCreate(savedInstanceState)

        BlinkReceiptSdk.debug(true)
    }

    override fun createFragment(): BaseFragment {
        return MainFragment()
    }

    @SuppressLint("RestrictedApi")
    fun hideToolbar() {
        supportActionBar?.setShowHideAnimationEnabled(false)
        supportActionBar?.hide()
    }
}