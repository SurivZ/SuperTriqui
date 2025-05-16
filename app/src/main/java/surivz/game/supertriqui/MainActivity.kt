package surivz.game.supertriqui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import surivz.game.supertriqui.ui.theme.SuperTriquiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperTriquiTheme {
                SuperTriquiApp()
            }
        }
    }
}
