package pabulo.teste.front.scenesManager.address

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.address.AddressUpdateController
import pabulo.teste.front.enumms.AddressMenu
import pabulo.teste.front.scenesManager.ScenesManager

class AddressUpdateScene(private val stageAddressUpdate: Stage) {

    private lateinit var scenesManager: ScenesManager
    private lateinit var stageInit: Stage

    fun showAddressMenu(stageInit: Stage){

        this.stageInit = stageInit
        scenesManager = ScenesManager(stageAddressUpdate)


        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/address/address-update.fxml"))
        val addressUpdate = Scene(fxmlLoader.load(), 1000.0, 630.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/order/order-style.css")?.toExternalForm()
        addressUpdate.stylesheets.add(cssMenu)


        val addresAddController = fxmlLoader.getController<AddressUpdateController>()
        addresAddController.setUpdateAddressAPP(this)


        stageAddressUpdate.isResizable = false
        stageAddressUpdate.scene = addressUpdate

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