package hr.bm.scanandsave.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import dagger.android.support.DaggerAppCompatActivity
import hr.bm.scanandsave.R
import hr.bm.scanandsave.ui.activities.login.LoginFragment
import hr.bm.scanandsave.ui.activities.main.MainFragment

abstract class BaseActivity : DaggerAppCompatActivity() {

    @LayoutRes
    open fun layoutRes(): Int {
        return R.layout.activity_default
    }

    open fun getRootView() : View? {
        return null
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val rootView = getRootView()
        if (rootView == null)
            setContentView(layoutRes())
        else
            setContentView(rootView)

//        ButterKnife.bind(this)

        if (savedInstanceState == null)
            setDefaultFragment(createFragment())
    }

    private fun setDefaultFragment(fragment: BaseFragment?) {
        if (fragment == null)
            return

        supportFragmentManager.beginTransaction()
            .add(R.id.screenContainer, fragment).commit()
    }

    protected abstract fun createFragment(): BaseFragment?

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            val fragment = supportFragmentManager.findFragmentById(R.id.screenContainer)
            if (supportFragmentManager.findFragmentById(R.id.screenContainer) is MainFragment) {
                if ((fragment as MainFragment).handleBackPressed())
                    return
            }
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}