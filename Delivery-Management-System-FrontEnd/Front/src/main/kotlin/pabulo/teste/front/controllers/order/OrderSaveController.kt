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
import pabulo.teste.front.dtos.orders.OrderSaveDto
import pabulo.teste.front.dtos.orders.SaverOrderDTOtoDb
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.addressResource.AddressResource
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.resource.sellerResource.SellerResource
import pabulo.teste.front.scenesManager.order.OrderSave

class OrderSaveController {

    private lateinit var orderSaveApp: OrderSave

    fun setOrderSaveApp(orderSaveApp: OrderSave) {

        this.orderSaveApp = orderSaveApp

    }

    //-----------------------Botões do Menu Lateral-------------------------//

    @FXML

    private fun saveOrder() {

        orderSaveApp.orderMenuOption(OrderChoicesMenu.SaveOrder)

    }


    @FXML

    private fun findOrder() {

        orderSaveApp.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        orderSaveApp.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }

    @FXML

    private fun createLoad() {

        orderSaveApp.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }

    @FXML

    private fun validateLoad() {
        orderSaveApp.orderMenuOption(OrderChoicesMenu.ValidateLoad)


    }

    @FXML

    private fun saveOrderState() {

        orderSaveApp.orderMenuOption(OrderChoicesMenu.SaveStateOrder)

    }

    @FXML

    private fun findOrderState() {

        orderSaveApp.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }

    @FXML

    private fun updateOrderState() {

        orderSaveApp.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)


    }

    @FXML

    private fun returnToMain() {

        orderSaveApp.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }

    //-----------------------Botões e Campos de Preenchimento----------------------------------------------------//


    @FXML

    private lateinit var customerRegistered: ChoiceBox<String>

    @FXML

    private lateinit var orderStatus: ChoiceBox<String>


    @FXML

    private lateinit var ordertype: ChoiceBox<String>

    /*---------------------------------------------OrderFields--------------------------------------------------------*/

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

    private lateinit var orderAddress: ChoiceBox<String>

    @FXML

    private lateinit var orderAddressCodeField: TextField


    /*-------------------------------------------------TableView----------------------------------------------------------*/

    @FXML
    private lateinit var orderTableView: TableView<OrderSaveDto>

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

    private lateinit var puchaseDateColumn: TableColumn<OrderSaveDto, String>

    @FXML

    private lateinit var invoiceDateColumn: TableColumn<OrderSaveDto, String>

    @FXML

    private lateinit var orderAddressColumn: TableColumn<OrderSaveDto, String>


    @FXML

    private lateinit var sellerRcaColumn: TableColumn<OrderSaveDto, Int>


    @FXML
    private lateinit var customerFindBtn: Button


    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var okButonDialog: Button

    @FXML

    private lateinit var saveOrderBT: Button


    private val orderResource = OrderResource()


    private val orderList: ObservableList<OrderSaveDto> = FXCollections.observableArrayList()


    private val webDb = CustomerConnection()


    private val orderConnection = OrderConnection(GsonProvider.gson)

    private val addressConnection = AddressResource()


    fun handleOkButton() {

        dialogPane.isVisible = false

        saveOrderBT.isDisable = false

        saveOrderBT.opacity = 1.0
    }


    fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        saveOrderBT.isDisable = true

        saveOrderBT.opacity = 0.5

    }


    private fun defineRca(orderCode: Int): Int {

        val orderToString = orderCode.toString()
        val firstTwoNumbers = orderToString.take(2)

        return firstTwoNumbers.toInt()


    }


    private fun defineLoadByOrderType(orderType: String): Int {

        val loadCodeDef: Int

        loadCodeField.isEditable = true

        if (orderType == "Retira Posterior") {

            loadCodeDef = 1

            loadCodeField.text = loadCodeDef.toString()

            loadCodeField.isEditable = false


        } else if (orderType == "Entrega Futura") {

            loadCodeDef = 2

            loadCodeField.text = loadCodeDef.toString()

            loadCodeField.isEditable = false

        } else if (orderType == "Retira Imediata") {

            loadCodeDef = 3

            loadCodeField.text = loadCodeDef.toString()

            loadCodeField.isEditable = false

        } else {

            loadCodeDef = loadCodeField.text.trim().toInt()

            loadCodeField.isEditable = true

            loadCodeField.text = "0"

        }

        return loadCodeDef

    }


    private fun findSellerNameByRca(sellerRca: Int): String {

        val sellerToFind = SellerResource()

        val seller = sellerToFind.findSellerInLocalDbByRca(sellerRca)

        val sellerName = seller?.sellerName ?: "Vendedor Não Cadastrado"

        return sellerName

    }


    @FXML
    private fun findCustomerNameByCode(): String {

        var customerFinded = " "


        if (customerCodeField.text.isNullOrBlank()) {


            showDialog("O campo do codigo do cliente está vazio por favor digite um numero maior do que 0")


        } else {

            try {


                val customerCode: Int = customerCodeField.text.toInt()

                try {

                    val customerWeb = webDb.fetchCustomerOnWebDbByCode(customerCode.toLong())

                    if (customerWeb != null) {

                        customerNameField.text = customerWeb.customerName

                        customerFinded = customerWeb.customerName
                    }

                } catch (e: Exception) {

                    println(e)
                }

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
            } catch (e: NumberFormatException) {

                showDialog("Por favor insira um numero valido")

            }
        }

        return customerFinded

    }


    /*--------------------------------------------------------------------------------------------------------------*/
    @FXML
    private fun saveOrderFuncion() {

        orderList.clear()


        when {
            orderCodeField.text.isNullOrBlank() -> {

                showDialog("O campo do codigo do pedido está vazio por favor digite um numero maior do que 0")

                return
            }

            customerCodeField.text.isNullOrBlank() -> {

                showDialog("O campo do codigo do cliente está vazio por favor digite um numero maior do que 0")

                return
            }

            purchaseDatePicker.value == null -> {

                showDialog("por favor digite a data de venda do pedido")

                return
            }


            ordertype.value != "Entrega Futura" && invoiceDatePicker.value == null -> {


                showDialog("É necesario informar uma data de faturamento para pedidos que não são do tipo entrega futura")

                return
            }

            invoiceDatePicker.value == null -> {}


            invoiceDatePicker.value < purchaseDatePicker.value && invoiceDatePicker.value != null -> {

                showDialog("A data de faturamento deve ser maior ou igual a da compra")

                return

            }
        }

        val orderCode: Int = orderCodeField.text.trim().toInt()
        val customerCode: Int = customerCodeField.text.trim().toInt()
        val sellerRca: Int = defineRca(orderCode)
        val customerName: String = findCustomerNameByCode()
        val sellerName: String = findSellerNameByRca(sellerRca)
        val ordertype: String = ordertype.value
        val loadNumber: Int = defineLoadByOrderType(ordertype)
        val orderAddress: String = orderAddress.value
        val orderStatus: String = orderStatus.value
        val puchaseDate: String = purchaseDatePicker.value?.toString() ?: " "
        val invoiceDate: String = invoiceDatePicker.value?.toString() ?: " "

        val newOrderToSave = OrderSaveDto(
            orderCode,
            customerCode,
            customerName,
            loadNumber,
            orderStatus,
            ordertype,
            puchaseDate,
            invoiceDate,
            sellerRca,
            sellerName,
            orderAddress
        )
        val orderSaveToDb = SaverOrderDTOtoDb(
            orderCode,
            customerCode,
            customerName,
            loadNumber,
            orderStatus,
            ordertype,
            puchaseDate,
            invoiceDate,
            sellerRca,
            sellerName,
            orderAddress
        )



        orderList.add(newOrderToSave)

        orderTableView.refresh()

        clearFields()

        orderResource.saveOrderOnLocalDb(orderSaveToDb)


    }


    private fun clearFields() {


        orderCodeField.clear()
        customerCodeField.clear()
        customerNameField.clear()
        loadCodeField.text = "0"
        orderStatus.value = "Default"
        ordertype.value = "Entrega"


    }

    private fun verifieNewOrderCodeExists(newOrderCode: Int) {

        val verifieOrderOnLocalDb = orderResource.findOrderByCode(newOrderCode)
        val verifieOrderOnWebDb = orderConnection.fetchOrderByCode(newOrderCode.toLong())

        val existsOrder = if (verifieOrderOnLocalDb != null || verifieOrderOnWebDb != null) {

            true

        } else {

            false

        }

        if (existsOrder == true) {

            showDialog("já existe um pedido com o codigo $newOrderCode salvo no banco de dados!!")
            orderCodeField.clear()

        }

    }


    @FXML


    fun initialize() {

        customerRegistered.items = FXCollections.observableArrayList(" SIM", "NAO", "Default")

        customerRegistered.value = "Default"

        orderStatus.items = FXCollections.observableArrayList(
            "Default",
            "Em Rota De Entrega",
            "Aguardando Solicitação",
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

        ordertype.value = "Entrega"

        loadCodeField.text = "0"

        ordertype.setOnAction { defineLoadByOrderType(ordertype.value) }


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


        orderCodeField.focusedProperty().addListener { _, _, newValue ->


            if (!newValue) {

                try {

                    val orderCode = orderCodeField.text.trim().toInt()

                    verifieNewOrderCodeExists(orderCode)

                } catch (e: NumberFormatException) {

                    showDialog("Erro: Valor Invalido para o campo numero do pedido")

                }

            }

        }




        orderCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderCode.get()).asObject() }
        customerCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.customerCode.get()).asObject() }
        customerNameColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.customerName.get()) }
        loadCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.loadNumber!!.get()).asObject() }
        orderStatusColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.status.get()) }
        orderTypeColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderType.get()) }
        sellerRcaColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.sellerRCA.get()).asObject() }
        invoiceDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.invoicingDate.get()) }
        puchaseDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.purchaseDate.get()) }
        orderAddressColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.orderAddress.get()) }


        val districts = addressConnection.getAddress()

        orderAddress.items = FXCollections.observableArrayList(districts)

        orderAddress.setOnAction { orderAddressCodeField.clear() }

        customerFindBtn.text

        customerFindBtn.setOnAction { findCustomerNameByCode() }

        orderTableView.items = orderList


    }

}