package com.example.cory_admin.Activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cory_admin.Adapter.CustomSpinerCategory
import com.example.cory_admin.Adapter.RvItemAddProductAdapter
import com.example.cory_admin.Model.Product
import com.example.cory_admin.Service.CategoryService
import com.example.cory_admin.Service.Product_Service
import com.example.cory_admin.databinding.ActivityAddProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddProductActivity : AppCompatActivity(), OnClickListener {

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var adapterClassify: RvItemAddProductAdapter
    private lateinit var adapterSideDishes: RvItemAddProductAdapter
    private var urlProduct: Uri? = null
    private var listClassify:MutableMap<String,Double> = mutableMapOf()
    private var listSideDishes:MutableMap<String,Double> = mutableMapOf()
    private var idCategory:String?=null
    private val db = Firebase.firestore

    private lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        handleClick()
        hanldeOnTextChange()

    }

    private fun initView() {

        setSpiner()

        adapterClassify = RvItemAddProductAdapter()
        binding.rvClassify.adapter = adapterClassify
        binding.rvClassify.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        adapterSideDishes = RvItemAddProductAdapter()
        binding.rvSideDishes.adapter = adapterSideDishes
        binding.rvSideDishes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    private fun setSpiner(){

        CategoryService(db).selectAllData { list->
            val customSpinerCategory = CustomSpinerCategory(this,list)
            binding.spinner.adapter = customSpinerCategory

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    idCategory = list[p2].id
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

    }

    //----------- Xử lý thêm sản phẩm ------------------------
    private fun hanldeOnTextChange() {
        binding.edtClassify.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.edtClassify.text.toString().trim().isEmpty()) {
                    binding.edtPriceOfClassify.isEnabled = false
                    binding.edtPriceOfClassify.setText("")
                } else {
                    binding.edtPriceOfClassify.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.edtSideDishes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.edtSideDishes.text.toString().trim().isEmpty()) {
                    binding.edtPriceOfSideDishes.isEnabled = false
                    binding.edtPriceOfSideDishes.setText("")
                } else {
                    binding.edtPriceOfSideDishes.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addClassify() {
        if (binding.edtClassify.text.toString().isEmpty()) {
            binding.edtClassify.error = "Thiếu trường này"
        } else if (binding.edtPriceOfClassify.text.toString().isEmpty()) {
            binding.edtPriceOfClassify.error = "Thiếu trường này"
        } else if (binding.edtPriceOfClassify.text.toString().toDouble() <= 0) {
            binding.edtPriceOfClassify.error = "Nhập số tiền lớn hơn 0"
        } else {

            listClassify[binding.edtClassify.text.toString()] =
                binding.edtPriceOfClassify.text.toString().toDouble()

            adapterClassify.addData(listClassify)
            adapterClassify.notifyDataSetChanged()
            binding.edtClassify.setText("")
            binding.edtPriceOfClassify.setText("")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addSideDishes() {
        if (binding.edtSideDishes.text.toString().isEmpty()) {
            binding.edtSideDishes.error = "Thiếu trường này"
        } else if (binding.edtPriceOfSideDishes.text.toString().isEmpty()) {
            binding.edtPriceOfSideDishes.error = "Thiếu trường này"
        } else if (binding.edtPriceOfSideDishes.text.toString().toDouble() <= 0) {
            binding.edtPriceOfSideDishes.error = "Số tiền phải lớn hơn 0"
        } else {

            listSideDishes[binding.edtSideDishes.text.toString()] =
                binding.edtPriceOfSideDishes.text.toString().toDouble()

            adapterSideDishes.addData(listSideDishes)
            adapterSideDishes.notifyDataSetChanged()

            binding.edtSideDishes.setText("")
            binding.edtPriceOfSideDishes.setText("")
        }
    }
    private fun addProduct() {
        if (urlProduct == null) {
            Toast.makeText(this, "Chọn ảnh làm đại diện cho sản phẩm", Toast.LENGTH_SHORT).show()
        } else if (binding.edtNameProduct.text.toString().trim().isEmpty()) {
            binding.edtNameProduct.error = "Nhập tên sản phẩm"
        } else if (binding.edtPrice.text.toString().trim().isEmpty()) {
            binding.edtPrice.error = "Để trống sẽ bị lỗ"
        } else if (binding.edtDescription.text.toString().trim().isEmpty()) {
            binding.edtDescription.error = "Thiếu mô tả"
        }else {
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Chờ 1 tí")
            progressDialog.show()

            var product = Product()
            product.name = binding.edtNameProduct.text.toString().trim()
            product.price = binding.edtPrice.text.toString().toDouble()
            product.description = binding.edtDescription.text.toString().trim()
            if (listClassify.isNotEmpty()) {
                product.classify = listClassify
            }
            if (listSideDishes.isNotEmpty()) {
                product.sideDishes = listSideDishes
            }
            if (idCategory != null) {
                product.idCategory = idCategory
            }

            urlProduct?.let {
                Product_Service(db).addProduct(it, product) { b ->
                    progressDialog.dismiss()
                    if (b) {
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                        resetData()
                    }
                }
            }

        }
    }

    private fun resetData(){
        listSideDishes.clear()
        listClassify.clear()
        urlProduct = null
        binding.imgProduct.setImageURI(null)
        binding.edtNameProduct.setText("")
        binding.edtPrice.setText("")
        binding.edtClassify.setText("")
        binding.edtPriceOfClassify.setText("")
        binding.edtSideDishes.setText("")
        binding.edtPriceOfSideDishes.setText("")
        binding.edtDescription.setText("")
        adapterClassify.reset()
        adapterSideDishes.reset()
    }


    //----------------Xử lý chọn ảnh từ thiết bị cục bộ----------------------
    private fun openFileChooseImage() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            urlProduct = data.data
            binding.imgProduct.setImageURI(urlProduct)
        }
    }

    private fun handleClick() {
        binding.imgProduct.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.btnAddClassify.setOnClickListener(this)
        binding.btnAddSideDishes.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.imgProduct -> openFileChooseImage()

            binding.btnAdd -> addProduct()
            binding.btnClear -> resetData()
            binding.btnAddClassify -> addClassify()
            binding.btnAddSideDishes -> addSideDishes()
            binding.btnBack ->{onBackPressed()}
            binding.btnCancel -> onBackPressed()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
    }
}