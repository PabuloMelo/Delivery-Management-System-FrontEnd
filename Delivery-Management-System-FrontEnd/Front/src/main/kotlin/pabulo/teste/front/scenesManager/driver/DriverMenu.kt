package pabulo.teste.front.scenesManager.driver

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.driver.DriverController
import pabulo.teste.front.scenesManager.ScenesManager

class DriverMenu(private val driverMenu: Stage) {

    private lateinit var stageInit: Stage
    private lateinit var scenesManager: ScenesManager


    fun driverInit(stageInit: Stage) {
        this.stageInit = stageInit
        scenesManager = ScenesManager(driverMenu)

        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/driver/driver-save.fxml"))
        val driverMenuInit = Scene(fxmlLoader.load(), 350.0, 629.0)
        val cssMenu =
            Main::class.java.getResource("/pabulo/teste/front/seller/seller-style.css")?.toExternalForm()
        driverMenuInit.stylesheets.add(cssMenu)

        val controller = fxmlLoader.getController<DriverController>()
        controller.setDriverMenuApp(this)



        stageInit.scene = driverMenuInit
        stageInit.isResizable = false


    }


    fun returnToMainMenu() {

        val menuInitial = Main()
        menuInitial.start(stageInit)


    }


}