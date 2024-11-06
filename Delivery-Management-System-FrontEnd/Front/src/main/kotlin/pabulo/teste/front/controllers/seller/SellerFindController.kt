package pabulo.teste.front.controllers.seller

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
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

    private lateinit var sellerRcafield: TextField

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

    private fun findSellerByRca() {

        tableView()

        val sellerRca: Int = sellerRcafield.text.trim().toInt()
        val sellerFind = SellerResource()

        clearFields()

        val seller = sellerFind.findSellerInLocalDbByRca(sellerRca)


        seller?.let {
            sellerList.add(it)


            if (!it.sellerImagePath.isNullOrEmpty()) {

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


    @FXML

    private fun findSellerByName() {

        tableView()

        val sellerName: String = sellerNameField.text.trim()
        val sellerFind = SellerResource()

        clearFields()

        val seller = sellerFind.findSellerInLocalDbByName(sellerName)


        seller?.let {
            sellerList.add(it)


            if (!it.sellerImagePath.isNullOrEmpty()) {

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



    private fun clearFields() {

        sellerRcafield.clear()
        sellerNameField.clear()


    }

    private fun tableView(){


        sellerImageView.image = null
        sellerList.clear()




    }


    fun initialize() {


        sellerRcaColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.sellerRca).asObject() }
        sellerNameColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.sellerName) }


        sellerTableView.items = sellerList

    }


}