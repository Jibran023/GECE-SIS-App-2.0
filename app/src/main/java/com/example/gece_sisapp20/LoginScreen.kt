package com.example.gece_sisapp20

import Faculty_Admin.Dashboards.AdminDashboard
import Faculty_Admin.Dashboards.FacultyDashboard
import Student.Dashboard.StudentDashboard
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.NonCancellable.key
import org.bouncycastle.jcajce.provider.symmetric.ARC4.Base
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.widget.CheckBox
import java.security.Key
import com.example.gece_sisapp20.EncryptionUtil



//class LoginScreen : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_loginscreen)
//
//        val login_API = "https://regres.in/api/users"
//        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//
//        val loginbtn = findViewById<Button>(R.id.loginbutton)
//        val username_input = findViewById<EditText>(R.id.username_input)
//        val password_input = findViewById<EditText>(R.id.password_input)
//
//
//        val valid_credentials_students = mapOf(
//            "js08312" to "mazerunner",
//            "smh00001" to "gecesisapp"
//        )
//        val valid_credentials_faculty = mapOf(
//            "mm04321" to "movania",
//            "ms05432" to "msamad"
//        )
//        val valid_credentials_admin = mapOf(
//            "muzammil" to "smh123",
//            "jibran" to "js08312"
//        )
//
//
//        loginbtn.setOnClickListener {
//            val username = username_input.text.toString().trim()
//            val password = password_input.text.toString().trim()
//
//            // Checking if username or password is empty
//            if (username.isEmpty())
//            {
//                username_input.error = "Username cannot be empty"
//                return@setOnClickListener
//            }
//            if (password.isEmpty())
//            {
//                password_input.error = "Password cannot be empty"
//                return@setOnClickListener
//            }
//
//            var userType: String? = null
//
//            // Checking the credentials for students
//            if (valid_credentials_students.containsKey(username) && valid_credentials_students[username] == password) {
//                userType = "student"
//            }
//            // Checking the credentials for faculty
//            else if (valid_credentials_faculty.containsKey(username) && valid_credentials_faculty[username] == password) {
//                userType = "faculty"
//            }
//            // Checking the credentials for admin
//            else if (valid_credentials_admin.containsKey(username) && valid_credentials_admin[username] == password) {
//                userType = "admin"
//            }
//
//
//            // Navigate to the respective dashboard based on the user type
//            when (userType) {
//                "student" -> {
//                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, StudentDashboard::class.java).apply {
//                        putExtra("USER_TYPE", userType)
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    }
//                    startActivity(intent)
//                }
//                "faculty" -> {
//                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, FacultyDashboard::class.java).apply {
//                        putExtra("USER_TYPE", userType)
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    }
//                    startActivity(intent)
//                }
//                "admin" -> {
//                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, AdminDashboard::class.java).apply {
//                        putExtra("USER_TYPE", userType)
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    }
//                    startActivity(intent)
//                }
//                else -> {
//                    Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        }
//    }
//}


// CODE WITH API - Faculty and User both should be retrieved in the API

//class LoginScreen : AppCompatActivity() {
//    private lateinit var textView: TextView
//    private lateinit var textViewlol: TextView
//    private lateinit var welcometxtviews: TextView
//    private lateinit var mainurl : String
//
//    val secretkey = "Humara@Secret@Nahinbtana"
//    val secretkeyspec =  SecretKeySpec(secretkey.toByteArray(), "AES")
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_loginscreen)
//
//        // METHOD TO RETRIEVE THE ENCRYPTED KEY FROM URL
////        val url = "https://gece.edu.pk/Application/fetchData.txt"
////        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
////        val stringreq = StringRequest(Request.Method.GET, url, {
////            response ->
////            Log.d("Login Screen", "Fetched Data: $response")
////            textView.text = response
////            Toast.makeText(this, "Fetched Data: $response", Toast.LENGTH_LONG).show()
////        }, { error: VolleyError ->
////            Log.e("Login Screen", "Error fetching the data: ${error.message}")
////            textView.text = error.toString()
////            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
////        })
////        reqQueue.add(stringreq)
//
//        textView = findViewById(R.id.testingpurpose)
//        textViewlol = findViewById(R.id.testingpurposes)
//        welcometxtviews = findViewById(R.id.welcometxtview)
//
//
//
//        val url = "https://gece.edu.pk/Application/fetchData.txt"
//        val decryptedkey = "7fgKWvh3HoprSGDiDEgSFFVAAKup4FTSlx7yV8Q1iHY="
//        val urlapi = "results.gece.edu.pk"
//
//        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val stringreq = StringRequest(Request.Method.GET, url, {
//                response ->
//            Log.d("Login Screen", "Fetched Data: $response")
//            try {
//                val encryptedkey = response // This is the encrypted key which we were retrieving from the URL, this would be replaced by muzammil and then we will be only using this
//                val decryptedResponse = encrypt(urlapi) // This will encrypt the api url
//                mainurl = decrypt(decryptedResponse)
//                Log.d("LoginScreen", "Decrypted Data: $decryptedResponse")
//                textViewlol.text = response // this is the key to be decrypted
//                textView.text = decryptedResponse // this will be the encrypted key of the api url
//                welcometxtviews.text = mainurl // this will be the url we get back after decryption
//                Log.d("newencryptedkey", "This is the encrypted key to be used: $decryptedResponse")
//                Toast.makeText(this, "Decrypted Data: $decryptedResponse", Toast.LENGTH_LONG).show()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(this, "Decryption failed", Toast.LENGTH_SHORT).show()
//            }
//
//        }, { error: VolleyError ->
//            Log.e("Login Screen", "Error fetching the data: ${error.message}")
//            textView.text = error.toString()
//            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
//        })
//        reqQueue.add(stringreq)
//
//
//
//        val loginbtn: Button = findViewById(R.id.loginbutton)
//        val usernameInput: EditText = findViewById(R.id.username_input)
//        val passwordInput: EditText = findViewById(R.id.password_input)
//
//        val apigetcohorts = "https://results.gece.edu.pk/cohorts.php"
//
//        // Create a JsonObjectRequest
//        val jsonArrayRequest = JsonArrayRequest(
//            Request.Method.GET,
//            apigetcohorts,
//            null, // No request body for GET requests
//            { response ->
//                // Handle the JSON array response
//                Log.d("CohortsData", "Fetched JSON Data: $response")
//                try {
//                    // Example of extracting data from the JSON array
//                    for (i in 0 until response.length()) {
//                        val jsonObject = response.getJSONObject(i)
//                        val cohort = jsonObject.getString("cohort")
//                        Log.d("CohortsData", "Cohort: $cohort")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            },
//            { error ->
//                // Handle the error
//                Log.e("CohortsData", "Error fetching the data: ${error.message}")
//            }
//        )
//
//        // Add the request to the RequestQueue
//        reqQueue.add(jsonArrayRequest)
//    }
//
//
//
//    // The method will use the same secret key and url.
//    private fun encrypt(string: String) : String {
//        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding") // Which mode of AES is to be used
//        cipher.init(Cipher.ENCRYPT_MODE, secretkeyspec) // Specifying the mode Encrypt or decrypt
//        var encryptbytes = cipher.doFinal(string.toByteArray(Charsets.UTF_8)) // Coverting the string that will be encrypted to bytes
//        return Base64.encodeToString(encryptbytes, Base64.DEFAULT) // returning the encrypted string
//    }
//
//    private fun decrypt(string: String) : String{
//        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
//        cipher.init(Cipher.DECRYPT_MODE, secretkeyspec)
//        var decryptbytes = cipher.doFinal(Base64.decode(string, Base64.DEFAULT)) // decoding the entered string
//        return String(decryptbytes, Charsets.UTF_8) // returning the decrypted string
//    }
//
//}


//        val url = "https://gece.edu.pk/Application/fetchData.txt"
//        val decryptedkey = "7fgKWvh3HoprSGDiDEgSFFVAAKup4FTSlx7yV8Q1iHY="
//        val urlapi = "results.gece.edu.pk"
//
//        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val stringreq = StringRequest(Request.Method.GET, url, {
//                response ->
//            Log.d("Login Screen", "Fetched Data: $response")
//            try {
//                val encryptedkey = response // This is the encrypted key which we were retrieving from the URL, this would be replaced by muzammil and then we will be only using this
//                val decryptedResponse = encrypt(urlapi) // This will encrypt the api url
//                mainurl = decrypt(decryptedResponse)
//                Log.d("LoginScreen", "Decrypted Data: $decryptedResponse")
//                textViewlol.text = response // this is the key to be decrypted
//                textView.text = decryptedResponse // this will be the encrypted key of the api url
//                welcometxtviews.text = mainurl // this will be the url we get back after decryption
//                Log.d("newencryptedkey", "This is the encrypted key to be used: $decryptedResponse")
//                Toast.makeText(this, "Decrypted Data: $decryptedResponse", Toast.LENGTH_LONG).show()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(this, "Decryption failed", Toast.LENGTH_SHORT).show()
//            }
//
//        }, { error: VolleyError ->
//            Log.e("Login Screen", "Error fetching the data: ${error.message}")
//            textView.text = error.toString()
//            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
//        })
//        reqQueue.add(stringreq)

data class UserCredentials(val id: String, val password: String)


class LoginScreen : AppCompatActivity() {
    private val validCredentialsStudents = mutableMapOf<String, UserCredentials>()
    private val validCredentialsFaculty = mutableMapOf<String, UserCredentials>()
    private val validCredentialsAdmin = mutableMapOf<String, UserCredentials>()
    private val validCredentialsother = mutableMapOf<String, UserCredentials>()

    private lateinit var mainurl : String
    val secretkey = "Humara@Secret@Nahinbtana"
    val secretkeyspec =  SecretKeySpec(secretkey.toByteArray(), "AES")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginscreen)

        val showpassword : CheckBox = findViewById(R.id.show_password_checkbox)
        val loginbtn: Button = findViewById(R.id.loginbutton)
        val usernameInput: EditText = findViewById(R.id.username_input)
        val passwordInput: EditText = findViewById(R.id.password_input)


        showpassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                // Hide password
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Move cursor to the end of the input field
            passwordInput.setSelection(passwordInput.text.length)
        }

        fetchUsersData()
        fetchfacultydata()
        fetchstudentsdata()

        loginbtn.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Checking if username or password is empty
            if (username.isEmpty()) {
                usernameInput.error = "Username cannot be empty"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordInput.error = "Password cannot be empty"
                return@setOnClickListener
            }

            var userType: String? = null
            var userID: String? = null

            var encryptedpassword = encryptPasswordToMD5(password)

            // Check the credentials for students
            validCredentialsStudents[username]?.let {
                if (it.password == encryptedpassword) {
                    userType = "student"
                    userID = it.id
                }
            }
            // Check the credentials for faculty
            validCredentialsFaculty[username]?.let {
                if (it.password == encryptedpassword) {
                    userType = "faculty"
                    userID = it.id
                }
            }
            // Check the credentials for admin
            validCredentialsAdmin[username]?.let {// Change the password to encryptedpassword at then end
                if (it.password == password) {
                    userType = "admin"
                    userID = it.id
                }
            }

            // Check the credentials for other users
            validCredentialsother[username]?.let {// Change the password to encryptedpassword at then end
                if (it.password == password) {
                    userType = "other"
                    userID = it.id
                }
            }

            // Navigate to the respective dashboard based on the user type
            when (userType) {
                "student" -> {
                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, StudentDashboard::class.java).apply {
                        putExtra("USER_TYPE", userType)
                        putExtra("USER_ID", userID)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                "faculty" -> {
                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, FacultyDashboard::class.java).apply {
                        putExtra("USER_TYPE", userType)
                        putExtra("USER_ID", userID)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                "admin" -> {
                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminDashboard::class.java).apply {
                        putExtra("USER_TYPE", userType)
                        putExtra("USER_ID", userID)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                "other" -> {
                    Toast.makeText(this, "User type: $userType", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, OtherDashboard::class.java).apply {
                        putExtra("USER_TYPE", userType)
                        putExtra("USER_ID", userID)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }



    // The method will use the same secret key and url.
    private fun encrypt(string: String) : String {
        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding") // Which mode of AES is to be used
        cipher.init(Cipher.ENCRYPT_MODE, secretkeyspec) // Specifying the mode Encrypt or decrypt
        var encryptbytes = cipher.doFinal(string.toByteArray(Charsets.UTF_8)) // Coverting the string that will be encrypted to bytes
        return Base64.encodeToString(encryptbytes, Base64.DEFAULT) // returning the encrypted string
    }

    private fun decrypt(string: String) : String{
        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretkeyspec)
        var decryptbytes = cipher.doFinal(Base64.decode(string, Base64.DEFAULT)) // decoding the entered string
        return String(decryptbytes, Charsets.UTF_8) // returning the decrypted string
    }

    // The above functions are used for the URL we will be retrieving



    private fun fetchUsersData(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetcohorts = "http://192.168.18.55/geceapi/Login/usersdata.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("UsersData", "Fetched JSON Data: $response")
                try {
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val id = jsonObject.getString("id") // Assuming 'id' is a string
                        val email = jsonObject.getString("Email")
                        val password = jsonObject.getString("Password")
                        val department = jsonObject.getString("department")

                        val credentials = UserCredentials(id, password)

                        when (department) {
                            "IT" -> validCredentialsAdmin[email] = credentials
                            "Registrar" -> validCredentialsAdmin[email] = credentials
                            else -> validCredentialsother[email] = credentials
                        }

                        Log.d("UsersData", "Email: $email Password: $password  Department: $department")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("UsersData", "Error fetching the data: ${error.message}")
            }
        )

        reqQueue.add(jsonArrayRequest)
    }


// THIS FUNCTION USES A JSON REQUEST SO THERE IS NO ENCRYPTION/DECRYPTION HERE | CHECK THE URL BEFORE USE!
//    private fun fetchUsersData() {
//        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apiGetUsers = "http://192.168.18.55/geceapi/usersdata.php"
//
//        val jsonArrayRequest = JsonArrayRequest(
//            Request.Method.GET,
//            apiGetUsers,
//            null,
//            { response ->
//                Log.d("JSON_USERS_DATA", "USERS JSON Data: $response") // OUTPUTTING THE JSON DATA
//                try {
//                    for (i in 0 until response.length()) {
//                        val jsonObject = response.getJSONObject(i)
//                        val id = jsonObject.getString("id")
//                        val email = jsonObject.getString("Email")
//                        val password = jsonObject.getString("Password")
//                        val department = jsonObject.getString("department")
//
//                        val credentials = UserCredentials(id, password)
//                        when (department) {
//                            "IT" -> validCredentialsAdmin[email] = credentials
//                            "Registrar" -> validCredentialsAdmin[email] = credentials
//                            else -> validCredentialsother[email] = credentials
//                        }
//                        Log.d("UsersDataNew", "Email: $email Password: $password Department: $department")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            },
//            { error ->
//                Log.e("UsersDataNew", "Error fetching the data: ${error.message}")
//            }
//        )
//
//        reqQueue.add(jsonArrayRequest)
//    }


    // THIS FUNCTION TAKES BASE64 STRING AND DECODES IT | IT USES usersdata2.php
//    private fun fetchUsersData() {
//        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apiGetUsers = "http://192.168.18.55/geceapi/usersdata2.php"
//
//        val stringRequest = StringRequest(
//            Request.Method.GET,
//            apiGetUsers,
//            { response ->
//                try {
//                    // Extract the encrypted data from the response string
//                    val base64EncodedEncryptedData = response.trim() // Ensure there's no extra whitespace
//                    Log.d("UsersDataString", "Base64 Encoded Encrypted Data: $base64EncodedEncryptedData")
//
//                    // Decode Base64 to get the data as byte array
//                    val decodedData = Base64.decode(base64EncodedEncryptedData, Base64.DEFAULT)
//
//                    // Convert byte array to String
//                    val decodedDataString = String(decodedData, Charsets.UTF_8)
//                    Log.d("DecodedData", "Decoded Data String: $decodedDataString")
//
//
//                    // Parse the decrypted JSON data
//                    val jsonArray = JSONArray(decodedDataString) // we parse it to an array
//                    for (i in 0 until jsonArray.length()) { // we retrieve the data
//                        val jsonObject = jsonArray.getJSONObject(i)
//                        val id = jsonObject.getString("id")
//                        val email = jsonObject.getString("Email")
//                        val password = jsonObject.getString("Password")
//                        val department = jsonObject.getString("department")
//
//                        val credentials = UserCredentials(id, password)
//                        when (department) {
//                            "IT" -> validCredentialsAdmin[email] = credentials
//                            "Registrar" -> validCredentialsAdmin[email] = credentials
//                            else -> validCredentialsother[email] = credentials
//                        }
//                        Log.d("UsersDataNew", "Email: $email Password: $password Department: $department")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            },
//            { error ->
//                Log.e("UsersDataNew", "Error fetching the data: ${error.message}")
//            }
//        )
//        reqQueue.add(stringRequest)
//    }


    // THIS FUNCTION TAKES BASE64 STRING AND DECODES IT | IT USES usersdata2.php
//    private fun fetchUsersData() {
//        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
//        val apiGetUsers = "http://192.168.18.55/geceapi/usersdataencoded.php"
//        val keY = "5v5CzfuycMTBnEu7X2KwbZ8qg65O9RPZr43gXTL5zMDobBa0qgJxUhbACYIfTGAX"
//
//        val stringRequest = StringRequest(
//            Request.Method.GET,
//            apiGetUsers,
//            { response ->
//                try {
//                    // Extract the encrypted data from the response string
//                    val base64EncodedEncryptedData = response.trim() // Ensure there's no extra whitespace
//                    Log.d("UsersDataString", "Base64 Encoded Encrypted Data: $base64EncodedEncryptedData")
//
////                    // Decode Base64 to get the data as byte array
////                    val decodedData = Base64.decode(base64EncodedEncryptedData, Base64.DEFAULT)
////
////                    // Convert byte array to String
////                    val decodedDataString = String(decodedData, Charsets.UTF_8)
////                    Log.d("DecodedData", "Decoded Data String: $decodedDataString")
//
//                    val decrypteddata = EncryptionUtil.decryptJson(base64EncodedEncryptedData, keY)
//                    Log.d("DecryptedData", "Decrypted Data: $decrypteddata")
//
//                    // Parse the decrypted JSON data
//                    val jsonArray = JSONArray(decrypteddata) // we parse it to an array
//                    for (i in 0 until jsonArray.length()) { // we retrieve the data
//                        val jsonObject = jsonArray.getJSONObject(i)
//                        val id = jsonObject.getString("id")
//                        val email = jsonObject.getString("Email")
//                        val password = jsonObject.getString("Password")
//                        val department = jsonObject.getString("department")
//
//                        val credentials = UserCredentials(id, password)
//                        when (department) {
//                            "IT" -> validCredentialsAdmin[email] = credentials
//                            "Registrar" -> validCredentialsAdmin[email] = credentials
//                            else -> validCredentialsother[email] = credentials
//                        }
//                        Log.d("UsersDataNew", "Email: $email Password: $password Department: $department")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            },
//            { error ->
//                Log.e("UsersDataNew", "Error fetching the data: ${error.message}")
//            }
//        )
//        reqQueue.add(stringRequest)
//    }


    private fun fetchfacultydata(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetcohorts = "http://192.168.18.55/geceapi/Login/facultydata.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("FacultyData", "Fetched JSON Data: $response")
                try {
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val id = jsonObject.getString("FacultyID") // Assuming 'id' is a string
                        val email = jsonObject.getString("Email")
                        val password = jsonObject.getString("Password")
                        val status = jsonObject.getString("Status")

                        if (status == "Active") {
                            val credentials = UserCredentials(id, password)
                            validCredentialsFaculty[email] = credentials
                        }

//                        facultyID = id

                        Log.d("FacultyData", "Email: $email Password: $password Status: $status")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("FacultyData", "Error fetching the data: ${error.message}")
            }
        )

        reqQueue.add(jsonArrayRequest)
    }

    private fun fetchstudentsdata(){
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val apigetcohorts = "http://192.168.18.55/geceapi/Login/studentsdata.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apigetcohorts,
            null,
            { response ->
                Log.d("StudentsData", "Fetched JSON Data: $response")
                try {
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val id = jsonObject.getString("id") // Assuming 'id' is a string
                        val email = jsonObject.getString("Email")
                        val status = jsonObject.getString("Status")
                        val password = jsonObject.getString("Password")

                        if (status == "Active") {
                            val credentials = UserCredentials(id, password)
                            validCredentialsStudents[email] = credentials
                        }
                        Log.d("StudentsData", "Email: $email Password: $password")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("StudentsData", "Error fetching the data: ${error.message}")
            }
        )

        reqQueue.add(jsonArrayRequest)
    }

    fun encryptPasswordToMD5(password: String): String { // Used to decrypt the password and checked in the database
        try {
            val md = MessageDigest.getInstance("MD5")
            val messageDigest = md.digest(password.toByteArray())
            val hexString = StringBuilder()
            for (byte in messageDigest) {
                val hex = String.format("%02x", byte)
                hexString.append(hex)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

}