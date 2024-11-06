package pabulo.teste.front.controllers.state

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.OrderConnection
import pabulo.teste.front.connectionBackEnd.StateConnection
import pabulo.teste.front.dtos.state.SaveStateDTOtoDB
import pabulo.teste.front.dtos.state.StateSaveDto
import pabulo.teste.front.entity.Order
import pabulo.teste.front.entity.State
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.loadResource.LoadResource
import pabulo.teste.front.resource.orderResource.OrderResource
import pabulo.teste.front.resource.stateResource.StateResource
import pabulo.teste.front.scenesManager.state.OrderStateSave

class OrderStateSaveController {

    private lateinit var stateOrderSaveAPP: OrderStateSave

    fun setOrderSaveApp(stateOrderSaveApp: OrderStateSave) {

        this.stateOrderSaveAPP = stateOrderSaveApp


    }


    //-----------------------Botões do Menu Lateral-------------------------//

    @FXML

    private fun saveOrder() {

        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.SaveOrder)

    }


    @FXML

    private fun findOrder() {

        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }

    @FXML

    private fun createLoad() {

        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }

    @FXML

    private fun validateLoad() {
        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.ValidateLoad)


    }


    @FXML

    private fun saveOrderState() {

        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.SaveStateOrder)

    }

    @FXML

    private fun findOrderState() {

        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }

    @FXML

    private fun updateOrderState() {

        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)


    }

    @FXML

    private fun returnToMain() {

        stateOrderSaveAPP.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }


    //____________________Botões de Preenchimento_____________________________//


    @FXML

    private lateinit var stateOrdercodeField: TextField

    @FXML

    private lateinit var customerCodeField: TextField

    @FXML

    lateinit var customerNameField: TextField


    @FXML

    private lateinit var stateinit: ChoiceBox<String>


    @FXML

    private lateinit var stateNV1: ChoiceBox<String>

    @FXML

    private lateinit var stateNV2: ChoiceBox<String>


    @FXML

    private lateinit var driver: ChoiceBox<String>

    @FXML

    private lateinit var resolveDate: DatePicker

    @FXML

    private lateinit var description: TextField

    /*-----------------------------------------------View---Fields------------------------------------------------*/

    @FXML

    private lateinit var stateTableView: TableView<StateSaveDto>

    @FXML

    private lateinit var orderCodeColumn: TableColumn<StateSaveDto, Int>

    @FXML
    private lateinit var customerCodeColumn: TableColumn<StateSaveDto, Int>

    @FXML

    private lateinit var customerNameTableColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var purchaseDateColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var invoiceDateColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var stateInitColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var firstLevelColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var secondLevelColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var resolveStateColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var resolveDateColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var resolveDriverColumn: TableColumn<StateSaveDto, String>

    @FXML

    private lateinit var descriptionColumn: TableColumn<StateSaveDto, String>


    /*---------------------------------------------------Use---Vars-------------------------------------------------*/

    private val stateList: ObservableList<StateSaveDto> = FXCollections.observableArrayList()

    private val orderResource = OrderResource()

    private val stateResource = StateResource()

    private val stateConnection = StateConnection(GsonProvider.gson)

    private val orderConnection = OrderConnection(GsonProvider.gson)

    @FXML

    private lateinit var findOrderByCodeBtn: Button

    /*---------------------------------------------------Funcions-----------------------------------------------------*/

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var okButonDialog: Button

    @FXML

    private lateinit var stateSaveBt: Button


    fun handleOkButton() {

        dialogPane.isVisible = false

        stateSaveBt.isDisable = false

        findOrderByCodeBtn.isDisable = false

    }


    fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        stateSaveBt.isDisable = true

        findOrderByCodeBtn.isDisable = true

    }


    @FXML

    private fun findOrderByCode(orderCode: Int): Order? {

        var orderFounded: Order? = null

        try {

            val orderWeb = orderConnection.fetchOrderByCode(orderCode.toLong())

            orderFounded = orderWeb

            if (orderWeb != null) {

                customerCodeField.text = orderWeb.customerCode.toString()
                customerNameField.text = orderWeb.customerName

            }


        } catch (e: Exception) {

            println(e.message)

        }

        if (orderFounded == null) {


            val order = orderResource.findOrderByCode(orderCode)

            orderFounded = order

            if (order != null) {

                customerCodeField.text = order.customerCode.toString()
                customerNameField.text = order.customerName
            }
        }

        if (orderFounded == null) {

            showDialog("Não foi encontardo nenhum pedido salvo com o codigo $orderCode no banco de dados")

            return orderFounded

        }

        return orderFounded

    }

    private fun verifieStateAlreadExists(orderStateCode: Int) {

        var stateFounded: State? = null

        try {

            stateFounded = stateConnection.fetchByOrderStateCode(orderStateCode.toLong())


        } catch (e: Exception) {

            println(e.message)

        }

        if (stateFounded == null) {

            try {

                stateFounded = stateResource.findOrderStateByCode(orderStateCode)

            } catch (e: Exception) {

                println(e.message)

            }
        }


        if (stateFounded != null) {

            showDialog("A situação do pedido $orderStateCode já foi salva anteriormente,  tente um pedido diferente")

            stateOrdercodeField.clear()


        }


    }


    private fun defineActualStatus(state: String, solveDate: String): String {

        var actualStatus: String = " "

        if (state == "Sem Pendencias" || state == "Default") {

            actualStatus = "Sem Problemas"

        } else if (state == "Com Pendencias" || state == "Não Entregue" && solveDate.isNullOrBlank()) {

            actualStatus = "Pendente"

        } else if (state == "Com Pendencias" || state == "Não Entregue" && !solveDate.isNullOrBlank()) {

            actualStatus = "Resolvido"

        }

        return actualStatus

    }


    @FXML

    private fun saveStateOrder() {

        when {

            stateOrdercodeField.text.isNullOrBlank() -> {

                showDialog("O campo codigo do pedido está vazio, por favor digite um numero maior do que 0")

                return

            }


        }


        val orderCode: Int = stateOrdercodeField.text.trim().toInt()
        val order = findOrderByCode(orderCode)
        val customerCode: Int? = order?.customerCode
        val customerName: String? = order?.customerName
        val state: String = stateinit.value
        val stateNV1: String = stateNV1.value
        val stateNV2: String = stateNV2.value
        val description: String = description.text
        val invoiceDate: String = order?.invoiceDate.toString()
        val purchaseDate: String = order?.purchaseDate.toString()
        val solveDriver: String = driver.value
        val solveDate: String = resolveDate.value?.toString() ?: " "
        val resolve: String = defineActualStatus(state, solveDate)


        val newStateToView = StateSaveDto(
            orderCode,
            customerCode,
            customerName,
            state,
            stateNV1,
            stateNV2,
            description,
            invoiceDate,
            purchaseDate,
            solveDriver,
            solveDate,
            resolve
        )

        stateList.add(newStateToView)

        stateTableView.refresh()

        val newStateToDb = SaveStateDTOtoDB(
            orderCode,
            customerCode,
            customerName,
            state,
            stateNV1,
            stateNV2,
            description,
            invoiceDate,
            purchaseDate,
            solveDriver,
            solveDate,
            resolve
        )
        clearFields()

        stateResource.saveStateOnLocalDb(newStateToDb)

    }

    private fun clearFields() {

        stateOrdercodeField.clear()
        customerCodeField.clear()
        customerNameField.clear()
        stateinit.value = " "
        stateNV1.value = " "
        stateNV2.value = " "
        driver.value = " "
        resolveDate.value = null

    }

    @FXML

    fun initialize() {

        orderCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.orderCodeState.get()).asObject() }
        customerCodeColumn.setCellValueFactory { cellData -> SimpleIntegerProperty(cellData.value.customerCode!!).asObject() }
        customerNameTableColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.customerName) }
        purchaseDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.purchaseDate) }
        invoiceDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.invoiceDate) }
        stateInitColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.state.get()) }
        firstLevelColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.stateNV1.get()) }
        secondLevelColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.stateNV2.get()) }
        resolveStateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.resolve.get()) }
        resolveDateColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.solveDate?.get()) }
        resolveDriverColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.solveDriver?.get()) }
        descriptionColumn.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.description.get()) }




        stateinit.items = FXCollections.observableArrayList(

            "Sem Pendencias", "Com Pendencias", "Não Entregue", "Default"

        )
        stateinit.value = "Default"


        stateNV1.items = FXCollections.observableArrayList(
            "Endereço Informado Divergente",
            "Cliente Ou Responsavel Ausente",
            "Material Errado",
            "Material Com Avaria Parcial",
            "Material Totalmente Avariado",
            "Fora Do Horario De Entrega",
            "Não Conseguiu Contato Com Cliente",
            "Material Fora Da Data Solicitada Pelo Cliente",
            "Falta De Mercadoria",
            "Local Remoto",
            "Local De Dificil Acesso",
            "Erro De Venda",
            "Erro De Separação",
            "Erro De Separação e Venda",
            "Erro De Entrega",
            "Erro De Venda e Entrega",
            "Nota Perdida",
            "Sem Pendencia",
            "Nota Sem Assinatura",

            )

        stateNV1.value = "Sem Pendencia"


        stateNV2.items = FXCollections.observableArrayList(

            "Erro Na Separação Do Material",
            "Erro No Pedido",
            "Endereco Fornecido na Nota Errado",
            "Ausencia De Numero De Contato",
            "Numero De Contato Fornecido na Nota Errado",
            "Data Não Informada Na Nota",
            "Data Informada Nota",
            "Erro De Rota",
            "Não Aplicavel",
            "Erro De Venda e Separacao",
            "Erro De Entrega",
            "Avaria Não Identificada Na Separação",
            "Erro De Venda",
            "Nota Perdida Na entrega",
            "Furo De Estoque",
            "Sem Pendencia",
            "Nota Não Assinada",

            )

        stateNV2.value = "Sem Pendencia"


        val loadResource = LoadResource()


        stateOrdercodeField.focusedProperty().addListener { _, _, newValue ->


            if (!newValue) {

                if (stateOrdercodeField.text.isNullOrBlank()) {

                    showDialog("O campo codigo do pedido está vazio, por favor digite um numero maior do que 0")

                }

                try {

                    verifieStateAlreadExists(stateOrdercodeField.text.trim().toInt())

                }catch (e: NumberFormatException){

                    showDialog("Erro: Valor Invalido para o campo numero do pedido, por favor digite um numero maior do que 0")

                }


            }


        }

        driver.items = FXCollections.observableArrayList(loadResource.getDrivers())
        driver.value = "Default"


        findOrderByCodeBtn.text

        findOrderByCodeBtn.setOnAction {


            if (stateOrdercodeField.text.isNullOrBlank()) {

                showDialog("O campo codigo do pedido está vazio, por favor digite um numero maior do que 0")

            } else {

                try {

                    findOrderByCode(stateOrdercodeField.text.trim().toInt())

                    stateTableView.items = stateList

                } catch (e: NumberFormatException) {

                    showDialog("Erro: valor invaldo, por favor digite apenas numeros e maiores do que 0")

                }

            }

        }


    }


}