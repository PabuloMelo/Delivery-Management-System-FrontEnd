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
import pabulo.teste.front.connectionBackEnd.OrderConnection
import pabulo.teste.front.dtos.load.LoadUpdateDto
import pabulo.teste.front.dtos.orders.OrderUpdateDTOtoDb
import pabulo.teste.front.entity.Load
import pabulo.teste.front.entity.Order
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.scenesManager.load.ValidateLoad
import java.io.File

class LoadValidateController {

    private lateinit var loadValidateAPP: ValidateLoad


    fun setLoadValidate(loadValidate: ValidateLoad) {

        this.loadValidateAPP = loadValidate


    }

    @FXML

    private fun saveOrder() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.SaveOrder)

    }


    @FXML

    private fun findOrder() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }

    @FXML

    private fun createALoad() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }

    @FXML

    private fun saveOrderState() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.SaveStateOrder)

    }

    @FXML

    private fun findOrderState() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }

    @FXML

    private fun updateOrderState() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)


    }

    @FXML

    private fun returnToMain() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }

    @FXML

    private fun loadValidateMenu() {

        loadValidateAPP.orderMenuOption(OrderChoicesMenu.ValidateLoad)

    }

    /*-------------------------------------------select----fields-----------------------------------------------------*/

    @FXML
    private lateinit var opensLoads: ChoiceBox<String>

    @FXML
    private lateinit var driverField: TextField

    @FXML
    private lateinit var totalOrdersOfLoad: TextField

    @FXML
    private lateinit var newLoadCodeField: TextField


    /*-----------------------------------------Action Buttons----------------------------------------------------------*/
    @FXML

    private lateinit var findLoadButton: Button

    @FXML
    private lateinit var validateLoadButton: Button

    /*-------------------------------------------LoadView-------------------------------------------------------------*/
    @FXML
    private lateinit var loadTableView: TableView<Order>

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

    private lateinit var driverNameTextField: TextField

    @FXML
    private lateinit var driverImageView: ImageView


    @FXML

    private lateinit var sucessValidate: Label

    /*---------------------------------------Use--variables-----------------------------------------------------------*/

    private val orderList: ObservableList<Order> = FXCollections.observableArrayList()

    private val orderValidateList = mutableListOf<Order>()

    private val orderResource = OrderResource()

    private val loadResource = LoadResource()

    private val validate: String = "Validação Pendente"

    private val orderConnection = OrderConnection(GsonProvider.gson)


    /*---------------------------------------Functions----------------------------------------------------------------*/


    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane


    fun handleOkButton() {

        dialogPane.isVisible = false

    }


    fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true


    }

    private fun verifieNewLoadAlreadExists(loadCode: Int) {

        validateLoadButton.isDisable = false

        val loadFounded: ObservableList<Order> = FXCollections.observableArrayList()


        try {


            val orders = orderConnection.fetchAllOrdersByLoadCode(loadCode.toLong())

            orders?.let { loadFounded.addAll(it) }

        } catch (e: Exception) {

            println(e.message)

        }

        if (loadFounded.isEmpty()) {


            try {

                val orders = orderResource.findALlOrdersByLoad(loadCode)

                orders.let { loadFounded.addAll(it) }


            } catch (e: Exception) {

                println(e.message)
            }

            if (!loadFounded.isEmpty()) {

                showDialog("Já existe um carregamento salvo no banco de dados com o codigo: $loadCode")

                newLoadCodeField.clear()

                validateLoadButton.isDisable = true

                return

            }

        }

    }


    private fun findOrdersByLoad() {

        clearTableView()

        val loadCode: Int = opensLoads.value.toInt()
        val load = loadResource.findLoadByLoadCode(loadCode)
        val driverName: String = load!!.driver
        val driverFound = loadResource.findDriverByName(driverName)


        driverFound?.let {

            if (it.driverPhotoPath.isNotEmpty()) {

                val imageFile = File(it.driverPhotoPath)

                if (imageFile.exists()) {

                    val image = Image(imageFile.toURI().toString())
                    driverImageView.image = image

                } else {

                    driverImageView.image = null

                }

            } else {

                driverImageView.image = null

            }

        }

        driverNameTextField.text = driverName
        driverField.text = driverName

        val orders = orderResource.findALlOrdersByLoad(loadCode)
        val totalOrders = orders.size

        totalOrdersOfLoad.text = totalOrders.toString()

        orders.forEach {
            orderList.add(it)
            orderValidateList.add(it)
        }


        loadTableView.refresh()

    }

    private fun findLoadByLoadCode(): Load {

        val loadCode: Int = opensLoads.value.toInt()

        val loadFound = loadResource.findLoadByLoadCode(loadCode)

        return loadFound!!

    }

    @FXML

    private fun validateLoad() {


        when{

            orderList.isEmpty() -> {

                showDialog("Por favor selecione e clique em pesquisar carregamento")

                return

            }

            newLoadCodeField.text.isNullOrBlank() -> {

                showDialog("O campo novo codigo de carregamento está vazio")

                return

            }

            !newLoadCodeField.text.trim().all { it.isDigit() } -> {

                showDialog("Digite somente numeros maiores que 0 para o campo novo codigo de carregamento ")

                return

            }


        }


        val newLoad: Int = newLoadCodeField.text.trim().toInt()
        val validateLoad = "VALIDADO"
        val loadToUpdate = findLoadByLoadCode()

        val loadDb = LoadUpdateDto(
            loadCode = newLoad,
            loadValidate = validateLoad

        )

        loadResource.updateLoadByCode(loadToUpdate.loadCode, loadDb)

        orderValidateList.forEach { order ->
            val orderDb = OrderUpdateDTOtoDb(
                loadNumber = newLoad,
            )
            orderResource.updateOrder(order.orderCode, orderDb)
        }


        sucessValidate.text = "Carregamento $newLoad Validado Com Sucesso!!"

        val newLoadCodesList = loadResource.findNotValidateLoadCode(validate)

        opensLoads.items = FXCollections.observableArrayList(newLoadCodesList)

        orderValidateList.clear()
        clearTableView()

        clearFields()


    }


    private fun clearTableView() {

        orderList.clear()

    }

    private fun clearFields() {

        opensLoads.value = " "
        sucessValidate.text = " "
        newLoadCodeField.text = " "
        totalOrdersOfLoad.text = " "

    }


    fun initialize() {


        loadCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.loadCode).asObject() }
        orderCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.orderCode).asObject() }
        customerCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode).asObject() }
        customerNameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName) }
        orderTypeColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.orderType) }
        orderStatusColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.orderStatus) }
        invoiceDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.invoiceDate) }
        purchaseDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.purchaseDate) }

        val loadCodesList = loadResource.findNotValidateLoadCode(validate)

        opensLoads.items = FXCollections.observableArrayList(loadCodesList)

        validateLoadButton.text

        validateLoadButton.setOnAction { validateLoad() }

        findLoadButton.setOnAction { findOrdersByLoad() }

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

        newLoadCodeField.focusedProperty().addListener { _, _, newValue ->


            if (!newValue) {

                try {

                    val loadCode = newLoadCodeField.text.trim().toInt()

                    verifieNewLoadAlreadExists(loadCode)

                }catch (e:NumberFormatException){

                    showDialog("Erro: Valor Invalido para o campo codigo do carregamento, " +
                            "por favor digite um numero maior do que 0")

                }
            }

        }


        loadTableView.items = orderList
    }
}