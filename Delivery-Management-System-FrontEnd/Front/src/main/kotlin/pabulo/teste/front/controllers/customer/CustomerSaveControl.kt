package pabulo.teste.front.controllers.customer

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.connectionBackEnd.CustomerConnection
import pabulo.teste.front.dtos.customer.SaveCustomerDto
import pabulo.teste.front.dtos.customer.SaveCustomerDtoToDb
import pabulo.teste.front.dtos.orders.OrderUpdateDTOtoDb
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.enumms.CustomerMenuEnumms
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.scenesManager.customer.CustomerSave

class CustomerSaveControl {

    private lateinit var customerSaveApp: CustomerSave


    fun setCustomerSaveApp(customerApp: CustomerSave) {

        this.customerSaveApp = customerApp

    }

    @FXML

    private fun saveCustomer() {

        customerSaveApp.customerMenuChoise(CustomerMenuEnumms.Save)

    }

    @FXML

    private fun findCustomer() {


        customerSaveApp.customerMenuChoise(CustomerMenuEnumms.Find)


    }

    @FXML
    private fun updateCustomer() {

        customerSaveApp.customerMenuChoise(CustomerMenuEnumms.Update)

    }


    @FXML
    private fun returnToInit() {

        customerSaveApp.customerMenuChoise(CustomerMenuEnumms.ReturnToMenuInit)

    }
    /*-------------------------------------Customer Fields ---------------------------------------------------------------------------*/


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

    /*-------------------------------------------Table View-------------------------------------------------------------*/

    @FXML

    private lateinit var customerTableView: TableView<SaveCustomerDto>

    @FXML

    private lateinit var customerCodeColumn: TableColumn<SaveCustomerDto, Int>


    @FXML

    private lateinit var customerNameColumn: TableColumn<SaveCustomerDto, String>

    @FXML

    private lateinit var customerPhoneColumn: TableColumn<SaveCustomerDto, String>

    @FXML

    private lateinit var customerTypeColumn: TableColumn<SaveCustomerDto, String>

    @FXML

    private lateinit var customerRegisteredColumn: TableColumn<SaveCustomerDto, String>


    /*-----------------------------------------Functions------------------------------------------------------------------*/

    private val customerList: ObservableList<SaveCustomerDto> = FXCollections.observableArrayList()


    private val orderResource = OrderResource()

    private lateinit var customerUpdate: SaveCustomerDtoToDb

    private val customerToDb = CustomerResource()

    private val customerConnection = CustomerConnection()

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var saveNewCustomerBT: Button


    fun handleOkButton() {

        dialogPane.isVisible = false

        saveNewCustomerBT.isDisable = false

    }


    private fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        saveNewCustomerBT.isDisable = true


    }

    private fun verifyCustomerExists() {

        try {


            val customerCodeToSearch = customerCodeField.text

            var customerFind: Customer? = null


            try {

                customerFind = customerConnection.fetchCustomerOnWebDbByCode(customerCodeToSearch.toLong())

                if (customerFind != null) {

                    showDialog("Já Existe um cliente com o codigo $customerCodeToSearch salvo no banco de dados")

                    customerCodeField.clear()

                }

            } catch (e: Exception) {

                println(e.message)

            }



            if (customerFind == null) {

                customerFind = customerToDb.findCustomerByCodeInLocalDb(customerCodeToSearch.toInt())

                if (customerFind != null) {

                    showDialog("Já Existe um cliente com o codigo $customerCodeToSearch salvo no banco de dados")

                    customerCodeField.clear()

                }

            }
        } catch (e: NumberFormatException) {


            showDialog("Por favor digite um numero valido maior do que 0")

            customerCodeField.clear()

        }


    }


    @FXML

    fun saveNewCustomer() {


        when{

            customerCodeField.text.isNullOrBlank() -> {

                showDialog("É necessario informar o codigo do cliente")

                return
            }

            customerNameField.text.isNullOrBlank() -> {

                showDialog("O cliente precisa de um nome")

                return
            }

        }


        val customerCode: Int = customerCodeField.text.trim().toInt()
        val customerName: String = customerNameField.text.trim()
        val phone: String = phoneField.text.trim()
        val customerType: String = customerTypes.value
        val customerRegistered: String = customerRegistered.value
        val newCustomerSave = SaveCustomerDto(customerCode, customerName, phone, customerType, customerRegistered)
        val saveCustomerToDb = SaveCustomerDtoToDb(customerCode, customerName, phone, customerType, customerRegistered)

        if (!customerCodeField.text.isNullOrBlank() || !customerNameField.text.isNullOrBlank()){

            customerList.add(newCustomerSave)
            customerUpdate = saveCustomerToDb
            customerTableView.refresh()

            customerToDb.saveCustomerToLocalDB(saveCustomerToDb)
            updateCustomerOnOrders()

            clearFields()

        }

    }


    private fun updateCustomerOnOrders() {

        val customer = customerUpdate
        val customerCode: Int = customer.customerCode
        val findALlOrderByCustomer = orderResource.findOrderByUserParameters(customerCode)

        if (findALlOrderByCustomer.isNotEmpty()) {


            findALlOrderByCustomer.forEach {

                val order = OrderUpdateDTOtoDb(it.orderCode, customer.customerCode, customer.customerName)

                orderResource.updateOrder(it.orderCode, order)
            }

        }
    }

    private fun clearFields() {
        customerCodeField.clear()
        customerNameField.clear()
        phoneField.clear()
        customerTypes.value = "Normal"
        customerRegistered.value = "SIM"
    }


    @FXML

    fun initialize() {

        customerTypes.items = FXCollections.observableArrayList("Normal", "Carteira")

        customerTypes.value = "Normal"

        customerRegistered.items = FXCollections.observableArrayList("SIM", "NÃO")

        customerRegistered.value = "SIM"

        customerCodeColumn.setCellValueFactory { it.value.customerCode.asObject() }
        customerNameColumn.setCellValueFactory { it.value.customerName }
        customerPhoneColumn.setCellValueFactory { it.value.phone }
        customerTypeColumn.setCellValueFactory { it.value.customerType }
        customerRegisteredColumn.setCellValueFactory { it.value.customerRegistered }


        customerCodeField.focusedProperty().addListener { _, _, newValue ->

            if (!newValue) {

                verifyCustomerExists()

            }

        }


        customerTableView.items = customerList


    }


}