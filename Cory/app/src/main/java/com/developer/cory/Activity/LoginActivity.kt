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
import com.developer.cory.Model.Temp
import com.developer.cory.R
import com.developer.cory.Service.AccountService
import com.developer.cory.data_local.DataLocalManager
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
    companion object {
        private const val RC_SIGN_IN = 20
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        var gso:GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)




        addEvents()
        initView()
    }

    private fun initView(){
        val account = DataLocalManager.getAccount()
        if (account != null) {
            binding.edtTaiKhoan.setText(account.numberPhone)
            binding.edtPassword.setText(account.password)
        }
    }

    private fun addEvents() {
        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            hiddenKeyboard()
            existsAccount()

            if(binding.chkbSaveInfor.isChecked){
                val account = Account()
                account.numberPhone = "0374164756"
                account.password = "123456"
                DataLocalManager.setAccount(account)
            }
        }

        binding.chkbSaveInfor.setOnClickListener{

        }

        binding.logInWithGoogle.setOnClickListener{

        }
    }


    //Kiểm tra tài khoản
    private fun existsAccount(){

        val taiKhoan = binding.edtTaiKhoan.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        val db = FirebaseFirestore.getInstance()
        AccountService(db).login(taiKhoan,password){b ->
            if(b){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("id_Account",taiKhoan)
                startActivity(intent)
                finishAffinity()
            }else{
                Toast.makeText(this,"Tài khoản hoặc mật khẩu không chính xác",Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Kiểm tra taì khaonr


    //ẩn bàn phím ảo
    fun hiddenKeyboard(){
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}