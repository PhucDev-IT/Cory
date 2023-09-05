package com.developer.cory.Activity


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.developer.cory.Adapter.CustomDialog
import com.developer.cory.MainActivity
import com.developer.cory.Model.Account
import com.developer.cory.Model.Temp
import com.developer.cory.R
import com.developer.cory.Service.AccountService
import com.developer.cory.data_local.DataLocalManager
import com.developer.cory.database.AccountDatabase
import com.developer.cory.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var accountLocal:Account?=null
    private lateinit var customDialog: CustomDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        customDialog = CustomDialog(this)
        addEvents()
        initView()
    }

    private fun initView() {
        accountLocal = AccountDatabase.getInstance(this).accountDao().selectAccount()
        if(accountLocal != null){
            binding.edtTaiKhoan.setText(accountLocal!!.userName)
            binding.edtPassword.setText(accountLocal!!.password)

            binding.chkbSaveInfor.isChecked = true
        }
    }

    private fun addEvents() {
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            hiddenKeyboard()
            if(binding.edtTaiKhoan.text.toString().trim().contains("@gmail.com")){
                checkEmailAccount()
            }else{
               checkPhoneAccount()
            }

            if (binding.chkbSaveInfor.isChecked && binding.edtTaiKhoan.text.toString().trim() != accountLocal?.userName) {
                val account = Account()
                account.userName = binding.edtTaiKhoan.text.toString().trim()
                account.password = binding.edtPassword.text.toString().trim()
                AccountDatabase.getInstance(this).accountDao().insert(account)
            }else
            if(!binding.chkbSaveInfor.isChecked && accountLocal!=null){
                AccountDatabase.getInstance(this).accountDao().delete(accountLocal!!)
            }
        }

    }


    //Kiểm tra tài khoản
    private fun checkPhoneAccount() {
        customDialog.dialogBasic("")
        val taiKhoan = binding.edtTaiKhoan.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        val db = FirebaseFirestore.getInstance()
        AccountService(db).login(taiKhoan, password) { b ->
            if (b) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("id_Account", taiKhoan)
                customDialog.closeDialog()
                startActivity(intent)
                finishAffinity()
            } else {
                Toast.makeText(this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT)
                    .show()
                customDialog.closeDialog()
            }
        }

    }

    fun checkEmailAccount() {

        val taiKhoan = binding.edtTaiKhoan.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        customDialog.dialogBasic("Đang xử lý ...")
        auth.signInWithEmailAndPassword(taiKhoan, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val verification = auth.currentUser?.isEmailVerified
                    if (verification == true) {
                        //Để lấy thông tin khác của account
                        val db = FirebaseFirestore.getInstance()
                        AccountService(db).login(taiKhoan, password) { b ->
                            if (b) {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("id_Account", taiKhoan)
                                customDialog.closeDialog()
                                startActivity(intent)
                                finishAffinity()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Tài khoản hoặc mật khẩu không chính xác",
                                    Toast.LENGTH_SHORT
                                ).show()
                                customDialog.closeDialog()
                            }
                        }

                    } else {
                        Toast.makeText(this, "Vui lòng xác minh Email của bạn", Toast.LENGTH_LONG)
                            .show()
                        customDialog.closeDialog()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Có lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
                customDialog.closeDialog()
            }
    }


    //ẩn bàn phím ảo
    fun hiddenKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}