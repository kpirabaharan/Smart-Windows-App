package com.example.smartwindowsapp

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var firstTimeUser = true
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        buttonClicks()
    }

    override fun onStart(){
        super.onStart()
        checkIfUserIsLoggedIn()
    }

    private fun buttonClicks(){
        btn_login.setOnClickListener {
            firstTimeUser = false
            createOrLoginUser()
        }
        btn_register.setOnClickListener {
            firstTimeUser = true
            createOrLoginUser()
        }
        iv_profileImage.setOnClickListener{
            selectImage()
        }
    }

    private fun createOrLoginUser(){
        val email = et_emailLogin.text.toString()
        val password = et_passwordLogin.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            GlobalScope.launch(Dispatchers.IO) {
            try{
                if(firstTimeUser){
                    auth.createUserWithEmailAndPassword(email, password).await()
                    auth.currentUser.let{
                        val update = UserProfileChangeRequest.Builder()
                            .setPhotoUri(fileUri)
                            .build()
                        it?.updateProfile(update)
                    }?.await()
                } else{
                    auth.signInWithEmailAndPassword(email, password).await()
                }
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoginActivity,"You are now logged in!", Toast.LENGTH_SHORT).show()
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoginActivity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            }
        }
    }
    private fun checkIfUserIsLoggedIn(){
        if(auth.currentUser != null){
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
    private fun selectImage(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){
            Activity.RESULT_OK ->{
                fileUri = data?.data
                iv_profileImage.setImageURI(fileUri)
            }
            ImagePicker.RESULT_ERROR->{
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else ->{
                Toast.makeText(this, "Task cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}