package hr.bm.scanandsave.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import hr.bm.scanandsave.ui.activities.login.LoginActivity
import hr.bm.scanandsave.ui.activities.login.LoginFragmentBindingModule
import hr.bm.scanandsave.ui.activities.main.MainActivity
import hr.bm.scanandsave.ui.activities.main.MainFragmentBindingModule
import hr.bm.scanandsave.ui.activities.register.RegisterActivity
import hr.bm.scanandsave.ui.activities.register.RegisterFragmentBindingModule

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [LoginFragmentBindingModule::class])
    abstract fun bindLoginActivity(): LoginActivity?

    @ContributesAndroidInjector(modules = [RegisterFragmentBindingModule::class])
    abstract fun bindRegisterActivity(): RegisterActivity?

    @ContributesAndroidInjector(modules = [MainFragmentBindingModule::class])
    abstract fun bindMainActivity(): MainActivity?
}