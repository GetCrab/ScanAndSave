package hr.bm.scanandsave.ui.activities.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import hr.bm.scanandsave.di.modules.ViewModelModule

@Module
abstract class MainFragmentBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideReceiptsFragment(): ReceiptsFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideGraphFragment(): GraphFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideStatsFragment(): StatsFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideSearchFragment(): SearchFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideMainFragment(): MainFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideBottomSheetFragment(): AddReceiptBottomSheetFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideReceiptItemFragment(): AddReceiptItemFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideEditReceiptItemFragment(): EditReceiptItemBottomSheetFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideSimpleReceiptDialogFragment(): SimpleReceiptDialogFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideDetailedReceiptFragment(): DetailedReceiptFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideEditDetailedReceiptFragment(): EditDetailedReceiptFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideCategoryBottomSheetFragment(): CategoryBottomSheetFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun provideAddCategoryDialogFragment(): AddCategoryDialogFragment
}