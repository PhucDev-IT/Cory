package com.example.cory_admin.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.cory_admin.Adapter.CustomSpinerCategory
import com.example.cory_admin.Adapter.CustomSpinerImage
import com.example.cory_admin.Model.FormatCurrency
import com.example.cory_admin.Model.TypeVoucher
import com.example.cory_admin.Model.Voucher
import com.example.cory_admin.Service.VoucherService
import com.example.cory_admin.databinding.ActivityAddVoucherBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar


@SuppressLint("SimpleDateFormat")
class AddVoucherActivity : AppCompatActivity(), OnClickListener {
    private val PICK_IMAGE_REQUEST = 1
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    private val typeVoucher =
        hashMapOf(
            TypeVoucher.FREESHIP.name to "Miễn phí vận chuyển",
            TypeVoucher.GIAMTHEOPHANTRAM.name to "Giảm giá theo %",
            TypeVoucher.GIAMTHEOTIEN.name to "Giảm giá theo tiền"
        )
    val today = Calendar.getInstance()

    private lateinit var binding: ActivityAddVoucherBinding
    private var typeVoucherSelected: String? = null
    private var uriVoucherLocal: Uri? = null
    private var uriVoucherSpinner: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    private val voucherService = VoucherService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleEvent()
        initView()
    }

    private fun initView() {

        initDefaultImgSpinner()
        initSpinnerTypeVoucher()
    }

    private fun initDefaultImgSpinner() {
        val documentRef = FirebaseFirestore.getInstance().collection("Images").document("Vouchers")
        val listImg = mutableListOf<String>()
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    if (data != null) {
                        // Lặp qua tất cả các cặp key-value trong document
                        for ((key, value) in data) {
                            listImg.add(value.toString())
                        }

                        val customSpiner = CustomSpinerImage(this,listImg)
                        binding.spinerImgVoucher.adapter = customSpiner

                        binding.spinerImgVoucher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                Glide.with(this@AddVoucherActivity).load(listImg[p2]).into(binding.imgVoucher)
                                uriVoucherLocal = null
                                uriVoucherSpinner = listImg[p2].toUri()
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }

                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Lỗi: ${exception.message}")
            }
    }

    private fun initSpinnerTypeVoucher() {
        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                typeVoucher.values.toList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinerTypeVoucher.adapter = adapter

        binding.spinerTypeVoucher.onItemSelectedListener = object : OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                typeVoucherSelected = typeVoucher.keys.elementAt(p2)
                if (p2 == 0) {
                    binding.textLayoutReduce.visibility = View.GONE
                    binding.edtTitleVoucher.setText(typeVoucher.values.elementAt(p2))
                } else {
                    binding.textLayoutReduce.visibility = View.VISIBLE
                    if (p2 == 1) {
                        binding.edtTitleVoucher.setText(
                            "Giảm ${
                                binding.edtReduce.text.toString().trim()
                            }%"
                        )
                    } else {
                        var reduce =
                            if (binding.edtReduce.text.toString().trim().isNotEmpty()) {
                                binding.edtReduce.text.toString().trim().toLong()
                            } else {
                                1
                            }
                        binding.edtTitleVoucher.setText(
                            "Giảm ${
                                FormatCurrency.numberFormat.format(reduce)
                            }"
                        )
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
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
            uriVoucherSpinner = null
            uriVoucherLocal = data.data
            binding.imgVoucher.setImageURI(uriVoucherLocal)
        }
    }


    //----------------------------------Thêm voucher
    private fun addVoucher() {

        if (uriVoucherLocal == null && uriVoucherSpinner == null) {
            Toast.makeText(this, "Chọn ảnh cho voucher", Toast.LENGTH_SHORT).show()
        } else if (binding.edtQuantity.text.toString().isEmpty()) {
            binding.edtQuantity.error = "Trường này là bắt buộc"
        } else if (binding.tvTimeStart.text.toString().isEmpty()) {
            binding.tvTimeStart.error = "Trường này là bắt buộc"
        } else if (binding.tvTimeEnd.text.toString().isEmpty()) {
            binding.tvTimeEnd.error = "Trường này là bắt buộc"
        } else if (binding.edtDescription.text.toString().isEmpty()) {
            binding.edtDescription.error = "Trường này là bắt buộc"
        } else if (binding.edtReduce.text.toString()
                .isEmpty() && ((typeVoucherSelected == typeVoucher.keys.elementAt(1))
                    || (typeVoucherSelected == typeVoucher.keys.elementAt(2)))
        ) {
            binding.edtReduce.error = "Trường này là bắt buộc"

        } else {

            val start = convertViewToDate(binding.tvTimeStart.text.toString())
            val end = convertViewToDate(binding.tvTimeEnd.text.toString())

            var voucher = Voucher()
            voucher.typeVoucher = typeVoucherSelected
            voucher.description = binding.edtDescription.text.toString().trim()
            voucher.quantity = binding.edtQuantity.text.toString().trim().toInt()
            voucher.title = binding.edtTitleVoucher.text.toString()
            voucher.startTime = start
            voucher.endTime = end
            if (typeVoucherSelected != typeVoucher.keys.elementAt(0)) {
                voucher.reduce = binding.edtReduce.text.toString().trim().toDouble()
            }

            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Chờ em tí")
            progressDialog.show()

            //Bởi vì đã xử lý nếu chọn ảnh từ thiết bị thì link ảnh đã chọn ở spiner sẽ = null nên không lo cả 2 đều có dữ liệu
            if (uriVoucherLocal != null) {
                voucherService.addImageAndVoucher(uriVoucherLocal!!, voucher) { b ->
                    progressDialog.dismiss()
                    if (b) {
                        refreshData()
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                uriVoucherSpinner?.let {
                    voucherService.addVoucher(voucher, it) { b ->
                        progressDialog.dismiss()
                        if (b) {
                            refreshData()
                            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleChooseDate(view: TextView) {
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH)
        val day = today.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
            view.text = "$i3-${i2 + 1}-$i"
        }, year, month, day).show()

    }

    private fun convertViewToDate(str: String): Long? {
        val time = dateFormat.parse(str)
        if (time != null) {
            return time.time
        }
        return null
    }

    private fun onTextReduceChanged() {
        binding.edtReduce.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (typeVoucherSelected == typeVoucher.keys.elementAt(2)) {
                    var reduce = if (binding.edtReduce.text.toString().trim().isNotEmpty()) {
                        binding.edtReduce.text.toString().trim().toLong()
                    } else {
                        1
                    }
                    binding.edtTitleVoucher.setText(
                        "Giảm ${
                            FormatCurrency.numberFormat.format(reduce)
                        }"
                    )

                } else if (typeVoucherSelected == typeVoucher.keys.elementAt(1)) {

                    binding.edtTitleVoucher.setText(
                        "Giảm ${
                            binding.edtReduce.text.toString().trim()
                        }%"
                    )

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }


    //-------------------- reset--------------------
    fun refreshData() {
        binding.imgVoucher.setImageURI(null)
        uriVoucherLocal = null
        uriVoucherSpinner = null
        binding.edtQuantity.setText("")
        binding.edtReduce.setText("1")
        binding.edtDescription.setText("")
        binding.tvTimeStart.text = dateFormat.format(today.time)
        binding.tvTimeEnd.text = dateFormat.format(today.time)
    }


    private fun handleEvent() {
        binding.lnImgVoucher.setOnClickListener(this)
        binding.tvTimeStart.setOnClickListener(this)
        binding.tvTimeEnd.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnClear.setOnClickListener(this)
        onTextReduceChanged()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.lnImgVoucher -> openFileChooseImage()
            binding.tvTimeStart -> handleChooseDate(binding.tvTimeStart)
            binding.tvTimeEnd -> handleChooseDate(binding.tvTimeEnd)
            binding.btnAdd -> addVoucher()
            binding.btnClear -> refreshData()
            binding.btnCancel -> onBackPressed()
            binding.btnBack -> onBackPressed()
        }
    }


}