package pabulo.teste.front.controllers.seller

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.SellerConnection
import pabulo.teste.front.entity.Seller
import pabulo.teste.front.enumms.SellerMenuChoices
import pabulo.teste.front.resource.sellerResource.SellerResource
import pabulo.teste.front.scenesManager.seller.SellerFind
import java.io.File

class SellerFindController {

    private lateinit var sellerFindApp: SellerFind

    fun setSellerFindApp(sellerFind: SellerFind) {

        this.sellerFindApp = sellerFind

    }


    @FXML

    private fun saveSellerMenu() {

        sellerFindApp.sellerMenuchoises(SellerMenuChoices.SalvarNovoVendedor)

    }

    @FXML

    private fun findSellerMenu() {

        sellerFindApp.sellerMenuchoises(SellerMenuChoices.EncontrarVendedor)

    }


    @FXML

    private fun returnToInit() {

        sellerFindApp.sellerMenuchoises(SellerMenuChoices.ReturnToMain)

    }

    /*--------------------------------------Find---Fields------------------------------------------------------------*/

    @FXML

    private lateinit var sellerNameField: TextField

    @FXML

    private lateinit var sellerRcaField: TextField

    /*-----------------------------------------------Find--View-------------------------------------------------------*/

    @FXML

    private lateinit var sellerImageView: ImageView

    @FXML

    private lateinit var sellerTableView: TableView<Seller>


    @FXML

    private lateinit var sellerRcaColumn: TableColumn<Seller, Int>

    @FXML
    private lateinit var sellerNameColumn: TableColumn<Seller, String>


    @FXML

    private var sellerList: ObservableList<Seller> = FXCollections.observableArrayList()

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var searchSellerBT: Button


    private val sellerConnectionBackend = SellerConnection(GsonProvider.gson)

    private val sellerFind = SellerResource()


    fun handleOkButton() {

        dialogPane.isVisible = false
        searchSellerBT.isDisable = false
        searchSellerBT.opacity = 1.0

    }


    private fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        searchSellerBT.isDisable = true

        searchSellerBT.opacity = 0.5


    }


    @FXML

    private fun findSellerByRca(sellerRca: Int) {


        try {

            val seller = sellerConnectionBackend.fetchSellerByRca(sellerRca.toLong())

            seller?.let {

                sellerList.add(it)

            }

        } catch (e: Exception) {

            println(e.message)

        }

        if (sellerList.isEmpty()) {


            val seller = sellerFind.findSellerInLocalDbByRca(sellerRca)



            seller?.let {
                sellerList.add(it)


                if (it.sellerImagePath.isNotEmpty()) {

                    val imageFile = File(it.sellerImagePath)

                    if (imageFile.exists()) {

                        val image = Image(imageFile.toURI().toString())
                        sellerImageView.image = image

                    } else {
                        sellerImageView.image = null

                    }

                } else {

                    sellerImageView.image = null
                }


            }

        }

        if (sellerList.isEmpty()) {

            showDialog("Nenhum Vendedor encontrado com o codigo $sellerRca ")

        }


    }


    @FXML

    private fun findSellerByName(sellerName: String) {


        try {

            val seller = sellerConnectionBackend.fetchSellerByName(sellerName)

            seller?.let {
                sellerList.add(it)
            }

        } catch (e: Exception) {

            println(e.message)

        }

        if (sellerList.isEmpty()) {


            val seller = sellerFind.findSellerInLocalDbByName(sellerName)


            seller?.let {
                sellerList.add(it)


                if (it.sellerImagePath.isNotEmpty()) {

                    val imageFile = File(it.sellerImagePath)

                    if (imageFile.exists()) {

                        val image = Image(imageFile.toURI().toString())
                        sellerImageView.image = image

                    } else {
                        sellerImageView.image = null

                    }

                } else {

                    sellerImageView.image = null
                }


            }
        }

        if (sellerList.isEmpty()) {

            showDialog("Nenhum Vendedor encontrado com o nome $sellerName ")

        }

    }

    @FXML

    private fun searchSellerByUserParameters() {

        tableView()


        if (sellerNameField.text.isNullOrBlank() && sellerRcaField.text.isNullOrBlank()) {

            showDialog("Nenhum parametro forncecido para busca")

            return

        }else if(!sellerNameField.text.isNullOrBlank() && !sellerRcaField.text.isNullOrBlank()){

            showDialog("por favor forneça apenas 1 parametro")

            clearFields()

            return

        }


        if (!sellerRcaField.text.isNullOrBlank() && sellerNameField.text.isNullOrBlank()) {

            if (sellerRcaField.text.all { it.isDigit() }) {

                val sellerRca: Int = sellerRcaField.text.trim().toInt()

                findSellerByRca(sellerRca)

            } else {

                showDialog("Por favor digite um numero valido maior do que 0")

                clearFields()


            }

        } else if (sellerList.isEmpty() && !sellerNameField.text.isNullOrBlank() && sellerRcaField.text.isNullOrBlank()) {

            val sellerName: String = sellerNameField.text.trim().uppercase()

            findSellerByName(sellerName)

        }

    }


    private fun clearFields() {

        sellerRcaField.clear()
        sellerNameField.clear()


    }

    private fun tableView() {


        sellerImageView.image = null
        sellerList.clear()


    }


    fun initialize() {


        sellerRcaColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.sellerRca).asObject() }
        sellerNameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.sellerName) }

        sellerTableView.items = sellerList

    }


}