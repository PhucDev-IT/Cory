package com.developer.cory.FragmentLayout

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.developer.cory.Adapter.CustomDialog
import com.developer.cory.R
import com.developer.cory.ViewModel.RegisterViewModel
import com.developer.cory.databinding.FragmentSignUpWithPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class SignUpWithPhoneFragment : Fragment() {
    private lateinit var _binding:FragmentSignUpWithPhoneBinding
    private val binding get() = _binding
    private lateinit var customDialog: CustomDialog
    private val shareViewModel : RegisterViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpWithPhoneBinding.inflate(inflater,container,false)

        customDialog = CustomDialog(requireActivity())

        db = Firebase.firestore
        mAuth = FirebaseAuth.getInstance()
        handleEvent()

        return binding.root
    }


    private fun handleEvent(){
        binding.btnRegister.setOnClickListener{
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



            shareViewModel.setFullName(binding.edtFullName.text.toString().trim())
            shareViewModel.setPhone(binding.edtNumberPhone.text.toString().trim())
            shareViewModel.setPassword(binding.edtPassword.text.toString().trim())

            isExistsNumberPhone()

        }
    }

    //------- KIỂM TRA SỰ TỒN TẠI CỦA SỐ ĐIỆN THOẠI ---------
    private fun isExistsNumberPhone(){
        customDialog.dialogBasic("Đang kiểm tra ...")

        shareViewModel.numberPhone.value?.let {

            db.collection("account").document(it)
                .get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val document = task.result
                        if(document.exists()){
                            // Số điện thoại đã được đăng ký
                            binding.edtNumberPhone.error = "Số điện thoại đã được sử dụng"
                            Toast.makeText(context,"Số điện thoại đã được sử dụng", Toast.LENGTH_SHORT).show()
                            customDialog.closeDialog()
                        }else {
                            // Số điện thoại chưa được đăng ký
                            sendVerificationCode()
                        }
                    }
                }
                .addOnFailureListener { err->
                    err.message?.let { Log.e("Lỗi rồi: ", it) }
                    customDialog.closeDialog()
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
                        customDialog.closeDialog()

                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        customDialog.closeDialog()
                        Log.e(ContentValues.TAG, "Lỗi: ${p0.message}")
                        Toast.makeText(context, p0.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onCodeSent(
                        verificationID: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationID, p1)
                        customDialog.closeDialog()
                        // Save verification ID
                        shareViewModel.setVerificationId(verificationID)
                        shareViewModel.isAllowSignUpWithPhone(true)
                        binding.btnRegister.visibility = View.GONE
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