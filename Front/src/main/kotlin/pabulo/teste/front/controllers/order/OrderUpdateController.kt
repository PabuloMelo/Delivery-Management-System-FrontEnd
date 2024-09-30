package pabulo.teste.front.controllers.order

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import pabulo.teste.front.dtos.orders.OrderUpdateDTO
import pabulo.teste.front.dtos.orders.OrderUpdateDTOtoDb
import pabulo.teste.front.entity.Order
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.resource.sellerResource.SellerResource
import pabulo.teste.front.scenesManager.order.OrderUpdate

class OrderUpdateController {


    private lateinit var orderUpdateApp: OrderUpdate


    fun setOrderUpdateApp(orderUpdate: OrderUpdate) {

        this.orderUpdateApp = orderUpdate


    }


    //-----------------------Botões do Menu Lateral-------------------------//

    @FXML

    private fun saveOrder() {

        orderUpdateApp.orderMenuOption(OrderChoicesMenu.SaveOrder)

    }


    @FXML

    private fun findOrder() {

        orderUpdateApp.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        orderUpdateApp.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }

    @FXML

    private fun createLoad() {

        orderUpdateApp.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }

    @FXML

    private fun validateLoad(){
        orderUpdateApp.orderMenuOption(OrderChoicesMenu.ValidateLoad)


    }


    @FXML

    private fun saveOrderState() {

        orderUpdateApp.orderMenuOption(OrderChoicesMenu.SaveStateOrder)

    }

    @FXML

    private fun findOrderState() {

        orderUpdateApp.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }

    @FXML

    private fun updateOrderState() {

        orderUpdateApp.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)


    }

    @FXML

    private fun returnToMain() {

        orderUpdateApp.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }


    //-----------------------Botões e Campos de Preenchimento----------------------------------------------------//


    @FXML

    private lateinit var customerRegistered: ChoiceBox<String>


    @FXML

    private lateinit var orderCodeToFind: TextField

    @FXML
    private lateinit var orderFindBtn: Button


    @FXML

    private lateinit var orderCodeField: TextField

    @FXML

    private lateinit var customerCodeField: TextField

    @FXML

    private lateinit var customerNameField: TextField

    @FXML

    private lateinit var loadCodeField: TextField

    @FXML

    private lateinit var orderType: ChoiceBox<String>

    @FXML

    private lateinit var orderStatus: ChoiceBox<String>


    @FXML

    private lateinit var purchaseDatePicker: DatePicker

    @FXML
    private lateinit var invoiceDatePicker: DatePicker


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

    private lateinit var purchaseDateColumn: TableColumn<Order, String>

    @FXML

    private lateinit var invoiceDateColumn: TableColumn<Order, String>


    @FXML

    private lateinit var sellerRcaColumn: TableColumn<Order, Int>

    @FXML
    private lateinit var customerFindBtn: Button


    private val orderResource = OrderResource()


    private val orderList: ObservableList<Order> = FXCollections.observableArrayList()


    private fun defineRca(orderCode: Int): Int {

        val orderToString = orderCode.toString()
        val firstTwoNumbers = orderToString.take(2)

        return firstTwoNumbers.toInt()


    }


    private fun findSellerNameByRca(sellerRca: Int): String {

        val sellerToFind = SellerResource()

        val seller = sellerToFind.findSellerInLocalDbByRca(sellerRca)

        val sellerName = seller?.sellerName ?: "Vendedor Não Cadastrado"

        return sellerName

    }


    @FXML
    private fun findOrderToUpdateByCode() {

        val orderCode: Int = orderCodeToFind.text.trim().toInt()

        clearOrderView()

        val order = orderResource.findOrderByCode(orderCode)
        order?.let { orderList.add(it) }

        orderTableView.refresh()
    }

    @FXML
    private fun findCustomerNameByCode(customerCode: Int): String {

        val customerResource = CustomerResource()
        val customer = customerResource.findCustomerByCodeInLocalDb(customerCode)


        val customerFinded: String = if (customer != null) {


            customerNameField.text = customer.customerName

            customer.customerName


        } else {

            customerNameField.text = "Cliente Não Encontrado"

            "Cliente Não encontardo"

        }


        return customerFinded

    }


    @FXML

    private fun orderToUpdate() {

        val originalOrdercode: Int = orderCodeToFind.text.trim().toInt()
        val updateOrderDto = OrderUpdateDTOtoDb()
        val orderCodeToRca =
            if (orderCodeField.text.isNotBlank()) orderCodeField.text.trim().toInt() else originalOrdercode
        val orderRca = defineRca(orderCodeToRca.toString().toInt())

        val orderUpdateDTO = OrderUpdateDTO(

            orderCode = orderCodeField.text.takeIf { it.isNotBlank() }?.trim()?.toInt()?.let { SimpleIntegerProperty(it) },
            customerCode = customerCodeField.text.takeIf { it.isNotBlank() }?.trim()?.toInt()
                ?.let { SimpleIntegerProperty(it) },
            customerName = customerNameField.text.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            loadNumber = loadCodeField.text.takeIf { it.isNotBlank() }?.trim()?.toInt()?.let { SimpleIntegerProperty(it) },
            orderType = orderType.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            status = orderStatus.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            invoicingDate = invoiceDatePicker.value?.toString()?.let { SimpleStringProperty(it) },
            purchaseDate = purchaseDatePicker.value?.toString()?.let { SimpleStringProperty(it) },
            sellerName = findSellerNameByRca(orderRca),
            sellerRCA = SimpleIntegerProperty(orderRca)

        )
        updateOrderDto.convertDTOtoDB(orderUpdateDTO)

        val orderUpdate = orderResource.updateOrder(originalOrdercode, updateOrderDto)

        if (orderUpdate != null) {




            val index = orderList.indexOfFirst { it.orderCode == originalOrdercode }

            if (index > 0) {

                orderList[index] = orderUpdate

                orderTableView.refresh()

            }

            clearOrderView()

            orderList.add(orderUpdate)

            orderTableView.refresh()

        }


    }

    private fun clearOrderView() {

        orderList.clear()


    }

    private fun clearFields() {


        orderCodeToFind.clear()
        orderCodeField.clear()
        customerCodeField.clear()
        customerNameField.clear()
        loadCodeField.clear()
        orderStatus.value = " "
        orderType.value = " "


    }


    @FXML


    fun initialize() {

        customerRegistered.items = FXCollections.observableArrayList(" SIM", "NAO", "Default")

        customerRegistered.value = "Default"

        orderStatus.items = FXCollections.observableArrayList(
            "Default",
            "Faturado",
            "Aguardando Solicitacao",
            "Pendente",
            "Entregue",
            "Cancelada",
            "Agendada"
        )

        orderStatus.value = "Default"

        orderType.items = FXCollections.observableArrayList(
            "Entrega",
            "Retira Posterior",
            "Carteira",
            "Carteira Entrega",
            "Carteita Retira Posterior",
            "Entrega Futura",
            "Retira Imediata"
        )

        orderType.value = "Entrega"



        customerFindBtn.text

        customerFindBtn.setOnAction { findCustomerNameByCode(customerCodeField.text.toInt()) }

        orderCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderCode).asObject() }
        customerCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.customerCode).asObject() }
        customerNameColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.customerName) }
        loadCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.loadCode).asObject() }
        orderStatusColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderStatus) }
        orderTypeColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderType) }
        sellerRcaColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderSellerRca).asObject() }
        invoiceDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.invoiceDate) }
        purchaseDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.purchaseDate) }


        orderTableView.items = orderList

    }


}