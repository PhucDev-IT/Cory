package com.developer.cory.FragmentLayout

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.developer.cory.R
import com.developer.cory.ViewModel.RegisterViewModel
import com.developer.cory.databinding.FragmentSignUpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


class SignUpFragment : Fragment() {
    private lateinit var _binding: FragmentSignUpBinding
    private val binding get() = _binding
    private lateinit var navController: NavController
    private val shareViewModel : RegisterViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)

        db = Firebase.firestore
        mAuth = FirebaseAuth.getInstance()
        handleEvent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun handleEvent(){
        binding.btnRegister.setOnClickListener {
            onClickSignUp()
        }
    }

    private fun onClickSignUp(){
        if(binding.edtFullName.text.toString().trim().isEmpty()){
            binding.edtFullName.error = "Vui lòng nhập tên của bạn"
        }else if(binding.edtNumberPhone.text.toString().trim().isEmpty()){
            binding.edtNumberPhone.error = "Chưa nhập số điện thoại"
        }else if(binding.edtPassword.text.toString().trim().isEmpty()){
            binding.edtPassword.error = "Mật khẩu không được để trống"
        }else if(binding.edtConfirmPassword.text.toString().trim().isEmpty() ||
            !binding.edtConfirmPassword.text.toString().trim().equals(binding.edtPassword.text.toString().trim())){
            binding.edtConfirmPassword.error = "Mật khẩu không khớp"
        }else {

            binding.progressBar.visibility = View.VISIBLE

            shareViewModel.setFullName(binding.edtFullName.text.toString().trim())
            shareViewModel.setPhone(binding.edtNumberPhone.text.toString().trim())
            shareViewModel.setPassword(binding.edtPassword.text.toString().trim())

            isExistsNumberPhone()

        }
    }

    //------- KIỂM TRA SỰ TỒN TẠI CỦA SỐ ĐIỆN THOẠI ---------
    private fun isExistsNumberPhone(){
        shareViewModel.numberPhone.value?.let {

            db.collection("account").document(it)
                .get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val document = task.result
                        if(document.exists()){
                            // Số điện thoại đã được đăng ký
                            binding.edtNumberPhone.error = "Số điện thoại đã được sử dụng"
                            Toast.makeText(context,"Số điện thoại đã được sử dụng", Toast.LENGTH_SHORT).show()
                        }else {
                            // Số điện thoại chưa được đăng ký
                            sendVerificationCode()
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                }
                .addOnFailureListener { err->
                    err.message?.let { Log.e("Lỗi rồi: ", it) }
                }
        }
    }

    private fun sendVerificationCode() {

        val options = shareViewModel.numberPhone.value?.let {
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(it)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        binding.progressBar.visibility = View.GONE

                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        binding.progressBar.visibility = View.GONE
                        Log.e(ContentValues.TAG, "Lỗi: ${p0.message}")
                        Toast.makeText(context, p0.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onCodeSent(
                        verificationID: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationID, p1)
                        binding.progressBar.visibility = View.GONE
                        // Save verification ID
                        shareViewModel.setVerificationId(verificationID)

                        navController.navigate(R.id.action_signUpFragment_to_verifyOtpFragment)

                    }
                })
                .build()
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }


    //ẩn bàn phím ảo
//    fun hiddenKeyboard(){
//        requireActivity().currentFocus?.let { view ->
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }
}