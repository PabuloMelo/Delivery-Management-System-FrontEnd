package pabulo.teste.front.controllers.seller

import javafx.fxml.FXML
import pabulo.teste.front.enumms.SellerMenuChoices
import pabulo.teste.front.scenesManager.seller.SellerMain

class SellerMainController {


    private lateinit var sellerMainApp: SellerMain

    fun setSellerMainApp(sellerMain: SellerMain) {

        this.sellerMainApp = sellerMain

    }


    @FXML

    private fun saveSeller() {

        sellerMainApp.sellerMenuchoises(SellerMenuChoices.SalvarNovoVendedor)

    }


    @FXML

    private fun findSeller() {

        sellerMainApp.sellerMenuchoises(SellerMenuChoices.EncontrarVendedor)

    }

    @FXML
    private fun returnToInit() {

        sellerMainApp.sellerMenuchoises(SellerMenuChoices.ReturnToMain)

    }


}