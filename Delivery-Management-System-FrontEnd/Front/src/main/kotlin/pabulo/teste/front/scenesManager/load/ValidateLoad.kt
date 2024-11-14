package pabulo.teste.front.scenesManager.load

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import pabulo.teste.front.Main
import pabulo.teste.front.controllers.load.LoadValidateController
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.scenesManager.ScenesManager
import pabulo.teste.front.scenesManager.order.OrderFind
import pabulo.teste.front.scenesManager.order.OrderSave
import pabulo.teste.front.scenesManager.order.OrderUpdate
import pabulo.teste.front.scenesManager.state.OrderStateFind
import pabulo.teste.front.scenesManager.state.OrderStateSave
import pabulo.teste.front.scenesManager.state.OrderStateUpdate

class ValidateLoad(private val validateLoad: Stage) {

    private lateinit var stageInit: Stage
    private lateinit var scenesManager: ScenesManager

    fun validateLoadInit(stageInit: Stage) {

        this.stageInit = stageInit
        scenesManager = ScenesManager(validateLoad)


        val fxmlLoader = FXMLLoader(Main::class.java.getResource("/pabulo/teste/front/load/load-validate.fxml"))
        val loadValidateScene = Scene(fxmlLoader.load(), 1000.0, 850.0)
        val cssMenu = Main::class.java.getResource("/pabulo/teste/front/order/order-style.css")?.toExternalForm()
        loadValidateScene.stylesheets.add(cssMenu)


        val controller = fxmlLoader.getController<LoadValidateController>()
        controller.setLoadValidate(this)

        validateLoad.scene = loadValidateScene
        validateLoad.isResizable = false


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