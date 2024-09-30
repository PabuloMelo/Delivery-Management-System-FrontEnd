package pabulo.teste.front.controllers.state

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import pabulo.teste.front.entity.State
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.stateResource.StateResource
import pabulo.teste.front.scenesManager.state.OrderStateFind

class OrderStateFindController {

    private lateinit var orderStateFind: OrderStateFind

    fun setOrderStateFind(orderStateFind: OrderStateFind) {

        this.orderStateFind = orderStateFind


    }
    //-----------------------Botões do Menu Lateral-------------------------//

    @FXML

    private fun saveOrder() {

        orderStateFind.orderMenuOption(OrderChoicesMenu.SaveOrder)

    }


    @FXML

    private fun findOrder() {

        orderStateFind.orderMenuOption(OrderChoicesMenu.FindOrder)

    }

    @FXML

    private fun updateOrder() {

        orderStateFind.orderMenuOption(OrderChoicesMenu.UpdateOrder)

    }

    @FXML

    private fun createLoad() {

        orderStateFind.orderMenuOption(OrderChoicesMenu.CreateALoad)

    }

    @FXML

    private fun validateLoad(){
        orderStateFind.orderMenuOption(OrderChoicesMenu.ValidateLoad)


    }

    @FXML

    private fun saveOrderState() {

        orderStateFind.orderMenuOption(OrderChoicesMenu.SaveStateOrder)

    }

    @FXML

    private fun findOrderState() {

        orderStateFind.orderMenuOption(OrderChoicesMenu.FindStateOrder)

    }

    @FXML

    private fun updateOrderState() {

        orderStateFind.orderMenuOption(OrderChoicesMenu.UpdateStateOrder)


    }

    @FXML

    private fun returnToMain() {

        orderStateFind.orderMenuOption(OrderChoicesMenu.ReturnToMenuInit)

    }

    /*------------------------------------------Fields----------------------------------------------------------------*/

    @FXML

    private lateinit var orderStateCodeField: TextField

    @FXML

    private lateinit var customerCodeField: TextField

    @FXML
    private lateinit var customerNameField: TextField

    @FXML
    private lateinit var invoiceDateInit: DatePicker

    @FXML

    private lateinit var invoiceDateEnd: DatePicker

    @FXML

    private lateinit var purchaseDateInit: DatePicker

    @FXML

    private lateinit var purchaseDateEnd: DatePicker


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


    //----------------------------------------------State Find View----------------------------------------------------//

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

    /*----------------------------------------------AUX----Vars-------------------------------------------------------*/

    private val stateList: ObservableList<State> = FXCollections.observableArrayList()


    private val stateResource = StateResource()

    @FXML

    private lateinit var findStateByCodeBtn: Button

    @FXML

    private lateinit var findOrdersStateByUserParameters: Button


    /*----------------------------------------------------------Functions-----------------------------------------------*/

    @FXML

    fun findOrderStateByCode() {
        clearTableView()
        val ordercode: Int = orderStateCodeField.text.trim().toInt()
        val state = stateResource.findOrderStateByCode(ordercode)

        state?.let { stateList.add(it) }

        clearFields()

    }


    private fun findOrderByUserParameters() {

        clearTableView()
        val orderCodeState: Int? = orderStateCodeField.text?.takeIf { it.isNotBlank() }?.toIntOrNull()
        val customerCode: Int? = customerCodeField.text?.takeIf { it.isNotBlank() }?.toIntOrNull()
        val stateInit: String? = stateinit.value?.takeIf { it.isNotBlank() }
        val stateNv1: String? = stateNV1.value?.takeIf { it.isNotBlank() }
        val stateNV2: String? = stateNV2.value?.takeIf { it.isNotBlank() }
        val resolve: String? = actualStatus.value?.takeIf { it.isNotBlank() }
        val stateDriver: String? = driver.value?.takeIf { it.isNotBlank() }
        val invoiceDateInitState: String? = invoiceDateInit.value?.toString()
        val invoiceDateEndtState: String? = invoiceDateEnd.value?.toString()
        val purchaseDateInit: String? = purchaseDateInit.value?.toString()
        val purchaseDateEnd: String? = purchaseDateEnd.value?.toString()

        val orderStateList: List<State> = stateResource.findOrderStateByUserParameters(
            orderCodeState,
            customerCode,
            stateInit,
            stateNv1,
            stateNV2,
            resolve,
            stateDriver,
            invoiceDateInitState,
            invoiceDateEndtState,
            purchaseDateInit,
            purchaseDateEnd
        )
        orderStateList.forEach { stateList.add(it) }



        stateTableView.refresh()



        clearFields()

    }


    private fun clearFields() {

        orderStateCodeField.clear()
        customerCodeField.clear()
        customerNameField.clear()
        stateinit.value = " "
        stateNV1.value = " "
        stateNV2.value = " "
        actualStatus.value = " "
        driver.value = " "
        invoiceDateInit.value = null
        invoiceDateEnd.value = null
        purchaseDateInit.value = null
        purchaseDateEnd.value = null

    }

    private fun clearTableView() {

        stateList.clear()


    }


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

        findOrdersStateByUserParameters.text

        findOrdersStateByUserParameters.setOnAction { findOrderByUserParameters() }

        findStateByCodeBtn.text

        findStateByCodeBtn.setOnAction { findOrderStateByCode() }

        stateTableView.items = stateList



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

        driver.items = FXCollections.observableArrayList("Default", "Elias", "Benedito", "Genildo")
        driver.value = " "


    }


}