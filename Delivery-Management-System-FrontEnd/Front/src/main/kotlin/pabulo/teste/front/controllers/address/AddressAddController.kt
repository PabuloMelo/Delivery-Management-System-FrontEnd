package pabulo.teste.front.controllers.address

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.dtos.address.SaveAddressDTO
import pabulo.teste.front.entity.Address
import pabulo.teste.front.enumms.AddressMenu
import pabulo.teste.front.resource.addressResource.AddressResource
import pabulo.teste.front.scenesManager.address.AddressAddScene

class AddressAddController {

    private lateinit var addressAddApp: AddressAddScene


    fun setMainAddressAPP(addressMenuApp: AddressAddScene) {

        this.addressAddApp = addressMenuApp

    }


    @FXML

    private fun saveAddress() {

        addressAddApp.menuAddress(AddressMenu.SAVE_ADDRESS)

    }

    @FXML

    private fun updateAddress() {

        addressAddApp.menuAddress(AddressMenu.UPDATE_ADDRESS)

    }

    @FXML

    private fun returnToInit() {


        addressAddApp.menuAddress(AddressMenu.RETURN)

    }

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var saveAddressBT: Button


    fun handleOkButton() {

        dialogPane.isVisible = false

        saveAddressBT.isDisable = false

    }


    private fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        saveAddressBT.isDisable = true


    }

    @FXML

    private lateinit var cityChoice: ChoiceBox<String>

    @FXML

    private lateinit var districtField: TextField

    @FXML

    private lateinit var addressTable: TableView<Address>

    @FXML

    private lateinit var cityColumn: TableColumn<Address, String>

    @FXML

    private lateinit var districtCodeColumn: TableColumn<Address, Int>

    @FXML
    private lateinit var districtNameColumn: TableColumn<Address, String>

    @FXML

    private var addressList: ObservableList<Address> = FXCollections.observableArrayList()

    private val addressResource = AddressResource()


    private fun addressExists (addressName: String): Boolean{

        val addressFind = addressResource.findAddressByName(addressName)

        return addressFind != null

    }

    @FXML

   private fun saveNewAddress(){

       clearTable()


       when {


           cityChoice.value.isNullOrBlank() -> {

               showDialog("Por Favor selecione uma cidade para o bairro")

               return

           }

           districtField.text.isNullOrBlank() -> {

               showDialog("O bairro precisa ter um nome")

               return

           }

           districtField.text.isNullOrBlank() && cityChoice.value.isNullOrBlank() -> {


               showDialog("Nenhum parametro foi fornecido para salvar um novo endereço")

               return

           }

       }

        val cityName: String = cityChoice.value
        val districtName: String = districtField.text.trim()

        val districtUni = addressExists(districtName)



        if (districtUni){

            showDialog("Já existe um bairro salvo com esse nome, por favor selecione outro")

            districtField.clear()

            return

        }else{


        val addresssToSave = SaveAddressDTO(districtName, cityName)

        val savedAddress = addressResource.saveNewAddressOnDb(addresssToSave)

        addressList.add(savedAddress)

        clearFields()

        }

   }

    private fun clearFields(){

        districtField.clear()
        cityChoice.value = " "

    }

    private fun clearTable(){

        addressList.clear()
        addressTable.refresh()


    }


    fun initialize() {

        cityColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.city) }
        districtCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.addressId).asObject() }
        districtNameColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.district) }



        saveAddressBT.text

        saveAddressBT.setOnAction { saveNewAddress() }

        cityChoice.items = FXCollections.observableArrayList("Canaã dos Carajás", "Parauapebas", "Marabá"," Xinguara", "Agua Azul do Norte")

        addressTable.items = addressList


    }


}