package pabulo.teste.front.controllers.state

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import pabulo.teste.front.dtos.state.StateUpdateDTO
import pabulo.teste.front.dtos.state.StateUpdateDTOtoDB
import pabulo.teste.front.entity.State
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.resource.stateResource.StateResource
import pabulo.teste.front.scenesManager.state.OrderStateUpdate

class OrderStateUpdateController {

    private lateinit var orderStateUpdateApp: OrderStateUpdate

    fun setOrderStateUpdateApp(orderStateUpdateApp: OrderStateUpdate) {

        this.orderStateUpdateApp = orderStateUpdateApp

    }

    //-----------------------Botões do Menu Lateral-------------------------//

    @FXML

    private fun saveOrder() {

        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.SaveOrder)

    }


    @FXML

    private fun findOrder() {

        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }

    @FXML

    private fun createLoad() {

        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }


    @FXML

    private fun validateLoad(){
        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.ValidateLoad)


    }


    @FXML

    private fun saveOrderState() {

        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.SaveStateOrder)

    }

    @FXML

    private fun findOrderState() {

        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }

    @FXML

    private fun updateOrderState() {

        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)


    }

    @FXML

    private fun returnToMain() {

        orderStateUpdateApp.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }
    //___________________________________________Botões de Preenchimento______________________________________________//


    @FXML

    private lateinit var stateinit: ChoiceBox<String>


    @FXML

    private lateinit var stateNV1: ChoiceBox<String>

    @FXML

    private lateinit var stateNV2: ChoiceBox<String>

    @FXML

    private lateinit var actualStatus: ChoiceBox<String>

    @FXML

    private lateinit var driver: ChoiceBox<String>

    @FXML

    private lateinit var stateOrdercodeField: TextField

    @FXML
    private lateinit var descriptionField: TextField


    @FXML

    private lateinit var resolveDatePicker: DatePicker
    /*--------------------------------------Views------------------------------------------------------------------------*/

    @FXML
    private lateinit var stateTableView: TableView<State>

    @FXML

    private lateinit var orderCodeColumn: TableColumn<State, Int>

    @FXML
    private lateinit var customerCodeColumn: TableColumn<State, Int>

    @FXML

    private lateinit var customerNameTableColumn: TableColumn<State, String>

    @FXML

    private lateinit var purchaseDateColumn: TableColumn<State, String>

    @FXML

    private lateinit var invoiceDateColumn: TableColumn<State, String>

    @FXML

    private lateinit var stateInitColumn: TableColumn<State, String>

    @FXML

    private lateinit var firstLevelColumn: TableColumn<State, String>

    @FXML

    private lateinit var secondLevelColumn: TableColumn<State, String>

    @FXML

    private lateinit var resolveStateColumn: TableColumn<State, String>

    @FXML

    private lateinit var resolveDateColumn: TableColumn<State, String>

    @FXML

    private lateinit var resolveDriverColumn: TableColumn<State, String>

    @FXML

    private lateinit var descriptionColumn: TableColumn<State, String>


    /*---------------------------------------------------Use---Vars-------------------------------------------------*/

    private val stateList: ObservableList<State> = FXCollections.observableArrayList()

    private val orderResource = OrderResource()

    private val stateResource = StateResource()

    @FXML

    private lateinit var findOrderByCodeBtn: Button

    @FXML
    private fun findOrderStateByCode() {

        val orderSateCode: Int = stateOrdercodeField.text.trim().toInt()

        val stateView = stateResource.findOrderStateByCode(orderSateCode)

        clearTable()


        stateView?.let { stateList.add(it) }


    }


    @FXML
    private fun updateOrderStateFunc() {

        val orderSateCode: Int = stateOrdercodeField.text.trim().toInt()

        val stateUpdateDTOtoDB = StateUpdateDTOtoDB()

        val orderStateToUpdate = StateUpdateDTO(

            state = stateinit.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            stateNV1 = stateNV1.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            stateNV2 = stateNV2.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            description = descriptionField.text?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            solveDriver = driver.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            solveDate = resolveDatePicker.value?.toString()?.let { SimpleStringProperty(it) },
            resolve = actualStatus.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },


            )

        stateUpdateDTOtoDB.covertDto(orderStateToUpdate)


        val stateUpdated = stateResource.updateState(orderSateCode, stateUpdateDTOtoDB)

        clearTable()
        clearFields()

        if (stateUpdated != null) {

            stateList.add(stateUpdated)

            stateTableView.refresh()

        }


    }


    private fun clearFields() {

        stateinit.value = " "
        stateNV1.value = " "
        stateNV2.value = " "
        actualStatus.value = " "
        driver.value = " "
        resolveDatePicker.value = null
        descriptionField.clear()


    }

    private fun clearTable() {

        stateList.clear()


    }

    @FXML

    fun initialize() {


        orderCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.orderCodeState).asObject() }
        customerCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode).asObject() }
        customerNameTableColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName) }
        purchaseDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.purchaseDate) }
        invoiceDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.invoiceDate) }
        stateInitColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.state) }
        firstLevelColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.stateNV1) }
        secondLevelColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.stateNV2) }
        resolveStateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.resolve) }
        resolveDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.solveDate) }
        resolveDriverColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.solveDriver) }
        descriptionColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.description) }







        stateinit.items = FXCollections.observableArrayList(


            "Sem Pendencias", "Com Pendencias", "Não Entregue", "Default"

        )
        stateinit.value = " "


        stateNV1.items = FXCollections.observableArrayList(
            "Endereco Informado Divergente",
            "Cliente Ou Responsavel Ausente",
            "Material Errado",
            "Material Com Avaria Parcial",
            "Material Totalmente Avariado",
            "Fora Do Horario De Entrega",
            "Nao Conseguiu Contato Com Cliente",
            "Maerial Fora Da Data Solicitada Pelo Cliente",
            "Falta De Mercadoria",
            "Local Remoto",
            "Local De Dificil Acesso",
            "Erro De Venda",
            "ErroDeSeparacao",
            "Erro De Separação e Venda",
            "Erro De Entrega",
            "Erro De Venda Entrega",
            "Nota Perdida",
            "Sem Pendencia",
            "Nota Sem  Assinatura",

            )

        stateNV1.value = " "


        stateNV2.items = FXCollections.observableArrayList(

            "Erro Na Separacao Do Material",
            "Erro No Pedido",
            "Endereco Da Nota Errado",
            "Ausencia De Numero De Contato",
            "Numero De Contato Errado",
            "Data Não Informada Na Nota",
            "Data Informada Nota",
            "Erro De Rota",
            "Nao Aplicavel",
            "Erro De Venda E Separação",
            "Erro De Entrega",
            "Avaria Nao Identificada Na Separação",
            "Erro De Venda",
            "Nota Perdida Na entrega",
            "Furo De Estoque",
            "Sem Pendencia",
            "Nota Nao Assinada",

            )

        stateNV2.value = " "

        actualStatus.items = FXCollections.observableArrayList("Pendente", "Resolvido", "Sem Problemas")
        actualStatus.value = " "

        val driverList = LoadResource()

        driver.items = FXCollections.observableArrayList(driverList.getDrivers())
        driver.value = " "

        stateTableView.items = stateList

        findOrderByCodeBtn.text

        findOrderByCodeBtn.setOnAction { findOrderStateByCode() }

    }


}