package com.example.cory_admin.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cory_admin.Adapter.RvOrdersDetailsAdapter
import com.example.cory_admin.Model.EnumOrder
import com.example.cory_admin.Model.FormatCurrency
import com.example.cory_admin.Model.Order
import com.example.cory_admin.Model.TypeVoucher
import com.example.cory_admin.R
import com.example.cory_admin.Service.OrdersService
import com.example.cory_admin.databinding.ActivityOrderDetailsBinding
import java.text.SimpleDateFormat

class OrderDetailsActivity : AppCompatActivity(),OnClickListener {

    private lateinit var binding:ActivityOrderDetailsBinding
    private lateinit var order:Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        handleEvent()
    }

    private fun handleEvent(){
        binding.btnBack.setOnClickListener (this)
        binding.btnXacNhan.setOnClickListener(this)
        binding.btnHuyDonHang.setOnClickListener(this)
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

        val adapter = RvOrdersDetailsAdapter(order.listCart!!)
        binding.rvProducts.adapter = adapter
        binding.rvProducts.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnBack -> onBackPressed()

            R.id.btnXacNhan ->{
                order.idOrder?.let { it1 ->
                    OrdersService().xacNhanDonHang(it1){ b ->
                        if(b){
                            Toast.makeText(this,"Đã xác nhận đơn hàng",Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }else{
                            Toast.makeText(this,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            R.id.btnHuyDonHang ->{
                order.idOrder?.let { it1 ->
                    OrdersService().huyDonHang(it1){ b ->
                        if(b){
                            Toast.makeText(this,"Đã từ chối đơn hàng này",Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }else{
                            Toast.makeText(this,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}