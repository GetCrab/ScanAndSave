package hr.bm.scanandsave.ui.activities.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.support.AndroidSupportInjection
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseFragment
import hr.bm.scanandsave.databinding.FragmentLoginBinding
import hr.bm.scanandsave.databinding.FragmentRegisterBinding
import hr.bm.scanandsave.utils.ViewModelFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class RegisterFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var registerViewModel: RegisterViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_register
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.firstNameLayout.setRequired()
        binding.lastNameLayout.setRequired()
        binding.usernameLayout.setRequired()
        binding.passwordLayout.setRequired()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()

        setOnClickListeners()

        setObservers()
    }

    private fun setObservers() {
        //Close activity when user finishes registration
        val liveData = registerViewModel.shouldCloseLiveData
        liveData.observe(viewLifecycleOwner, {
            requireActivity().finish()
            liveData.removeObservers(this)
        })

        //Show progress bar when loading
        registerViewModel.getLoading().observe(viewLifecycleOwner, { loading ->
            if (loading)
                binding.layoutProgressBar.visibility = View.VISIBLE
            else
                binding.layoutProgressBar.visibility = View.INVISIBLE
        })

        //Subscribe to first name and last name value change, check if they are empty
        val firstNameTextChange = RxTextView.textChanges(binding.firstName)
        val lastNameTextChange = RxTextView.textChanges(binding.lastName)
        val usernameTextChange = RxTextView.textChanges(binding.username)
        val passwordTextChange = RxTextView.textChanges(binding.password)
        addToDisposable(Observable.combineLatest(listOf(firstNameTextChange, lastNameTextChange,
            usernameTextChange, passwordTextChange)) {
            val firstName = it[0].toString()
            val lastName = it[1].toString()
            val username = it[2].toString()
            val password = it[3].toString()

            //If first name or last name is empty disable register button
            binding.btnRegister.isEnabled = firstName.length in 1..30 && lastName.length in 1..30
                    && username.isNotEmpty() && password.isNotEmpty()
        }.subscribeOn(AndroidSchedulers.mainThread()).subscribe())
    }

    private fun setOnClickListeners() {
        binding.btnRegister.setOnClickListener(this)
    }

    private fun createViewModel() {
        registerViewModel = viewModelFactory.let {
            ViewModelProvider(this, it)[RegisterViewModel::class.java]
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnRegister -> registerViewModel.saveData(
                binding.username.text.toString(),
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.password.text.toString()
            )
        }
    }
}