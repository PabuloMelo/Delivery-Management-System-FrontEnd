package pabulo.teste.front.controllers.load

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.CustomerConnection
import pabulo.teste.front.connectionBackEnd.OrderConnection
import pabulo.teste.front.dtos.load.LoadDTOtoDB
import pabulo.teste.front.dtos.orders.OrderSaveDto
import pabulo.teste.front.dtos.orders.SaverOrderDTOtoDb
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.entity.Order
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.addressResource.AddressResource
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.resource.sellerResource.SellerResource
import pabulo.teste.front.scenesManager.load.LoadSave
import java.io.File
import kotlin.random.Random

class LoadSaveController {

    private lateinit var loadSaveApp: LoadSave

    fun setLoadSave(loadSave: LoadSave) {

        this.loadSaveApp = loadSave

    }

    //-----------------------Botões do Menu Lateral-------------------------//

    @FXML

    private fun saveOrder() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.SaveOrder)

    }


    @FXML

    private fun findOrder() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }

    @FXML

    private fun createLoad() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }

    @FXML

    private fun saveOrderState() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.SaveStateOrder)

    }

    @FXML

    private fun findOrderState() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }

    @FXML

    private fun updateOrderState() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)


    }

    @FXML

    private fun returnToMain() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }

    @FXML

    private fun loadValidateMenu() {

        loadSaveApp.orderMenuOption(OrderChoicesMenu.ValidateLoad)

    }


//-----------------------Botões e Campos de Preenchimento----------------------------------------------------//

    @FXML

    private lateinit var driver: ChoiceBox<String>


    @FXML

    private lateinit var customerRegistered: ChoiceBox<String>

    @FXML

    private lateinit var orderStatus: ChoiceBox<String>


    @FXML

    private lateinit var ordertype: ChoiceBox<String>
    /*--------------------------------------------------------------------------------------------------------------------*/

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
    private lateinit var invoiceDatePicker: DatePicker

    @FXML
    private lateinit var orderAddressCodeField: TextField


    /*-------------------------------------------------TableView----------------------------------------------------------*/

    @FXML
    private lateinit var loadTableView: TableView<OrderSaveDto>

    @FXML
    private lateinit var orderCodeColumn: TableColumn<OrderSaveDto, Int>

    @FXML
    private lateinit var customerCodeColumn: TableColumn<OrderSaveDto, Int>

    @FXML
    private lateinit var customerNameColumn: TableColumn<OrderSaveDto, String>


    @FXML

    private lateinit var loadCodeColumn: TableColumn<OrderSaveDto, Int>

    @FXML

    private lateinit var orderStatusColumn: TableColumn<OrderSaveDto, String>

    @FXML

    private lateinit var orderTypeColumn: TableColumn<OrderSaveDto, String>


    @FXML

    private lateinit var purchaseDateColumn: TableColumn<OrderSaveDto, String>

    @FXML

    private lateinit var invoiceDateColumn: TableColumn<OrderSaveDto, String>


    @FXML

    private lateinit var orderAddressColumn: TableColumn<OrderSaveDto, String>

    @FXML

    private lateinit var driverViewField: TextField

    @FXML
    private lateinit var customerFindBtn: Button

    @FXML

    private lateinit var driverImageView: ImageView

    @FXML

    private lateinit var addOrderToLoad: Button

    @FXML

    private lateinit var saveLoad: Button

    @FXML

    private lateinit var orderAddress: ChoiceBox<String>

    /*--------------------------------------------------------------------------------------------------------------------*/
    private val orderResource = OrderResource()

    private val loadResource = LoadResource()


    private val orderList: ObservableList<OrderSaveDto> = FXCollections.observableArrayList()

    private val ordersToSaveList = mutableListOf<SaverOrderDTOtoDb>()

    private val verifieerUniqueOrder = mutableSetOf<Int>()

    private var loadToSave: Int = 0

    private val webDb = CustomerConnection()

    private val orderConnection = OrderConnection(GsonProvider.gson)

    private val addressConnection = AddressResource()

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


    private fun defineRca(orderCode: Int): Int {

        val orderToString = orderCode.toString()
        val firstTwoNumbers = orderToString.take(2)

        return firstTwoNumbers.toInt()
    }

    //  adicionar a função para ir criando a lista de pedidos antes de de fato criar um carregamento


    private fun findSellerNameByRca(sellerRca: Int): String {

        val sellerToFind = SellerResource()

        val seller = sellerToFind.findSellerInLocalDbByRca(sellerRca)

        val sellerName = seller?.sellerName ?: "Vendedor Não Cadastrado"

        return sellerName

    }

    private fun verifieLoadExistTest(loadCode: Int): Boolean {

        loadResource.findLoadByLoadCode(loadCode) ?: return false

        return true
    }


    private fun generateRandomCode(): Int {

        var randownInt: Int

        do {
            randownInt = Random.nextInt(1, 10000)
        } while (verifieLoadExistTest(randownInt))

        return randownInt

    }


    @FXML
    private fun findCustomerNameByCode(customerCode: Int): String {


        var customerFinded: String = " "

        var customerHandle: Customer? = null



        if (customerCodeField.text.isNullOrBlank()) {


            showDialog("O campo do codigo do cliente está vazio por favor digite um numero maior do que 0")


        } else {


            try {


                try {

                    val customerWeb = webDb.fetchCustomerOnWebDbByCode(customerCode.toLong())

                    if (customerWeb != null) {

                        customerHandle = customerWeb

                        customerNameField.text = customerWeb.customerName

                        customerFinded = customerWeb.customerName
                    }

                } catch (e: Exception) {

                    println(e)
                }

                if (customerHandle == null) {

                    try {

                        val customerResource = CustomerResource()
                        val customer = customerResource.findCustomerByCodeInLocalDb(customerCode)

                        customerFinded = if (customer != null) {

                            customerNameField.text = customer.customerName

                            customer.customerName

                        } else {

                            customerNameField.text = "Cliente Não Encontrado"

                            "Cliente Não encontardo"

                        }
                    } catch (e: Exception) {

                        println(e)

                    }

                }

            } catch (e: NumberFormatException) {

                println(e.message)
            }


        }

        return customerFinded


    }

    private fun findDriverId(driverName: String): Int {

        val driver = loadResource.findDriverByName(driverName)
        val driverId = driver!!.driverID

        return driverId
    }


    @FXML

    private fun saveloadonDb(): Int {

        val driver: String = driver.value
        val departureDate: String = invoiceDatePicker.value.toString()
        val driverFinded = loadResource.findDriverByName(driver)
        val loadValidate = "Validação Pendente"
        val loadCode: Int = findDriverId(driver)

        driverFinded?.let {

            if (it.driverPhotoPath.isNotEmpty()) {

                val imagefile = File(it.driverPhotoPath)

                if (imagefile.exists()) {

                    val image = Image(imagefile.toURI().toString())
                    driverImageView.image = image

                } else {
                    driverImageView.image = null
                }

            } else {
                driverImageView.image = null
            }
        }


        driverViewField.text = driver


        val verifieLoadExists = loadResource.findLoadByLoadCode(loadCode)

        val loadUnique: Int = if (verifieLoadExists == null) {
            loadResource.saveLoadOnLocalDb(LoadDTOtoDB(loadCode, driver, departureDate, loadValidate))
            loadCode

        } else {
            val randownLoad = generateRandomCode()
            loadResource.saveLoadOnLocalDb(LoadDTOtoDB(randownLoad, driver, departureDate, loadValidate))
            randownLoad

        }

        loadCodeField.text = loadUnique.toString()

        return loadUnique

    }


    private fun verifieOrderAlreadyExists(orderCode: Int) {

        var orderFounded: Order? = null

        try {

            orderFounded = orderConnection.fetchOrderByCode(orderCode.toLong())


        } catch (e: Exception) {

            println(e.message)

        }

        if (orderFounded == null) {

            try {

                orderFounded = orderResource.findOrderByCode(orderCode)

            } catch (e: Exception) {

                println(e.message)
            }

        }

        if (orderFounded != null) {

            showDialog("Já existe um pedido salvo no banco de dados com o codigo: $orderCode")
        }


    }


    /*--------------------------------------------------------------------------------------------------------------*/
    @FXML
    private fun addOrdersToLoad() {

        when {

            driver.value == null -> {

                showDialog("Por favor selecione um motorista")

                return


            }

            orderCodeField.text.isNullOrBlank() -> {

                showDialog("O campo do codigo do pedido está vazio por favor digite um numero maior do que 0")

                return

            }

            !orderCodeField.text.trim().all { it.isDigit() } -> {

                showDialog("Por Favor digite somente numeros maiores do que 0 no campo codigo do pedido")

                return

            }

            customerCodeField.text.isNullOrBlank() -> {

                showDialog("O campo do codigo do cliente está vazio por favor digite um numero maior do que 0")

                return

            }

            !customerCodeField.text.trim().all { it.isDigit() } -> {

                showDialog("Por Favor digite somente numeros maiores do que 0 no campo codigo do cliente")

                return

            }

            customerNameField.text.isNullOrBlank() -> {

                showDialog("Nenhum cliente foi selecionado para atribuir ao pedido")

                return
            }

            orderAddress.value == null -> {

                showDialog("É necessario informar o bairro de destino do pedido")

                return

            }

            ordertype.value == null -> {

                showDialog("É necessario informar o tipo de pedido")

                return

            }

            orderStatus.value == null -> {


                showDialog("É necessario informar o status atual do pedido")

                return
            }

            purchaseDatePicker.value == null -> {

                showDialog("O pedido precisa de uma data de compra")

                return

            }

            invoiceDatePicker.value == null -> {

                showDialog("Pedidos que sairão para a entrega precisam de uma data de faturamento")

                return
            }

            purchaseDatePicker.value != null && invoiceDatePicker.value != null && purchaseDatePicker.value > invoiceDatePicker.value -> {

                showDialog("A data de faturamento do pedido não pode ser menor do que sua data de compra")

                return
            }


        }


        val orderCode: Int = orderCodeField.text.trim().toInt()
        val customerCode: Int = customerCodeField.text.trim().toInt()
        val sellerRca: Int = defineRca(orderCode)
        val customerName: String = findCustomerNameByCode(customerCode)
        val sellerName: String = findSellerNameByRca(sellerRca)
        val loadNumber = 1
        val orderAddressA: String = orderAddress.value


        val ordertype: String = ordertype.value
        val orderStatus: String = orderStatus.value
        val purchaseDate: String = purchaseDatePicker.value?.toString() ?: " "
        val invoiceDate: String = invoiceDatePicker.value?.toString() ?: " "


        val newOrderToSave = OrderSaveDto(
            orderCode,
            customerCode,
            customerName,
            loadNumber,
            orderStatus,
            ordertype,
            purchaseDate,
            invoiceDate,
            sellerRca,
            sellerName,
            orderAddressA
        )
        val orderSaveToDb = SaverOrderDTOtoDb(
            orderCode,
            customerCode,
            customerName,
            loadNumber,
            orderStatus,
            ordertype,
            purchaseDate,
            invoiceDate,
            sellerRca,
            sellerName,
            orderAddressA

        )


        val orderWasAdd = verifieerUniqueOrder.add(orderSaveToDb.orderCode)

        if (orderWasAdd) {

            showDialog("pedido ${orderSaveToDb.orderCode} adicionado")
            ordersToSaveList.add(orderSaveToDb)
            orderList.add(newOrderToSave)

        } else {

            showDialog("pedido ${orderSaveToDb.orderCode} já esta no carregamento")

        }

        loadTableView.refresh()
        enableSaveButon()

        clearFields()

        driver.isDisable = true

        driver.opacity = 1.0


    }

    private fun addNewLoad() {
        ordersToSaveList.forEach { order ->
            order.loadNumber = saveloadonDb() //loadToSave

        }
    }

    private fun saveLoadOrder() {

        addNewLoad()
        ordersToSaveList.forEach { orderResource.saveOrderOnLocalDb(it) }


        clearFields()
        ordersToSaveList.clear()
        orderList.clear()
        driver.value = " "

        enableSaveButon()

        driver.isDisable = false
        driver.opacity = 1.0

    }


    private fun clearFields() {


        orderCodeField.clear()
        customerCodeField.clear()
        customerNameField.clear()
        orderStatus.value = "Default"
        ordertype.value = "Entrega"
        purchaseDatePicker.value = null


    }

    private fun enableSaveButon() {

        if (orderList.isEmpty()) {

            saveLoad.isDisable = true
            saveLoad.opacity = 0.5

        } else {
            saveLoad.isDisable = false
            saveLoad.opacity = 1.0
        }

    }


    @FXML


    fun initialize() {


        loadCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.loadNumber!!.get()).asObject() }
        orderCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.orderCode.get()).asObject() }
        customerCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode.get()).asObject() }
        customerNameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName.get()) }
        orderTypeColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.orderType.get()) }
        orderStatusColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.status.get()) }
        invoiceDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.invoicingDate.get()) }
        purchaseDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.purchaseDate.get()) }
        orderAddressColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.orderAddress.get()) }
        //Customer Actions

        customerFindBtn.text

        customerFindBtn.setOnAction {


            if (customerCodeField.text.isNullOrBlank()) {

                showDialog("O campo do codigo do cliente está vazio por favor digite um numero maior do que 0")

            } else {

                findCustomerNameByCode(customerCodeField.text.trim().toInt())
            }

        }

        val addressList = addressConnection.getAddress()

        orderAddress.items = FXCollections.observableArrayList(addressList)

        orderAddress.setOnAction { orderAddressCodeField.clear() }

        customerCodeField.setOnAction {
            if (customerCodeField.text.isNullOrBlank()) {

                showDialog("O campo do codigo do cliente está vazio por favor digite um numero maior do que 0")

            } else {

                findCustomerNameByCode(customerCodeField.text.trim().toInt())
            }

        }

        // Driver and Load Actions
        val drivers = loadResource.getDrivers()

        driver.items = FXCollections.observableArrayList(drivers)
        driver.value = "Default"

        enableSaveButon()
        //Action Buttons

        addOrderToLoad.text

        addOrderToLoad.setOnAction { addOrdersToLoad() }

        saveLoad.text


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

        orderCodeField.focusedProperty().addListener { _, _, newValue ->


            if (!newValue) {

                try {

                    val orderCode = orderCodeField.text.trim().toInt()

                    verifieOrderAlreadyExists(orderCode)

                } catch (e: NumberFormatException) {


                    showDialog("Erro: Valor Invalido para o campo numero do pedido, por favor digite um numero maior do que 0")

                }


            }

        }


        orderAddressCodeField.focusedProperty().addListener { _, _, newValue ->


            if (!newValue) {


                try {

                    val districtCode: Int = orderAddressCodeField.text.trim().toInt()

                    val districtFounded = addressConnection.findAddressByDistrictCode(districtCode)

                    if (districtFounded == null) {

                        showDialog("Nenhum bairro localizado com esse codigo: $districtCode ")

                        orderAddressCodeField.clear()

                    } else {

                        orderAddress.value = districtFounded.district


                    }

                } catch (e: NumberFormatException) {

                    showDialog("Erro: Valor Invalido para o campo codigo do bairro")

                }


            }


        }





        saveLoad.setOnAction { saveLoadOrder() }

        customerRegistered.items = FXCollections.observableArrayList(" SIM", "NAO", "Default")

        customerRegistered.value = "Default"

        orderStatus.items = FXCollections.observableArrayList(
            "Default",
            "Em Rota De Entrega",
            "Pendente",
            "Entregue",
            "Cancelada",
            "Agendada"
        )

        orderStatus.value = "Default"

        ordertype.items = FXCollections.observableArrayList(
            "Entrega",
            "Carteira Entrega",
            "Carteita Retira Posterior",
            "Retira Imediata"
        )


        loadTableView.items = orderList

    }


}