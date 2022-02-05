package com.example.contactscompose

import android.Manifest
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import com.example.contactscompose.ui.theme.ContactsComposeTheme

import android.graphics.BitmapFactory
import android.widget.SearchView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val viewModel: ContactsViewModel by viewModels()

        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        requestPermissionLauncher.launch(Manifest.permission.WRITE_CONTACTS)

        viewModel.runGetContacts(application)
        viewModel.getContacts().observe(this, Observer<ArrayList<Contact>> { contacts ->
            setContent {
                Navigation(contacts = contacts)
            }
        }
        )

        setContent {
            Text(text = "loading")
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
fun MainScreen(contacts: ArrayList<Contact>, navController: NavController){
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        SearchView(textState)
        ContactsList(contacts = contacts, navController, state= textState)
    }
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

//@Preview(showBackground = true)
//@Composable
//fun Demo() {
//    val contactList = ArrayList<Contact>()
//    contactList.add(Contact("122344","aa", "bbb", null, "ccc", "ddd", "eee", "fff", "ggg"))
//    contactList.add(Contact("1234324553","bbaaaa", "bbb", null, "ccc", "ddd", "eee", "fff", "ggg"))
//    ContactsList(contacts = contactList)

//    ContactCard(
//        contact = Contact(
//            "avi", "xahavi", null, "ccc",
//            "ddd", "eee", "fff", "ggg"
//        )
//    )

@Composable
fun Navigation(contacts: ArrayList<Contact>){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main"){
        composable("main"){
            MainScreen(contacts = contacts, navController )
        }
        composable("details/{contact_index}"
        ,arguments = listOf(navArgument("contact_index") { type = NavType.IntType })
        ) {navBackStackEntry ->
            navBackStackEntry.arguments?.getInt("contact_index")?.let{ index ->
                ContactDetailsScreen(contacts.get(index))

            }
        }
    }
}


@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = androidx.compose.ui.graphics.Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "search icon",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(onClick = { state.value = TextFieldValue("") }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )

                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = androidx.compose.ui.graphics.Color.White,
            cursorColor = androidx.compose.ui.graphics.Color.White,
            leadingIconColor = androidx.compose.ui.graphics.Color.White,
            trailingIconColor = androidx.compose.ui.graphics.Color.White,
            backgroundColor = androidx.compose.ui.graphics.Color.Gray,
            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    SearchView(textState)
}

@Composable
fun ContactsList(contacts: ArrayList<Contact>,navController: NavController,  state: MutableState<TextFieldValue>) {
    var filteredContacts: ArrayList<Contact>
    LazyColumn() {
        val searchedText = state.value.text
        filteredContacts = if(searchedText.isEmpty()){
            contacts
        } else{
            val resultsList = ArrayList<Contact>()
            for(contact in contacts){
                if(contact.firstName?.lowercase(Locale.getDefault())
                        ?.contains(searchedText.lowercase(Locale.getDefault())) == true
                    ||  contact.lastName?.lowercase(Locale.getDefault())
                        ?.contains(searchedText.lowercase(Locale.getDefault())) == true
                ) {
                    resultsList.add(contact)
                }
            }
            resultsList
        }
        items(filteredContacts, key = { it.id }) { contact ->
            ContactCard(contact = contact,
            onItemClick = {
                navController.navigate("details/${contacts.indexOf(contact)}"){
                    popUpTo("main"){
                        saveState = true
                    }

                    launchSingleTop = true
                    restoreState = true
                }

            })
        }
    }
}

fun filterContacts(text: String) {

}

@Composable
fun ContactCard(contact: Contact, onItemClick:(Contact) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .padding(16.dp)
            .clickable(onClick = { onItemClick(contact) })
    ) {

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                //todo add background(color=)
                .padding(16.dp)


        ) {
            contact.firstName?.let { ContactImage(contact.image, it.get(0)) }
            ContactBasicContent(contact)
        }
    }
}


@Composable
fun ContactImage(path: String?, letter: Char) {
    Card(
        shape = CircleShape,
        modifier = Modifier.size(48.dp),
        elevation = 4.dp
    ) {
        if (path != null && path != "") {
            Image(bitmap = imagePathToBitmap(path), contentDescription = "Contact Image")
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.Blue)
            ) {
                Text(
                    text = letter.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }

        }
    }
}


@Composable
fun ContactBasicContent(contact: Contact) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
    ) {
        contact.firstName?.let { Text(text = it) }
        contact.lastName?.let { Text(text = it) }
    }
}


fun imagePathToBitmap(path: String): ImageBitmap {
    val bmOptions = BitmapFactory.Options()
    var bitmap = BitmapFactory.decodeFile(path, bmOptions)
    return bitmap.asImageBitmap()
}