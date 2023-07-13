package com.developer.cory.Activity


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.developer.cory.MainActivity
import com.developer.cory.Model.Account
import com.developer.cory.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addEvents()
    }

    private fun addEvents() {
        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            hiddenKeyboard()
            existsAccount()
        }
    }


    //Kiểm tra tài khoản
    private fun existsAccount(){

        val numberPhone = binding.edtNumberPhone.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        val account = Account(numberPhone,password)
        if (!account.checkNumberPhone() || !account.checkPassword()) {
            Toast.makeText(this,"Tài khoản hoặc mật khẩu không chinnh xác",Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("account").document(numberPhone)
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val fieldValue = documentSnapshot.getString("password")
                    if(password == fieldValue){
                        val intent = Intent(this,MainActivity::class.java)
                        intent.putExtra("phone",numberPhone)
                        startActivity(intent)
                        finishAffinity()
                    }else{
                        Toast.makeText(this,"Mật khẩu không chính xác",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,"Tài khoản không tồn tại",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Lỗi: ","${exception.message}")
            }
    }


    //ẩn bàn phím ảo
    fun hiddenKeyboard(){
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}