package hr.bm.scanandsave.ui.activities.login

import android.os.Bundle
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseActivity
import hr.bm.scanandsave.base.BaseFragment

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBar)
        super.onCreate(savedInstanceState)
    }

    override fun createFragment(): BaseFragment {
        return LoginFragment()
    }
}