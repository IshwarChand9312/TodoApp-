package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.example.todoapp.databinding.ActivityTaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db"
class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding : ActivityTaskBinding
    lateinit var myCalender : Calendar

    var finalDate = 0L
    var finalTime = 0L

    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    var labels  = arrayListOf<String>("Critical","Important","On Time")

    val db by lazy {
        AppDatabase.getDatabase(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_task)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateEdt.setOnClickListener(this)
        binding.timeEdt.setOnClickListener(this)
        binding.saveBtn.setOnClickListener(this)

        setUpSpinner()

    }

    private fun setUpSpinner() {

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item , labels)

        binding.spinnerCategory.adapter = adapter

    }

    override fun onClick(view : View) {

        when(view.id)
        {
            R.id.dateEdt ->{
              setListener()
            }
            R.id.timeEdt ->{

                timesetListener()
            }

            R.id.saveBtn ->{

                saveTodo()

            }
        }
    }

    private fun saveTodo() {
        val category = binding.spinnerCategory.selectedItem.toString()
        val title = binding.titleInpLay.editText?.text.toString()
        val description = binding.taskInLay.editText?.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao().insertTask(
                        TodoModel(
                                title,
                                description,
                                category,
                                finalDate,
                                finalTime
                        )
                )
            }
            finish()
        }
    }

    private fun timesetListener() {

        myCalender = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, min ->

            myCalender.set(Calendar.HOUR_OF_DAY , hourOfDay)
            myCalender.set(Calendar.MINUTE, min)
            updateTime()
        }

        val timePickerDialog = TimePickerDialog(
                this , timeSetListener , myCalender.get(Calendar.HOUR_OF_DAY) , myCalender.get(Calendar.MINUTE) , false
        )

        timePickerDialog.show()
    }

    private fun updateTime() {

        val format = "h:mm a"
        val sdf = SimpleDateFormat(format)
        finalTime = myCalender.time.time
        binding.timeEdt.setText(sdf.format(myCalender.time))
    }

    private fun setListener() {

        myCalender = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year , month, dayOfMonth ->

            myCalender.set(Calendar.YEAR , year)
            myCalender.set(Calendar.MONDAY , month)
            myCalender.set(Calendar.DAY_OF_MONTH , dayOfMonth)
            updateDate()
        }


            val datePickerDialog = DatePickerDialog(
                    this, dateSetListener, myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)
            )


        datePickerDialog.datePicker.minDate =   System.currentTimeMillis()
        datePickerDialog.show()

    }

    private fun updateDate() {

        //Mon, 5 Jan 2020
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        finalDate = myCalender.time.time
        binding.dateEdt.setText(sdf.format(myCalender.time))

        binding.timeInLay.visibility = View.VISIBLE
    }
}