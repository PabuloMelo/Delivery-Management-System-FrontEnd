package pabulo.teste.front.controllers.order

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import pabulo.teste.front.dtos.orders.OrderSaveDto
import pabulo.teste.front.dtos.orders.SaverOrderDTOtoDb
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.resource.sellerResource.SellerResource
import pabulo.teste.front.scenesManager.order.OrderSave
import kotlin.math.ceil
import kotlin.properties.Delegates

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

    private fun validateLoad(){
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

    private lateinit var sellerRcaColumn: TableColumn<OrderSaveDto, Int>


    @FXML
    private lateinit var customerFindBtn: Button


    private val orderResource = OrderResource()


    private val orderList: ObservableList<OrderSaveDto> = FXCollections.observableArrayList()


    private fun defineRca(orderCode: Int): Int {

        val orderToString = orderCode.toString()
        val firstTwoNumbers = orderToString.take(2)

        return firstTwoNumbers.toInt()


    }


    private fun findSellerNameByRca(sellerRca: Int): String {

        val sellerToFind = SellerResource()

        val seller = sellerToFind.findSellerInLocalDbByRca(sellerRca)

        val sellerName = if (seller != null) {

            seller.sellerName

        } else {
            "Vendedor Não Cadastrado"

        }

        return sellerName

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

    /*--------------------------------------------------------------------------------------------------------------*/
    @FXML
    private fun SaveOrder() {


        val orderCode: Int = orderCodeField.text.trim().toInt()
        val customerCode: Int = customerCodeField.text.trim().toInt()
        val sellerRca: Int = defineRca(orderCode)
        val customerName: String = findCustomerNameByCode(customerCode)
        val sellerName: String = findSellerNameByRca(sellerRca)


        val loadNumber: Int = loadCodeField.text.trim().toInt()
        val ordertype: String = ordertype.value
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
            sellerName
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
            sellerName
        )


        orderList.add(newOrderToSave)

        orderTableView.refresh()

        clearFields()

        orderResource.saveOrderOnLocalDb(orderSaveToDb)


    }



    private fun clearFields(){


        orderCodeField.clear()
        customerCodeField.clear()
        customerNameField.clear()
        loadCodeField.clear()
        orderStatus.value = "Default"
        ordertype.value = "Entrega"


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


        orderCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.orderCode.get()).asObject() }
        customerCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.customerCode.get()).asObject() }
        customerNameColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.customerName.get()) }
        loadCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.loadNumber!!.get()).asObject() }
        orderStatusColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.status.get()) }
        orderTypeColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.orderType.get()) }
        sellerRcaColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.sellerRCA.get()).asObject() }
        invoiceDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.invoicingDate.get()) }
        puchaseDateColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.purchaseDate.get()) }


        customerFindBtn.text

        customerFindBtn.setOnAction { findCustomerNameByCode(customerCodeField.text.trim().toInt()) }

        orderTableView.items = orderList

    }

}