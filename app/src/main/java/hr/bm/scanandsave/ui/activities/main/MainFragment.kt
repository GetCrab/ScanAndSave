package hr.bm.scanandsave.ui.activities.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import hr.bm.scanandsave.R
import hr.bm.scanandsave.base.BaseFragment
import hr.bm.scanandsave.databinding.FragmentGraphBinding
import hr.bm.scanandsave.databinding.FragmentMainBinding
import hr.bm.scanandsave.utils.ViewModelFactory
import java.util.*
import javax.inject.Inject

class MainFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mViewModel: MainViewModel

    private lateinit var mainViewPagerAdapter: MainViewPagerAdapter
    private lateinit var fragmentList: List<Fragment>

    private var mPagerTabBackstack: LinkedList<Int> = LinkedList()

    override fun layoutRes(): Int {
        return R.layout.fragment_main
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()
        initViewPager()
        setObservers()
        setOnClickListeners()
        setActionBar()

        val drawerToggle = ActionBarDrawerToggle(activity, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

//        binding.toolbar.setDisplayHomeAsUpEnabled(true)
    }

    private fun createViewModel() {
        mViewModel = viewModelFactory.let {
            ViewModelProvider(this, it)[MainViewModel::class.java]
        }

        //TODO move this somewhere else?
        fragmentList = listOf<Fragment>(
            ReceiptsFragment(mViewModel),
            GraphFragment(),
            StatsFragment(),
            SearchFragment(),
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun initViewPager() {
        mainViewPagerAdapter = activity?.let { MainViewPagerAdapter(it, fragmentList) }!!
        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = mainViewPagerAdapter
        }

        DrawableCompat.setTint(binding.receiptTab.drawable, ContextCompat.getColor(activity!!, R.color.colorAccent))
    }

    //TODO ovo prebacit u MainActivity i ostavit samo clickListener?
    private fun setActionBar() {
        baseActivity?.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        baseActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        baseActivity?.supportActionBar?.setDisplayShowCustomEnabled(true)
        baseActivity?.supportActionBar?.setCustomView(R.layout.view_title_toolbar)
        baseActivity?.supportActionBar?.elevation = 0F
        baseActivity?.supportActionBar?.customView?.findViewById<ImageButton>(R.id.btnDrawer)?.setOnClickListener { showMenu() }
        baseActivity?.supportActionBar?.customView?.findViewById<TextView>(R.id.title)?.text = getString(R.string.receipts)
    }

    private fun setObservers() {

    }

    private fun setOnClickListeners() {
        binding.receiptTab.setOnClickListener(this)
        binding.graphTab.setOnClickListener(this)
        binding.statsTab.setOnClickListener(this)
        binding.searchTab.setOnClickListener(this)
        binding.addReceiptBtn.setOnClickListener(this)
//        binding.toolbar.setNavigationOnClickListener { showMenu() }
    }

    private fun showAddReceiptDialog() {
        AddReceiptBottomSheetFragment(mViewModel).show(activity!!.supportFragmentManager, "add_receipt")
    }

    override fun onClick(v: View?) {
        when(v) {
            //TODO find better solution for currentItem
            binding.receiptTab -> selectTab(0)
            binding.graphTab -> selectTab(1)
            binding.statsTab -> selectTab(2)
            binding.searchTab -> selectTab(3)
            binding.addReceiptBtn -> showAddReceiptDialog()
        }
    }

    private fun selectTab(index: Int) {
        mPagerTabBackstack.add(binding.viewPager.currentItem)
        binding.viewPager.currentItem = index
        when (index) {
            0 -> {
                DrawableCompat.setTint(binding.receiptTab.drawable, ContextCompat.getColor(activity!!, R.color.colorAccent))
                DrawableCompat.setTint(binding.graphTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.statsTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.searchTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
            }
            1 -> {
                DrawableCompat.setTint(binding.receiptTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.graphTab.drawable, ContextCompat.getColor(activity!!, R.color.colorAccent))
                DrawableCompat.setTint(binding.statsTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.searchTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
            }
            2 -> {
                DrawableCompat.setTint(binding.receiptTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.graphTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.statsTab.drawable, ContextCompat.getColor(activity!!, R.color.colorAccent))
                DrawableCompat.setTint(binding.searchTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
            }
            3 -> {
                DrawableCompat.setTint(binding.receiptTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.graphTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.statsTab.drawable, ContextCompat.getColor(activity!!, R.color.colorBlack))
                DrawableCompat.setTint(binding.searchTab.drawable, ContextCompat.getColor(activity!!, R.color.colorAccent))
            }
        }
    }

    private fun showMenu() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    fun handleBackPressed(): Boolean {
        if (mPagerTabBackstack.isEmpty()) {
            return false
        }

        selectTab(mPagerTabBackstack.last())
        mPagerTabBackstack.removeLast()
        mPagerTabBackstack.removeLast()
        return true
    }

    override fun onResume() {
        //TODO do this some other way, maybe completely custom toolbar, so that transition is not visible
        baseActivity?.supportActionBar?.show()
        super.onResume()
    }

}