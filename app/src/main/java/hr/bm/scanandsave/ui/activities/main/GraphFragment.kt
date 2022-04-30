package hr.bm.scanandsave.ui.activities.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseFragment
import hr.bm.scanandsave.databinding.FragmentGraphBinding
import hr.bm.scanandsave.databinding.FragmentMainBinding
import hr.bm.scanandsave.databinding.FragmentReceiptsBinding
import hr.bm.scanandsave.utils.ViewModelFactory
import javax.inject.Inject

class GraphFragment : BaseFragment(), View.OnClickListener {

    private var binding: FragmentGraphBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var graphViewModel: GraphViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_receipts
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()

        setObservers()

        setOnClickListeners()
    }

    private fun setObservers() {

    }

    private fun setOnClickListeners() {

    }

    private fun createViewModel() {
        graphViewModel = viewModelFactory.let {
            ViewModelProvider(this, it)[GraphViewModel::class.java]
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(v: View?) {
        when (v) {

        }
    }
}