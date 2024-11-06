package pabulo.teste.front.controllers.state

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import pabulo.teste.front.adapters.GsonProvider
import pabulo.teste.front.connectionBackEnd.StateConnection
import pabulo.teste.front.dtos.state.StateUpdateDTO
import pabulo.teste.front.dtos.state.StateUpdateDTOWebDB
import pabulo.teste.front.dtos.state.StateUpdateDTOtoDB
import pabulo.teste.front.dtos.state.StateUpdateDTOtoWebDB
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

    private fun validateLoad() {
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

    private lateinit var driver: ChoiceBox<String>

    @FXML

    private lateinit var stateOrdercodeField: TextField

    @FXML
    private lateinit var descriptionField: TextField


    @FXML

    private lateinit var resolveDatePicker: DatePicker

    @FXML
    private lateinit var resolveField: TextField

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

    private val stateConnection = StateConnection(GsonProvider.gson)

    @FXML

    private lateinit var findOrderByCodeBtn: Button

    private var orderStateNumberToDbWebUpdate: Long? = null

    @FXML

    private lateinit var dialogLabel: Label

    @FXML

    private lateinit var dialogPane: AnchorPane

    @FXML

    private lateinit var okButonDialog: Button

    @FXML

    private lateinit var saveUpdateBT: Button


    fun handleOkButton() {

        dialogPane.isVisible = false


        findOrderByCodeBtn.isDisable = false

    }


    fun showDialog(message: String) {

        dialogLabel.text = message

        dialogPane.isVisible = true

        findOrderByCodeBtn.isDisable = true


    }



    @FXML
    private fun findOrderStateByCode() {

        clearTable()

        stateinit.isDisable = false

        saveUpdateBT.isDisable = true

        if (stateOrdercodeField.text.isNullOrBlank()){

            showDialog("O campo codigo do pedido está vazio por favor digite um numero maior do que 0")

            return
        }

       try {

           val orderSateCode: Int = stateOrdercodeField.text.trim().toInt()

           try {

               val stateView = stateConnection.fetchByOrderStateCode(orderSateCode.toLong())

               orderStateNumberToDbWebUpdate = stateView?.orderCodeState?.toLong()

               stateView?.let { stateList.add(it) }

           } catch (e: Exception) {

               println(e.message)

           }


           if (stateList.isEmpty()) {


               val stateView = stateResource.findOrderStateByCode(orderSateCode)

               orderStateNumberToDbWebUpdate = stateView?.orderCodeState?.toLong()

               stateView?.let { stateList.add(it) }
           }

           if (stateList.isEmpty()){

               showDialog("Não foi encotrado nenhum pedido com o codigo $orderSateCode no banco de dados")

               stateinit.isDisable = true

               clearFields()

           }else{

               saveUpdateBT.isDisable = false

           }


       }catch (e: NumberFormatException){

           showDialog("Erro: Valor Invalido para o campo numero do pedido")

           stateinit.isDisable = true

           saveUpdateBT.isDisable = true

           clearFields()

       }


    }


    private fun defineActualStatus(state: String, solveDate: String?): String {
        return when {
            state == "Sem Pendencias" || state == "Default" -> "Sem Problemas"
            (state == "Com Pendencias" || state == "Não Entregue") && solveDate.isNullOrBlank() -> "Pendente"
            (state == "Com Pendencias" || state == "Não Entregue") && !solveDate.isNullOrBlank() -> "Resolvido"
            else -> "Sem Pendencias"
        }
    }

    private fun verifieOrigianlStateResolve(orderStateCode: Int): String {


        val verifierIfOrderStateUpdateExistsOnLocalDB = stateResource.findOrderStateByCode(orderStateCode)

        val verifierIfOrderStateUpdateExistsOnWebDB = stateConnection.fetchByOrderStateCode(orderStateCode.toLong())

        var stateResolve: String

        if (verifierIfOrderStateUpdateExistsOnWebDB != null && verifierIfOrderStateUpdateExistsOnLocalDB == null) {

            val state = stateConnection.fetchByOrderStateCode(orderStateCode.toLong())

            stateResolve = state!!.resolve

        } else if (verifierIfOrderStateUpdateExistsOnLocalDB != null && verifierIfOrderStateUpdateExistsOnWebDB == null) {

            val state = stateResource.findOrderStateByCode(orderStateCode)

            stateResolve = state!!.solveDate

        } else {
            val state = stateConnection.fetchByOrderStateCode(orderStateCode.toLong())

            stateResolve = state!!.resolve
        }

        return stateResolve

    }


    @FXML

    private fun defineResolve(orderStateCode: Int) {


        if (stateinit.value?.isNotBlank() == true || resolveDatePicker.value != null) {

            resolveField.text = defineActualStatus(stateinit.value, resolveDatePicker.value?.toString())

        } else if (stateinit.value?.isNotBlank() == true || resolveDatePicker.value == null) {

            resolveField.text = defineActualStatus(stateinit.value, resolveDatePicker.value?.toString())

        } else {

            resolveField.text = verifieOrigianlStateResolve(orderStateCode)

        }


    }


    @FXML
    private fun updateOrderStateFunc() {


        val orderStateCode: Int = stateOrdercodeField.text.trim().toInt()

        val verifierIfOrderStateUpdateExistsOnLocalDB = stateResource.findOrderStateByCode(orderStateCode)

        val verifierIfOrderStateUpdateExistsOnWebDB = stateConnection.fetchByOrderStateCode(orderStateCode.toLong())


        val stateUpdateDTOtoDB = StateUpdateDTOtoDB()

        val stateUpdateDTOtoWebDB = StateUpdateDTOtoWebDB()

// dto para atualização local
        val orderStateToUpdate = StateUpdateDTO(

            state = stateinit.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            stateNV1 = stateNV1.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            stateNV2 = stateNV2.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            description = descriptionField.text?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            solveDriver = driver.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
            solveDate = resolveDatePicker.value?.toString()?.let { SimpleStringProperty(it) },
            resolve = resolveField.text?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },

            )

        stateUpdateDTOtoDB.covertDto(orderStateToUpdate)


        if(!stateinit.value.isNullOrBlank() || !stateNV1.value.isNullOrBlank() || !stateNV2.value.isNullOrBlank() || !descriptionField.text.isNullOrBlank()
            || !driver.value.isNullOrBlank() || resolveDatePicker.value != null) {

// dto para atualização web

            val orderStateToUpdateWeb = StateUpdateDTOWebDB(


                orderNumber = orderStateNumberToDbWebUpdate,
                state = stateinit.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
                stateNV1 = stateNV1.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
                stateNV2 = stateNV2.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
                description = descriptionField.text?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
                solveDriver = driver.value?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },
                solveDate = resolveDatePicker.value?.toString()?.let { SimpleStringProperty(it) },
                resolve = resolveField.text?.takeIf { it.isNotBlank() }?.let { SimpleStringProperty(it) },

                )

            stateUpdateDTOtoWebDB.covertDto(orderStateToUpdateWeb)

            // verifica se o pedido existe apenas no banco de dados web para atualiza-lo

            if (verifierIfOrderStateUpdateExistsOnWebDB != null && verifierIfOrderStateUpdateExistsOnLocalDB == null) {

                try {

                    stateConnection.updateOrderStateOnWebDb(orderStateCode.toLong(), stateUpdateDTOtoWebDB)

                    val stateUpdated = stateConnection.fetchByOrderStateCode(orderStateCode.toLong())

                    clearTable()
                    clearFields()

                    if (stateUpdated != null) {

                        stateList.add(stateUpdated)

                        stateTableView.refresh()

                    }

                } catch (e: Exception) {

                    println(e.message)

                }

                // verifica se o pedido existe apenas no banco de dados local para atualiza-lo
            } else if (verifierIfOrderStateUpdateExistsOnWebDB == null && verifierIfOrderStateUpdateExistsOnLocalDB != null) {

                clearTable()
                clearFields()

                try {

                    val stateUpdated = stateResource.updateState(orderStateCode, stateUpdateDTOtoDB)
                    if (stateUpdated != null) {

                        stateList.add(stateUpdated)

                        stateTableView.refresh()

                    }
                } catch (e: Exception) {

                    println(e.message)
                }


                // verifica se o pedido existe em ambos bancos de dados  para atualiza-lo


            } else if (verifierIfOrderStateUpdateExistsOnWebDB != null && verifierIfOrderStateUpdateExistsOnLocalDB != null) {

                clearTable()
                clearFields()
                try {


                    val stateUpdated = stateResource.updateState(orderStateCode, stateUpdateDTOtoDB)



                    if (stateUpdated != null) {

                        stateList.add(stateUpdated)

                        stateTableView.refresh()

                    }
                } catch (e: Exception) {

                    println(e.message)
                }


                try {

                    stateConnection.updateOrderStateOnWebDb(orderStateCode.toLong(), stateUpdateDTOtoWebDB)

                    if (stateList.isEmpty()) {

                        val stateUpdatedview =
                            stateUpdateDTOtoWebDB.orderNumber?.let { stateConnection.fetchByOrderStateCode(it) }

                        stateList.add(stateUpdatedview)

                        stateTableView.refresh()
                    }

                } catch (e: Exception) {

                    println(e.message)

                }


            }

          showDialog("Situação do Pedido: $orderStateCode atualizada com sucesso")

            saveUpdateBT.isDisable = true


        }else{

            showDialog("Todos os parametros estavam vazios. Nenhum campo foi alterado na situação do pedido: $orderStateCode")


        }



    }


    private fun clearFields() {
        stateOrdercodeField.clear()
        stateinit.value = " "
        stateNV1.value = " "
        stateNV2.value = " "
        driver.value = " "
        resolveDatePicker.value = null
        descriptionField.clear()
        resolveField.clear()

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




        saveUpdateBT.isDisable = true


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


        stateinit.isDisable = true

        stateinit.setOnAction {

            if (stateOrdercodeField.text.isNullOrBlank()) {

                showDialog("O campo codigo do pedido está vazio, por favor digite um numero maior do 0 e clique em pesquisar primeiro")

                showDialog("Erro: Valor Invalido para o campo numero do pedido")

            }
            try {

                defineResolve(stateOrdercodeField.text.toInt())

            }catch (e: NumberFormatException){





            }

        }


        val driverList = LoadResource()

        driver.items = FXCollections.observableArrayList(driverList.getDrivers())
        driver.value = " "

        stateTableView.items = stateList

        findOrderByCodeBtn.text

        findOrderByCodeBtn.setOnAction { findOrderStateByCode() }

    }


}