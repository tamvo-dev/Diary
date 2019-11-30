package com.example.diary.ui.post

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diary.R
import com.example.diary.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorListener
import kotlinx.android.synthetic.main.activity_post.*
import java.util.*
import android.view.ViewGroup
import android.widget.Button
import com.example.diary.ui.main.MainActivity
import com.google.firebase.Timestamp


class PostActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var mReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var post : Post? = null
    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0
    private var background = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)


        auth = FirebaseAuth.getInstance()
        mReference = FirebaseDatabase.getInstance().reference.child("posts")

        post_floatButton.setOnClickListener {
            addPost()
        }

        post_bottom_nav.setOnNavigationItemSelectedListener {
            it.isCheckable = false
            when(it.itemId){
                R.id.date_picker -> {
                    initDatePicker()
                }

                R.id.time_picker -> {
                    initTimePicker()
                }

                R.id.post_gb_color -> {
                    initColorPicker()
                }

                R.id.date_delete_post -> {

                }

            }

            true
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.day = dayOfMonth
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.minute = minute
    }

    fun addPost(){
        val content = post_edit_content.text.toString()
        val title = post_edit_title.text.toString()

        if (content.isBlank()){
            Toast.makeText(this, "Phải nhập content", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isBlank()){
            Toast.makeText(this, "Phải nhập title", Toast.LENGTH_SHORT).show()
            return
        }

        if (intent.action.equals(MainActivity.CREATE_ACTION)){
            post = Post()
            post?.let {
                it.title = title
                it.content = content
                val date = Date(year, month, day, hour, minute)
                val timestamp = Timestamp(date)
                it.timestamp = timestamp.seconds
                it.background = background
            }
            val userName = auth.currentUser?.displayName
            if (userName != null){
                post?.author = userName
            }

            val id = mReference.push().key!!
            post?.id = id
            mReference.child(id).setValue(post).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "Đã thêm thành công!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun initTimePicker(){
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, hourOfDay, minute, true)
        timePickerDialog.show()
    }

    fun initDatePicker(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val datePickerDialog = DatePickerDialog(this, this, year, month, date)
        datePickerDialog.show()
    }

    fun initColorPicker() {
        val colorListener =  object : ColorListener{
            override fun onColorSelected(color: Int, fromUser: Boolean) {
                this@PostActivity.background = color
            }

        }

        ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Ok", colorListener)
            .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }

            })
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .show();
    }

}