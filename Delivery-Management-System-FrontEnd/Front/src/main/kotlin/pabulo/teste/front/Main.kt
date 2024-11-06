package pabulo.teste.front

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.adapters.AutoInsertOrdersTypeOnDb
import pabulo.teste.front.controllers.MenuController
import pabulo.teste.front.enumms.MenuChoices
import pabulo.teste.front.scenesManager.ScenesManager
import pabulo.teste.front.scenesManager.TestMenu
import pabulo.teste.front.scenesManager.customer.CustomerMenu
import pabulo.teste.front.scenesManager.driver.DriverMenu
import pabulo.teste.front.scenesManager.load.LoadSave
import pabulo.teste.front.scenesManager.order.OrderMenu
import pabulo.teste.front.scenesManager.seller.SellerMain


class Main : Application() {

    private lateinit var stageInit: Stage
    private lateinit var sceneManager: ScenesManager
    private val autoInsertOrdersTypeOnDb = AutoInsertOrdersTypeOnDb()


    override fun start(stageInit: Stage) {

        this.stageInit = stageInit
        sceneManager = ScenesManager(stageInit)


        // val fxmlLoader = FXMLLoader(Main::class.java.getResource("login-view.fxml"))
        //val scene = Scene(fxmlLoader.load(), 800.0, 600.0)
        //val css = Main::class.java.getResource("loginStyle.css").toExternalForm()
        // scene.stylesheets.add(css)
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/menu/Menu-view.fxml"))
        val mainScene = Scene(fxmlLoader.load(), 1200.0, 800.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/menu/menuStyle.css")?.toExternalForm()
        mainScene.stylesheets.add(cssMenu)

        val controller = fxmlLoader.getController<MenuController>()
        controller.setMainApp(this)

        autoInsertOrdersTypeOnDb.checkLoadTypeOnLocalDb()

        stageInit.title = "Delivery Management System"
        stageInit.scene = mainScene
        stageInit.isResizable = true
        stageInit.show()
    }


    fun changeScene(choice: MenuChoices) {


        when (choice) {

            MenuChoices.CustomerMenu -> {


                val customerMenu = CustomerMenu(stageInit)
                customerMenu.showCustomerMenu(stageInit)


            }

            MenuChoices.Home -> {


            }


            MenuChoices.LoadMenu -> {

                val loadManin = LoadSave(stageInit)
                loadManin.laodSaveInit(stageInit)

            }

            MenuChoices.OrderMenu -> {

                val orderMenu = OrderMenu(stageInit)
                orderMenu.orderMenuInit(stageInit)

            }

            MenuChoices.SellerMenu -> {

                val sellerMain = SellerMain(stageInit)
                sellerMain.sellerInit(stageInit)

            }

            MenuChoices.DriverMenu -> {

                val driver = DriverMenu(stageInit)
                driver.driverInit(stageInit)

            }

            MenuChoices.TestArea -> {

                val test = TestMenu(stageInit)
                test.showTestMenu(stageInit)

            }


            else -> {}
        }


    }


}

fun main() {
    Application.launch(Main::class.java)
}





