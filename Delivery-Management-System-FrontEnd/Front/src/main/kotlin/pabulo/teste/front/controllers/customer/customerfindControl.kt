package pabulo.teste.front.controllers.customer

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.connectionBackEnd.CustomerConnection
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.enumms.CustomerMenuEnumms
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.scenesManager.customer.CustomerFind

class CustomerFindControl {

    private lateinit var customerFindApp: CustomerFind


    fun setCustomerFindApp(customerFindApp: CustomerFind) {

        this.customerFindApp = customerFindApp


    }

    @FXML

    private fun saveMenu() {
        customerFindApp.customerMenuChoise(CustomerMenuEnumms.Save)

    }


    @FXML
    private fun findMenu() {

        customerFindApp.customerMenuChoise(CustomerMenuEnumms.Find)

    }

    @FXML
    private fun updateMenu() {
        customerFindApp.customerMenuChoise(CustomerMenuEnumms.Update)

    }


    @FXML

    private fun returnToInit() {
        customerFindApp.customerMenuChoise(CustomerMenuEnumms.ReturnToMenuInit)


    }
    /*-------------------------------------Customer--Find--Fields---------------------------------------------------*/

    @FXML
    private lateinit var findByCode: TextField

    @FXML

    private lateinit var findByName: TextField

    /*----------------------------------------TableView---------------------------------------------------------------*/

    @FXML

    private lateinit var customerFindTableView: TableView<Customer>

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

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane


    @FXML

    private var customerList: ObservableList<Customer> = FXCollections.observableArrayList()

    private val webDb = CustomerConnection()

    /*------------------------------------------------Functions------------------------------------------------------*/


    fun handleOkButton() {

        dialogPane.isVisible = false


    }


    private fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true


    }


    @FXML

    fun searchCustomerByCode() {

        clearTableView()

        //busca o cliente no db online

        if (findByCode.text.isNullOrBlank()) {

            showDialog("O campo do codigo do cliente esta vazio está vazio por favor digite um numero maior do que 0")

            return
        }

        try {


            val customerCode = findByCode.text


            try {

                val customerWeb = webDb.fetchCustomerOnWebDbByCode(customerCode.toLong())

                customerWeb?.let { customerList.add(it) }


            } catch (e: Exception) {

                println(e.message)

            }

            //se não encontrar busca no db local
            if (customerList.isEmpty()) {
                try {
                    val customerToFind = CustomerResource()

                    val customer = customerToFind.findCustomerByCodeInLocalDb(customerCode.toInt())

                    customer?.let { customerList.add(it) }


                } catch (e: NumberFormatException) {

                    showDialog("ERRO: Digite somente numeros e maiores que 0")

                    clearFields()

                }
            }

            if (customerList.isEmpty()) {

                showDialog("Não foi encontrado nenhum cliente com o codigo $customerCode")

            }

        } catch (e: NumberFormatException) {

            showDialog("ERRO: Digite somente numeros e maiores que 0")

            clearFields()

        }


    }


    @FXML

    fun searchCustomerByName() {

        clearTableView()


        if (findByName.text.isNullOrBlank()) {

            showDialog("O campo nome do cliente está vazio")

            return
        }

        try {


            val customerName: String = findByName.text.trim()

            //tenta buscar no serverWeb
            try {

                val customerWeb = webDb.fetchCustomerOnWebDbByName(customerName)

                customerWeb?.forEach { customer ->

                    customerList.add(customer)
                }


            } catch (e: Exception) {

                println(e.message)

            }

            //se não tenta buscar no web
            if (customerList.isEmpty()) {
                try {
                    val customerToFind = CustomerResource()

                    val customer = customerToFind.findCustomerByNameInLocalDb(customerName)

                    customer?.let { customerList.add(it) }


                } catch (e: Exception) {

                    showDialog("ERRO: Digite um nome valido")

                    clearFields()
                }
            }


            if (customerList.isEmpty()) {

                showDialog("Nenhum cliente foi encontrado com o nome $customerName")
            }

        } catch (e: Exception) {

            showDialog("ERRO: Digite um nome valido")

            clearFields()

        }


    }


    private fun clearFields() {
        findByCode.clear()
        findByName.clear()


    }

    private fun clearTableView() {
        customerList.clear()
    }


    fun initialize() {

        customerCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode).asObject() }
        customerNameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName) }
        customerPhoneColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.phone) }
        customerRegisteredColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerRegistered) }
        customerTypeColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerType) }


        customerFindTableView.items = customerList

    }


}