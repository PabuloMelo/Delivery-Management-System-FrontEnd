package pabulo.teste.front.scenesManager.customer

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.enumms.CustomerMenuEnumms
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.customer.CustomerControl
import pabulo.teste.front.scenesManager.ScenesManager


class CustomerMenu(private val stageCustomer: Stage) {

    private lateinit var sceneManeger: ScenesManager
    private lateinit var stageInit: Stage


    fun showCustomerMenu(stageInit: Stage) {

        this.stageInit = stageInit
        sceneManeger = ScenesManager(stageCustomer)

        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/customer/customer-init.fxml"))
        val customerMainScene = Scene(fxmlLoader.load(), 1100.0, 800.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/customer/customer-styles.css")?.toExternalForm()
        customerMainScene.stylesheets.add(cssMenu)


        val customerController = fxmlLoader.getController<CustomerControl>()
        customerController.setMainCustomerApp(this)





        stageCustomer.scene = customerMainScene

    }


    //Menu de opções do Cliente

    fun customerChoise(choiseCustomer: CustomerMenuEnumms) {


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