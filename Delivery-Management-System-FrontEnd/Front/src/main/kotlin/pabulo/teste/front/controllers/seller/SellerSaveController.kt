package pabulo.teste.front.controllers.seller

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.SellerConnection
import pabulo.teste.front.dtos.sellers.SaveSellerDto
import pabulo.teste.front.dtos.sellers.SaveSellerDtoToDb
import pabulo.teste.front.entity.Seller
import pabulo.teste.front.enumms.SellerMenuChoices
import pabulo.teste.front.resource.sellerResource.SellerResource
import pabulo.teste.front.scenesManager.seller.SellerSave
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class SellerSaveController {

    private lateinit var sellerSaveApp: SellerSave


    fun setSellerSaveApp(sellerSave: SellerSave) {

        this.sellerSaveApp = sellerSave

    }


    @FXML

    private fun saveSellerMenu() {

        sellerSaveApp.sellerMenuchoises(SellerMenuChoices.SalvarNovoVendedor)

    }


    @FXML
    private fun findSellerMenu() {

        sellerSaveApp.sellerMenuchoises(SellerMenuChoices.EncontrarVendedor)

    }

    @FXML

    private fun returnToInit() {

        sellerSaveApp.sellerMenuchoises(SellerMenuChoices.ReturnToMain)


    }


    /*---------------------------------------Fields-------------------------------------------------------------------*/

    @FXML

    private lateinit var sellerNameField: TextField

    @FXML

    private lateinit var sellerRcaField: TextField

    @FXML

    private lateinit var uploadImageBtn: Button


    private var sellerList: ObservableList<SaveSellerDto> = FXCollections.observableArrayList()


    /*----------------------------------------------views-----------------------------------------------------------*/


    @FXML

    private lateinit var imageSellerView: ImageView


    @FXML

    private lateinit var imageSellerChoice: ImageView

    @FXML

    private lateinit var tableSellerView: TableView<SaveSellerDto>

    @FXML

    private lateinit var sellerRcaColumn: TableColumn<SaveSellerDto, Int>

    @FXML

    private lateinit var sellerNameColumn: TableColumn<SaveSellerDto, String>

    private var selectedImageFile: File? = null

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

   private lateinit var saveSellerBT: Button


    private val sellerConnectionBackend = SellerConnection(GsonProvider.gson)

    private val sellerToDb = SellerResource()


    fun handleOkButton() {

        dialogPane.isVisible = false
        saveSellerBT.isDisable = false
        saveSellerBT.opacity = 1.0

    }


    private fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        saveSellerBT.isDisable = true

        saveSellerBT.opacity = 0.5


    }

    @FXML


    private fun handleUploadImage() {

        val fileChooser = FileChooser()

        fileChooser.title = "Select Image"
        fileChooser.extensionFilters.addAll(

            FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")

        )

        val selectFile: File? = fileChooser.showSaveDialog(uploadImageBtn.scene.window)

        selectFile?.let {

            val image = Image(it.toURI().toString())
            imageSellerChoice.image = image
            selectedImageFile = it

        }

    }


    private fun saveImageToSeller(file: File, sellerRca: Int, sellerName: String): String {

        return try {

            val targetDirectory =
                Path.of("C:/Users/USUARIO/Desktop/ProjectFront/Front/src/main/resources/pabulo/teste/front/sellersImage")

            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory)

            }


            val fileExtension = file.extension
            val newFileName = "${sellerRca}-${sellerName}.$fileExtension"


            val targetPath = targetDirectory.resolve(newFileName)

            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING)

            val imagePath = targetPath.toString()


            imagePath

        } catch (e: Exception) {

            e.printStackTrace()
            ""

        }


    }


    private fun verifieSellerExists() {

        try {

            val sellerRca = sellerRcaField.text.trim().toInt()
            var sellerFind: Seller? = null

            val verifieOnLocalDb = sellerToDb.findSellerInLocalDbByRca(sellerRca)


            try {

                sellerFind = sellerConnectionBackend.fetchSellerByRca(sellerRca.toLong())

                if (sellerFind != null) {

                    showDialog("Já existe Um vendedor salvo com o RCA $sellerRca")

                    sellerRcaField.clear()

                }

            } catch (e: Exception) {


                println(e.message)

            }

            if (sellerFind == null) {

                try {


                    sellerFind = sellerToDb.findSellerInLocalDbByRca(sellerRca)

                    if (sellerFind != null) {

                        showDialog("Já existe Um vendedor salvo com o RCA $sellerRca")

                        sellerRcaField.clear()


                    }

                } catch (e: Exception) {

                    println(e.message)

                }
            }


        } catch (e: NumberFormatException) {

            showDialog("Por favor digite um numero valido maior do que 0")

            sellerRcaField.clear()


        }


    }


    @FXML
    private fun saveSeller() {

        when {

            sellerRcaField.text.isNullOrBlank() -> {

                showDialog("O campo codigo do vendedor está vazio por favor digite um numero maior do que 0")

                return
            }

            sellerNameField.text.isNullOrBlank() -> {

                showDialog("O Vendedor precisa ter um Nome")

                return

            }
        }

        val sellerRca: Int = sellerRcaField.text.trim().toInt()
        val sellerName: String = sellerNameField.text.trim()
        var sellerPath = ""

        selectedImageFile?.let {

            val savedImagepath = saveImageToSeller(it, sellerRca, sellerName)
            sellerPath = savedImagepath


            val image = Image(File(savedImagepath).toURI().toString())

            imageSellerView.image = image


        }


        val newSellerSave = SaveSellerDto(sellerRca, sellerName, sellerPath)
        val saveSellerDtoToDb = SaveSellerDtoToDb(sellerRca, sellerName, sellerPath)

        sellerList.add(newSellerSave)

        tableSellerView.refresh()

        sellerToDb.saveSellerOnDb(saveSellerDtoToDb)

        clearFields()
        selectedImageFile = null

    }

    private fun clearFields() {

        sellerRcaField.clear()
        sellerNameField.clear()
        imageSellerChoice.image = null


    }


    fun initialize() {

        sellerRcaColumn.setCellValueFactory { it.value.sellerRca.asObject() }
        sellerNameColumn.setCellValueFactory { it.value.sellerName }


        sellerRcaField.focusedProperty().addListener { _, _, newValue ->

            if (!newValue){

                verifieSellerExists()

            }

        }


        uploadImageBtn.text

        uploadImageBtn.setOnAction { handleUploadImage() }

        tableSellerView.items = sellerList


    }


}