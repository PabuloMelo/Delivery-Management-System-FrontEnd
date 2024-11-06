package pabulo.teste.front.controllers.state

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.CustomerConnection
import pabulo.teste.front.connectionBackEnd.StateConnection
import pabulo.teste.front.entity.Customer
import pabulo.teste.front.entity.State
import pabulo.teste.front.enumms.OrderChoicesMenu
import pabulo.teste.front.resource.customerResouce.CustomerResource
import pabulo.teste.front.resource.stateResource.StateResource
import pabulo.teste.front.scenesManager.state.OrderStateFind
import java.time.LocalDate

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

    private fun validateLoad() {
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


    private val stateConnection = StateConnection(GsonProvider.gson)

    private val customerConnection = CustomerConnection()

    private val customerResource = CustomerResource()


    /*----------------------------------------------------------Functions-----------------------------------------------*/

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var okButonDialog: Button


    fun handleOkButton() {

        dialogPane.isVisible = false

        findStateByCodeBtn.isDisable = false

    }


    fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        findStateByCodeBtn.isDisable = true

    }

    @FXML

    fun findOrderStateByCode() {
        clearTableView()

        if (orderStateCodeField.text.isNullOrBlank()) {

            showDialog("O campo codigo do pedido está vazio, por favor digite um numero maior do que 0")

            return

        }

        val ordercode: Int = orderStateCodeField.text.trim().toInt()

        try {

            val stateWeb = stateConnection.fetchByOrderStateCode(ordercode.toLong())

            stateWeb?.let { stateList.add(it) }

        } catch (e: Exception) {

        }


        if (stateList.isEmpty()) {

            try {


                val state = stateResource.findOrderStateByCode(ordercode)

                state?.let { stateList.add(it) }

                if (stateList.isEmpty()) {

                    showDialog("Não foi localizado nenhum pedido com o codigo $ordercode salvo no banco de dados")

                    return

                }


            } catch (e: NumberFormatException) {

                showDialog("Erro: Valor Invalido para o campo numero do pedido, por favor digite um numero maior do que 0")

                return
            }
        }
        clearFields()

    }


    private fun findOrdersByUserParametersOnLocalDb() {

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

    private fun findOrdersByUserParametersOnWebDb() {

        clearTableView()
        val orderCodeState: Long? = orderStateCodeField.text?.takeIf { it.isNotBlank() }?.toLongOrNull()
        val customerCode: Long? = customerCodeField.text?.takeIf { it.isNotBlank() }?.toLongOrNull()
        val customerName: String? = customerNameField.text?.takeIf { it.isNotBlank() }
        val stateInit: String? = stateinit.value?.takeIf { it.isNotBlank() }
        val stateNv1: String? = stateNV1.value?.takeIf { it.isNotBlank() }
        val stateNV2: String? = stateNV2.value?.takeIf { it.isNotBlank() }
        val resolve: String? = actualStatus.value?.takeIf { it.isNotBlank() }
        val stateDriver: String? = driver.value?.takeIf { it.isNotBlank() }
        val invoiceDateInitState: LocalDate? = invoiceDateInit.value
        val invoiceDateEndtState: LocalDate? = invoiceDateEnd.value
        val purchaseDateInit: LocalDate? = purchaseDateInit.value
        val purchaseDateEnd: LocalDate? = purchaseDateEnd.value


        val ordersList = stateConnection.fetchByUserParameter(
            orderCodeState,
            customerCode,
            customerName,
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


        ordersList?.forEach { stateList.add(it) }

        stateTableView.refresh()

        clearFields()

    }

    private fun searchOrders() {

        when {

            !orderStateCodeField.text.isNullOrBlank() && !orderStateCodeField.text.trim().all { it.isDigit() } -> {

                showDialog("Erro Valor invalido para o campo codigo do pedido, por favor digite apenas numeros e maiored do que 0")

                return
            }

            !customerCodeField.text.isNullOrBlank() && !customerCodeField.text.trim().all { it.isDigit() } -> {

                showDialog("Erro: Valor Invalido para o campo numero do cliente, por favor digite um numero maior do que 0")

                return

            }

            !customerNameField.text.isNullOrBlank() -> {

                val customerList: ObservableList <Customer> = FXCollections.observableArrayList()

                val customerName = customerNameField.text.trim()

                try {

                   val customerWeb = customerConnection.fetchCustomerOnWebDbByName(customerName)

                    customerWeb?.forEach { customerList.add(it)  }


                }catch (e: Exception){


                }

                if (customerList.isEmpty()){

                    try {

                        val customerLc = customerResource.findCustomerByNameInLocalDb(customerName)

                        customerLc?.let { customerList.add(it) }

                    }catch (e: Exception){


                    }

                }

                if(customerList.isEmpty()){

                    showDialog("Não há nenhum cliente salvo no banco de dados com o nome: $customerName")

                    customerList.clear()

                    return

                }

            }

            purchaseDateInit.value != null && purchaseDateEnd.value != null && purchaseDateInit.value > purchaseDateEnd.value -> {


                showDialog("a data de venda inicial não pode ser menor do que a data de venda final")

                return

            }

            invoiceDateInit.value != null && invoiceDateEnd.value != null && invoiceDateInit.value > invoiceDateEnd.value -> {

                showDialog("a data de faturamento inicial não pode ser maior do que a data de faturamento final")

                return

            }

            invoiceDateInit.value != null && purchaseDateInit.value != null && invoiceDateInit.value < purchaseDateInit.value -> {

                showDialog("A data de faturamento inicial não pode ser menor do que a data de venda inicial")

                return

            }

            invoiceDateEnd.value != null && purchaseDateInit.value != null && invoiceDateEnd.value < purchaseDateInit.value -> {

                showDialog("A data de faturamento final não pode ser menor do que a data de venda inicial")

                return

            }



        }


        if (!orderStateCodeField.text.isNullOrBlank() || !customerCodeField.text.isNullOrBlank() || !customerNameField.text.isNullOrBlank()
            || !stateinit.value.isNullOrBlank() || !stateNV1.value.isNullOrBlank() || !stateNV2.value.isNullOrBlank() || !actualStatus.value.isNullOrBlank()
            || !driver.value.isNullOrBlank() || purchaseDateInit.value != null || purchaseDateEnd.value != null || invoiceDateInit.value != null || invoiceDateEnd.value != null

        ) {


            clearTableView()

            try {

                findOrdersByUserParametersOnWebDb()

            } catch (e: Exception) {

                println(e.message)

            }

            if (stateList.isEmpty()) {

                try {

                    findOrdersByUserParametersOnLocalDb()

                } catch (e: Exception) {

                    println(e.message)

                }


            }

            if (stateList.isEmpty()){

                showDialog("Nenhum pedido foi encontrado com os parametros fornceidos")

                return

            }

        }else{

            showDialog("Nenhum parametro foi fornecido para busca do pedido por favor escolha um parametro")

            return

        }

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

        findOrdersStateByUserParameters.setOnAction { searchOrders() }

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