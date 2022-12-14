package com.example.lab.view.fragment
import android.app.AlertDialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab.R
import com.example.lab.adapter.UnAuthReservAdapter
import com.example.lab.application.MyApplication
import com.example.lab.data.responseDto.ReservResponseDto
import com.example.lab.databinding.FragmentNotificationBinding
import com.example.lab.utils.DateManager
import com.example.lab.view.activity.MainActivity
import com.example.lab.view.viewinitializer.ViewInitializerFactory
import com.example.lab.viewmodel.ReservViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentNotificationBinding
    private lateinit var reservVM:ReservViewModel
    private lateinit var callback:OnBackPressedCallback
    private lateinit var lablist:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        reservVM = ViewModelProvider(this)[ReservViewModel::class.java]
        activity?.let {
            (it as MainActivity).apply {
                hideTitleBar()
                hideBottomNavBar()
            }
        }

        /**
         * ?????? ??????????????? ????????? ?????????
         */
        MyApplication.member?.let {
            ViewInitializerFactory().getInitializer(it.role, "NOTIFICATION").init(this, bind)
        }

        initView()
        initRecyclerView()
        initLabSpinner()

        return bind.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(){
        bind.todayTV.text = DateManager.getDateUntilDate(Calendar.getInstance().timeInMillis)
    }

    /** ????????? ?????? ????????? ????????? */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initLabSpinner(){
        // ???????????? ????????? ???????????? ???????????? ????????? ????????? xml??? ?????? ???????????? ?????? ??????
        lablist = resources.getStringArray(R.array.lab_list)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lablist)

        // ????????? ??????
        bind.labSelector.adapter = spinnerAdapter
        bind.labSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @SuppressLint("SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("${lablist[position]} ?????? ???", "")
                // ?????? ?????? ?????? ????????? ??????????????? ???????????? ????????? ?????? ???????????? ?????????
                reservVM.unauthReservList.value?.let {
                    (bind.notifyRecyclerView.adapter as UnAuthReservAdapter).groupingDataList(it, lablist[position])
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    /**
     * ?????????????????? ?????????
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView(){
        // ?????? ?????? ????????? ??????
        reservVM.getUnauthReservs()

        reservVM.unauthReservList.observe(viewLifecycleOwner){
            bind.notifyRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = UnAuthReservAdapter(it)
                // ????????? ???????????? ?????? ???????????? ?????????
                (adapter as UnAuthReservAdapter).groupingDataList(it, bind.labSelector.selectedItem as String)
            }
        }
        // ?????? ?????? ?????? ?????????
        bind.approvalBtn.setOnClickListener {
            val list = (bind.notifyRecyclerView.adapter as UnAuthReservAdapter).getSelectedItem()

            reservVM.authReservs(list, true)
        }

        // ?????? ?????? ?????? ?????????
        bind.rejectBtn.setOnClickListener {
            val list = (bind.notifyRecyclerView.adapter as UnAuthReservAdapter).getSelectedItem()

            reservVM.authReservs(list, false)
        }

        // ?????? ?????? ?????????
        reservVM.authResult.observe(viewLifecycleOwner){ result ->
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle("?????? ??????")
                    setMessage(result.message?:"????????? ??????????????????.")
                    setPositiveButton("??????") { dialog, _ -> dialog.dismiss()}
                }
                builder.create()
            }
            alertDialog?.show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction().remove(this@NotificationFragment).commit();
                requireActivity().supportFragmentManager.popBackStack();
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        activity?.let {
            (it as MainActivity).apply {
                showTitleBar()
                showBottomNavBar()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}