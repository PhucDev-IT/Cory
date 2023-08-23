package com.developer.cory.FragmentLayout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.cory.Adapter.RvItemCartAdapter
import com.developer.cory.Interface.CkbCartInterface
import com.developer.cory.Interface.RvChooseCartInterface
import com.developer.cory.MainActivity
import com.developer.cory.Model.CartModel
import com.developer.cory.Model.FormatCurrency
import com.developer.cory.R
import com.developer.cory.ViewModel.PayOrderViewModel
import com.developer.cory.databinding.FragmentCartBinding


class CartFragment : Fragment(), View.OnClickListener {
    private lateinit var _binding: FragmentCartBinding
    private val binding get() = _binding
    private  var listChoseCart: MutableList<CartModel> = ArrayList()

    private lateinit var navController: NavController

    private val sharedViewModel : PayOrderViewModel by activityViewModels()
    private lateinit var adapter:RvItemCartAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        initView()

       handleClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }



    private fun initView(){
        sumMoney = 0.0  //Khi back lại nó bị lưu value cũ nên cần reset

        adapter = RvItemCartAdapter(emptyList(), object : RvChooseCartInterface {
            override fun onClickListener(price: Double, cart: CartModel) {

                sumMoney = 0.0
                for (i in 0 until listChoseCart.size) {
                    if (listChoseCart[i] == cart) {
                        listChoseCart[i].totalMoney = price
                    }
                    sumMoney += listChoseCart[i].totalMoney
                }
                binding.tvSumMoney.text = FormatCurrency.numberFormat.format(sumMoney)

            }
        }, object : CkbCartInterface {
            override fun isChecked(cartModel: CartModel) {
                listChoseCart.add(cartModel)
                sumMoney += cartModel.totalMoney
                binding.tvSumMoney.text = FormatCurrency.numberFormat.format(sumMoney)
            }

            override fun isNotChecked(cartModel: CartModel) {
                sumMoney -= cartModel.totalMoney
                listChoseCart.remove(cartModel)
                binding.tvSumMoney.text = sumMoney.toString()
                binding.tvSumMoney.text = FormatCurrency.numberFormat.format(sumMoney)
            }
        }
        )
        binding.rvItemCart.adapter = adapter
        binding.rvItemCart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        getData()
    }

    private  var sumMoney:Double = 0.0
    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {

        binding.swipRefresh.isRefreshing = true
        sharedViewModel.getListCart()
        Handler().postDelayed({
            if(sharedViewModel.mListCart.value == null){
                binding.lnDefaultCart.visibility = View.VISIBLE
                binding.scrollCart.visibility = View.GONE
            }else{
                sharedViewModel.mListCart.observe(viewLifecycleOwner){list->
                    binding.lnDefaultCart.visibility = View.GONE
                    binding.scrollCart.visibility = View.VISIBLE
                    adapter.setData(list)
                    adapter.notifyDataSetChanged()
                }
            }

            binding.swipRefresh.isRefreshing = false
        },2000)

    }

    private fun handleClick(){
        binding.btnBuyProduct.setOnClickListener(this)
        binding.tvMuaSamNgay.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnBuyProduct -> {
                if(listChoseCart.size>0){
                    sharedViewModel.setListCartSelected(listChoseCart)
                    sharedViewModel.setTotalMoney(sumMoney)
                    navController.navigate(R.id.action_cartFragment_to_payOrdersFragment2)
                }else{
                    Toast.makeText(context,"Chọn sản phẩm cần mua bạn nhé!",Toast.LENGTH_SHORT).show()
                }
            }

            R.id.btnBack -> requireActivity().onBackPressed()

            R.id.tvMuaSamNgay -> {
                val intent = Intent(context,MainActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            }

        }
    }


}