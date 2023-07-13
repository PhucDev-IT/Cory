package com.developer.cory.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener

import com.developer.cory.databinding.ActivityRegisterBinding
import com.developer.cory.Model.Account
import com.developer.cory.Model.User
import com.developer.cory.R

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    lateinit var dialog: AlertDialog
    private lateinit var mAuth: FirebaseAuth
    private var verificationId: String = ""
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resetBackgroundInput()
        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        handleButtonClick()

    }

    private fun handleButtonClick() {
        binding.btnSendOTP.setOnClickListener {
            hiddenKeyboard()
            requestSendOTP()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            hiddenKeyboard()
            onClickRegister()
        }

        binding.tvBackToLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }



    }

    //Gửi mã OTP
    private fun isStrEmpty(str: String): Boolean {
        return str.isEmpty()
    }

    @SuppressLint("ResourceAsColor")
    private fun requestSendOTP() {
        val account = Account(binding.edtNumberPhone.text.toString().trim(),binding.edtPassword.text.toString().trim())
        if (!account.checkNumberPhone()) {

            binding.edtNumberPhone.error = "Vui lòng nhập số điện thoại"
            binding.texInputPhone.boxStrokeColor = resources.getColor(R.color.red)

        } else if (!account.checkPassword()) {

            binding.texInputPassword.isPasswordVisibilityToggleEnabled = false
            binding.edtPassword.error = "Mật khẩu phải lớn hơn 6 kí tự"

        } else if (isStrEmpty(binding.edtConfirmPassword.text.toString().trim())) {
            binding.texInputConfirPassword.isPasswordVisibilityToggleEnabled = false
            binding.texInputConfirPassword.boxStrokeColor = resources.getColor(R.color.red)
            binding.edtConfirmPassword.error = "Mật khẩu không khớp"

        } else if (binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString()) {

            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()

        } else {
            binding.btnSendOTP.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            checkExistsNumberPhone()
        }



    }
    //Xử lý lại icon sau khi có thông báo lỗi nhập dữ liệu
    private fun resetBackgroundInput(){
        binding.edtPassword.addTextChangedListener { object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               if(binding.edtPassword.text.toString().trim().isNotEmpty()){
                   binding.texInputPassword.boxStrokeColor = resources.getColor(R.color.textInputLayout)
                   binding.texInputPassword.isPasswordVisibilityToggleEnabled = true
               }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

        }
        binding.edtNumberPhone.addTextChangedListener { object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun afterTextChanged(p0: Editable?) {
                val phoneNumber = p0?.toString() ?: ""
                if (phoneNumber.length < 10) {
                    binding.texInputPhone.boxStrokeColor =
                        resources.getColor(android.R.color.holo_red_dark)
                } else {
                    binding.texInputPhone.boxStrokeColor =
                        resources.getColor(R.color.textInputLayout) // Màu ban đầu của boxStrokeColor
                }

                Toast.makeText(binding.root.context,"Nhấn vào phone",Toast.LENGTH_SHORT).show()
            }
        }
        }

        binding.edtConfirmPassword.addTextChangedListener { object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p3>=1){
                    binding.texInputPassword.boxStrokeColor = resources.getColor(R.color.textInputLayout)
                    binding.texInputPassword.isPasswordVisibilityToggleEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

        }
    }

    //Kiểm tra số điện thoại đã tồn tại chuưa ?
    private fun checkExistsNumberPhone() {

        db.collection("account").document(binding.edtNumberPhone.text.toString().trim())
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val document = task.result
                    if(document.exists()){
                        // Số điện thoại đã được đăng ký
                        binding.progressBar.visibility = View.GONE
                        binding.btnSendOTP.visibility = View.VISIBLE
                        binding.edtNumberPhone.error = "Số điện thoại đã được sử dụng"
                        Toast.makeText(this,"Số điện thoại đã được sử dụng",Toast.LENGTH_SHORT).show()
                    }else {
                        // Số điện thoại chưa được đăng ký
                        binding.progressBar.visibility = View.GONE
                        sendVerificationCode()
                    }
                }

            }
            .addOnFailureListener { err->
                err.message?.let { Log.e("Lỗi rồi: ", it) }
            }

    }

    private fun sendVerificationCode() {

        var numberPhone = binding.edtNumberPhone.text.toString().trim()
        if(numberPhone.startsWith('0')){
            numberPhone = numberPhone.removeRange(0, 1)
        }
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+84$numberPhone")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSendOTP.visibility = View.VISIBLE
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSendOTP.visibility = View.VISIBLE
                    Log.e(ContentValues.TAG, "Lỗi: ${p0.message}")
                    Toast.makeText(application, p0.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onCodeSent(
                    verificationID: String,
                    p1: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationID, p1)
                    binding.progressBar.visibility = View.GONE
                    binding.btnSendOTP.visibility = View.VISIBLE
                    // Save verification ID
                    this@RegisterActivity.verificationId = verificationID
                    showDialogGetOTP()

                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private var inputOTP:String?=null
    @SuppressLint("MissingInflatedId")
    private fun showDialogGetOTP() {
        val build = AlertDialog.Builder(this, R.style.CustomAlert)
        val viewDialog = layoutInflater.inflate(R.layout.verification_otp_layout, null)

        val tvNumberPhone = viewDialog.findViewById<TextView>(R.id.tvNbPhone_dialog)
        val btnClose = viewDialog.findViewById<ImageButton>(R.id.imgCloseDialog)


        //Set up
        val digit_1 = viewDialog.findViewById<EditText>(R.id.digit_1)
        val digit_2 = viewDialog.findViewById<EditText>(R.id.digit_2)
        val digit_3 = viewDialog.findViewById<EditText>(R.id.digit_3)
        val digit_4 = viewDialog.findViewById<EditText>(R.id.digit_4)
        val digit_5 = viewDialog.findViewById<EditText>(R.id.digit_5)
        val digit_6 = viewDialog.findViewById<EditText>(R.id.digit_6)


        viewDialog.findViewById<Button>(R.id.btnConfirm_dialog).setOnClickListener {
            inputOTP = digit_1.text.toString() +
                    digit_2.text.toString() +
                    digit_3.text.toString() +
                    digit_4.text.toString() +
                    digit_5.text.toString() +
                    digit_6.text.toString()
            onClickverifyOTP(inputOTP!!)
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        //Xử lý nhập mã OTP
        setUpOTPInputs(digit_1,digit_2,digit_3,digit_4,digit_5,digit_6)

        tvNumberPhone.text = binding.edtNumberPhone.text.toString().trim()


        build.setView(viewDialog)
        build.setCancelable(false)
        dialog = build.create()
        dialog.show()
    }

    //Input mã OTP
    private fun onClickverifyOTP(otp:String) {
        if (otp.isEmpty()) {
            // Show error message
            Toast.makeText(applicationContext, "Vui lòng nhập mã OTP", Toast.LENGTH_LONG).show()
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    Toast.makeText(
                        application, "Xác minh thành công",
                        Toast.LENGTH_SHORT
                    ).show()


                    binding.edtNumberPhone.isEnabled = false
                    dialog.dismiss()
                    binding.btnSendOTP.visibility = View.GONE
                    binding.btnRegister.visibility = View.VISIBLE
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(
                            application, "Mã xác minh không chính xác",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // Update UI
                }
            }
    }

    //Set up input OTP
    private fun setUpOTPInputs(digit_1:EditText, digit_2: EditText, digit_3:EditText, digit_4:EditText, digit_5:EditText, digit_6:EditText,) {
        digit_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    digit_2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
        digit_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    digit_3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
        digit_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    digit_4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
        digit_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    digit_5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
        digit_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    digit_6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
    }

    //Đăng kí thành công
    private fun onClickRegister()
    {
        val numberPhone = binding.edtNumberPhone.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val phanQuyen:String = "customer"

        val account = Account(numberPhone,password, phanQuyen)

        db.collection("account").document(numberPhone).set(account)
            .addOnCompleteListener {
                saveUser(numberPhone)
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { err->
                Toast.makeText(this,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show()
                Log.e(TAG, "$err.message")
            }

    }

    //Lưu thông tin người dùng
    private fun saveUser(numberPhone:String){
        val user = User(numberPhone)
        db.collection("Users").add(user).addOnCompleteListener {
            Toast.makeText(this,"Đăng ký thành công",Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(this,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show()
                db.collection("account").document(numberPhone).delete()
                    .addOnCompleteListener {
                        Log.d("Success: ","Xóa account: $numberPhone")
                    }
                    .addOnFailureListener {  err->
                        Log.e("Lỗi: ","Không thể xóa: ${err.message} ")
                    }
            }
    }

    @Override
    override fun onBackPressed() {
        super .onBackPressed()
    }

    //ẩn bàn phím ảo
    fun hiddenKeyboard(){
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}