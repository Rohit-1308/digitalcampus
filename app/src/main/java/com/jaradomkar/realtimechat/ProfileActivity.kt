package com.jaradomkar.realtimechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.jaradomkar.realtimechat.model.totalAttendanceData
import com.jaradomkar.realtimechat.model.userInfoData
import com.jaradomkar.realtimechat.repository.Repository
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject.NULL

class ProfileActivity : AppCompatActivity() {

    private lateinit var backArrow:ImageView
    private lateinit var editProfile:Button
    private lateinit var changePassword:Button
    private lateinit var viewModel:MainViewModel
    private lateinit var editName:EditText
    private lateinit var editEmail:EditText
    private lateinit var editPhone:EditText
    private lateinit var editBranch:EditText
    private lateinit var editYear:EditText
    private lateinit var editPresenti:EditText
    private lateinit var editRollNumber:EditText
    private lateinit var listlayout:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val repository = Repository();
        val viewModelFactory = MainViewModelFactory(repository);
        viewModel = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java);

        val userData = userInfoData(intent.getStringExtra("email").toString())
        viewModel.pushEmailForUserInfo(userData)


        backArrow=findViewById(R.id.back_arrow)
        editProfile=findViewById(R.id.edit_profile)
        changePassword=findViewById(R.id.user_change_password)
        editName=findViewById(R.id.edit_name)
        editEmail=findViewById(R.id.edit_email)
        editPhone=findViewById(R.id.edit_phone)
        editBranch=findViewById(R.id.edit_branch)
        editYear=findViewById(R.id.edit_year)
        editPresenti=findViewById(R.id.edit_presenti)
        editRollNumber=findViewById(R.id.edit_rollNumber)
        listlayout=findViewById(R.id.profile_list_layout)


        editProfile.setOnClickListener{
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }
        if(editName.text==NULL){
            editProfile.text="Add Profile"
            listlayout.visibility=INVISIBLE
        }
        else{
            editProfile.text="Edit Profile"
            listlayout.visibility=VISIBLE
        }
        viewModel.userDataResponse.observe(this) { response ->
            if (response.isSuccessful) {

                if(response.body() !==null) {
                    listlayout.visibility=VISIBLE
                    editName.setText(response.body()?.UserInfo!!.name.toString())
                    editEmail.setText(response.body()?.UserInfo!!.email.toString())
                    editPhone.setText(response.body()?.UserInfo!!.phone.toString())
                    editBranch.setText(response.body()?.UserInfo!!.branch.toString())
                    editYear.setText(response.body()?.UserInfo!!.year.toString())
                    editRollNumber.setText(response.body()?.UserInfo!!.rollNumber.toString())

                    val data = totalAttendanceData((response.body()?.UserInfo!!.branch.toString()),response.body()?.UserInfo!!.rollNumber.toString())
                    viewModel.pushRollNumber(data)

                    Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.totalAttendanceDataResponse.observe(this){response->

            if(response.isSuccessful){
                editPresenti.setText(response.body()?.totalPercentage!!.toString()+" %")
            }

        }

        backArrow.setOnClickListener{
            val intent = Intent(this@ProfileActivity,MainActivity::class.java);
            startActivity(intent)
        }

        changePassword.setOnClickListener{
            val intent = Intent(this@ProfileActivity,ChangePasswordActivity::class.java);
            startActivity(intent)
        }


    }
}

private fun EditText.extractText(toString: String) {

}
