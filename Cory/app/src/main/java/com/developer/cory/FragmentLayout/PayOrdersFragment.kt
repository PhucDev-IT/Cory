package com.developer.cory.FragmentLayout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels

import androidx.navigation.NavController
import androidx.navigation.Navigation

import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Activity.PurchaseHistoryActivity
import com.developer.cory.Adapter.RvOrdersAdapter

import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Order
import com.developer.cory.Model.Temp
import com.developer.cory.Model.pushNotification
import com.developer.cory.R
import com.developer.cory.Service.AddressService
import com.developer.cory.Service.CartService
import com.developer.cory.ViewModel.PayOrderViewModel
import com.developer.cory.databinding.FragmentPayOrdersBinding
import com.developer.cory.modules.ChoXacNhanFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.Instant
import java.util.Date


class PayOrdersFragment : Fragment() {

    private val PHI_VAN_CHUYEN: Double = 15300.0
    private val THANH_TOAN_TAI_BAN: String = "Thanh toán tại bàn"
    private val THANH_TOAN_MOMO: String = "Thanh toán bằng Momo"

    private lateinit var _binding: FragmentPayOrdersBinding
    private val binding get() = _binding

    private val addressService = AddressService()
    private lateinit var navController: NavController
    private val sharedViewModel: PayOrderViewModel by activityViewModels()

    private lateinit var dbRef: DatabaseReference

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

        sharedViewModel.tongTienSanPham.observe(viewLifecycleOwner) { values ->
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
            xuLyPhuongThucThanhToan(binding.lnPayBuyMoMo)
        }

        binding.rdnMomo.setOnClickListener {
            xuLyPhuongThucThanhToan(binding.rdnMomo)
        }

        binding.lnPayBuyCory.setOnClickListener {
            xuLyPhuongThucThanhToan(binding.lnPayBuyCory)
        }

        binding.rdnCory.setOnClickListener {
            xuLyPhuongThucThanhToan(binding.rdnCory)
        }

        binding.switchSuDungXu.setOnClickListener {
            xuLyPhuongThucThanhToan(binding.switchSuDungXu)
        }

        binding.lnAddress.setOnClickListener {

            navController.navigate(R.id.action_payOrdersFragment2_to_optionAddressFragment)

        }

        binding.tvChooseVoucher.setOnClickListener {
            navController.navigate(R.id.action_payOrdersFragment2_to_optionVouchersFragment)
        }

        binding.btnBuyProduct.setOnClickListener {

            dbRef = FirebaseDatabase.getInstance().getReference("Orders")
            xuLyThanhToanHoaDon()

        }
    }

    //Xử lý phương thức thanh toán
    private fun xuLyPhuongThucThanhToan(view: View) {
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

    fun xuLyThanhToanHoaDon() {

        if (sharedViewModel.mAddress.value == null) {
            Toast.makeText(context, "Bạn chưa có địa chỉ nhận hàng", Toast.LENGTH_SHORT).show()
            return
        } else if (!binding.rdnCory.isChecked && !binding.rdnMomo.isChecked) {
            Toast.makeText(context, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (binding.rdnCory.isChecked) {
            xuLyThanhToan(THANH_TOAN_TAI_BAN)
        } else if (binding.rdnMomo.isChecked) {
            xuLyThanhToan(THANH_TOAN_MOMO)
        }
    }

    //Thanh toán tại bàn
    fun xuLyThanhToan(phuongThucThanhToan: String) {

        var order: Order = Order()
        order.phuongThucThanhToan = phuongThucThanhToan
        order.listCart = sharedViewModel.listCart.value
        order.mAddress = sharedViewModel.mAddress.value
        order.tongPhiVanChuyen = sharedViewModel.tongPhiVanChuyen.value
        order.tongTienSanPham = sharedViewModel.tongTienSanPham.value
        order.usedXu = sharedViewModel.giamGiaXu.value
        order.voucher = sharedViewModel.voucher.value
        order.orderDate = Date()
        order.status = "Chờ xác nhận"

        var id = dbRef.push().key!!
        order.idOrder = id

        Temp.user?.id?.let {
            dbRef.child(it).child(id).setValue(order)
                .addOnCompleteListener {
                    context?.let { it1 ->
                        pushNotification().sendNotification(
                            it1,
                            "Đặt hàng thành công",
                            "Bạn vừa đặt ${order.listCart?.size} sản phẩm chỉ với ${
                                FormatCurrency.numberFormat.format(
                                    sharedViewModel.tongThanhToan.value
                                )
                            }"
                        )
                    }

                    sharedViewModel.listCart.value?.let { it1 -> CartService().removeCart(it1) }

                    val intent = Intent(context, PurchaseHistoryActivity::class.java)
                    intent.putExtra("key_tab","Chờ xác nhận")
                    startActivity(intent)
                    activity?.finish()

                }
                .addOnFailureListener { err ->
                    Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                    Log.e("Lỗi thanh toán: ", err.localizedMessage)
                }
        }
    }


}