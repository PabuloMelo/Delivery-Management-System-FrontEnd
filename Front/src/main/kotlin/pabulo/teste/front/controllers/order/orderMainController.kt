package pabulo.teste.front.controllers.order

import javafx.fxml.FXML
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.scenesManager.order.OrderMenu

class OrderMainController {

    private lateinit var orderMenuApp: OrderMenu


    fun setOrderMenuApp(orderMenu: OrderMenu) {

        this.orderMenuApp = orderMenu


    }


    @FXML

    private fun saveOrder() {

        orderMenuApp.orderMenuOption(OrderChoicesMenu.SaveOrder)


    }

    @FXML

    private fun findOrder() {
        orderMenuApp.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        orderMenuApp.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }


    @FXML

    private fun createALoad(){

        orderMenuApp.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }

    @FXML

    private fun validateLoad(){
        orderMenuApp.orderMenuOption(OrderChoicesMenu.ValidateLoad)


    }

    @FXML

    private fun saveOrderState(){

        orderMenuApp.orderMenuOption(OrderChoicesMenu.SaveStateOrder)


    }

    @FXML

    private fun findOrderState(){

        orderMenuApp.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }


    @FXML

    private fun updateOrderState(){

        orderMenuApp.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)

    }

    @FXML

    private fun returnToMainMenu(){

        orderMenuApp.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }

}