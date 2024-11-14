package pabulo.teste.front.scenesManager.address

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.address.AddressMenuController
import pabulo.teste.front.enumms.AddressMenu
import pabulo.teste.front.scenesManager.ScenesManager

class AddressInit(private val stageAddressInit: Stage) {

    private lateinit var scenesManager: ScenesManager
    private lateinit var stageInit: Stage

    fun showAddressMenu(stageInit: Stage) {

        this.stageInit = stageInit
        scenesManager = ScenesManager(stageAddressInit)


        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/address/address-init.fxml"))
        val addressMainScene = Scene(fxmlLoader.load(), 220.0, 450.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/order/order-style.css")?.toExternalForm()
        addressMainScene.stylesheets.add(cssMenu)


        val addresMenController = fxmlLoader.getController<AddressMenuController>()
        addresMenController.setMainAddressAPP(this)

        stageAddressInit.isResizable = false

        stageAddressInit.scene = addressMainScene


    }

    fun menuAddress(choice: AddressMenu) {


        when (choice) {


            AddressMenu.SAVE_ADDRESS -> {

                val saveAddress = AddressAddScene(stageInit)
                saveAddress.showAddressMenu(stageInit)


            }

            AddressMenu.UPDATE_ADDRESS -> {

                val addressUpdate = AddressUpdateScene(stageInit)
                addressUpdate.showAddressMenu(stageInit)

            }

            AddressMenu.RETURN -> {


                val returnToMain = Main()
                returnToMain.start(stageInit)

            }


        }


    }


}