package pabulo.teste.front.scenesManager.order

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.order.OrderMainController
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.scenesManager.ScenesManager
import pabulo.teste.front.scenesManager.load.LoadSave
import pabulo.teste.front.scenesManager.load.ValidateLoad
import pabulo.teste.front.scenesManager.state.OrderStateFind
import pabulo.teste.front.scenesManager.state.OrderStateSave
import pabulo.teste.front.scenesManager.state.OrderStateUpdate

class OrderMenu(private val orderMenu: Stage) {

    private lateinit var stageInit: Stage
    private lateinit var scenesManager: ScenesManager

    fun orderMenuInit(stageInit: Stage) {
        this.stageInit = stageInit
        scenesManager = ScenesManager(orderMenu)


        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/order/order-init.fxml"))
        val orderMenuScene = Scene(fxmlLoader.load(), 1100.0, 800.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/order/order-style.css")?.toExternalForm()
        orderMenuScene.stylesheets.add(cssMenu)

        val controller = fxmlLoader.getController<OrderMainController>()
        controller.setOrderMenuApp(this)

        orderMenu.scene = orderMenuScene


    }


    fun orderMenuOption(orderChoicesMenu: OrderChoicesMenu) {

        when (orderChoicesMenu) {


            OrderChoicesMenu.SaveOrder -> {

                val orderSave = OrderSave(stageInit)
                orderSave.saveOrderInit(stageInit)

            }

            OrderChoicesMenu.FindOrder -> {

                val orderFind = OrderFind(stageInit)
                orderFind.orderFindInit(stageInit)

            }


            OrderChoicesMenu.UpdateOrder -> {

                val orderUpdate = OrderUpdate(stageInit)
                orderUpdate.saveUpdateOrderInit(stageInit)


            }

            OrderChoicesMenu.CreateALoad -> {

                val saveLoad = LoadSave(stageInit)
                saveLoad.laodSaveInit(stageInit)

            }


            OrderChoicesMenu.ValidateLoad -> {

                val validateLoad = ValidateLoad(stageInit)
                validateLoad.validateLoadInit(stageInit)



            }

            OrderChoicesMenu.SaveStateOrder -> {

                val stateSave = OrderStateSave(stageInit)
                stateSave.stateInit(stageInit)

            }

            OrderChoicesMenu.FindStateOrder -> {

                val stateFind = OrderStateFind(stageInit)
                stateFind.orderStateFindInit(stageInit)


            }

            OrderChoicesMenu.UpdateStateOrder -> {

                val stateUpdate = OrderStateUpdate(stageInit)
                stateUpdate.stateOrderUpdateinit(stageInit)

            }

            OrderChoicesMenu.ReturnToMenuInit -> {

                val returnToMain = Main()
                returnToMain.start(stageInit)

            }
        }


    }


}