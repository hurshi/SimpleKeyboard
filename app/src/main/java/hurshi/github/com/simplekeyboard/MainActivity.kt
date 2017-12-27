package hurshi.github.com.simplekeyboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simpleKeyboard.initEdittext(editText)
        simpleKeyboard.initEdittext(editText2)


    }
}
