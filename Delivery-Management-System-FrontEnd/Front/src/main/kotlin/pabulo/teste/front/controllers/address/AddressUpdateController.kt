package pabulo.teste.front.controllers.address

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.dtos.address.UpdateAddressDTO
import pabulo.teste.front.dtos.address.UpdateAddressView
import pabulo.teste.front.entity.Address
import pabulo.teste.front.enumms.AddressMenu
import pabulo.teste.front.resource.addressResource.AddressResource
import pabulo.teste.front.scenesManager.address.AddressUpdateScene

class AddressUpdateController {

    private lateinit var addressUpdateApp: AddressUpdateScene


    fun setUpdateAddressAPP(addressMenuApp: AddressUpdateScene) {

        this.addressUpdateApp = addressMenuApp

    }

    @FXML

    private fun saveAddress() {

        addressUpdateApp.menuAddress(AddressMenu.SAVE_ADDRESS)

    }

    @FXML

    private fun updateAddress() {

        addressUpdateApp.menuAddress(AddressMenu.UPDATE_ADDRESS)

    }

    @FXML

    private fun returnToInit() {

        addressUpdateApp.menuAddress(AddressMenu.RETURN)

    }

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var updateAddressBT: Button


    @FXML

    private lateinit var districtCodeFind: TextField

    @FXML

    private lateinit var districtSavedFind: ChoiceBox<String>

    @FXML

    private lateinit var newCityAddress: ChoiceBox<String>

    @FXML

    private lateinit var newDistrictName: TextField

    @FXML

    private lateinit var searchBT: Button

    @FXML

    private lateinit var cleanFieldsBT: Button

    @FXML

    private lateinit var updateAddressTableView: TableView<Address>

    @FXML

    private lateinit var updatedCityColumn: TableColumn<Address, String>

    @FXML

    private lateinit var updatedDistrictCodeColumn: TableColumn<Address, Int>

    @FXML

    private lateinit var updatedDistrictNameColumn: TableColumn<Address, String>


    private val addressList: ObservableList<Address> = FXCollections.observableArrayList()

    private val addressConnection = AddressResource()

    private var addressFoundedHandler: Address? = null


    fun handleOkButton() {

        dialogPane.isVisible = false

        searchBT.isDisable = false

    }


    private fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        searchBT.isDisable = true


    }


    private fun findAddressByCode(addressCode: Int) {

        addressList.clear()

        val addressFounded = addressConnection.findAddressByDistrictCode(addressCode)

        addressFounded?.let {
            addressList.add(it)

            addressFoundedHandler = addressFounded

        }



        updateAddressTableView.refresh()

        if (addressList.isEmpty()) {

            showDialog("Nenhum endereço foi encontrado com o codigo: $addressCode ")

            districtCodeFind.clear()

        }


    }

    private fun findAddressByName(addressName: String) {

        addressList.clear()

        val addressFounded = addressConnection.findAddressByDistrictName(addressName)

        addressFounded?.let {
            addressList.add(it)

            addressFoundedHandler = addressFounded

        }

        updateAddressTableView.refresh()


    }

    @FXML
    private fun updateAddressFunction() {


        if (!addressList.isEmpty()) {

            when {


                newDistrictName.text.all { it.isDigit() } && !newDistrictName.text.isNullOrBlank() -> {

                    showDialog("O bairro precisa de um nome valido")

                    newDistrictName.clear()

                    return

                }

                newDistrictName.text.isNullOrBlank() && newCityAddress.value == null -> {


                    showDialog("Nenhum dado atualizado, por favor fornceça algum")

                    return
                }

                else -> {

                    addressToUpdate()

                }

            }

        } else {


            showDialog("Por favor clique em Pesquisar, para selecionar um bairro para atualizar primeiro")

            return

        }


    }

    @FXML
    private fun findAddressByUserParameters() {



        when {

            !districtCodeFind.text.isNullOrBlank() && districtSavedFind.value == null -> {

                if (districtCodeFind.text.trim()
                        .all { !it.isDigit() } || districtCodeFind.text <= 0.toString()
                ) {

                    showDialog("Erro: Digite apenas numeros Maiores do que 0")

                    return

                } else {

                    val addressFindCode: Int = districtCodeFind.text.trim().toInt()

                    findAddressByCode(addressFindCode)

                }

            }

            districtCodeFind.text.isNullOrBlank() && districtSavedFind.value != null -> {


                val addressName: String = districtSavedFind.value

                findAddressByName(addressName)

            }

            !districtCodeFind.text.isNullOrBlank() && districtSavedFind.value != null -> {


                showDialog("Por Favor Escolha apenas 1 parametro para busca de endereço")


                return

            }

            districtCodeFind.text.isNullOrBlank() && districtSavedFind.value == null -> {


                showDialog("Os campos para busca do endereço estão vazios, por favor escolha 1 parametro")

                return

            }


        }

        updateAddressBT.isDisable = addressList.isEmpty()

    }

    private fun addressExists(addressName: String): Boolean {

        val addressFind = addressConnection.findAddressByName(addressName)

        return addressFind != null

    }

    private fun addressToUpdate() {


        val addressUpdateDTO = UpdateAddressDTO()

        val addressOnUpdate = UpdateAddressView(

            district = newDistrictName.text.takeIf { it.isNotBlank() }?.trim()?.let { SimpleStringProperty(it) },
            city = newCityAddress.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) }

        )

        addressUpdateDTO.convertViewToDTO(addressOnUpdate)




        if (!newDistrictName.text.isNullOrBlank()) {

            val newDitrict = newDistrictName.text

            if (addressExists(newDitrict)) {

                showDialog("Já existe um bairro salvo com esse nome: $newDitrict , por favor selecione outro")

            } else {


                val addressUpdated =
                    addressFoundedHandler?.district?.let { addressConnection.updateAddress(addressUpdateDTO, it) }

                addressList.clear()

                addressList.add(addressUpdated)

                showDialog("Bairro: ${addressFoundedHandler?.district} atualizado com sucesso")

                clearFields()

               val newAddresslist = addressConnection.getAddress()

                districtSavedFind.items = FXCollections.observableArrayList(newAddresslist)

            }


        } else {

            val addressUpdated =
                addressFoundedHandler?.let { addressConnection.updateAddress(addressUpdateDTO, it.district) }

            addressList.clear()

            addressList.add(addressUpdated)

            showDialog("Bairro: ${addressFoundedHandler?.district} atualizado com sucesso")

        }

        updateAddressBT.isDisable = true


    }


    @FXML

    private fun clearFields() {

        districtSavedFind.value = null
        districtCodeFind.clear()
        newCityAddress.value = null
        newDistrictName.clear()

    }


    fun initialize() {


        updatedCityColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.city) }
        updatedDistrictCodeColumn.setCellValueFactory { cell -> SimpleIntegerProperty(cell.value.addressId).asObject() }
        updatedDistrictNameColumn.setCellValueFactory { cell -> SimpleStringProperty(cell.value.district) }


        searchBT.setOnAction { findAddressByUserParameters() }

        updateAddressBT.isDisable = true

        updateAddressBT.setOnAction { updateAddressFunction() }

        cleanFieldsBT.setOnAction { clearFields() }

        newCityAddress.items = FXCollections.observableArrayList(
            "Canaã dos Carajás",
            "Parauapebas",
            "Marabá",
            " Xinguara",
            "Agua Azul do Norte"
        )

        val addressListSaved = addressConnection.getAddress()

        districtSavedFind.items = FXCollections.observableArrayList(addressListSaved)

        updateAddressTableView.items = addressList

    }


}