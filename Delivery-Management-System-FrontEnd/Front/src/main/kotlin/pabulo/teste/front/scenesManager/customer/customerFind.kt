package pabulo.teste.front.scenesManager.customer

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.enumms.CustomerMenuEnumms
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.customer.CustomerFindControl
import pabulo.teste.front.scenesManager.ScenesManager

class CustomerFind(private val findStage: Stage) {

    private lateinit var stageInit: Stage
    private lateinit var scenesManager: ScenesManager

    fun findInit(stageInit: Stage) {

        this.stageInit = stageInit
        scenesManager = ScenesManager(findStage)


        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/customer/customer-find.fxml"))
        val customerFindScene = Scene(fxmlLoader.load(), 1100.0, 800.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/customer/customer-styles.css")?.toExternalForm()
        customerFindScene.stylesheets.add(cssMenu)

        val controller = fxmlLoader.getController<CustomerFindControl>()
        controller.setCustomerFindApp(this)

        findStage.scene = customerFindScene


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


        }

    }

}