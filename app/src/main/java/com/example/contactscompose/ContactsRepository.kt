package com.example.contactscompose


import android.app.Application
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ContactsRepository {

    var contacts: ArrayList<Contact>? = ArrayList();

    val contactsLiveData: MutableLiveData<ArrayList<Contact>> by lazy {
        MutableLiveData<ArrayList<Contact>>()
    }

    suspend fun getContacts(application: Application){
        return withContext(Dispatchers.IO){
            val contactsUri = ContactsContract.Contacts.CONTENT_URI
            val contentResolver = application.getContentResolver()

            // Querying the table ContactsContract.Contacts to retrieve all the
            // contacts
            val contactsCursor: Cursor? = contentResolver.query(
                contactsUri,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC "
            )
            if (contactsCursor != null) {
                if (contactsCursor.moveToFirst()) {
                    do {
                        val contactId = contactsCursor.getLong(
                            contactsCursor
                                .getColumnIndex("_ID")
                        )
                        val dataUri = ContactsContract.Data.CONTENT_URI

                        // Querying the table ContactsContract.Data to retrieve
                        // individual items like
                        // home phone, mobile phone, work email etc corresponding to
                        // each contact
                        val dataCursor: Cursor = contentResolver.query(
                            dataUri,
                            null,
                            ContactsContract.Data.CONTACT_ID + "=" + contactId,
                            null, null
                        )!!
                        var homePhone: String? = ""
                        var mobilePhone: String? = ""
                        var workPhone: String? = ""
                        var photoPath: String? = ""
                        var photoByte: ByteArray? = null
                        var homeEmail: String? = ""
                        var workEmail: String? = ""
                        var familyName: String? = ""
                        var firstName: String? = ""
                        var id:String = UUID.randomUUID().toString()
                        if (dataCursor.moveToFirst()) {
                            // Getting Display Name
                            do {
                                //Getting LastName
                                if (dataCursor
                                        .getString(dataCursor.getColumnIndex("mimetype"))
                                    == ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                                ) familyName = dataCursor.getString(
                                    dataCursor
                                        .getColumnIndex("data3")
                                )
                                //Getting FirstName
                                if (dataCursor
                                        .getString(dataCursor.getColumnIndex("mimetype"))
                                    == ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                                ) firstName = dataCursor.getString(
                                    dataCursor
                                        .getColumnIndex("data2")
                                )
                                // Getting Phone numbers
                                if (dataCursor
                                        .getString(
                                            dataCursor
                                                .getColumnIndex("mimetype")
                                        )
                                    == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                                ) {
                                    when (dataCursor.getInt(
                                        dataCursor
                                            .getColumnIndex("data2")
                                    )) {
                                        ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> homePhone =
                                            dataCursor.getString(
                                                dataCursor
                                                    .getColumnIndex("data1")
                                            )
                                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> mobilePhone =
                                            dataCursor
                                                .getString(
                                                    dataCursor
                                                        .getColumnIndex("data1")
                                                )
                                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> workPhone =
                                            dataCursor.getString(
                                                dataCursor
                                                    .getColumnIndex("data1")
                                            )
                                    }
                                }
                                // Getting EMails
                                if (dataCursor
                                        .getString(
                                            dataCursor
                                                .getColumnIndex("mimetype")
                                        )
                                    == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                                ) {
                                    when (dataCursor.getInt(
                                        dataCursor
                                            .getColumnIndex("data2")
                                    )) {
                                        ContactsContract.CommonDataKinds.Email.TYPE_HOME -> homeEmail =
                                            dataCursor.getString(
                                                dataCursor
                                                    .getColumnIndex("data1")
                                            )
                                        ContactsContract.CommonDataKinds.Email.TYPE_WORK -> workEmail =
                                            dataCursor.getString(
                                                dataCursor
                                                    .getColumnIndex("data1")
                                            )
                                    }
                                }

                                // Getting Photo
                                if (dataCursor
                                        .getString(
                                            dataCursor
                                                .getColumnIndex("mimetype")
                                        )
                                    == ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                ) {
                                    photoByte = dataCursor.getBlob(
                                        dataCursor
                                            .getColumnIndex("data15")
                                    )
                                    if (photoByte != null) {
                                        val bitmap = BitmapFactory
                                            .decodeByteArray(
                                                photoByte, 0,
                                                photoByte.size
                                            )

                                        // Getting Caching directory
                                        val cacheDirectory: File = application.getBaseContext()
                                            .getCacheDir()

                                        // Temporary file to store the contact image
                                        val tmpFile = File(
                                            cacheDirectory.path + "/wpta_"
                                                    + contactId + ".png"
                                        )

                                        // The FileOutputStream to the temporary
                                        // file
                                        try {
                                            val fOutStream = FileOutputStream(
                                                tmpFile
                                            )

                                            // Writing the bitmap to the temporary
                                            // file as png file
                                            bitmap.compress(
                                                Bitmap.CompressFormat.PNG, 100,
                                                fOutStream
                                            )

                                            // Flush the FileOutputStream
                                            fOutStream.flush()

                                            // Close the FileOutputStream
                                            fOutStream.close()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        photoPath = tmpFile.path
                                    }
                                }
                            } while (dataCursor.moveToNext())
                            var details: String = ""

                            // Concatenating various information to single string
                            if (homePhone != null && homePhone != "") details =
                                "HomePhone : $homePhone\n"
                            if (mobilePhone != null && mobilePhone != "") details += "MobilePhone : $mobilePhone\n"
                            if (workPhone != null && workPhone != "") details += "WorkPhone : $workPhone\n"
                            if (homeEmail != null && homeEmail != "") details += "HomeEmail : $homeEmail\n"
                            if (workEmail != null && workEmail != "") details += "WorkEmail : $workEmail\n"
                            if (familyName != null && familyName != "") details += "FamilyName : $familyName\n"
                            if (firstName != null && firstName != "") details += "firstName : $firstName\n"


                            val contact = Contact(id, firstName, familyName, photoPath, workEmail, homeEmail, homePhone, mobilePhone, workPhone)

                            contacts?.add(contact)
                            contactsLiveData.postValue(contacts)


                        }
                    } while (contactsCursor.moveToNext())
                }
            }
        }
    }
}