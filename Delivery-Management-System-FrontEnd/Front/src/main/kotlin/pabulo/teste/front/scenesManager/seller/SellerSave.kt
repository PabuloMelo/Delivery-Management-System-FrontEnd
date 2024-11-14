package pabulo.teste.front.scenesManager.seller

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.seller.SellerSaveController
import pabulo.teste.front.enumms.SellerMenuChoices
import pabulo.teste.front.scenesManager.ScenesManager

class SellerSave(private val saveSeller: Stage) {

    private lateinit var stageInit: Stage
    private lateinit var scenesManager: ScenesManager


    fun initSaveSeller(stageInit: Stage) {

        this.stageInit = stageInit
        scenesManager = ScenesManager(stageInit)


        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/seller/save-seller.fxml"))
        val sellerSaveScene = Scene(fxmlLoader.load(), 1100.0, 800.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/seller/seller-style.css")?.toExternalForm()
        sellerSaveScene.stylesheets.add(cssMenu)

        val controller = fxmlLoader.getController<SellerSaveController>()
        controller.setSellerSaveApp(this)

        saveSeller.scene = sellerSaveScene

        saveSeller.isResizable = false


    }


    fun sellerMenuchoises(sellerMenuChoices: SellerMenuChoices) {

        when (sellerMenuChoices) {

            SellerMenuChoices.SalvarNovoVendedor -> {

                val saveSeller = SellerSave(stageInit)
                saveSeller.initSaveSeller(stageInit)

            }


            SellerMenuChoices.EncontrarVendedor -> {

                val sellerFind = SellerFind(stageInit)
                sellerFind.sellerFindInit(stageInit)


            }

            SellerMenuChoices.ReturnToMain -> {

                val returnToMain = Main()
                returnToMain.start(stageInit)

            }

        }


    }
}