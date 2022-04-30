package hr.bm.scanandsave.ui.activities.register

import dagger.Module
import dagger.android.ContributesAndroidInjector
import hr.bm.scanandsave.di.modules.ViewModelModule

@Module
abstract class RegisterFragmentBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideRegisterFragment(): RegisterFragment
}