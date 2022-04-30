package hr.bm.scanandsave.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.bm.scanandsave.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hr.bm.scanandsave.di.util.ViewModelKey
import hr.bm.scanandsave.ui.activities.login.LoginViewModel
import hr.bm.scanandsave.ui.activities.main.*
import hr.bm.scanandsave.ui.activities.register.RegisterViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(loginViewModel: MainViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReceiptsViewModel::class)
    abstract fun bindReceiptsViewModel(receiptsViewModel: ReceiptsViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GraphViewModel::class)
    abstract fun bindGraphViewModel(graphViewModel: GraphViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatsViewModel::class)
    abstract fun bindStatsViewModel(statsViewModel: StatsViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel?): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailedReceiptViewModel::class)
    abstract fun bindDetailedReceiptViewModel(detailedReceiptViewModel: DetailedReceiptViewModel?): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory?): ViewModelProvider.Factory
}