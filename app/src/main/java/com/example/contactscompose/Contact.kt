package com.example.contactscompose

import java.io.Serializable

data class Contact(
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val image: String?,
    val workEmail: String?,
    val homeEmail: String?,
    val homePhone: String?,
    val mobilePhone: String?,
    val workPhone: String?): Serializable