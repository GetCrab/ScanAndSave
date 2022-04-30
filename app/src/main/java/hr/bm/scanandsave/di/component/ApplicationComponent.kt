package hr.bm.scanandsave.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import hr.bm.scanandsave.base.BaseApplication
import hr.bm.scanandsave.di.modules.ActivityBindingModule
import hr.bm.scanandsave.di.modules.ApplicationModule
import hr.bm.scanandsave.di.modules.DatabaseModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, AndroidSupportInjectionModule::class, ActivityBindingModule::class, DatabaseModule::class])
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}