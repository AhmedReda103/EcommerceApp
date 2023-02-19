package com.example.ecommerceapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.data.User
import com.example.ecommerceapp.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (
    var firebaseAuth: FirebaseAuth
) :ViewModel()  {

    private var _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    var register: Flow<Resource<FirebaseUser>> = _register
    private var _validation = Channel<RegisterFieldState>()
    var validation = _validation.receiveAsFlow()


     fun createAccountWithEmailAndPassword(user : User, password:String){

         if(checkValidation(user.email , password)){
            runBlocking {
                _register.emit(Resource.Loading())
            }

            firebaseAuth.createUserWithEmailAndPassword(user.email , password)
                .addOnSuccessListener {
                    it.user?.let {
                        _register.value = Resource.Success(it)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message)
                }
        }else{
            val registrationFieldState = RegisterFieldState(validateEmail(user.email),
            validatePassword(password)
            )
             runBlocking {
                 _validation.send(registrationFieldState)
             }

        }
    }

    private fun checkValidation(email : String, password: String) : Boolean{

        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        val shouldRegister = emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
        return shouldRegister
    }

}