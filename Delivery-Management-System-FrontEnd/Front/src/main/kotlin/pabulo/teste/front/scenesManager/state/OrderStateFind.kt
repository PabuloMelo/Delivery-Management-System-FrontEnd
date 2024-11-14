package pabulo.teste.front.scenesManager.state

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.state.OrderStateFindController
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.scenesManager.ScenesManager
import pabulo.teste.front.scenesManager.load.LoadSave
import pabulo.teste.front.scenesManager.load.ValidateLoad
import pabulo.teste.front.scenesManager.order.OrderFind
import pabulo.teste.front.scenesManager.order.OrderSave
import pabulo.teste.front.scenesManager.order.OrderUpdate

class OrderStateFind(private val orderStateFind: Stage) {

    private lateinit var stageInit: Stage
    private lateinit var scenesManager: ScenesManager


    fun orderStateFindInit(stateInit: Stage) {

        this.stageInit = stateInit
        scenesManager = ScenesManager(orderStateFind)


        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/state/order-state-find.fxml"))
        val stateFind = Scene(fxmlLoader.load(), 1300.0, 850.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/order/order-style.css")?.toExternalForm()
        stateFind.stylesheets.add(cssMenu)

        val controller = fxmlLoader.getController<OrderStateFindController>()
        controller.setOrderStateFind(this)

        orderStateFind.scene = stateFind

        orderStateFind.isResizable = false


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