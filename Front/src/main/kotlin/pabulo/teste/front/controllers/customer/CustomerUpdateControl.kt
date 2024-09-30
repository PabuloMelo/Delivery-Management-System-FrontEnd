package pabulo.teste.front.controllers.customer

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import pabulo.teste.front.dtos.customer.CustomerUpdateDto
import pabulo.teste.front.dtos.customer.UpdateCustomerDtoToDb
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.enumms.CustomerMenuEnumms
import pabulo.teste.front.resource.customerResouce.CustomerResource
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

    /*------------------------------------------------Functions------------------------------------------------------*/

    @FXML

    fun findCustomerByCode() {

        val customerCode: Int = findByCode.text.toInt()
        val customerToFind = CustomerResource()

        clearFields()

        val customer = customerToFind.findCustomerByCodeInLocalDb(customerCode)

        customer?.let { customerList.add(it) }


    }

    @FXML
    fun updateCustomer() {


        val originalCustomerCode = findByCode.text.toInt()

        val updateDto = UpdateCustomerDtoToDb()
        val customerUpdateDto = CustomerUpdateDto(

            customerCode = if (customerCodeField.text.isNotBlank()) SimpleIntegerProperty(customerCodeField.text.trim().toInt()) else null,
            customerName = if (customerNameField.text.isNotBlank()) SimpleStringProperty(customerNameField.text.trim()) else null,
            phone = if (phoneField.text.isNotBlank()) SimpleStringProperty(phoneField.text.trim()) else null,
            customerType = customerTypes.value?.let { if (it.isNotBlank()) SimpleStringProperty(it) else null },
            customerRegistered = customerRegistered.value?.let { if (it.isNotBlank()) SimpleStringProperty(it) else null }

        )

        updateDto.convertDto(customerUpdateDto)

        val updatedCustomer = customerResource.updateCustomerOnLocalDb(originalCustomerCode, updateDto)

        if (updatedCustomer != null) {



            val index = customerList.indexOfFirst { it.customerCode == originalCustomerCode }

            if (index >= 0){

                customerList[index] = updatedCustomer

                customerUpdateTableView.refresh()


            }


        }else {

            println("Erro ao atualizar o cliente")

        }

        clearUpdateFields()

    }


    private fun clearFields() {
        customerList.clear()
    }

   private fun clearUpdateFields(){

        customerCodeField.clear()
        customerNameField.clear()
        phoneField.clear()





    }


    fun initialize() {

        customerTypes.items = FXCollections.observableArrayList("Normal", "Carteira")



        customerRegistered.items = FXCollections.observableArrayList("SIM", "NÃO")







        customerCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode).asObject() }
        customerNameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName) }
        customerPhoneColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.phone) }
        customerRegisteredColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerRegistered) }
        customerTypeColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerType) }


        customerUpdateTableView.items = customerList

    }


}