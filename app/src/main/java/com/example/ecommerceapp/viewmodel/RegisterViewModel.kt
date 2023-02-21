package com.example.ecommerceapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.data.User
import com.example.ecommerceapp.util.*
import com.example.ecommerceapp.util.Constants.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val firebaseAuth: FirebaseAuth,
    private val db : FirebaseFirestore
) :ViewModel()  {

    private var _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    var register: Flow<Resource<User>> = _register
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
                        saveInfo(it.uid , user )
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

    private fun saveInfo(Uuid: String , user : User) {
        db.collection(USER_COLLECTION).document(Uuid).set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }


    private fun checkValidation(email : String, password: String) : Boolean{

        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        val shouldRegister = emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
        return shouldRegister
    }

}