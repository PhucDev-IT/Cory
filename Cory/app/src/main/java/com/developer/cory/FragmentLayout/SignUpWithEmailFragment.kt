package com.developer.cory.FragmentLayout

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.developer.cory.Adapter.CustomDialog
import com.developer.cory.Model.Account
import com.developer.cory.Model.User
import com.developer.cory.R
import com.developer.cory.Service.AccountService
import com.developer.cory.ViewModel.RegisterViewModel
import com.developer.cory.databinding.FragmentSignUpWithEmailBinding
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

class SignUpWithEmailFragment : Fragment() {
    private lateinit var _binding: FragmentSignUpWithEmailBinding
    private val binding get() = _binding
    private lateinit var customDialog: CustomDialog
    private val shareViewModel: RegisterViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpWithEmailBinding.inflate(inflater, container, false)

        customDialog = CustomDialog(requireActivity())

        db = Firebase.firestore
        mAuth = FirebaseAuth.getInstance()
        handleEvent()

        return binding.root
    }


    private fun handleEvent() {
        binding.btnRegister.setOnClickListener {
            onClickSignUp()
        }
    }

    private fun onClickSignUp() {
        if (binding.edtFullName.text.toString().trim().isEmpty()) {
            binding.edtFullName.error = "Vui lòng nhập tên của bạn"
        } else if (binding.edtEmail.text.toString().trim().isEmpty()) {
            binding.edtEmail.error = "Chưa nhập email"
        } else if (binding.edtPassword.text.toString().trim().isEmpty()) {
            binding.edtPassword.error = "Mật khẩu không được để trống"
        } else if (binding.edtConfirmPassword.text.toString().trim().isEmpty() ||
            binding.edtConfirmPassword.text.toString().trim() != binding.edtPassword.text.toString()
                .trim()
        ) {
            binding.edtConfirmPassword.error = "Mật khẩu không khớp"
        } else {


            shareViewModel.setFullName(binding.edtFullName.text.toString().trim())
            shareViewModel.setEmail(binding.edtEmail.text.toString().trim())
            shareViewModel.setPassword(binding.edtPassword.text.toString().trim())

            checkEmailExists()
        }
    }

    //Kiểm tra email đã tồn tại chưa?
    private fun checkEmailExists() {
        customDialog.dialogBasic("Đang kiểm tra ...")
        shareViewModel.email.value?.let {
            db.collection("account").document(it)
                .get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        binding.edtEmail.error = "Email đã tồn tại!"
                        Toast.makeText(
                            context,
                            "Email đã tồn tại, vui lòng thử email khác",
                            Toast.LENGTH_SHORT
                        ).show()
                        customDialog.closeDialog()
                    } else {
                        createUserWithEmail()
                    }
                }
                .addOnFailureListener { error ->
                    Log.e(TAG, "Lỗi: ${error.message}")
                    Toast.makeText(context, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show()
                    customDialog.closeDialog()
                }
        }
    }

    private fun createUserWithEmail() {
        mAuth.createUserWithEmailAndPassword(
            shareViewModel.email.value!!,
            shareViewModel.password.value!!
        )
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Vui lòng xác minh tài khoản trong Email của bạn",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveUserAndAccount()
                        shareViewModel.updateIsAllowSignUp(true,"email")
                        customDialog.closeDialog()
                        binding.btnRegister.visibility = View.GONE
                    }
                        ?.addOnFailureListener {
                            Toast.makeText(context, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
                        }


                } else {
                    Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserAndAccount(){
        val account = Account(null,shareViewModel.email.value!!,shareViewModel.password.value)
        val user = User(shareViewModel.fullName.value!!,account.email!!)
        AccountService(db).addAccount(account,user){b ->
            if(!b){
                Log.e(TAG,"Có lỗi")
            }
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