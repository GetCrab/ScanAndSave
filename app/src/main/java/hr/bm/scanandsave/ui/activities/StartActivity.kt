package hr.bm.scanandsave.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import hr.bm.scanandsave.ui.activities.login.LoginActivity
import hr.bm.scanandsave.ui.activities.register.RegisterActivity
import hr.bm.scanandsave.utils.AppConstants
import hr.bm.scanandsave.utils.getPreferenceBool

class StartActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Launch a different activity
        val launchIntent = Intent()
        val launchActivity: Class<*> = try {
            val className = getScreenClassName()
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            RegisterActivity::class.java
        }
        launchIntent.setClass(applicationContext, launchActivity)
        startActivity(launchIntent)
        finish()
    }

    private fun getScreenClassName(): String {
        //If user is registered start login activity, otherwise start register activity
        return if (getPreferenceBool(applicationContext, AppConstants.REGISTER_PREFERENCE)) {
            LoginActivity::class.java.name
        } else {
            RegisterActivity::class.java.name
        }
    }
}