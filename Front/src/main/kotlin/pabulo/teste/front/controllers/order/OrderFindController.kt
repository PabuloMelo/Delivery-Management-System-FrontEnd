package pabulo.teste.front.controllers.order

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.ImageView
import pabulo.teste.front.entity.Order
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.scenesManager.order.OrderFind

class OrderFindController {

    private lateinit var orderFindApp: OrderFind

    fun setOrderfindApp(orderFindApp: OrderFind) {

        this.orderFindApp = orderFindApp

    }

//-----------------------Botões do Menu Lateral-------------------------//

    @FXML

    private fun saveOrder() {

        orderFindApp.orderMenuOption(OrderChoicesMenu.SaveOrder)

    }


    @FXML

    private fun findOrder() {

        orderFindApp.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        orderFindApp.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }

    @FXML

    private fun createLoad() {

        orderFindApp.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }

    @FXML

    private fun validateLoad(){
        orderFindApp.orderMenuOption(OrderChoicesMenu.ValidateLoad)


    }

    @FXML

    private fun saveOrderState() {

        orderFindApp.orderMenuOption(OrderChoicesMenu.SaveStateOrder)

    }

    @FXML

    private fun findOrderState() {

        orderFindApp.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }

    @FXML

    private fun updateOrderState() {

        orderFindApp.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)


    }

    @FXML

    private fun returnToMain() {

        orderFindApp.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }


    //-------------------------------------------------------------------------//

    @FXML

    private lateinit var orderCodeField: TextField

    @FXML

    private lateinit var customerCodeField: TextField

    @FXML

    private lateinit var customerNameField: TextField

    @FXML

    private lateinit var loadCodeField: TextField


    @FXML

    private lateinit var purchaseDatePicker: DatePicker

    @FXML

    private lateinit var purchaseDatePickerEnd: DatePicker


    @FXML
    private lateinit var invoiceDatePicker: DatePicker

    @FXML
    private lateinit var invoiceDatePickerEnd: DatePicker

    /*-------------------------------------------------TableView----------------------------------------------------------*/

    @FXML
    private lateinit var orderTableView: TableView<Order>

    @FXML
    private lateinit var orderCodeColumn: TableColumn<Order, Int>

    @FXML
    private lateinit var customerCodeColumn: TableColumn<Order, Int>

    @FXML
    private lateinit var customerNameColumn: TableColumn<Order, String>


    @FXML

    private lateinit var loadCodeColumn: TableColumn<Order, Int>

    @FXML

    private lateinit var orderStatusColumn: TableColumn<Order, String>

    @FXML

    private lateinit var orderTypeColumn: TableColumn<Order, String>


    @FXML

    private lateinit var puchaseDateColumn: TableColumn<Order, String>

    @FXML

    private lateinit var invoiceDateColumn: TableColumn<Order, String>


    @FXML

    private lateinit var sellerRcaColumn: TableColumn<Order, Int>


    private val orderResource = OrderResource()

    private val driversResource = LoadResource()


    private val orderList: ObservableList<Order> = FXCollections.observableArrayList()


    @FXML
    private lateinit var findByUserParemeters: Button


    @FXML
    private lateinit var driverImageView: ImageView


    @FXML

    private lateinit var driverNameView: TextField

    //--------------------------------------------Botões de Preenchimento---------------------------------------------//

    @FXML

    private lateinit var ordertype: ChoiceBox<String>


    @FXML

    private fun findOrderByCode() {

        val orderCode: Int = orderCodeField.text.trim().toInt()

        clearList()

        val orderResource = OrderResource()

        val order = orderResource.findOrderByCode(orderCode)

        order?.let { orderList.add(it) }

        clearFields()

    }

    @FXML

    private fun findOrderByUserParameters() {

        val customerCode: Int? = customerCodeField.text?.takeIf { it.isNotBlank() }?.trim()?.toIntOrNull()
        val customerName: String? = customerNameField.text?.takeIf { it.isNotBlank() }?.trim()
        val loadCode: Int? = loadCodeField.text?.takeIf { it.isNotBlank() }?.trim()?.toIntOrNull()
        val orderType: String? = ordertype.value?.takeIf { it.isNotBlank() }
        val purchaseDateInit: String? = purchaseDatePicker.value?.toString()
        val purchaseDateEnd: String? = purchaseDatePickerEnd.value?.toString()
        val invoiceDateInit: String? = invoiceDatePicker.value?.toString()
        val invoiceDateEnd: String? = invoiceDatePickerEnd.value?.toString()



        val orders: List<Order> = orderResource.findOrderByUserParameters(
            customerCode,
            customerName,
            loadCode,
            orderType,
            purchaseDateInit,
            purchaseDateEnd,
            invoiceDateInit,
            invoiceDateEnd
        )
        clearList()

        orders.forEach { orderList.add(it) }

        clearFields()


    }


    private fun clearList() {
        orderList.clear()

    }

    private fun clearFields() {

        orderCodeField.clear()
        customerCodeField.clear()
        customerNameField.clear()
        loadCodeField.clear()


    }


    @FXML

    private fun initialize() {


        orderCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderCode).asObject() }
        customerCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.customerCode).asObject() }
        customerNameColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.customerName) }
        loadCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.loadCode).asObject() }
        orderStatusColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderStatus) }
        orderTypeColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderType) }
        sellerRcaColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderSellerRca).asObject() }
        invoiceDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.invoiceDate) }
        puchaseDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.purchaseDate) }


        ordertype.items = FXCollections.observableArrayList(
            "Entrega",
            "Retira Posterior",
            "Carteira",
            "Carteira Entrega",
            "Carteita Retira Posterior",
            "Entrega Futura",
            "Retira Imediata",
            "Default"
        )
        ordertype.value = " "


        findByUserParemeters.text

        findByUserParemeters.setOnAction { findOrderByUserParameters() }

        orderTableView.items = orderList

    }


}