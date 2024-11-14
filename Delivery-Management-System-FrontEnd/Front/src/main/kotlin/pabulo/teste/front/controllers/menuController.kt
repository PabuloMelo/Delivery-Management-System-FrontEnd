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
import pabulo.teste.front.connectionBackEnd.validators.CustomerValidation
import pabulo.teste.front.connectionBackEnd.validators.LoadValidation
import pabulo.teste.front.connectionBackEnd.validators.OrderValidation
import pabulo.teste.front.connectionBackEnd.validators.StateValidation
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.entity.Load
import pabulo.teste.front.entity.Order
import pabulo.teste.front.entity.State
import pabulo.teste.front.enumms.MenuChoices
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.resource.orderResource.OrderResource
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

    private val orderThrowable = StringBuilder(" ")


    private fun customersOnLocalDb() {

        customerList.clear()

        val customerResource = CustomerResource()


        val customers: List<Customer?> = customerResource.customerListToView()

        customers.forEach {

            val customerExists = customerValidation.verifiesCustomerExistsOnDataWeb(it?.customerCode!!.toLong())

            it.customerState = customerExists


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


    private val stateList: ObservableList<State> = FXCollections.observableArrayList()


    private val stateResource = StateResource()


    /*----------------------------------------------------------------------------------------------------------------*/

    private fun stateOrdersOnLocalDb() {

        stateList.clear()

        val states: List<State> = stateResource.findAllStateOnLocalDb()

        val stateValidation = StateValidation()

        states.forEach {

            val stateExists = stateValidation.verifiesOrderStateExists(it!!.orderCodeState.toLong())

            it.stateThrowble = stateExists

            stateList.add(it)

            stateTableView.refresh()

        }


    }


    private fun loadsOrdersOnLocalDb() {

        loadList.clear()

        val loadValidation = LoadValidation()


        val loadsOrders: List<Load?> = loadResource.findLoadToTable()

        loadsOrders.forEach {

            val loadExists = loadValidation.verifiesLoadExists(it!!.loadCode.toLong())

            it.loadThrowable = loadExists


            loadList.add(it)

            loadTableView.refresh()
        }


    }


    private fun ordersOnLocalDb() {

        orderList.clear()
        val orderValidation = OrderValidation()

        val orders: List<Order?> = orderResource.ordersToTableView()

        orders.forEach {

            val orderExists = orderValidation.verifiesOrderExistsOnWebDb(it!!.orderCode.toLong())

            orderThrowable.append(orderExists)


            if (it.customerName == "Cliente Não encontardo") {

                if (orderThrowable.isEmpty()) {

                    orderThrowable.append(" Cadastro do Cliente Pendente")

                } else {

                    orderThrowable.append(" e Cadastro do Cliente Pendente")

                }

            }


            it.orderTrouble = orderThrowable.toString()

            orderList.add(it)

            orderTableView.refresh()

        }


    }


    private fun openLoadsFounded(): Int {

        val validate = "Validação Pendente"
        val opensLoads = loadResource.findNotValidateLoadCode(validate)

        val totalOpenLoad: Int = opensLoads.size

        return totalOpenLoad
    }


    private fun ordersTrouble(): Int {

        val customerNotFounded = "Cliente Não encontardo"
        val orders: List<Order?> = orderResource.ordersProblems(customerNotFounded)

        val ordersTrouble = orders.size

        return ordersTrouble

    }


    private fun ordersTotalCount(): String {

        val orders: List<Order?> = orderResource.ordersToTableView()
        val ordersCount = orders.size


        return ordersCount.toString()
    }


    private fun tableViewRefresh() {

        ordersOnLocalDb()
        customersOnLocalDb()
        loadsOrdersOnLocalDb()
        stateOrdersOnLocalDb()


    }


    fun initialize() {
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