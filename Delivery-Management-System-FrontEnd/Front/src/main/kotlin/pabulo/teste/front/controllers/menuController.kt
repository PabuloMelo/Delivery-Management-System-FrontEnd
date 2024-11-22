package pabulo.teste.front.controllers

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import pabulo.teste.front.Main
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.*
import pabulo.teste.front.connectionBackEnd.validators.*
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.entity.Load
import pabulo.teste.front.entity.Order
import pabulo.teste.front.entity.State
import pabulo.teste.front.enumms.MenuChoices
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.resource.sellerResource.SellerResource
import pabulo.teste.front.resource.stateResource.StateResource


class MenuController {

    private lateinit var mainApp: Main

    fun setMainApp(mainApp: Main) {

        this.mainApp = mainApp


    }

    @FXML
    private fun customer() {
        mainApp.changeScene(MenuChoices.CustomerMenu)
    }


    @FXML

    private fun order() {
        mainApp.changeScene(MenuChoices.OrderMenu)

    }

    @FXML

    private fun seller() {
        mainApp.changeScene(MenuChoices.SellerMenu)
    }

    @FXML
    private fun driver() {

        mainApp.changeScene(MenuChoices.DriverMenu)


    }

    @FXML

    private fun loadMenu() {

        mainApp.changeScene(MenuChoices.LoadMenu)

    }

    @FXML

    private fun addressMenu() {

        mainApp.changeScene(MenuChoices.addressMenu)

    }

    @FXML

    private fun testeArea() {

        mainApp.changeScene(MenuChoices.TestArea)

    }


    /*---------------------------------------------------------Cards---Area--------------------------------------------*/

    @FXML

    private lateinit var ordersWithTrouble: Label

    @FXML

    private lateinit var orderTotalCountLabel: Label

    @FXML

    private lateinit var loadsTotalCountLabel: Label

    /*--------------------------------------------------------CustomerArea------------------------------------------------*/

    @FXML

    private lateinit var customerFindTableView: TableView<Customer>

    @FXML


    private lateinit var customerCodeCustomerColumn: TableColumn<Customer, Int>

    @FXML

    private lateinit var customerNameColumn: TableColumn<Customer, String>

    @FXML

    private lateinit var customerPhoneColumn: TableColumn<Customer, String>

    @FXML

    private lateinit var customerRegisteredColumn: TableColumn<Customer, String>

    @FXML

    private lateinit var customerTypeColumn: TableColumn<Customer, String>

    @FXML

    private lateinit var customerThrowableColumn: TableColumn<Customer, String>

    @FXML

    private lateinit var updateButton: Button

    @FXML

    private var customerList: ObservableList<Customer> = FXCollections.observableArrayList()

    private val customerValidation = CustomerValidation()

    private var throwableCount: Int = 0

    private val orderConnection = OrderConnection(GsonProvider.gson)

    private val orderValidation = OrderValidation()

    private val customerResource = CustomerResource()

    private val customerConnection = CustomerConnection()

    private fun customersOnLocalDb() {

        customerList.clear()


        val customers: List<Customer?> = customerResource.customerListToView()

        customers.forEach {

            val customerExists = customerValidation.verifiesCustomerExistsOnDataWeb(it?.customerCode!!.toLong())

            if (customerExists.isNotBlank()) {

                it.customerState = customerExists

                throwableCount++

                println("inicial: $throwableCount")


            }
            customerList.add(it)

            customerFindTableView.refresh()

        }


    }

    /*--------------------------------------------------Order--Area-------------------------------------------------------*/
    @FXML
    private lateinit var orderTableView: TableView<Order>

    @FXML
    private lateinit var orderCodeOrderColumn: TableColumn<Order, Int>

    @FXML
    private lateinit var customerCodeOrderColumn: TableColumn<Order, Int>

    @FXML

    private lateinit var loadCodeOrderColumn: TableColumn<Order, Int>

    @FXML
    private lateinit var customerNameOrderColumn: TableColumn<Order, String>

    @FXML

    private lateinit var orderTypeColumn: TableColumn<Order, String>

    @FXML
    private lateinit var orderThrowableColumn: TableColumn<Order, String>
    /*--------------------------------------------------------------------------------------------------------------------*/

    private var loadList: ObservableList<Load> = FXCollections.observableArrayList()

    private val orderList: ObservableList<Order> = FXCollections.observableArrayList()

    private val orderResource = OrderResource()

    private val loadResource = LoadResource()

    private val loadValidation = LoadValidation()

    private val loadConnection = LoadConnection(GsonProvider.gson)

    private val testConnection = ConnectionBackend()

    /*-------------------------------------------------Load--Table---------------------------------------------------------*/

    @FXML

    private lateinit var loadTableView: TableView<Load>

    @FXML

    private lateinit var loadCodeLoadColumn: TableColumn<Load, Int>

    @FXML
    private lateinit var driverLoadColumn: TableColumn<Load, String>


    @FXML
    private lateinit var validateLoadColumn: TableColumn<Load, String>


    @FXML
    private lateinit var loadDepartureDateColumn: TableColumn<Load, String>

    @FXML

    private lateinit var loadThrowableColumn: TableColumn<Load, String>
    /*--------------------------------------------------------------------------------------------------------------------*/

    @FXML

    private lateinit var stateTableView: TableView<State>

    @FXML

    private lateinit var orderCodeColumn: TableColumn<State, Int>

    @FXML
    private lateinit var customerCodeColumn: TableColumn<State, Int>

    @FXML

    private lateinit var customerNameTableColumn: TableColumn<State, String>

    @FXML

    private lateinit var purchaseDateColumn: TableColumn<State, String>

    @FXML

    private lateinit var invoiceDateColumn: TableColumn<State, String>

    @FXML

    private lateinit var stateInitColumn: TableColumn<State, String>

    @FXML

    private lateinit var firstLevelColumn: TableColumn<State, String>

    @FXML

    private lateinit var secondLevelColumn: TableColumn<State, String>

    @FXML

    private lateinit var resolveStateColumn: TableColumn<State, String>

    @FXML

    private lateinit var resolveDateColumn: TableColumn<State, String>

    @FXML

    private lateinit var resolveDriverColumn: TableColumn<State, String>


    @FXML

    private lateinit var descriptionColumn: TableColumn<State, String>

    @FXML

    private lateinit var orderStateThrowableColumn: TableColumn<State, String>

    @FXML

    private lateinit var syncButton: Button

    private val stateConnection = StateConnection(GsonProvider.gson)


    private val stateList: ObservableList<State> = FXCollections.observableArrayList()


    private val stateResource = StateResource()

    private val stateValidation = StateValidation()

    private val sellerConnection = SellerConnection(GsonProvider.gson)

    private val sellerResource = SellerResource()

    private val sellerValidate = SellerValidate()

    private var opensLoads: Int = 0


    /*----------------------------------------------------------------------------------------------------------------*/

    private fun stateOrdersOnLocalDb() {

        stateList.clear()

        val states: List<State> = stateResource.findAllStateOnLocalDb()



        states.forEach {

            val stateExists = stateValidation.verifiesOrderStateExists(it.orderCodeState.toLong())

            if (stateExists.isNotBlank()) {

                it.stateThrowble = stateExists

                throwableCount++

                println("segundo: $throwableCount")
            }
            stateList.add(it)

            stateTableView.refresh()

        }


    }


    private fun loadsOrdersOnLocalDb() {

        loadList.clear()

     //  var loadProblem = 0

        val loadsOrders: List<Load?> = loadResource.findLoadToTable()

        loadsOrders.forEach {

            val loadExists = loadValidation.verifiesLoadExists(it!!.loadCode.toLong())

            if (loadExists.isNotBlank()) {

                it.loadThrowable = loadExists

                throwableCount ++

                println("terceiro: $throwableCount")

            }

        }

        val testLoadsBefore = loadResource.ignoreLoadsDefault()

        println("Carregamentos no DB $testLoadsBefore")

        if (testLoadsBefore.isNotEmpty()) {

            testLoadsBefore.forEach { load -> loadList.add(load) }

            loadTableView.refresh()

        }


    }

    private val orderThrowable = StringBuilder(" ")

    private fun ordersOnLocalDb() {

        orderList.clear()

       // var orderProblem = 0

        val orders: List<Order?> = orderResource.ordersToTableView()

        println("pediddo: $orders")

        orders.forEach {

            orderThrowable.clear()

                val orderExists = orderValidation.verifiesOrderExistsOnWebDb(it!!.orderCode.toLong())

                orderThrowable.append(orderExists)


                if (orderThrowable.isBlank() && it.customerName == "Cliente Não encontardo") {

                    orderThrowable.append("Cadastro do Cliente Pendente")

                    throwableCount++

                    println("quarto: $throwableCount")

                } else if (it.customerName == "Cliente Não encontardo" && orderThrowable.isNotBlank()) {

                    orderThrowable.append(" e Cadastro do Cliente Pendente")

                    throwableCount += 2

                    println("quarto: $throwableCount")

                }




                it.orderTrouble = orderThrowable.toString()

                orderList.add(it)
                orderTableView.refresh()




        }


    }


    private fun openLoadsFounded(): Int {

        val validate = "Validação Pendente"
        val loadsFounded = loadResource.findNotValidateLoadCode(validate)

        val totalOpenLoad: Int = loadsFounded.size

        opensLoads = totalOpenLoad

        return totalOpenLoad
    }


    private fun ordersTrouble(): Int {

        val ordersTrouble = throwableCount

        println("total: $throwableCount")

        ordersWithTrouble.text = ordersTrouble.toString()

        return ordersTrouble

    }


    private fun ordersTotalCount(): String {

        val orders: List<Order?> = orderResource.ordersToTableView()
        val ordersCount = orders.size


        return ordersCount.toString()
    }


    private fun tableViewRefresh() {

        throwableCount = 0

        ordersOnLocalDb()
        customersOnLocalDb()
        loadsOrdersOnLocalDb()
        stateOrdersOnLocalDb()
        ordersTrouble()
        syncButtonManager()

        testConnection.testConnection()


    }


    fun syncDbData() {


        val customers = customerResource.customerListToView()

        customers.forEach {

                customer ->

            val testCustomerExists = customerConnection.fetchCustomerOnWebDbByCode(customer!!.customerCode.toLong())

            if (testCustomerExists == null) {

                customerConnection.saveCustomerOnWebDb(customer.customerCode)

                val customerSync = customerValidation.testSyncSuccess(customer.customerCode.toLong())

                customer.apply {

                    customerResource.updateCustomerSync(customerCode, customerSync)

                }
            }

        }

        val loads = loadResource.loadToTableView()


        loads.forEach {


                load ->

            val loadExists = orderConnection.fetchAllOrdersByLoadCode(load!!.loadCode.toLong())

            if (loadExists == null) {

                loadConnection.saveLoadOnWebDb(load.loadCode)

                val loadSync = loadValidation.syncLoad(load.loadCode.toLong())


                load.apply {


                    loadResource.updateLoadSync(loadCode, loadSync)


                }
            }

        }

        val sellers = sellerResource.findAllSellerInLocalDb()

        sellers.forEach {

                seller ->

            val sellerExist = sellerValidate.testSellerExists(seller!!.sellerRca.toLong())

            if (!sellerExist) {

                sellerConnection.saveSellerOnWebDb(seller.sellerRca)

            }


        }


        val orders = orderResource.ordersToTableView()

        orders.forEach {

                order ->

            val orderExists = orderConnection.fetchOrderByCode(order!!.orderCode.toLong())

            if (orderExists == null) {

                orderConnection.saveOrderOnDBWeb(order.orderCode)

                val orderSync = orderValidation.syncOrderDbValidation(order.orderCode.toLong())

                order.apply {

                    orderResource.updateOrderSync(orderCode, orderSync)

                }

            }
        }


        val states = stateResource.findAllStateOnLocalDb()

        states.forEach {

                state ->

            val stateExists = stateConnection.fetchByOrderStateCode(state.orderCodeState.toLong())

            if (stateExists == null) {

                stateConnection.saveStateOnWebDb(state.orderCodeState)

                val stateSync = stateValidation.stateValidadet(state.orderCodeState.toLong())

                state.apply {

                    stateResource.updateStateSync(state.orderCodeState, stateSync)

                }
            }
        }


        orderConnection.updateAllOrders()

        customerResource.deleteCustomerValidated()
        loadResource.deleteLoadValidated()
        orderResource.deleteOrderValidated()
        stateResource.deleteStateValidated()

        tableViewRefresh()

        syncButton.isDisable = true

        syncButton.opacity = 0.5


    }


    private fun syncButtonManager() {

        val testConnectionActive = testConnection.testConnection()

        if (throwableCount > 0 || opensLoads > 0 || testConnectionActive == 2 || testConnectionActive == 3) {

            syncButton.isDisable = true

            syncButton.opacity = 0.5


        } else {

            syncButton.isDisable = false

            syncButton.opacity = 1.0


        }

    }


    fun initialize() {

        syncButton.isDisable = true


        /*-----------------------------------------Customer table-----------------------------------------------------*/
        customerCodeCustomerColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode).asObject() }
        customerNameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName) }
        customerPhoneColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.phone) }
        customerRegisteredColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerRegistered) }
        customerTypeColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerType) }
        customerThrowableColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerState) }

        /*-------------------------------------------Tables--Config---------------------------------------------------*/
        customerFindTableView.items = customerList

        customerFindTableView.selectionModel.isCellSelectionEnabled = true

        customerFindTableView.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            if (event.isControlDown && event.code == KeyCode.C) {
                val selectedCells = customerFindTableView.selectionModel.selectedCells
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


        /*----------------------------------------------------OrdersTable----------------------------------------------*/

        orderCodeOrderColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderCode).asObject() }
        loadCodeOrderColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.loadCode).asObject() }
        customerCodeOrderColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.customerCode).asObject() }
        customerNameOrderColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.customerName) }
        orderTypeColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderType) }
        orderThrowableColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderTrouble) }

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

        orderTableView.items = orderList
        /*--------------------------------------------------Load--Table-----------------------------------------------*/

        loadCodeLoadColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.loadCode).asObject() }
        driverLoadColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.driver) }
        validateLoadColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.loadValidate) }
        loadDepartureDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.departureDate) }
        loadThrowableColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.loadThrowable) }

        loadTableView.selectionModel.isCellSelectionEnabled = true

        loadTableView.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            if (event.isControlDown && event.code == KeyCode.C) {
                val selectedCells = loadTableView.selectionModel.selectedCells
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


        loadTableView.items = loadList
        /*--------------------------------------------State--Table----------------------------------------------------*/


        orderCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.orderCodeState).asObject() }
        customerCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode).asObject() }
        customerNameTableColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName) }
        purchaseDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.purchaseDate) }
        invoiceDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.invoiceDate) }
        stateInitColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.state) }
        firstLevelColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.stateNV1) }
        secondLevelColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.stateNV2) }
        resolveStateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.resolve) }
        resolveDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.solveDate) }
        resolveDriverColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.solveDriver) }
        descriptionColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.description) }
        orderStateThrowableColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.stateThrowble) }

        stateTableView.selectionModel.isCellSelectionEnabled = true

        stateTableView.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            if (event.isControlDown && event.code == KeyCode.C) {
                val selectedCells = stateTableView.selectionModel.selectedCells
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


        stateTableView.items = stateList


        val ordersTroubleCount = ordersTrouble()
        ordersWithTrouble.text = ordersTroubleCount.toString()

        updateButton.text

        updateButton.setOnAction { tableViewRefresh() }


        val ordersTotal = ordersTotalCount()


        val openLoadsCount = openLoadsFounded()

        loadsTotalCountLabel.text = openLoadsCount.toString()

        orderTotalCountLabel.text = ordersTotal
    }


}