package com.developer.cory.FragmentLayout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Activity.Cart_Pay_Orders_Activity
import com.developer.cory.Adapter.RvItemCartAdapter
import com.developer.cory.Interface.CheckboxInterface
import com.developer.cory.Interface.RvPriceInterface
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.Model.Temp
import com.developer.cory.R
import com.developer.cory.Service.CartService
import com.developer.cory.databinding.FragmentCartBinding


class CartFragment : Fragment(), View.OnClickListener {
    private lateinit var _binding: FragmentCartBinding
    private val binding get() = _binding
    private lateinit var listChoseCart: MutableList<CartModel>
    private val cartService = CartService()
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        getData()
        binding.btnBuyProduct.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnBuyProduct -> {
                val bundle = Bundle()
                bundle.putSerializable("key_listBuyProduct", listChoseCart.toTypedArray())
                bundle.putDouble("key_totalMoney",sumMoney)
                navController.navigate(R.id.action_cartFragment_to_payOrdersFragment2,bundle)
            }

        }
    }


    private var sumMoney: Double = 0.0
    private fun getData() {
        Toast.makeText(context, "${Temp.user?.id}", Toast.LENGTH_SHORT).show()
        listChoseCart = mutableListOf()
        Temp.user?.id?.let {
            cartService.selectCartByID(it) { listCart ->
                val adapter = RvItemCartAdapter(listCart, object : RvPriceInterface {
                    override fun onClickListener(price: Double, pos: Int) {

                        sumMoney = 0.0
                        for (cart: CartModel in listChoseCart) {
                            if (cart == listCart[pos]) {

                            }
                            sumMoney += cart.totalMoney
                        }
                        binding.tvSumMoney.text = FormatCurrency.numberFormat.format(sumMoney)

                    }
                }, object : CheckboxInterface {
                    override fun isChecked(pos: Int) {
                        listChoseCart.add(listCart[pos])
                        sumMoney += listCart[pos].totalMoney
                        binding.tvSumMoney.text = FormatCurrency.numberFormat.format(sumMoney)
                    }

                    override fun isNotChecked(pos: Int) {
                        sumMoney -= listCart[pos].totalMoney
                        binding.tvSumMoney.text = sumMoney.toString()
                        binding.tvSumMoney.text = FormatCurrency.numberFormat.format(sumMoney)
                    }
                }
                )
                binding.rvItemCart.adapter = adapter
                binding.rvItemCart.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }


}