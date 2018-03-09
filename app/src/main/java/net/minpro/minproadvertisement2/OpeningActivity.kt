package net.minpro.minproadvertisement2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_opening.*
import kotlin.collections.ArrayList

class OpeningActivity : AppCompatActivity() {


    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)

        val messageList = resources.getStringArray(R.array.message_list)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageList)
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val place = editTextPlace.text
            val speciality = editTextSpeciality.text
            val message = messageList[i]
            startActivity(Intent(this@OpeningActivity, MainActivity::class.java).apply {
                putExtra(IntentKey.PLACE.name, place)
                putExtra(IntentKey.SPECIALITY.name, place)
                putExtra(IntentKey.MESSAGE.name, message)
            })
        }

    }
}
