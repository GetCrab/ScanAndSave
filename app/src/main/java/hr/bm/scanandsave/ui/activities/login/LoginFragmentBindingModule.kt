package hr.bm.scanandsave.ui.activities.login

import dagger.Module
import dagger.android.ContributesAndroidInjector
import hr.bm.scanandsave.di.modules.ViewModelModule
import hr.bm.scanandsave.ui.activities.login.LoginFragment

@Module
abstract class LoginFragmentBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideLoginFragment(): LoginFragment
}