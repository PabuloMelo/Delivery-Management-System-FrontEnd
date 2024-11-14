package pabulo.teste.front.controllers.address

import javafx.fxml.FXML
import pabulo.teste.front.enumms.AddressMenu
import pabulo.teste.front.scenesManager.address.AddressInit

class AddressMenuController {

    private lateinit var addressMenuApp: AddressInit


    fun setMainAddressAPP(addressMenuApp: AddressInit) {

        this.addressMenuApp = addressMenuApp

    }

    @FXML
    private fun saveAddress() {

        addressMenuApp.menuAddress(AddressMenu.SAVE_ADDRESS)

    }

    @FXML
    private fun updateAddress() {

        addressMenuApp.menuAddress(AddressMenu.UPDATE_ADDRESS)

    }

    @FXML
    private fun returnToInit(){

        addressMenuApp.menuAddress(AddressMenu.RETURN)


    }


}