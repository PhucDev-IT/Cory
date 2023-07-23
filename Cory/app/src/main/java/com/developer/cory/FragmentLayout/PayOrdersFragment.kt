package com.developer.cory.FragmentLayout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvOrdersAdapter
import com.developer.cory.Model.Address
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Temp
import com.developer.cory.R
import com.developer.cory.Service.AddressService
import com.developer.cory.databinding.FragmentPayOrdersBinding


class PayOrdersFragment : Fragment() {
  private lateinit var _binding:FragmentPayOrdersBinding
    private val binding get() =  _binding
    private var phiVanChuyen:Double = 0.0
    private val addressService = AddressService()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPayOrdersBinding.inflate(inflater,container,false)

        init()
        handleEvent()
        getAddress()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    @SuppressLint("SetTextI18n")
    private fun init(){
        val listCart = arguments?.getSerializable("key_listBuyProduct") as Array<CartModel>
        val totalMoney = arguments?.getDouble("key_totalMoney")

        binding.tvSumMoneyProduct.text = FormatCurrency.numberFormat.format(totalMoney)
        val adapter = RvOrdersAdapter(listCart.toList())
        binding.rvOrders.adapter = adapter
        binding.rvOrders.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)



        binding.tvXu.text = "Dùng ${Temp.account.numberXu} xu"
    }
    private lateinit var listAddress:List<Address>
    @SuppressLint("SetTextI18n")
    private fun getAddress(){
        listAddress = listOf()
        Temp.user?.id?.let {
            addressService.selectByIDUser(it){ list->
                listAddress = list
                if(listAddress.isNotEmpty()){

                    binding.tvNotAddress.visibility = View.GONE
                    binding.lnAddressDetails.visibility = View.VISIBLE

                    binding.tvFullName.text = listAddress[0].fullName
                    binding.tvNumberPhone.text = listAddress[0].numberPhone
                    binding.tvAddress.text = "${listAddress[0].phuongXa} , ${listAddress[0].quanHuyen}, ${listAddress[0].tinhThanhPho}"
                    binding.tvAddressDetails.text = listAddress[0].addressDetails
                }
            }
        }

    }



    private fun handleEvent(){
        binding.lnPayBuyMoMo.setOnClickListener {
            handlePay(binding.lnPayBuyMoMo)
        }

        binding.rdnMomo.setOnClickListener {
            handlePay(binding.rdnMomo)
        }

        binding.lnPayBuyCory.setOnClickListener {
            handlePay(binding.lnPayBuyCory)
        }

        binding.rdnCory.setOnClickListener {
            handlePay(binding.rdnCory)
        }

        binding.lnAddress.setOnClickListener {
            navController.navigate(R.id.action_payOrdersFragment2_to_optionAddressFragment)
        }
    }

    //Xử lý phương thức thanh toán
    private fun handlePay(view:View){

        when(view){
            binding.lnPayBuyMoMo, binding.rdnMomo ->{
                binding.rdnMomo.isChecked = true
                binding.rdnCory.isChecked = false

                phiVanChuyen = 15300.0
            }

            binding.lnPayBuyCory, binding.rdnCory ->{
                binding.rdnMomo.isChecked = false
                binding.rdnCory.isChecked = true

                phiVanChuyen = 0.0
            }
        }


    }
}