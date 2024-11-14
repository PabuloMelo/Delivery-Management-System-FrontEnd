package pabulo.teste.front.controllers.customer


import javafx.fxml.FXML
import javafx.scene.control.TextField
import pabulo.teste.front.enumms.CustomerMenuEnumms
import pabulo.teste.front.scenesManager.customer.CustomerMenu


class CustomerControl {

    private lateinit var customerApp: CustomerMenu


    fun setMainCustomerApp(customerApp: CustomerMenu) {

        this.customerApp = customerApp

    }


    /* -----------------Menu de opções do Cliente-------------------------------------------------- */

    @FXML

    private fun saveCustomer() {

        customerApp.customerChoise(CustomerMenuEnumms.Save)


    }

    @FXML
    private fun findCustomer() {

        customerApp.customerChoise(CustomerMenuEnumms.Find)

    }

    @FXML
    private fun customerUpdate() {

        customerApp.customerChoise(CustomerMenuEnumms.Update)

    }

    @FXML

    private fun retunrMenuInit(){

        customerApp.customerChoise(CustomerMenuEnumms.ReturnToMenuInit)

    }



/*------------------------------------------------------------------------------------------------------------------*/


@FXML

private lateinit var customerCode: TextField




}