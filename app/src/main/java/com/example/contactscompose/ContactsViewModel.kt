package com.example.contactscompose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class ContactsViewModel (
    application: Application
) : AndroidViewModel(application) {
    private val contactsRepository: ContactsRepository = ContactsRepository()

    fun runGetContacts(application: Application){
        viewModelScope.launch {
            val result = contactsRepository.getContacts(application)
        }
    }

    fun getContacts(): MutableLiveData<ArrayList<Contact>> {
        return contactsRepository.contactsLiveData
    }




}