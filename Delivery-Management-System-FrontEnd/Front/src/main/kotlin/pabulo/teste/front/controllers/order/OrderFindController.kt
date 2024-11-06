package pabulo.teste.front.controllers.order

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.CustomerConnection
import pabulo.teste.front.connectionBackEnd.OrderConnection
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.entity.Order
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.scenesManager.order.OrderFind
import java.time.LocalDate

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

    private fun validateLoad() {
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

    private val orderConnection = OrderConnection(GsonProvider.gson)

    private val driversResource = LoadResource()

    private val customerConnection = CustomerConnection()

    private val customerResource = CustomerResource()


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

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var okButonDialog: Button


    fun handleOkButton() {

        dialogPane.isVisible = false

    }


    fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true


    }


    @FXML

    private fun findOrderByCode() {

        clearList()

        if (orderCodeField.text.isNullOrBlank()) {

            showDialog("O campo do codigo do pedido está vazio por favor digite um numero maior do que 0")

            return
        }

        try {


            val orderCode: Int = orderCodeField.text.trim().toInt()

            clearList()


            try {

                val order = orderConnection.fetchOrderByCode(orderCode.toLong())

                order?.let { orderList.add(it) }


            } catch (e: Exception) {

                println(e.message)

            }

            if (orderList.isEmpty()) {

                try {


                    val orderResource = OrderResource()

                    val order = orderResource.findOrderByCode(orderCode)

                    order?.let { orderList.add(it) }

                } catch (e: Exception) {

                    println(e.message)
                }
            }


        } catch (e: NumberFormatException) {

            showDialog("Erro: Valor Invalido para o campo numero do pedido")

        }
    }

    private fun findOrderByUserParametersOnWebDb() {

        clearList()

        //verifica se o valor inserido no campo do orderCode code é valido

        val orderCodeText = orderCodeField.text?.trim()

        var orderCode: Long? = null

        when {
            orderCodeText.isNullOrBlank() -> {
            }

            orderCodeText.all { it.isDigit() } -> {

                orderCode = orderCodeText.toLong()
            }

            else -> {

                showDialog("Por favor digite somente numeros maiores do 0")

                return
            }

        }

        //verifica se o valor inserido no campo do customer code é valido

        val customerCodeText = customerCodeField.text?.trim()
        var customerCode: Long? = null

        when {

            customerCodeText.isNullOrBlank() -> {}

            customerCodeText.all { it.isDigit() } -> {

                customerCode = customerCodeText.toLong()

            }

            else -> {

                showDialog("Por favor digite somente numeros maiores do 0")

                return

            }


        }

        //verifica se o valor inserido no campo do load code é valido

        val loadCodeTXT = loadCodeField.text?.trim()
        var loadCode: Long? = null

        when {

            loadCodeTXT.isNullOrBlank() -> {}

            loadCodeTXT.all { it.isDigit() } -> {

                loadCode = loadCodeTXT.toLong()

            }

            else -> {

                showDialog("Por favor digite somente numeros maiores do 0")

                return

            }

        }

        // verifica a condição das buscas por datas


        when {

            purchaseDatePicker.value != null && purchaseDatePickerEnd.value != null && purchaseDatePicker.value > purchaseDatePickerEnd.value -> {


                showDialog("a data de venda inicial não pode ser menor do que a data de venda final")

                return

            }

            invoiceDatePicker.value != null && invoiceDatePickerEnd.value != null && invoiceDatePicker.value > invoiceDatePickerEnd.value -> {

                showDialog("a data de faturamento inicial não pode ser menor do que a data de faturamento final")

                return

            }

            invoiceDatePicker.value != null && purchaseDatePicker.value != null && invoiceDatePicker.value < purchaseDatePicker.value -> {

                showDialog("A data de faturamento inicial não pode ser menor do que a data de venda inicial")

                return

            }

            invoiceDatePickerEnd.value != null && purchaseDatePicker.value != null && invoiceDatePickerEnd.value < purchaseDatePicker.value -> {

                showDialog("A data de faturamento final não pode ser menor do que a data de venda inicial")

                return

            }


        }


        val purchaseDateInit: LocalDate? = purchaseDatePicker.value
        val purchaseDateEnd: LocalDate? = purchaseDatePickerEnd.value
        val invoiceDateInit: LocalDate? = invoiceDatePicker.value
        val invoiceDateEnd: LocalDate? = invoiceDatePickerEnd.value
        val customerName: String? = customerNameField.text?.takeIf { it.isNotBlank() }?.trim()
        val orderType: String? = ordertype.value?.takeIf { it.isNotBlank() }

        if (!orderCodeField.text.isNullOrBlank() || !customerCodeField.text.isNullOrBlank() || !loadCodeField.text.isNullOrBlank() || !customerNameField.text.isNullOrBlank() ||
            !ordertype.value.isNullOrBlank() || purchaseDatePicker.value != null || purchaseDatePickerEnd.value != null || invoiceDatePicker.value != null ||
            invoiceDatePickerEnd.value != null

        ) {

            try {


                val orders: List<Order>? = orderConnection.fetchOrdersByUserParameters(
                    orderCode,
                    customerCode,
                    customerName,
                    loadCode,
                    orderType,
                    purchaseDateInit,
                    purchaseDateEnd,
                    invoiceDateInit,
                    invoiceDateEnd
                )




                orders?.let { orderList.addAll(it) }


            } catch (e: Exception) {

                showDialog("Erro ao bucar  pedidos ${e.message}")
            }

        } else {

            showDialog("Nenhum parametro foi fornecido para busca do pedido")

        }
        clearFields()

    }


    private fun findOrderByUserParametersOnLocalDb() {

        //verifica se o valor inserido no campo do customer code é valido

        val customerCodeText = customerCodeField.text?.trim()
        var customerCode: Int? = null

        when {

            customerCodeText.isNullOrBlank() -> {}

            customerCodeText.all { it.isDigit() } -> {

                customerCode = customerCodeText.toInt()

            }


            else -> {

                showDialog("Por favor digite somente numeros maiores do 0")

                return

            }
        }



            //verifica se o valor inserido no campo do load code é valido

                val loadCodeTXT = loadCodeField.text?.trim()
                var loadCode
                : Int
                ?
                = null

            when {

                loadCodeTXT.isNullOrBlank() -> {}

                loadCodeTXT.all { it.isDigit() } -> {

                    loadCode = loadCodeTXT.toInt()

                }

                else -> {

                    showDialog("Por favor digite somente numeros maiores do 0")

                    return

                }

            }

                // verifica a condição das buscas por datas


                when {

                purchaseDatePicker.value != null && purchaseDatePickerEnd.value != null && purchaseDatePicker.value > purchaseDatePickerEnd.value -> {


                    showDialog("a data de venda inicial não pode ser maior do que a data de venda final")

                    return

                }

                invoiceDatePicker.value != null && invoiceDatePickerEnd.value != null && invoiceDatePicker.value > invoiceDatePickerEnd.value -> {

                    showDialog("a data de faturamento inicial não pode ser maior do que a data de faturamento final")

                    return

                }

                invoiceDatePicker.value != null && purchaseDatePicker.value != null && invoiceDatePicker.value < purchaseDatePicker.value -> {

                    showDialog("A data de faturamento inicial não pode ser menor do que a data de venda inicial")

                    return

                }
            }

                val customerName
                : String
                ?
                = customerNameField.text?.takeIf { it.isNotBlank() }?.trim()
                val orderType
                : String
                ?
                = ordertype.value?.takeIf { it.isNotBlank() }
                val purchaseDateInit
                : String
                ?
                = purchaseDatePicker.value?.toString()
                val purchaseDateEnd
                : String
                ?
                = purchaseDatePickerEnd.value?.toString()
                val invoiceDateInit
                : String
                ?
                = invoiceDatePicker.value?.toString()
                val invoiceDateEnd
                : String
                ?
                = invoiceDatePickerEnd.value?.toString()

            if (!customerCodeField.text.isNullOrBlank() || !loadCodeField.text.isNullOrBlank() || !customerNameField.text.isNullOrBlank() ||
                !ordertype.value.isNullOrBlank() || purchaseDatePicker.value != null || purchaseDatePickerEnd.value != null || invoiceDatePicker.value != null ||
                invoiceDatePickerEnd.value != null

            ) {

                try {

                    clearList()

                    val orders: List<Order>? = orderResource.findOrderByUserParameters(
                        customerCode,
                        customerName,
                        loadCode,
                        orderType,
                        purchaseDateInit,
                        purchaseDateEnd,
                        invoiceDateInit,
                        invoiceDateEnd
                    )

                    orders?.let { orderList.addAll(it) }

                    if (orderList.isEmpty()) {

                        showDialog("Nenhum pedido foi encontrado com os parametros fornecidos")

                    }

                } catch (e: Exception) {

                    showDialog("Erro ao bucar  pedidos ${e.message}")

                }
            } else {

                showDialog("Nenhum parametro foi fornecido para busca do pedido")

            }

                    clearFields ()


        }

        @FXML

        private fun searchOrders() {

            clearList()

            when{

                !customerNameField.text.isNullOrBlank() -> {

                    val customerList: ObservableList<Customer> = FXCollections.observableArrayList()

                    val customerName = customerNameField.text.trim()

                    try {

                        val customerWeb = customerConnection.fetchCustomerOnWebDbByName(customerName)

                        customerWeb?.forEach { customerList.add(it) }


                    } catch (e: Exception) {


                    }

                    if (customerList.isEmpty()) {

                        try {

                            val customerLc = customerResource.findCustomerByNameInLocalDb(customerName)

                            customerLc?.let { customerList.add(it) }

                        } catch (e: Exception) {


                        }

                    }

                    if (customerList.isEmpty()) {

                        showDialog("Não há nenhum cliente salvo no banco de dados com o nome: $customerName")

                        customerList.clear()

                        return

                    }

                }


            }


            try {

                findOrderByUserParametersOnWebDb()

            } catch (e: Exception) {

                println(e.message)

            }
            if (orderList.isEmpty()) {
                try {

                    findOrderByUserParametersOnLocalDb()

                } catch (e: Exception) {

                    println(e.message)

                }
            }

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


            orderTableView.selectionModel.isCellSelectionEnabled = true

            orderTableView.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
                if (event.isControlDown && event.code == KeyCode.C) {
                    val selectedCells = orderTableView.selectionModel.selectedCells
                    if (selectedCells.isNotEmpty()) {
                        val selectedCell = selectedCells[0]
                        val selectedColumn = selectedCell.tableColumn
                        val rowIndex = selectedCell.row
                        val cellData = selectedColumn.getCellData(rowIndex)?.toString() ?: ""

                        val clipboard = Clipboard.getSystemClipboard()
                        val content = ClipboardContent()
                        content.putString(cellData)
                        clipboard.setContent(content)
                    }
                }
            }


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

            findByUserParemeters.setOnAction { searchOrders() }

            orderTableView.items = orderList

        }


    }