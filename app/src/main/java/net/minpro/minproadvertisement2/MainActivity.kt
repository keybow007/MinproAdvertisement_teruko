package net.minpro.minproadvertisement2

import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {


    private var strMessage: String = ""
    private lateinit var charArray: CharArray
    private var charLength = 0
    private var strDisplay: String = ""
    private var cnt = 0

    private var timer: Timer? = null

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)

        strMessage = getString(R.string.message)
        charArray = strMessage.toCharArray()
        charLength = charArray.size
        strDisplay = ""

        buttonStart.setOnClickListener {
            displayMessage()
            makeSpeech(strMessage)
        }

        buttonStop.setOnClickListener { stop() }
    }

    private fun stop() {
        timer?.cancel()
        cnt = 0
        strDisplay = ""
        textView.text = ""
        tts.stop()
    }

    private fun initScreen() {
        cnt = 0
        strDisplay = ""
        textView.text = ""

    }

    //TTS
    override fun onInit(status: Int) {
        if (status != TextToSpeech.ERROR){
            tts.setLanguage(Locale.JAPANESE)
            tts.setSpeechRate(1.0f)
        }
    }


    private fun displayMessage() {
        initScreen()
        timer = Timer()
        timer?.schedule(1000, 147, {
            runOnUiThread {
                if (cnt >= charLength) {
                    timer?.cancel()
                    return@runOnUiThread
                }
                strDisplay = strDisplay + charArray[cnt]
                cnt++
                textView.text = strDisplay
            }
        })
    }

    private fun makeSpeech(message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "adv_message")
            return
        }
        val map = HashMap<String, String>()
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "adv_message")
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, map)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        tts.shutdown()
    }
}
