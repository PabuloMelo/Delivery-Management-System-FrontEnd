package pabulo.teste.front.controllers

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import pabulo.teste.front.connectionBackEnd.ConnectionBackend
import pabulo.teste.front.scenesManager.TestMenu

class TestController {

    private lateinit var testApp: TestMenu

    fun setTestSave(testMenu: TestMenu) {

        this.testApp = testMenu

    }


    @FXML

    private lateinit var testParameterField: TextField

    @FXML

    private lateinit var textLabel: Label


    @FXML

    private lateinit var button: Button

    @FXML

    private lateinit var fieldFind: TextField

    @FXML

    private lateinit var findbutton: Button


    private val connection = ConnectionBackend()

    @FXML
    fun test() {


       val customerSave: Int = testParameterField.text.trim().toInt()

        connection.saveCustomerOnWebDb(customerSave)


    }

    @FXML

    fun findTest(){


        val customerFind: Long = fieldFind.text.trim().toLong()

        connection.fetchCustomerOnWebDbByCode(customerFind)



    }


}
