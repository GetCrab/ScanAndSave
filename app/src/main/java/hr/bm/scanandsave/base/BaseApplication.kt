package hr.bm.scanandsave.base

import com.microblink.BlinkReceiptSdk
import dagger.android.support.DaggerApplication
import hr.bm.scanandsave.di.component.ApplicationComponent
import hr.bm.scanandsave.di.component.DaggerApplicationComponent

class BaseApplication : DaggerApplication() {

    override fun applicationInjector(): ApplicationComponent {
        val component: ApplicationComponent =
            DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)
        return component
    }

    override fun onTerminate() {
        BlinkReceiptSdk.terminate()
        super.onTerminate()
    }
}