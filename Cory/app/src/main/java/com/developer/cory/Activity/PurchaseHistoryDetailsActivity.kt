package com.developer.cory.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvPurchaseHistoryDetailsAdapter
import com.developer.cory.Model.EnumOrder
import com.developer.cory.Model.FormatCurrency

import com.developer.cory.Model.Order
import com.developer.cory.Model.TypeVoucher
import com.developer.cory.Service.OrdersService
import com.developer.cory.databinding.ActivityPruchaseHistoryDetailsBinding
import java.text.SimpleDateFormat

class PurchaseHistoryDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPruchaseHistoryDetailsBinding
    private lateinit var order:Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPruchaseHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        handleEvent()
    }

    private fun handleEvent(){
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnHuyDonHang.setOnClickListener {
            order.idOrder?.let { it1 -> OrdersService().huyDonHang(it1){b ->
                if(b){
                    Toast.makeText(this,"Hủy đơn hàng thành công",Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else{
                    Toast.makeText(this,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show()
                }
            } }
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initView() {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        order = intent.getSerializableExtra("key_order") as Order

        val tongTienSanPham: Double? = order.tongTienSanPham
        val tongPhiVanChuyen: Double? = order.tongPhiVanChuyen
        var giamGiaVanChuyen: Double = 0.0
        var voucherGiamGia: Double = 0.0
        val suDungXu: Int = order.usedXu!!

        binding.tvFullName.text = order.mAddress?.fullName
        binding.tvNumberPhone.text = order.mAddress?.numberPhone
        binding.tvAddress.text =
            "${order.mAddress?.phuongXa} , ${order.mAddress?.quanHuyen}, ${order.mAddress?.tinhThanhPho}"
        binding.tvAddressDetails.text = order.mAddress?.addressDetails

        binding.tvPhuongThucThanhToan.text = order.phuongThucThanhToan
        binding.tvIdOrder.text = order.idOrder

        binding.tvThoiGianDatHang.text = sdf.format(order.orderDate)
        binding.tvTongTienHang.text = FormatCurrency.numberFormat.format(tongTienSanPham)
        binding.tvTongPhiVanChuyen.text = FormatCurrency.numberFormat.format(tongPhiVanChuyen)
        binding.tvSuDungXuTichLuy.text = FormatCurrency.numberFormat.format(suDungXu)
        if (order.voucher?.typeVoucher == TypeVoucher.FREESHIP.name) {
            giamGiaVanChuyen = tongPhiVanChuyen!!.unaryMinus()
            binding.tvGiamGiaVanChuyen.text = FormatCurrency.numberFormat.format(giamGiaVanChuyen)
        } else if (order.voucher?.typeVoucher == TypeVoucher.GIAMTHEOTIEN.name) {
            voucherGiamGia = (order.voucher?.reduce!!).unaryMinus()

            binding.tvVoucherGiamGia.text = FormatCurrency.numberFormat.format(voucherGiamGia)
        } else if (order.voucher?.typeVoucher == TypeVoucher.GIAMTHEOPHANTRAM.name) {
            voucherGiamGia = (tongTienSanPham!! * order.voucher!!.reduce).unaryMinus()
            binding.tvVoucherGiamGia.text = FormatCurrency.numberFormat.format(voucherGiamGia)
        }

        val sumMoney = (tongTienSanPham?.plus(tongPhiVanChuyen!!)
            ?: 0.0) + giamGiaVanChuyen + voucherGiamGia + suDungXu
        binding.tvTongThanhToan.text = FormatCurrency.numberFormat.format(sumMoney)


        if(order.status == EnumOrder.CHOXACNHAN.name){
            binding.btnHuyDonHang.visibility = View.VISIBLE
        }

        val adapter = RvPurchaseHistoryDetailsAdapter(order.listCart!!)
        binding.rvProducts.adapter = adapter
        binding.rvProducts.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }

}