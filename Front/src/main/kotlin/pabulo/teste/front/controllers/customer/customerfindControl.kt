package pabulo.teste.front.controllers.customer

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
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

    private var customerList: ObservableList<Customer> = FXCollections.observableArrayList()

    /*------------------------------------------------Functions------------------------------------------------------*/

    @FXML

    fun findCustomerByCode() {

        val customerCode: Int? = findByCode.text?.takeIf { it.isNotBlank() }?.trim()?.toIntOrNull()
        val customerToFind = CustomerResource()

        clearTableView()
        clearFields()
        val customer = customerToFind.findCustomerByCodeInLocalDb(customerCode!!)


        customer?.let { customerList.add(it) }


    }


    @FXML

    fun findCustomerByName() {

        val customerName: String? = findByName.text?.takeIf { it.trim().isNotBlank() }
        val customerToFind = CustomerResource()

        clearTableView()
        clearFields()
        val customer = customerToFind.findCustomerByNameInLocalDb(customerName!!)

        customer?.let { customerList.add(it) }


    }



    private fun clearFields(){
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