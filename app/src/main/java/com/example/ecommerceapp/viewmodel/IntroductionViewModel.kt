package com.example.ecommerceapp.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.R
import com.example.ecommerceapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth ,
    private val dataStore : DataStore<Preferences>
) : ViewModel() {


    private var user = firebaseAuth.currentUser

     private var isButtonClicked = booleanPreferencesKey(Constants.START_KEY)


    private val _navigate = MutableStateFlow(0)
     val navigate = _navigate

    companion object{
        const val SHOPPING_ACTIVITY = 10
        const val ACCOUNT_OPTIONS_FRAGMENT = R.id.action_introductionFragment_to_accountOptionsFragment
    }

    init {
       viewModelScope.launch {
            setButtonStartKeyInitialValue()
            setNavigation()
        }
    }

    private suspend fun setButtonStartKeyInitialValue() {
        dataStore.edit {pref->
            pref[isButtonClicked] = false
        }
    }

    private suspend fun setNavigation(){

        val preferences = dataStore.data.first()
        val isClicked = preferences[isButtonClicked]

        if(user !=null){
            _navigate.emit(SHOPPING_ACTIVITY)

        }else if(isClicked!!){
            _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
        }
        else{
            Unit
        }
    }

    fun startButtonClicked(){
        viewModelScope.launch {
        dataStore.edit {
            it[isButtonClicked] = true
          }
        }
    }



}