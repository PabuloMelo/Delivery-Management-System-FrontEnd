package pabulo.teste.front.controllers

import javafx.fxml.FXML
import javafx.scene.control.TextField
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.*
import pabulo.teste.front.scenesManager.TestMenu

class TestController {

    private lateinit var testApp: TestMenu

    fun setTestSave(testMenu: TestMenu) {

        this.testApp = testMenu

    }


    @FXML

    private lateinit var pedidoField: TextField



    @FXML

    private lateinit var loadField: TextField

    @FXML

    private lateinit var customer: TextField

    @FXML

    private lateinit var state: TextField

    @FXML

    private lateinit var sellerField: TextField

    @FXML

    private lateinit var orderFindField: TextField


    private val connection = ConnectionBackend()

    private val customerConnection = CustomerConnection()

    private val loadConnection = LoadConnection(GsonProvider.gson)

    private val orderConnection = OrderConnection(GsonProvider.gson)

    private val stateConnection = StateConnection(GsonProvider.gson)

    private val sellerConnection = SellerConnection(GsonProvider.gson)

    @FXML
    fun orderTest() {


       val orderSave: Int = pedidoField.text.trim().toInt()

        //connection.saveCustomerOnWebDb(customerSave)

        orderConnection.saveOrderOnDBWeb(orderSave)


    }

    @FXML

    fun loadTest(){


        val customerFind: Int = loadField.text.trim().toInt()

       // connection.fetchCustomerOnWebDbByCode(customerFind)

        loadConnection.saveLoadOnWebDb(customerFind)



    }

    @FXML

    fun customerTest(){

        val customerSave: Int = customer.text.trim().toInt()

        customerConnection.saveCustomerOnWebDb(customerSave)


    }

    @FXML

    fun saveStateTest(){

        val stateSave: Int = state.text.trim().toInt()

        stateConnection.saveStateOnWebDb(stateSave)


    }

    @FXML

    fun saveSellerTest(){

        val sellerCode: Int = sellerField.text.trim().toInt()


        sellerConnection.saveSellerOnWebDb(sellerCode)

    }

    @FXML

    fun testOrderFind(){

        val orderCode: Long = orderFindField.text.toLong()

        orderConnection.fetchOrderByCode(orderCode)
    }

    @FXML

    fun updateOrdersData(){

        orderConnection.updateAllOrders()


    }


}
