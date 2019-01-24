package net.minpro.minproadvertisement2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import kotlinx.android.synthetic.main.activity_opening.*
import net.minpro.minproadvertisement2.R.id.*
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

//都道府県ゆるキャラを格納するためのデータクラス
data class LocalMascot(val place: String, val mascot: String)



class OpeningActivity : AppCompatActivity() {

    lateinit var adapterMessage: ArrayAdapter<String>
    lateinit var adapterPlace: ArrayAdapter<String>
    var localMascotList = ArrayList<LocalMascot>()
    var stateList = ArrayList<String>()

    var inputPlace = ""
    var inputMascot = ""
    var message = ""

    var messageSpeed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)

        //都道府県データリストのセット（CSVから格納）
        setLocalMascot()

        //メッセージリストのセット
        setMessageList()


    }

    private fun startCm() {
        //スピードの取得
        messageSpeed = inputSpeed.text.toString().toLong()
        startActivity(Intent(this@OpeningActivity, MainActivity::class.java).apply {
            putExtra(IntentKey.PLACE.name, inputPlace)
            putExtra(IntentKey.SPECIALITY.name, inputMascot)
            putExtra(IntentKey.MESSAGE.name, message)
            putExtra(IntentKey.MESSAGE_SPEED.name, messageSpeed)
        })

    }

    private fun setMessageList() {
        val msgList = resources.getStringArray(R.array.message_list)

        adapterMessage = ArrayAdapter(this, android.R.layout.simple_list_item_1, msgList)
        messageList.adapter = adapterMessage
        messageList.setOnItemClickListener { adapterView, view, i, l ->
            message = msgList[i]
            startCm()   //開始
        }

    }

    private fun setLocalMascot() {

        val assetManager = resources.assets
        val inputStream = assetManager.open("data/localMascot.csv")
        val parser = CSVParserBuilder().withSeparator(',').build()
        val reader = CSVReaderBuilder(InputStreamReader(inputStream)).withCSVParser(parser).build()
        importDataFromCSV(reader)
    }

    private fun importDataFromCSV(reader: CSVReader) {
        var tempList: MutableList<Array<String>>? = null

        try {
            tempList = reader.readAll()
            writeCsvDataToModelClass(tempList)
        } catch (e: IOException){
            e.stackTrace
        } finally {
            reader.close()
        }

    }

    private fun writeCsvDataToModelClass(tempList: MutableList<Array<String>>) {
        val iterator = tempList.iterator()

        while (iterator.hasNext()){
            val record = iterator.next()
            val localMascot = LocalMascot(record[0], record[1])
            localMascotList.add(localMascot)
            stateList.add(record[0])
        }

        adapterPlace = ArrayAdapter(this, android.R.layout.simple_list_item_1, stateList)
        placeList.adapter = adapterPlace

        placeList.setOnItemClickListener { parent, view, position, id ->
            inputPlace = localMascotList[position].place
            inputMascot = localMascotList[position].mascot
            textPlace.text = inputPlace
        }

    }


}
