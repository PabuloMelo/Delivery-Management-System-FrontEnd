package pabulo.teste.front.scenesManager

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.TestController

class TestMenu(private val stageTest: Stage) {

    private lateinit var sceneManeger: ScenesManager
    private lateinit var stageInit: Stage


    fun showTestMenu(stageInit: Stage) {

        this.stageInit = stageInit
        sceneManeger = ScenesManager(stageTest)

        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/menu/report-connection-DB.fxml"))
        val testMainScene = Scene(fxmlLoader.load(), 1100.0, 800.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/customer/customer-styles.css")?.toExternalForm()
        testMainScene.stylesheets.add(cssMenu)


        val testController = fxmlLoader.getController<TestController>()
        testController.setTestSave(this)

        stageTest.scene = testMainScene

    }
}