package pabulo.teste.front.controllers.driver

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import pabulo.teste.front.dtos.driver.SaveDriverDtoToDb
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.scenesManager.driver.DriverMenu
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class DriverController {

    private lateinit var driverMainApp: DriverMenu

    fun setDriverMenuApp(driverMainApp: DriverMenu) {
        this.driverMainApp = driverMainApp

    }


    @FXML

    private fun returnToMain() {

        driverMainApp.returnToMainMenu()

    }


    @FXML

    private lateinit var saveButton: Button


    @FXML

    private lateinit var driverNameField: TextField


    @FXML

    private lateinit var driverPhotoImportButton: Button

    @FXML

    private lateinit var driverImageView: ImageView


    @FXML

    private lateinit var sucessLabel: Label

    @FXML

    private lateinit var uploadImageBtn: Button

    private var selectedImageFile: File? = null

    private val loadResource = LoadResource()


    private fun handleUploadImage() {

        val fileChooser = FileChooser()

        fileChooser.title = "Select Image"
        fileChooser.extensionFilters.addAll(

            FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")

        )

        val selectFile: File? = fileChooser.showSaveDialog(uploadImageBtn.scene.window)

        selectFile?.let {

            val image = Image(it.toURI().toString())
            driverImageView.image = image
            selectedImageFile = it

        }

    }

    private fun saveImageToDriver(file: File, driverName: String): String {

        return try {

            val targetDirectory =
                Path.of("C:/Users/USUARIO/Desktop/ProjectFront/Front/src/main/resources/pabulo/teste/front/driversImages")

            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory)

            }


            val fileExtension = file.extension
            val newFileName = "${driverName}.$fileExtension"


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
    private fun saveDriver() {

        val driverName: String = driverNameField.text.trim()
        var driverPath = ""



        selectedImageFile?.let {

            val savedImagepath = saveImageToDriver(it, driverName)
            driverPath = savedImagepath


            val image = Image(File(savedImagepath).toURI().toString())

            driverImageView.image = image


        }


        val driverToDb = LoadResource()

        val saveDriverOnDb = SaveDriverDtoToDb(driverName, driverPath)


        driverToDb.saveANewDriverOnLocalDb(saveDriverOnDb)

        sucessLabel.text = "Motorista $driverName salvo com sucesso!"

        selectedImageFile = null

    }


    fun initialize() {

        uploadImageBtn.text

        uploadImageBtn.setOnAction { handleUploadImage() }

    }


}