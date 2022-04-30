package hr.bm.scanandsave.ui.activities.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseFragment
import hr.bm.scanandsave.databinding.FragmentLoginBinding
import hr.bm.scanandsave.utils.ViewModelFactory
import hr.bm.scanandsave.utils.getErrorMessage
import javax.inject.Inject

class LoginFragment : BaseFragment(), View.OnClickListener {

    private var binding: FragmentLoginBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var loginViewModel: LoginViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_login
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()

        setObservers()

        setOnClickListeners()
    }

    private fun setObservers() {
        loginViewModel.getUser().observe(viewLifecycleOwner) { user ->
            binding?.username?.setText(user.username)
        }

        //Show progress bar when loading
        loginViewModel.getLoading().observe(viewLifecycleOwner, { loading ->
            if (loading)
                binding?.layoutProgressBar?.visibility = View.VISIBLE
            else
                binding?.layoutProgressBar?.visibility = View.INVISIBLE
        })

        loginViewModel.getError().observe(viewLifecycleOwner, { error ->
            binding!!.passwordLayout.showError(getErrorMessage(context, error))
        })
    }

    private fun setOnClickListeners() {
        binding?.btnLogin?.setOnClickListener(this)
    }

    private fun createViewModel() {
        loginViewModel = viewModelFactory.let {
            ViewModelProvider(this, it)[LoginViewModel::class.java]
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding?.btnLogin -> loginViewModel.login(binding?.password?.text.toString())
        }
    }
}