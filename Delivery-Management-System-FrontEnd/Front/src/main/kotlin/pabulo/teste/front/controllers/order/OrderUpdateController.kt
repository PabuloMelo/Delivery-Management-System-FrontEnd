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
import pabulo.teste.front.connectionBackEnd.OrderConnection
import pabulo.teste.front.dtoConverter.order.OrderUpdateToWeb
import pabulo.teste.front.dtos.orders.OrderSaveDto
import pabulo.teste.front.dtos.orders.OrderUpdateDTO
import pabulo.teste.front.dtos.orders.OrderUpdateDTOtoDb
import pabulo.teste.front.entity.Order
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.addressResource.AddressResource
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

    private lateinit var orderAddressColumn: TableColumn<OrderSaveDto, String>

    @FXML
    private lateinit var customerFindBtn: Button

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var okButonDialog: Button

    @FXML

    private lateinit var saveUpdateOrder: Button


    private val orderUpdateToWeb = OrderUpdateToWeb()
    private val orderResource = OrderResource()
    private val orderConnection = OrderConnection(GsonProvider.gson)
    private val customerConnection = CustomerConnection()
    private val addressConnection = AddressResource()


    private val orderList: ObservableList<Order> = FXCollections.observableArrayList()


    @FXML

    fun handleOkButton() {

        dialogPane.isVisible = false

        saveUpdateOrder.isDisable = false

        saveUpdateOrder.opacity = 1.0
    }

    fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        saveUpdateOrder.isDisable = true

        saveUpdateOrder.opacity = 0.5

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

        if(orderCodeToFind.text.isNullOrBlank()){

            showDialog("Campo numero do pedido está vazio por favor digite um numero maior do que 0")

            return
        }

            try {
                val orderCode: Int = orderCodeToFind.text.trim().toInt()

                try {

                    val orderDb = orderConnection.fetchOrderByCode(orderCode.toLong())

                    orderDb?.let { orderList.add(it) }

                } catch (e: Exception) {

                    println(e.message)


                }

                if (orderList.isEmpty()) {
                    try {

                        val order = orderResource.findOrderByCode(orderCode)
                        order?.let { orderList.add(it) }

                    } catch (e: Exception) {

                        println(e.message)

                    }

                    if (orderList.isEmpty()){

                        showDialog("Não há nenhum pedido salvo com o codigo $orderCode")

                    }

                }
            }catch (e: NumberFormatException){


                showDialog("Digite somente numeros")

            }



        orderTableView.refresh()
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

            } else if (customerLocal != null && customerWeb != null) {

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

            if(customerFinded) {

                try {


                    val orderUpdateWeb = orderUpdateToWeb.convertOrderUpdateLocalToWeb(updateOrderDto)

                    orderConnection.updateOrderOnWebDb(originalOrdercode.toLong(), orderUpdateWeb)

                    val orderOnDbView = orderConnection.fetchOrderByCode(orderUpdateWeb.orderCode!!)

                    clearOrderView()

                    orderList.add(orderOnDbView)

                    showDialog("pedido atualizado com sucesso no banco de dados Web")

                    orderTableView.refresh()

                }catch (e: Exception){

                    println(e.message)

                }

            }else{

                showDialog("ERRO: não foi possivel atualizar o pedido no banco de dados web" +
                        " \n pois o cliente ${updateOrderDto.customerCode} não está salvo no banco de dados web")

            }
        }else{

            try {


                val orderUpdateWeb = orderUpdateToWeb.convertOrderUpdateLocalToWeb(updateOrderDto)

                orderConnection.updateOrderOnWebDb(originalOrdercode.toLong(), orderUpdateWeb)

                val orderOnDbView = orderConnection.fetchOrderByCode(orderUpdateWeb.orderCode!!)

                clearOrderView()

                orderList.add(orderOnDbView)

                showDialog("pedido atualizado com sucesso no banco de dados Web")

                orderTableView.refresh()

                clearFields()

            }catch (e: Exception){

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

            orderList.add(orderUpdate)

            orderTableView.refresh()

            clearFields()

        }

    }


    @FXML

    private fun orderToUpdate() {

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

        updateOrderDto.convertDTOtoDB(orderUpdateDTO)


        val verifieOrderOnLocalDb = orderResource.findOrderByCode(originalOrdercode)
        val verifieOrderOnWebDb = orderConnection.fetchOrderByCode(originalOrdercode.toLong())

        if (verifieOrderOnLocalDb != null && verifieOrderOnWebDb == null) {

            saveOrderUpdateOnLocalDb(originalOrdercode, updateOrderDto)

        } else if (verifieOrderOnLocalDb == null && verifieOrderOnWebDb != null) {

            saveOrderUpdateOnWeblDb(originalOrdercode, updateOrderDto)

        }else if (verifieOrderOnLocalDb != null && verifieOrderOnWebDb != null){

            saveOrderUpdateOnLocalDb(originalOrdercode, updateOrderDto)

            saveOrderUpdateOnWeblDb(originalOrdercode, updateOrderDto)

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


    private fun enableSaveButon(){

        if (orderList.isEmpty()) {

            saveUpdateOrder.isDisable = true
            saveUpdateOrder.opacity = 0.5

        }else {
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
            "Aguardando Solicitacao",
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
        orderAddressColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderAddress.get()) }


        saveUpdateOrder.setOnAction { orderToUpdate() }


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






        val addressConnection = AddressResource()

        val districts = addressConnection.getAddress()

        orderAddress.items = FXCollections.observableArrayList(districts)

        orderTableView.items = orderList


        dialogPane.isVisible = false


    }


}