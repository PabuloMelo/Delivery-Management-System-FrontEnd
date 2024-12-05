package pabulo.teste.front.controllers.order

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.CustomerConnection
import pabulo.teste.front.connectionBackEnd.LoadConnection
import pabulo.teste.front.connectionBackEnd.OrderConnection
import pabulo.teste.front.dtoConverter.order.OrderUpdateToWeb
import pabulo.teste.front.dtos.orders.OrderUpdateDTO
import pabulo.teste.front.dtos.orders.OrderUpdateDTOtoDb
import pabulo.teste.front.entity.Order
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.addressResource.AddressResource
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.resource.sellerResource.SellerResource
import pabulo.teste.front.scenesManager.order.OrderUpdate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    private fun validateLoad() {
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

    @FXML

    private lateinit var orderAddress: ChoiceBox<String>

    @FXML

    private lateinit var orderAddressCodeField: TextField


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

    private lateinit var orderAddressColumn: TableColumn<Order, String>

    @FXML
    private lateinit var customerFindBtn: Button


    @FXML

    private lateinit var findOrderBT: Button

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane


    @FXML

    private lateinit var saveUpdateOrder: Button


    private val orderUpdateToWeb = OrderUpdateToWeb()
    private val orderResource = OrderResource()
    private val orderConnection = OrderConnection(GsonProvider.gson)
    private val customerConnection = CustomerConnection()
    private val addressConnection = AddressResource()
    private val loadConnection = LoadConnection(GsonProvider.gson)


    private val orderList: ObservableList<Order> = FXCollections.observableArrayList()

    private var orderHandle: Order? = null


    @FXML

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


    private fun findSellerNameByRca(sellerRca: Int): String {

        val sellerToFind = SellerResource()

        val seller = sellerToFind.findSellerInLocalDbByRca(sellerRca)

        val sellerName = seller?.sellerName ?: "Vendedor Não Cadastrado"

        return sellerName

    }


    @FXML
    private fun findOrderToUpdateByCode() {

        clearOrderView()

        if (orderCodeToFind.text.isNullOrBlank()) {

            showDialog("Campo numero do pedido está vazio por favor digite um numero maior do que 0")

            return
        }

        try {
            val orderCode: Int = orderCodeToFind.text.trim().toInt()

            orderHandle = null

            try {

                val orderDb = orderConnection.fetchOrderByCode(orderCode.toLong())

                orderHandle = orderDb

                orderDb?.let { orderList.add(it) }

            } catch (e: Exception) {

                println(e.message)


            }

            if (orderList.isEmpty()) {
                try {

                    val order = orderResource.findOrderByCode(orderCode)

                    orderHandle = order

                    order?.let { orderList.add(it) }

                } catch (e: Exception) {

                    println(e.message)

                }

                if (orderList.isEmpty()) {

                    showDialog("Não há nenhum pedido salvo com o codigo $orderCode")

                }

            }
        } catch (e: NumberFormatException) {


            showDialog("Digite somente numeros")

        }

        orderTableView.refresh()

        enableSaveButon()
    }

    @FXML
    private fun findCustomerNameByCode(customerCode: Int): String {

        val customerResource = CustomerResource()
        val customerLocal = customerResource.findCustomerByCodeInLocalDb(customerCode)
        val customerWeb = customerConnection.fetchCustomerOnWebDbByCode(customerCode.toLong())

        var customerFinded = " "

        if (customerLocal != null || customerWeb != null) {

            if (customerLocal != null && customerWeb == null) {

                customerNameField.text = customerLocal.customerName

                customerFinded = customerLocal.customerName

            } else if (customerLocal == null && customerWeb != null) {

                customerNameField.text = customerWeb.customerName

                customerFinded = customerWeb.customerName

            }

        } else {

            customerNameField.text = "Cliente Não Encontrado"

            customerFinded = "Cliente Não encontardo"

        }

        return customerFinded

    }

    private fun verifieNewOrderCodeExists(newOrderCode: Int) {

        val verifieOrderOnLocalDb = orderResource.findOrderByCode(newOrderCode)
        val verifieOrderOnWebDb = orderConnection.fetchOrderByCode(newOrderCode.toLong())

        val existsOrder = verifieOrderOnLocalDb != null || verifieOrderOnWebDb != null

        if (existsOrder) {

            showDialog("já existe um pedido com o codigo $newOrderCode salvo no banco de dados!!")
            orderCodeField.clear()

        }

    }

    private fun verifieNewCustomerExists(newCustomerCode: Int): Boolean {

        val customer = customerConnection.fetchCustomerOnWebDbByCode(newCustomerCode.toLong())

        return customer != null

    }


    private fun saveOrderUpdateOnWeblDb(originalOrdercode: Int, updateOrderDto: OrderUpdateDTOtoDb) {


        if (updateOrderDto.customerCode != null) {

            val customerFinded = verifieNewCustomerExists(updateOrderDto.customerCode!!)

            if (customerFinded) {

                try {


                    val orderUpdateWeb = orderUpdateToWeb.convertOrderUpdateLocalToWeb(updateOrderDto)

                    orderConnection.updateOrderOnWebDb(originalOrdercode.toLong(), orderUpdateWeb)

                    val orderOnDbView = orderConnection.fetchOrderByCode(orderUpdateWeb.orderCode!!)

                    clearOrderView()

                    orderList.add(orderOnDbView)

                    showDialog("pedido atualizado com sucesso no banco de dados Web")

                    orderTableView.refresh()

                } catch (e: Exception) {

                    println(e.message)

                }

            } else {

                showDialog(
                    "ERRO: não foi possivel atualizar o pedido no banco de dados web" +
                            " \n pois o cliente ${updateOrderDto.customerCode} não está salvo no banco de dados web"
                )

            }
        } else {

            try {


                val orderUpdateWeb = orderUpdateToWeb.convertOrderUpdateLocalToWeb(updateOrderDto)

                orderConnection.updateOrderOnWebDb(originalOrdercode.toLong(), orderUpdateWeb)

                val orderOnDbView = orderConnection.fetchOrderByCode(orderUpdateWeb.orderCode!!)

                clearOrderView()

                orderList.add(orderOnDbView)

                showDialog("pedido atualizado com sucesso no banco de dados Web")


                saveUpdateOrder.isDisable = true
                saveUpdateOrder.opacity = 0.5

                orderTableView.refresh()

                clearFields()

            } catch (e: Exception) {

                println(e.message)

            }


        }
    }


    private fun saveOrderUpdateOnLocalDb(originalOrdercode: Int, updateOrderDto: OrderUpdateDTOtoDb) {


        val orderUpdate = orderResource.updateOrder(originalOrdercode, updateOrderDto)

        if (orderUpdate != null) {


            val index = orderList.indexOfFirst { it.orderCode == originalOrdercode }

            if (index > 0) {

                orderList[index] = orderUpdate

                orderTableView.refresh()

            }

            clearOrderView()

            showDialog("pedido atualizado com sucesso no banco de dados local")

            saveUpdateOrder.isDisable = true
            saveUpdateOrder.opacity = 0.5

            orderList.add(orderUpdate)

            orderTableView.refresh()

            clearFields()

        }

    }

    private fun defineLoadByOrderType(orderType: String): Int {

        var loadCodeDef = 0

        loadCodeField.isEditable = true

        when (orderType) {
            "Retira Posterior" -> {

                loadCodeDef = 1

                loadCodeField.text = loadCodeDef.toString()

                loadCodeField.isEditable = false


            }

            "Entrega Futura" -> {

                loadCodeDef = 2

                loadCodeField.text = loadCodeDef.toString()

                loadCodeField.isEditable = false

            }

            "Retira Imediata" -> {

                loadCodeDef = 3

                loadCodeField.text = loadCodeDef.toString()

                loadCodeField.isEditable = false

            }


        }

        return loadCodeDef

    }


    private fun convertStringInToLocalDate(dateAtConverter: String): LocalDate {

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateAtConverter, dateFormatter)

        return date
    }


    @FXML

    private fun orderToUpdate() {


        when {

            loadCodeField.text.isNotBlank() && loadCodeField.text == "0" -> {


                showDialog("O novo codigo de carregamento não pode ser igual a 0 por favor digite um numero valido")

                return

            }

            loadCodeField.text.isNotBlank() && !loadCodeField.text.trim().all { it.isDigit() } -> {


                showDialog("Valor invalido para o novo codigo de carregamento, por favor digite apenas numeros maiores do que 0")

                return

            }

            orderCodeField.text.isNotBlank() && !orderCodeField.text.trim().all { it.isDigit() } -> {


                showDialog("Valor invalido para o novo codigo do pedido, por favor digite apenas numeros maiores do que 0")

                return

            }

            orderCodeField.text.isNotBlank() && orderCodeField.text.trim() == "O" -> {

                showDialog("O novo codigo do pedido não pode ser igual a 0 por favor digite um numero valido")

                return


            }


            customerCodeField.text.isNotBlank() && !customerCodeField.text.trim().all { it.isDigit() } -> {


                showDialog("Valor invalido para o novo codigo do cliente, por favor digite apenas numeros maiores do que 0")

                return

            }

            customerCodeField.text.isNotBlank() && customerCodeField.text.trim() == "0" -> {

                showDialog("O novo codigo do cliente não pode ser igual a 0 por favor digite um numero valido")

                return


            }

            customerCodeField.text.isNotBlank() -> {


                val customer = customerConnection.fetchCustomerOnWebDbByCode(customerCodeField.text.trim().toLong())

                if (customer == null) {


                    showDialog("Novo Cliente selecionado não se encontra salvo no banco de dados, por favor selecione outro ou salve-o primeiro")

                    return
                }

            }

            loadCodeField.text.isNotBlank() -> {


                val loadOnWebDb = loadConnection.fetchLoadByLoadCode(loadCodeField.text.toLong())

                if (loadOnWebDb) {

                    showDialog("O novo numero de carregamento selecionado não se encontra salvo no banco de dados, por favor selecione outro ou salve-o primeiro")
                    return
                }

            }

            orderHandle?.orderType == "ENTREGA FUTURA" && !orderStatus.value.isNullOrBlank() && orderStatus.value != "Pendente"
                    && invoiceDatePicker.value == null -> {

                showDialog("Pedidos do tipo entrega futura com status diferente de 'Pendente' precisam ter uma data de faturamento ")

                return
            }

            purchaseDatePicker.value != null && purchaseDatePicker.value > convertStringInToLocalDate(orderHandle!!.invoiceDate) && invoiceDatePicker.value == null -> {

                showDialog("A nova data de compra não deve ser maior do que a data de faturamento anterior")

                return
            }

            invoiceDatePicker.value != null && invoiceDatePicker.value > convertStringInToLocalDate(orderHandle!!.purchaseDate) && purchaseDatePicker.value == null -> {

                showDialog("A nova data de faturamento não deve ser maior do que a data de compra anterior")

                return

            }

            invoiceDatePicker.value == null && purchaseDatePicker.value == null -> {}


            invoiceDatePicker.value < purchaseDatePicker.value && invoiceDatePicker.value != null && purchaseDatePicker.value != null -> {

                showDialog("A nova data de faturamento deve ser maior ou igual a da compra")

                return

            }

        }
        val originalOrdercode: Int = orderCodeToFind.text.trim().toInt()
        val updateOrderDto = OrderUpdateDTOtoDb()
        val orderCodeToRca =
            if (orderCodeField.text.isNotBlank()) orderCodeField.text.trim().toInt() else originalOrdercode
        val orderRca = defineRca(orderCodeToRca.toString().toInt())
        val orderUpdateDTO = OrderUpdateDTO(

            orderCode = orderCodeField.text.takeIf { it.isNotBlank() }?.trim()?.toInt()
                ?.let { SimpleIntegerProperty(it) },
            customerCode = customerCodeField.text.takeIf { it.isNotBlank() }?.trim()?.toInt()
                ?.let { SimpleIntegerProperty(it) },
            customerName = customerNameField.text.takeIf { it.isNotBlank() }?.trim().let { SimpleStringProperty(it) },
            loadNumber = loadCodeField.text.takeIf { it.isNotBlank() }?.trim()?.toInt()
                ?.let { SimpleIntegerProperty(it) },
            orderType = orderType.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            status = orderStatus.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            invoicingDate = invoiceDatePicker.value?.toString()?.let { SimpleStringProperty(it) },
            purchaseDate = purchaseDatePicker.value?.toString()?.let { SimpleStringProperty(it) },
            sellerName = findSellerNameByRca(orderRca),
            sellerRCA = SimpleIntegerProperty(orderRca),
            orderAddress = orderAddress.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) }

        )
        println(invoiceDatePicker.value)

        if (orderCodeField.text.isNullOrEmpty() &&
            customerCodeField.text.isNullOrEmpty() &&
            loadCodeField.text.isNullOrEmpty() && orderType.value.isNullOrEmpty() && orderStatus.value.isNullOrEmpty() && invoiceDatePicker.value == null && purchaseDatePicker.value == null && orderAddress.value.isNullOrEmpty()
        ) {

            showDialog("Nenhum parametro fornecido para atualizar o pedido")

            return


        } else {

            updateOrderDto.convertDTOtoDB(orderUpdateDTO)


            val verifieOrderOnLocalDb = orderResource.findOrderByCode(originalOrdercode)
            val verifieOrderOnWebDb = orderConnection.fetchOrderByCode(originalOrdercode.toLong())

            if (verifieOrderOnLocalDb != null && verifieOrderOnWebDb == null) {

                saveOrderUpdateOnLocalDb(originalOrdercode, updateOrderDto)

            } else if (verifieOrderOnLocalDb == null && verifieOrderOnWebDb != null) {

                saveOrderUpdateOnWeblDb(originalOrdercode, updateOrderDto)

            }


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


    private fun enableSaveButon() {

        if (orderList.isEmpty()) {

            saveUpdateOrder.isDisable = true
            saveUpdateOrder.opacity = 0.5

        } else {
            saveUpdateOrder.isDisable = false
            saveUpdateOrder.opacity = 1.0
        }

    }


    @FXML


    fun initialize() {

        customerRegistered.items = FXCollections.observableArrayList(" SIM", "NAO", "Default")
        orderStatus.items = FXCollections.observableArrayList(
            "Default",
            "Em Rota De Entrega",
            "Pendente",
            "Entregue",
            "Cancelada",
            "Agendada"
        )



        orderType.items = FXCollections.observableArrayList(
            "Entrega",
            "Retira Posterior",
            "Carteira",
            "Carteira Entrega",
            "Carteita Retira Posterior",
            "Entrega Futura",
            "Retira Imediata"
        )



        customerCodeField.text

        customerCodeField.focusedProperty().addListener { _, _, newValue ->

            if (!newValue) {
                try {

                    val customerCode = customerCodeField.text.toInt()
                    verifieNewOrderCodeExists(customerCode)
                    findCustomerNameByCode(customerCode)

                } catch (e: NumberFormatException) {

                    showDialog("Erro: Valor Invalido para o campo numero do pedido")

                }
            }


        }

        orderCodeField.focusedProperty().addListener { _, _, newValue ->


            if (!newValue) {

                try {

                    val orderCode = orderCodeField.text.toInt()

                    verifieNewOrderCodeExists(orderCode)

                } catch (e: NumberFormatException) {

                    showDialog("Erro: Valor Invalido para o campo numero do pedido")

                }

            }

        }

        enableSaveButon()

        orderCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderCode).asObject() }
        customerCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.customerCode).asObject() }
        customerNameColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.customerName) }
        loadCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.loadCode).asObject() }
        orderStatusColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderStatus) }
        orderTypeColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderType) }
        sellerRcaColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderSellerRca).asObject() }
        invoiceDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.invoiceDate) }
        purchaseDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.purchaseDate) }
        orderAddressColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderAddress) }


        saveUpdateOrder.setOnAction { orderToUpdate() }

        orderType.setOnAction { defineLoadByOrderType(orderType.value) }


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


        findOrderBT.setOnAction { findOrderToUpdateByCode() }


        val addressConnection = AddressResource()

        val districts = addressConnection.getAddress()

        orderAddress.items = FXCollections.observableArrayList(districts)

        orderTableView.items = orderList


        dialogPane.isVisible = false


    }


}