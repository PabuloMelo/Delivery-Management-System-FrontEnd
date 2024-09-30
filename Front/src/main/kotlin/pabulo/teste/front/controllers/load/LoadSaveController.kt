package pabulo.teste.front.controllers.load

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import pabulo.teste.front.dtos.load.LoadDTOtoDB
import pabulo.teste.front.dtos.orders.OrderSaveDto
import pabulo.teste.front.dtos.orders.SaverOrderDTOtoDb
import pabulo.teste.front.enumms.OrderChoicesMenu
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

    private lateinit var driverViewField: TextField

    @FXML
    private lateinit var customerFindBtn: Button

    @FXML

    private lateinit var driverImageView: ImageView

    @FXML

    private lateinit var addOrderToLoad: Button

    @FXML

    private lateinit var saveLoad: Button

    /*--------------------------------------------------------------------------------------------------------------------*/
    private val orderResource = OrderResource()

    private val loadResource = LoadResource()


    private val orderList: ObservableList<OrderSaveDto> = FXCollections.observableArrayList()

    private val ordersToSaveList = mutableListOf<SaverOrderDTOtoDb>()

    private val verifieerUniqueOrder = mutableSetOf<Int>()

    private var loadToSave: Int = 0


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

        val loadTest = loadResource.findLoadByLoadCode(loadCode)

        if (loadTest == null) {

            return false
        }
        return true
    }


    private fun generateRandomCode(): Int {

        var randownInt: Int

        do {
            randownInt = Random.nextInt(1, 10000)
        } while (verifieLoadExistTest(randownInt) == true)

        return randownInt

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

    private fun findDriverId(driverName: String): Int {

        var driver = loadResource.findDriverByName(driverName)
        var driverId = driver!!.driverID

        return driverId
    }


    @FXML

    private fun saveloadonDb(): Int {

        val driver: String = driver.value
        val departureDate: String = invoiceDatePicker.value.toString()
        val driverFinded = loadResource.findDriverByName(driver)
        val loadValidate: String = "Validação Pendente"
        var loadCode: Int = findDriverId(driver)

        driverFinded?.let {

            if (!it.driverPhotoPath.isNullOrEmpty()) {

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


    /*--------------------------------------------------------------------------------------------------------------*/
    @FXML
    private fun addOrdersToLoad() {


        val orderCode: Int = orderCodeField.text.trim().toInt()
        val customerCode: Int = customerCodeField.text.trim().toInt()
        val sellerRca: Int = defineRca(orderCode)
        val customerName: String = findCustomerNameByCode(customerCode)
        val sellerName: String = findSellerNameByRca(sellerRca)
        val loadNumber = 1


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
            sellerName
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
            sellerName
        )


        val orderWasAdd = verifieerUniqueOrder.add(orderSaveToDb.orderCode)

        if (orderWasAdd) {

            println("pedido ${orderSaveToDb.orderCode} adicionado")
            ordersToSaveList.add(orderSaveToDb)
            orderList.add(newOrderToSave)

        } else {

            println("pedido ${orderSaveToDb.orderCode} já esta no carregamento")

        }

        loadTableView.refresh()
        enableSaveButon()

        clearFields()

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

    }


    private fun clearFields() {


        orderCodeField.clear()
        customerCodeField.clear()
        customerNameField.clear()
        orderStatus.value = "Default"
        ordertype.value = "Entrega"
        purchaseDatePicker.value = null


    }

   private fun enableSaveButon(){

        if (orderList.isEmpty() == true) {

            saveLoad.isDisable = true
            saveLoad.opacity = 0.5

        }else {
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
        //Customer Actions

        customerFindBtn.text

        customerFindBtn.setOnAction { findCustomerNameByCode(customerCodeField.text.trim().toInt()) }

        customerCodeField.setOnAction { findCustomerNameByCode(customerCodeField.text.trim().toInt()) }

        // Driver and Load Actions
        val drivers = loadResource.getDrivers()

        driver.items = FXCollections.observableArrayList(drivers)
        driver.value = "Default"

        enableSaveButon()
        //Action Buttons

        addOrderToLoad.text

        addOrderToLoad.setOnAction { addOrdersToLoad() }

        saveLoad.text




        saveLoad.setOnAction { saveLoadOrder() }




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

        ordertype.items = FXCollections.observableArrayList(
            "Entrega",
            "Retira Posterior",
            "Carteira",
            "Carteira Entrega",
            "Carteita Retira Posterior",
            "Entrega Futura",
            "Retira Imediata"
        )


        loadTableView.items = orderList

    }


}