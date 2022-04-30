package hr.bm.scanandsave.ui.activities.main

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseActivity
import hr.bm.scanandsave.base.BaseFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_ActionBar_Grey)
        super.onCreate(savedInstanceState)
    }

    override fun createFragment(): BaseFragment {
        return MainFragment()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }
}