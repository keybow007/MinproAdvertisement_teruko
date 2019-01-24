package net.minpro.minproadvertisement2

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_opening.*
import net.minpro.minproadvertisement2.R.id.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule



class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    data class MsgComponent (val msgDisplay: String, val msgDisplayCharArray: CharArray , val msgSpeech: String)

    //passed from opening
    //private var strDisplayFormer: String = ""
    //private var strDisplayLatter: String = ""
    private var strDisplay: String = ""
    private var strSpeech: String = ""
    private var cnt = 0

    private var timer: Timer? = null

    private lateinit var tts: TextToSpeech

    private lateinit var animation: AnimationDrawable

    private lateinit var msg: MsgComponent
    //private lateinit var msgFormar: MsgComponent
    //private lateinit var msgLatter: MsgComponent

    private var isInFormer = true
    private var isEscape = false

    private lateinit var musicPlayer: MediaPlayer

    var messageSpeed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //背景画像をランダムに設定
        setRandomBackground()

        //BGMをランダムに設定
        setRandomBgm()

        //TTS
        tts = TextToSpeech(this, this)

        imageEnding.apply {
            visibility = View.INVISIBLE
            setOnClickListener {
                if (musicPlayer.isPlaying) musicPlayer.stop()
                finish()
            }
        }
        setMessage()

        imageTeruko.setOnClickListener { setRandomBackground() }

        button.setOnClickListener {
            isInFormer = true
            button.isEnabled = false
            makeSpeech(strSpeech)
        }


        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener(){
            override fun onStart(p0: String?) {
                //should use runOnUiThread in changing UI
                runOnUiThread {
                    displayMessage(msg)
                    startAnimation()
                    musicPlayer.start()

                }
            }

            override fun onDone(p0: String?) {
                runOnUiThread {
                    stopAnimation()
                    imageEnding.visibility = View.VISIBLE
                    imageEnding.setBackgroundResource(R.drawable.image_ending)
                    //if (musicPlayer.isPlaying) musicPlayer.stop()
                }

            }

            override fun onError(p0: String?) {
            }
        })



    }




    private fun setRandomBgm() {
        val bgmArray = arrayOf(
                R.raw.music01,
                R.raw.music02,
                R.raw.music03,
                R.raw.music04,
                R.raw.music05,
                R.raw.music06,
                R.raw.music07,
                R.raw.music08,
                R.raw.music09,
                R.raw.music10,
                R.raw.music11,
                R.raw.music12,
                R.raw.music13,
                R.raw.music14,
                R.raw.music15,
                R.raw.music16,
                R.raw.music17,
                R.raw.music18,
                R.raw.music19,
                R.raw.music20
        )
        val random = Random()
        val bgmRes = bgmArray[random.nextInt(20)]
        musicPlayer = MediaPlayer.create(this, bgmRes)
        musicPlayer.setVolume(0.1f, 0.1f)

    }

    private fun setRandomBackground() {

        //https://stackoverflow.com/questions/6945678/storing-r-drawable-ids-in-xml-array
        //https://developer.android.com/reference/android/content/res/TypedArray
        val backGroundArray = resources.obtainTypedArray(R.array.background_list)
        val random = Random()
        val backGroundRes = backGroundArray.getResourceId(random.nextInt(100), -1)
        constraintLayout.setBackgroundResource(backGroundRes)
        backGroundArray.recycle()
    }

    private fun setMessage() {

        val place = intent.extras.getString(IntentKey.PLACE.name)
        val speciality = intent.extras.getString(IntentKey.SPECIALITY.name)
        val msgOpening = "「" + speciality + "」" + getString(R.string.after_speciality) +
                place + getString(R.string.after_place)
        //高槻
        val msgBody = intent.extras.getString(IntentKey.MESSAGE.name)

        msg = MsgComponent(msgOpening + msgBody,
                (msgOpening + msgBody).toCharArray(),
                msgOpening + msgBody)

        strDisplay = ""
        strSpeech = msg.msgSpeech
    }



    private fun stopAnimation() {
        animation.stop()
        button.isEnabled = true
        imageTeruko.setBackgroundResource(R.drawable.teruko04)

    }

    private fun startAnimation() {
        imageTeruko.setBackgroundResource(R.drawable.animation)
        animation = imageTeruko.background as AnimationDrawable
        animation.start()
    }





    private fun initScreen() {
        cnt = 0
//        strDisplayFormer = ""
//        strDisplayLatter = ""
        strDisplay = ""
        textMessage.text = ""

    }

    //TTS
    override fun onInit(status: Int) {
        if (status != TextToSpeech.ERROR){
            tts.setLanguage(Locale.JAPANESE)
            tts.setSpeechRate(1.0f)
        }
    }


    private fun displayMessage(message: MsgComponent) {
        initScreen()
        timer = Timer()
        messageSpeed = intent.extras.getLong(IntentKey.MESSAGE_SPEED.name)

        timer?.schedule(100, messageSpeed, {
            runOnUiThread {
                if (cnt >= message.msgDisplayCharArray.size) {
                    isEscape = !isInFormer
//                    if (!isEscape) {
//                        isInFormer = false
//                        timer?.cancel()
//                        //displayMessage(msgLatter)
//                        displayMessage(msg)
//                        return@runOnUiThread
//                    }
                    timer?.cancel()
                    return@runOnUiThread
                }
                strDisplay = strDisplay + message.msgDisplayCharArray[cnt]
                //strDisplayFormer = strDisplayFormer + message.msgDisplayCharArray[cnt]
                cnt++
                //textMessage.text = strDisplayFormer
                textMessage.text = strDisplay
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

    override fun onResume() {
        super.onResume()
        tts.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        tts.stop()
        tts.shutdown()
        musicPlayer.release()
    }
}
