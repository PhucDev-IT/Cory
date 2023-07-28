package com.developer.cory.FragmentLayout

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.os.DeadObjectException
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgument
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvOrdersAdapter
import com.developer.cory.Interface.onAddressListener
import com.developer.cory.Model.Address
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Temp
import com.developer.cory.Model.pushNotification
import com.developer.cory.R
import com.developer.cory.Service.AddressService
import com.developer.cory.ViewModel.PayOrderViewModel
import com.developer.cory.ViewModel.SharedDataViewModel
import com.developer.cory.databinding.FragmentPayOrdersBinding
import com.developer.cory.modules.OptionAddressFragment


class PayOrdersFragment : Fragment() {

    private val PHI_VAN_CHUYEN: Double = 15300.0

    private lateinit var _binding: FragmentPayOrdersBinding
    private val binding get() = _binding

    private val addressService = AddressService()
    private lateinit var navController: NavController
    private val sharedViewModel: PayOrderViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPayOrdersBinding.inflate(inflater, container, false)

        init()
        handleEvent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        initAddress()

        sharedViewModel.listCart.observe(viewLifecycleOwner) { listCart ->
            val adapter = RvOrdersAdapter(listCart.toList())
            binding.rvOrders.adapter = adapter
            binding.rvOrders.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
        }

        observeData()

        binding.tvXu.text = "Dùng ${Temp.account.numberXu} xu"
    }

    fun observeData() {
        sharedViewModel.giamGiaVanChuyen.observe(viewLifecycleOwner) { values ->
            binding.tvGiamGiaVanChuyen.text = FormatCurrency.numberFormat.format(values)
        }

        sharedViewModel.voucherGiamGia.observe(viewLifecycleOwner) { values ->
            binding.tvVoucherGiamGia.text = FormatCurrency.numberFormat.format(values)
        }

        sharedViewModel.giamGiaXu.observe(viewLifecycleOwner) { values ->
            binding.tvSuDungXuTichLuy.text = FormatCurrency.numberFormat.format(values)
        }

        sharedViewModel.tongPhiVanChuyen.observe(viewLifecycleOwner) { values ->
            binding.tvTongPhiVanChuyen.text = FormatCurrency.numberFormat.format(values)
        }

        sharedViewModel.tongThanhToan.observe(viewLifecycleOwner) { values ->
            binding.tvTotalMoney.text = FormatCurrency.numberFormat.format(values)
        }

        sharedViewModel.tongThanhToan.observe(viewLifecycleOwner) { values ->
            binding.tvTongTienThanhToan.text = FormatCurrency.numberFormat.format(values)
        }

        sharedViewModel.totalMoney.observe(viewLifecycleOwner) { values ->
            binding.tvSumMoneyProduct.text = FormatCurrency.numberFormat.format(values)
        }

    }

    @SuppressLint("SetTextI18n")
    fun initAddress() {
        sharedViewModel.setDefaultAddress(addressService)

        sharedViewModel.mAddress.observe(viewLifecycleOwner) { address ->
            binding.tvNotAddress.visibility = View.GONE
            binding.lnAddressDetails.visibility = View.VISIBLE

            binding.tvFullName.text = address.fullName
            binding.tvNumberPhone.text = address.numberPhone
            binding.tvAddress.text =
                "${address.phuongXa} , ${address.quanHuyen}, ${address.tinhThanhPho}"
            binding.tvAddressDetails.text = address.addressDetails
        }
    }


    private fun handleEvent() {

        binding.lnPayBuyMoMo.setOnClickListener {
            xuLyThanhToan(binding.lnPayBuyMoMo)
        }

        binding.rdnMomo.setOnClickListener {
            xuLyThanhToan(binding.rdnMomo)
        }

        binding.lnPayBuyCory.setOnClickListener {
            xuLyThanhToan(binding.lnPayBuyCory)
        }

        binding.rdnCory.setOnClickListener {
            xuLyThanhToan(binding.rdnCory)
        }

        binding.switchSuDungXu.setOnClickListener {
            xuLyThanhToan(binding.switchSuDungXu)
        }

        binding.lnAddress.setOnClickListener {

            navController.navigate(R.id.action_payOrdersFragment2_to_optionAddressFragment)

        }

        binding.tvChooseVoucher.setOnClickListener {
            navController.navigate(R.id.action_payOrdersFragment2_to_optionVouchersFragment)
        }

        binding.btnBuyProduct.setOnClickListener {
            context?.let { it1 -> pushNotification().sendNotification(it1) }
        }
    }

    //Xử lý phương thức thanh toán
    private fun xuLyThanhToan(view: View) {
        when (view) {
            binding.lnPayBuyMoMo, binding.rdnMomo -> {
                binding.rdnMomo.isChecked = true
                binding.rdnCory.isChecked = false
                sharedViewModel.setTongPhiVanChuyen(PHI_VAN_CHUYEN)

                binding.lnChooseTable.visibility = View.GONE

            }

            binding.lnPayBuyCory, binding.rdnCory -> {
                binding.rdnMomo.isChecked = false
                binding.rdnCory.isChecked = true
                sharedViewModel.setTongPhiVanChuyen(0.0)
                sharedViewModel.setGiamGiaVanChuyen(0.0)

                binding.lnChooseTable.visibility = View.VISIBLE
            }

            binding.switchSuDungXu -> {

                if (binding.switchSuDungXu.isChecked) {
                    sharedViewModel.setGiamGiaXu(Temp.account.numberXu.unaryMinus())
                } else {
                    sharedViewModel.setGiamGiaXu(0)
                }
            }
        }

        sharedViewModel.tinhTongTienThanhToan()
    }


}