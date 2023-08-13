package com.example.cory_admin.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.cory_admin.Adapter.CustomSpinerImage
import com.example.cory_admin.Model.Voucher
import com.example.cory_admin.databinding.ActivityAddVoucherBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar


@SuppressLint("SimpleDateFormat")
class AddVoucherActivity : AppCompatActivity(),OnClickListener {
    private val PICK_IMAGE_REQUEST = 1
    private val type = arrayOf<String>("Miễn phí vận chuyển", "Giảm giá theo tiền", "Gảm giá theo %")
    val today = Calendar.getInstance()

    private lateinit var binding:ActivityAddVoucherBinding
    private var typeVoucherSelected:String? = null
    private val db = Firebase.firestore
    private var uriVoucherLocal:Uri? = null
    private var uriVoucherSpinner:Uri? = null
    private lateinit var datePickerDialog:DatePickerDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleEvent()
        initView()
    }

    private fun initView(){

        initSpinnerImgVoucher()
        initSpinnerTypeVoucher()
    }

    private fun initSpinnerTypeVoucher(){
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,type)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinerTypeVoucher.adapter = adapter

        binding.spinerTypeVoucher.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                typeVoucherSelected = type[p2]
                if(p2 == 0){
                    binding.textLayoutReduce.visibility = View.GONE
                }else{
                    binding.textLayoutReduce.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }
    private fun initSpinnerImgVoucher(){
        db.collection("imagesVoucher").get()
            .addOnSuccessListener { documents->
                var listImg = mutableListOf<String>()
                for(document in documents){
                    val str:String = document.toObject(String::class.java)
                  listImg.add(str)
                }

                val adapter = CustomSpinerImage(this,listImg)
                binding.spinerImgVoucher.adapter = adapter

                binding.spinerImgVoucher.onItemSelectedListener = object : OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        uriVoucherSpinner = listImg[p2].toUri()
                        uriVoucherLocal = null
                        binding.imgVoucher.setImageURI(uriVoucherSpinner)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }
            }
            .addOnFailureListener { err->
                Log.e(TAG,"Lỗi load ảnh voucher: ${err.message}")
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


    private var dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    //Thêm voucher
    private fun addVoucher(){
        if(uriVoucherLocal==null && uriVoucherSpinner==null){
            Toast.makeText(this,"Chọn ảnh cho voucher",Toast.LENGTH_SHORT).show()
        }else if(binding.edtTitleVoucher.text.toString().isEmpty()){
            binding.edtTitleVoucher.error = "Trường này là bắt buộc"
        }else if(binding.edtQuantity.text.toString().isEmpty()){
            binding.edtQuantity.error = "Trường này là bắt buộc"
        }
        else if(binding.edtTimeStart.text.toString().isEmpty()){
            binding.edtTimeStart.error = "Trường này là bắt buộc"
        }else if(binding.edtTimeEnd.text.toString().isEmpty()){
            binding.edtTimeEnd.error = "Trường này là bắt buộc"
        }else if(binding.edtDescription.text.toString().isEmpty()){
            binding.edtDescription.error = "Trường này là bắt buộc"
        }else if(typeVoucherSelected != type[0]){
            if(binding.edtReduce.text.toString().isEmpty()){
                binding.edtReduce.error = "Trường này là bắt buộc"
            }
        }else{

            //Bởi vì đã xử lý nếu chọn ảnh từ thiết bị thì link ảnh đã chọn ở spiner sẽ = null nên không lo cả 2 đều có dữ liệu
            val uri = if(uriVoucherLocal!=null){
                uriVoucherLocal as Uri
            }else{
                uriVoucherSpinner as Uri
            }

            var voucher = Voucher()
            voucher.typeVoucher = typeVoucherSelected
            voucher.description = binding.edtDescription.text.toString().trim()
            voucher.quantity = binding.edtQuantity.text.toString().trim().toInt()

            voucher.startTime = dateFormat.parse(binding.edtTimeStart.text.toString())?.time as Timestamp
            voucher.endTime = dateFormat.parse(binding.edtTimeEnd.text.toString())?.time as Timestamp

            if(typeVoucherSelected != type[0]){
                voucher.reduce = binding.edtReduce.text.toString().trim().toDouble()
            }

        }
    }



    @SuppressLint("SetTextI18n")
    private fun handleChooseDate(edtText:EditText) {
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            edtText.setText("$i3-${i2 + 1}-$i")
        }, 2023, 6, 3).show()

    }



    private fun handleEvent(){
        binding.lnImgVoucher.setOnClickListener(this)
        binding.edtTimeStart.setOnClickListener(this)
        binding.edtTimeEnd.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.lnImgVoucher -> openFileChooseImage()

            binding.edtTimeStart -> handleChooseDate(binding.edtTimeStart)
            binding.edtTimeEnd -> handleChooseDate(binding.edtTimeEnd)
        }
    }


}