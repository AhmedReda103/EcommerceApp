package com.example.ecommerceapp.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.User
import com.example.ecommerceapp.databinding.FragmentRegisterBinding
import com.example.ecommerceapp.util.RegisterValidation
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val TAG = "RegisterFragment"
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            this.btnRegisterRegister.setOnClickListener {
                val user = User(
                    this.etFirstNameRegister.text.toString().trim(),
                    this.etLastNameRegister.text.toString().trim(),
                    this.etEmailRegister.text.toString().trim(),
                )
                val password = this.etPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnRegisterRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnRegisterRegister.revertAnimation()
                        Log.d(TAG, it.data?.email.toString())
                    }
                    is Resource.Error -> {
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }

        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etEmailRegister.apply {
                            requestFocus()
                            error = (validation.email as RegisterValidation.Failed).message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etPasswordRegister.apply {
                            requestFocus()
                            error = (validation.password as RegisterValidation.Failed).message
                        }
                    }
                }

            }
        }


    }
}