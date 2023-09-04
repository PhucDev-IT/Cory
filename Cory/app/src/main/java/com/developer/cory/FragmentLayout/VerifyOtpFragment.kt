package com.developer.cory.FragmentLayout

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.developer.cory.Model.Account
import com.developer.cory.Model.User
import com.developer.cory.R
import com.developer.cory.Service.AccountService
import com.developer.cory.ViewModel.RegisterViewModel

import com.developer.cory.databinding.FragmentVerifyOtpBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class VerifyOtpFragment : Fragment() {
    private lateinit var _binding: FragmentVerifyOtpBinding
    private val binding get() = _binding
    private lateinit var navController: NavController
    private val shareViewModel: RegisterViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)

        db = Firebase.firestore
        mAuth = FirebaseAuth.getInstance()

        initView()
        handleEvent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.tvNumberPhone.text = shareViewModel.numberPhone.value

        setUpOTPInputs()
    }

    private fun handleEvent() {
        binding.btnXacNhan.setOnClickListener {
            onClickverifyOTP()
        }
    }

    //Input mã OTP
    private fun onClickverifyOTP() {

        if (binding.digit1.text.toString().trim().isEmpty() ||
            binding.digit1.text.toString().trim().isEmpty() ||
            binding.digit1.text.toString().trim().isEmpty() ||
            binding.digit1.text.toString().trim().isEmpty() ||
            binding.digit1.text.toString().trim().isEmpty() ||
            binding.digit1.text.toString().trim().isEmpty()
        ) {

            Toast.makeText(context, "Vui lòng nhập mã OTP", Toast.LENGTH_LONG).show()
            return
        }

        val otp: String = binding.digit1.text.toString().trim() +
                binding.digit2.text.toString().trim() +
                binding.digit3.text.toString().trim() +
                binding.digit4.text.toString().trim() +
                binding.digit5.text.toString().trim() +
                binding.digit6.text.toString().trim()

        val credential =
            shareViewModel.verificationId.value?.let { PhoneAuthProvider.getCredential(it, otp) }
        if (credential != null) {
            binding.progressbar.visibility = View.VISIBLE
            signInWithPhoneAuthCredential(credential)
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    Toast.makeText(
                        context, "Xác minh thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                    saveAccount()
                    binding.progressbar.visibility = View.GONE
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(
                            context, "Mã xác minh không chính xác",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // Update UI
                }
            }
    }

    //Đăng kí thành công
    private fun saveAccount() {

        val account =
            Account(shareViewModel.numberPhone.value,null, shareViewModel.password.value)
        val user = User(shareViewModel.fullName.value!!,account.numberPhone!!)
        AccountService(db).addAccount(account,user){b ->
            if(b){
                navController.navigate(R.id.action_verifyOtpFragment_to_registerCompletedFragment)
            }else{
                Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }
        }

    }


    //Set up view input otp
    //Set up input OTP
    private fun setUpOTPInputs() {
        binding.digit1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.digit2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
        binding.digit2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.digit3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
        binding.digit3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.digit4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
        binding.digit4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.digit5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
        binding.digit5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.digit6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // không cần thực hiện
            }
        })
    }

}