package pabulo.teste.front.controllers.seller

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import pabulo.teste.front.dtos.sellers.SaveSellerDto
import pabulo.teste.front.dtos.sellers.SaveSellerDtoToDb
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


    @FXML
    private fun saveSeller() {

        val sellerRca: Int = sellerRcaField.text.trim().toInt()
        val sellerName: String = sellerNameField.text.trim()
        var sellerPath = ""

        selectedImageFile?.let {

            val savedImagepath = saveImageToSeller(it, sellerRca, sellerName)
            sellerPath = savedImagepath


            val image = Image(File(savedImagepath).toURI().toString())

            imageSellerView.image = image


        }


        val sellerToDb = SellerResource()
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

        uploadImageBtn.text

        uploadImageBtn.setOnAction { handleUploadImage() }

        tableSellerView.items = sellerList


    }


}