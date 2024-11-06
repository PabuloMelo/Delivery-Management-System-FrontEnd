package pabulo.teste.front.scenesManager.customer

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.enumms.CustomerMenuEnumms
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.customer.CustomerSaveControl
import pabulo.teste.front.scenesManager.ScenesManager

class CustomerSave(private val saveStage: Stage) {

    private lateinit var stageInit: Stage
    private lateinit var scenesManager: ScenesManager

    fun saveInit(stageInit: Stage) {
        this.stageInit = stageInit
        scenesManager = ScenesManager(saveStage)


        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/customer/customer-save.fxml"))
        val customerSaveScene = Scene(fxmlLoader.load(), 1150.0, 800.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/customer/customer-styles.css")?.toExternalForm()
        customerSaveScene.stylesheets.add(cssMenu)

        val controller = fxmlLoader.getController<CustomerSaveControl>()
        controller.setCustomerSaveApp(this)

        saveStage.scene = customerSaveScene

    }

    fun customerMenuChoise(choiseCustomer: CustomerMenuEnumms) {


        when (choiseCustomer) {


            CustomerMenuEnumms.Save -> {

                val customerSave = CustomerSave(stageInit)
                customerSave.saveInit(stageInit)


            }

            CustomerMenuEnumms.Find -> {

                val customerFind = CustomerFind(stageInit)
                customerFind.findInit(stageInit)

            }

            CustomerMenuEnumms.Update -> {

                val customerUpdate = CustomerUpdate(stageInit)
                customerUpdate.initUpdate(stageInit)


            }

            CustomerMenuEnumms.ReturnToMenuInit -> {

                val menuInit = Main()
                menuInit.start(stageInit)


            }


            else -> {}

        }


    }


}