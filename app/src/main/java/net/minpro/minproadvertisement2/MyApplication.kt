package net.minpro.minproadvertisement2

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by keybowNew on 2018/03/08.
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //カスタムフォント
        //拡張子（ttf/otf)
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/font.otf")
                .setFontAttrId(R.attr.fontPath)
                .build())

    }
}