package pabulo.teste.front.scenesManager

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main

class ScenesManager(private val stage: Stage) {

    fun changeScene(fxmlPath: String, cssPath: String? = null, width: Double = 1100.0, height: Double = 800.0) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource(fxmlPath))
        val scene = Scene(fxmlLoader.load(), width, height)

        if (cssPath != null) {
            val css = Main::class.java.getResource(cssPath)?.toExternalForm()
            scene.stylesheets.add(css)
        }

        stage.scene = scene
    }





}