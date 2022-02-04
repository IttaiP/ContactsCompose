package com.example.contactscompose

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import com.example.contactscompose.ui.theme.ContactsComposeTheme

import android.graphics.Bitmap

import android.graphics.BitmapFactory

import android.os.Environment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: ContactsViewModel by viewModels()



        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        requestPermissionLauncher.launch(Manifest.permission.WRITE_CONTACTS)

        viewModel.runGetContacts(application)
        viewModel.getContacts().observe(this, Observer<ArrayList<Contact>> { contacts ->
            Log.i("CONTACTS", contacts.toString())

        })



        if (viewModel.getContacts().value?.size == 0) {
            setContent {
                ContactsComposeTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        LoadingMessage("Android")
                    }
                }
            }
        } else {
            //todo: present contacts
            setContent {
//                ContactsList(contacts = dummyData)
            }
        }
    }

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        })
}

@Composable
fun LoadingMessage(name: String) {
    Text(text = "Hello $name!")
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    val dummyData: ArrayList<Contact> = ArrayList()
//    dummyData.add(Contact("aa", "bbb", null, "ccc", "ddd", "eee", "fff", "ggg"))
//    dummyData.add(Contact("bbaaaa", "bbb", null, "ccc", "ddd", "eee", "fff", "ggg"))
//
//    ContactsList(contacts = dummyData)
//}

@Preview(showBackground = true)
@Composable
fun Demo() {
    Card(elevation = 5.dp){
        Row(){
            Box(
                Modifier
                    .size(width = 150.dp, height = 150.dp)
                    .clip(CircleShape)) {
                Image(painterResource(R.drawable.background_color),"content description"
                )                     // clip to the circle shape

                Text(text = "f",   textAlign = TextAlign.Center,
                )

            }
            Contact(contact = Contact("aa", "bbb", null, "ccc", "ddd", "eee", "fff", "ggg"))
            
        }
        
    }


}

@Composable
fun ContactsList(contacts: ArrayList<Contact>) {
    LazyColumn {
        items(contacts) { contact ->
            Contact(contact = contact)
        }
    }
}


@Composable
fun Contact(contact: Contact) {
    Column {
        contact.firstName?.let { Text(text = it) }
        contact.lastName?.let { Text(text = it) }
        contact.firstName?.let { Text(text = it) }
        contact.firstName?.let { Text(text = it) }
        contact.firstName?.let { Text(text = it) }
        contact.firstName?.let { Text(text = it) }
        contact.firstName?.let { Text(text = it) }

    }
}

@Composable
fun ContactImage(path: String?, letter: String) {
    if (path != null) {
        Image(bitmap = imagePathToBitmap(path), contentDescription = "Contact Image")
    } else {
        Box() {
            Image(painterResource(R.drawable.background_color),"content description", modifier =
            Modifier
                .size(width = 400.dp, height = 100.dp)
                .clip(CircleShape)  )                     // clip to the circle shape

            Text(text = letter,   textAlign = TextAlign.Center,
            )
        }

    }
}


fun imagePathToBitmap(path: String): ImageBitmap {
    val bmOptions = BitmapFactory.Options()
    var bitmap = BitmapFactory.decodeFile(path, bmOptions)
    return bitmap as ImageBitmap
}