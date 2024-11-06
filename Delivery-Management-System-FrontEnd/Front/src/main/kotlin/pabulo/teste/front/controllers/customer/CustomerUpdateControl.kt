package pabulo.teste.front.controllers.customer

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.CustomerConnection
import pabulo.teste.front.connectionBackEnd.OrderConnection
import pabulo.teste.front.dtoConverter.customer.convertLocalUpdateCustomer
import pabulo.teste.front.dtos.customer.CustomerUpdateDto
import pabulo.teste.front.dtos.customer.UpdateCustomerDtoToDb
import pabulo.teste.front.dtos.orders.OrderUpdateDTO
import pabulo.teste.front.dtos.orders.OrderUpdateDTOtoDb
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.entity.Order
import pabulo.teste.front.enumms.CustomerMenuEnumms
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.scenesManager.customer.CustomerUpdate

class CustomerUpdateControl {

    private lateinit var customerUpdateApp: CustomerUpdate

    fun setUpdateCustomerApp(customerUpdate: CustomerUpdate) {

        this.customerUpdateApp = customerUpdate

    }


    @FXML

    private fun customerMenuSave() {

        customerUpdateApp.customerMenuChoise(CustomerMenuEnumms.Save)


    }

    @FXML

    private fun customerFindMenu() {

        customerUpdateApp.customerMenuChoise(CustomerMenuEnumms.Find)


    }


    @FXML

    private fun customerUpdateMenu() {

        customerUpdateApp.customerMenuChoise(CustomerMenuEnumms.Update)


    }

    @FXML


    private fun returnToMenuInit() {

        customerUpdateApp.customerMenuChoise(CustomerMenuEnumms.ReturnToMenuInit)

    }


    /*---------------------------------------------Customer--Update------------------------------------------------------*/


    @FXML
    private lateinit var findByCode: TextField


    @FXML
    private lateinit var customerCodeField: TextField

    @FXML

    private lateinit var customerNameField: TextField

    @FXML

    private lateinit var phoneField: TextField


    @FXML

    private lateinit var customerTypes: ChoiceBox<String>


    @FXML

    private lateinit var customerRegistered: ChoiceBox<String>


    /*----------------------------------------TableView---------------------------------------------------------------*/

    @FXML

    private lateinit var customerUpdateTableView: TableView<Customer>

    @FXML

    private lateinit var customerCodeColumn: TableColumn<Customer, Int>

    @FXML

    private lateinit var customerNameColumn: TableColumn<Customer, String>

    @FXML

    private lateinit var customerPhoneColumn: TableColumn<Customer, String>

    @FXML

    private lateinit var customerRegisteredColumn: TableColumn<Customer, String>

    @FXML

    private lateinit var customerTypeColumn: TableColumn<Customer, String>

    @FXML

    private var customerList: ObservableList<Customer> = FXCollections.observableArrayList()

    private val customerResource = CustomerResource()

    private val orderResource = OrderResource()

    private val customerConnection = CustomerConnection()

    private val orderConnection = OrderConnection(GsonProvider.gson)

    /*------------------------------------------------Functions------------------------------------------------------*/

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

    fun findCustomerByCode() {

        clearFields()

        if (findByCode.text.isNullOrBlank()) {

            showDialog("O campo do codigo do pedido está vazio por favor digite um numero maior do que 0")

            return
        }
        try {

            val customerCode: Int = findByCode.text.toInt()

            try {

                val customerWebDb = customerConnection.fetchCustomerOnWebDbByCode(customerCode.toLong())

                customerWebDb?.let { customerList.add(it) }

            } catch (e: Exception) {

                println(e.message)

            }


            if (customerList.isEmpty()) {

                try {

                    val customerToFind = CustomerResource()

                    clearFields()

                    val customer = customerToFind.findCustomerByCodeInLocalDb(customerCode)

                    customer?.let { customerList.add(it) }

                    if (customerList.isEmpty()) {


                        showDialog("Não foi encontrado nenhum cliente com o codigo informado")

                    }

                } catch (e: Exception) {

                    println(e.message)
                }
            }

        } catch (e: NumberFormatException) {

            showDialog("Erro: Valor Invalido para o campo numero do cliente")

        }
    }

    @FXML
    fun updateCustomer() {


        if (customerList.isEmpty()) {

            showDialog("É necessario informar um cliente para ser atualizado")

            return
        }


        when {

            customerCodeField.text.trim().isNullOrBlank() -> {

            }

            customerCodeField.text.trim().all { it.isDigit() } -> {


            }

            else -> {

                showDialog("Por favor digite somente numeros maiores do 0")

                return

            }

        }

        if (phoneField.text.trim().all { it.isDigit() }) {

        } else {

            showDialog("Por favor digite somente numeros maiores do 0")

            return

        }


        val originalCustomerCode = findByCode.text.toInt()

        val verifieCustomerLocal = customerResource.findCustomerByCodeInLocalDb(originalCustomerCode)
        val verifieCustomerWeb = customerConnection.fetchCustomerOnWebDbByCode(originalCustomerCode.toLong())

        val updateDto = UpdateCustomerDtoToDb()
        val customerUpdateDto = CustomerUpdateDto(

            customerCode = if (customerCodeField.text.isNotBlank()) SimpleIntegerProperty(
                customerCodeField.text.trim().toInt()
            ) else null,
            customerName = if (customerNameField.text.isNotBlank()) SimpleStringProperty(customerNameField.text.trim()) else null,
            phone = if (phoneField.text.isNotBlank()) SimpleStringProperty(phoneField.text.trim()) else null,
            customerType = customerTypes.value?.let { if (it.isNotBlank()) SimpleStringProperty(it) else null },
            customerRegistered = customerRegistered.value?.let { if (it.isNotBlank()) SimpleStringProperty(it) else null }

        )


        if (customerCodeField.text.isNotBlank() || customerNameField.text.isNotBlank() || phoneField.text.isNotBlank()
            || customerTypes.value != null || customerRegistered.value != null
        ) {


            updateDto.convertDto(customerUpdateDto)

            if (verifieCustomerWeb == null && verifieCustomerLocal != null) {

                val updatedCustomer = customerResource.updateCustomerOnLocalDb(originalCustomerCode, updateDto)

                if (updatedCustomer != null) {

                    showDialog("Cliente atualizado com sucesso")

                    val index = customerList.indexOfFirst { it.customerCode == originalCustomerCode }

                    if (index >= 0) {

                        customerList[index] = updatedCustomer

                        customerUpdateTableView.refresh()

                    }


                } else {

                    showDialog("Erro ao atualizar o cliente")

                }

            } else if (verifieCustomerLocal == null && verifieCustomerWeb != null) {

                val customerUpdateToWeb = convertLocalUpdateCustomer(updateDto)

                val customerUpdate =
                    customerConnection.updateCustomerOnDbWeb(originalCustomerCode.toLong(), customerUpdateToWeb)

                showDialog("$customerUpdate")

            } else if (verifieCustomerLocal != null && verifieCustomerWeb != null) {

                val updatedCustomer = customerResource.updateCustomerOnLocalDb(originalCustomerCode, updateDto)

                if (updatedCustomer != null) {

                    showDialog("Cliente atualizado com sucesso")


                    val index = customerList.indexOfFirst { it.customerCode == originalCustomerCode }

                    if (index >= 0) {

                        customerList[index] = updatedCustomer

                        customerUpdateTableView.refresh()

                    }


                } else {

                    showDialog("Erro ao atualizar o cliente")

                }

                val customerUpdateToWeb = convertLocalUpdateCustomer(updateDto)

                val customerUpdate =
                    customerConnection.updateCustomerOnDbWeb(originalCustomerCode.toLong(), customerUpdateToWeb)

                showDialog("$customerUpdate")


            }


            val ordersCustomerOnLocalDb = orderResource.findOrderByUserParameters(originalCustomerCode)

            val newCustomerCode = customerUpdateDto.customerCode?.get()
            val newCustomerName = customerUpdateDto.customerName?.get()


            if (newCustomerName != null || newCustomerCode != null) {
                ordersCustomerOnLocalDb.forEach {

                        orders ->

                    if (newCustomerCode != null && orders.customerCode != newCustomerCode && newCustomerName != null && orders.customerName != newCustomerName) {

                        val orderCustomerUpdate = OrderUpdateDTOtoDb(orders.orderCode, newCustomerCode, newCustomerName)

                        orderResource.updateOrder(orders.orderCode, orderCustomerUpdate)


                    } else if (newCustomerCode != null && orders.customerCode != newCustomerCode && newCustomerName == null) {

                        val orderCustomerUpdate = OrderUpdateDTOtoDb(orders.orderCode, newCustomerCode)

                        orderResource.updateOrder(orders.orderCode, orderCustomerUpdate)

                    } else if (newCustomerName != null && orders.customerName != newCustomerName && newCustomerCode == null) {


                        val orderCustomerUpdate =
                            OrderUpdateDTOtoDb(orders.orderCode, orders.customerCode, newCustomerName)

                        orderResource.updateOrder(orders.orderCode, orderCustomerUpdate)
                    }


                }
            }

        } else {

            showDialog("Nenhum parametro foi fornecido para a atualização do cliente")

            return
        }

        clearUpdateFields()

    }


    private fun clearFields() {
        customerList.clear()
    }

    private fun clearUpdateFields() {

        customerCodeField.clear()
        customerNameField.clear()
        phoneField.clear()


    }


    fun initialize() {

        customerTypes.items = FXCollections.observableArrayList("Normal", "Carteira")



        customerRegistered.items = FXCollections.observableArrayList("SIM", "NÃO")


        customerUpdateTableView.selectionModel.isCellSelectionEnabled = true

        customerUpdateTableView.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            if (event.isControlDown && event.code == KeyCode.C) {
                val selectedCells = customerUpdateTableView.selectionModel.selectedCells
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




        customerCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode).asObject() }
        customerNameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName) }
        customerPhoneColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.phone) }
        customerRegisteredColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerRegistered) }
        customerTypeColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerType) }


        customerUpdateTableView.items = customerList

    }


}