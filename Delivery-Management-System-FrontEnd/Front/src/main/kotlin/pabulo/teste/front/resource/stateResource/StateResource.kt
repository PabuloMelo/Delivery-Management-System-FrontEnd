package pabulo.teste.front.resource.stateResource

import pabulo.teste.front.dtos.state.SaveStateDTOtoDB
import pabulo.teste.front.dtos.state.StateUpdateDTOtoDB
import pabulo.teste.front.entity.State
import java.nio.file.Paths
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class StateResource {

    private val dbUrl: String = getDbUrl()

    private fun getDbUrl(): String {
        val resourceUrl = javaClass.getResource("/pabulo/teste/front/DB/deliverySystemDb.db")
            ?: throw IllegalArgumentException("Database file not found")
        return "jdbc:sqlite:${Paths.get(resourceUrl.toURI())}"
    }

    private val urlLocalDb =
      dbUrl


    fun saveStateOnLocalDb(stateSave: SaveStateDTOtoDB) {
        try {


            val connection = DriverManager.getConnection(urlLocalDb)
            val statement = connection.prepareStatement(
                "INSERT INTO situacao( order_number,customer_code, customer_name ,state ,first_level, second_level, " + "description, invoicing_date, purchase_date, solve_date, solve_driver, resolve) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"
            )

            statement.setInt(1, stateSave.orderCodeState)
            statement.setInt(2, stateSave.customerCode!!)
            statement.setString(3, stateSave.customerName)
            statement.setString(4, stateSave.state)
            statement.setString(5, stateSave.stateNV1)
            statement.setString(6, stateSave.stateNV2)
            statement.setString(7, stateSave.description)
            statement.setString(8, stateSave.invoiceDate)
            statement.setString(9, stateSave.purchaseDate)
            statement.setString(10, stateSave.solveDate)
            statement.setString(11, stateSave.solveDriver)
            statement.setString(12, stateSave.resolve)

            statement.executeUpdate()

            statement.close()

            connection.close()

        } catch (e: RuntimeException) {

            println("Erro ao salvar")


        }
    }


    fun findAllStateOnLocalDb(): List<State> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val query = "SELECT * FROM situacao"
        val statesList = mutableListOf<State>()

        val statement = connection.prepareStatement(query)

        val resultSet: ResultSet = statement.executeQuery()

        while (resultSet.next()) {

            val states = State(

                orderCodeState = resultSet.getInt("order_number"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                state = resultSet.getString("state"),
                stateNV1 = resultSet.getString("first_level"),
                stateNV2 = resultSet.getString("second_level"),
                description = resultSet.getString("description"),
                invoiceDate = resultSet.getString("invoicing_date"),
                purchaseDate = resultSet.getString("purchase_date"),
                solveDate = resultSet.getString("solve_date"),
                solveDriver = resultSet.getString("solve_driver"),
                resolve = resultSet.getString("resolve"),
                loadCode = 1,

            )

            statesList.add(states)

        }

        resultSet.close()
        statement.close()
        connection.close()

        return statesList

    }

    fun findOrderStateByCode(orderStateCode: Int): State? {

        val connection = DriverManager.getConnection(urlLocalDb)
        val query =


            "SELECT order_number,customer_code, customer_name ,state ,first_level, second_level, description, " +
                    "invoicing_date, purchase_date, solve_date, solve_driver, resolve FROM situacao WHERE order_number = ?"


        val statement = connection.prepareStatement(query)


        statement.setInt(1, orderStateCode)

        val resultSet = statement.executeQuery()

        val state: State? = if (resultSet.next()) {

            State(

                orderCodeState = resultSet.getInt("order_number"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                state = resultSet.getString("state"),
                stateNV1 = resultSet.getString("first_level"),
                stateNV2 = resultSet.getString("second_level"),
                description = resultSet.getString("description"),
                invoiceDate = resultSet.getString("invoicing_date"),
                purchaseDate = resultSet.getString("purchase_date"),
                solveDate = resultSet.getString("solve_date"),
                solveDriver = resultSet.getString("solve_driver"),
                resolve = resultSet.getString("resolve"),
                loadCode = 1,


            )
        } else {

            null

        }

        resultSet.close()
        statement.close()
        connection.close()

        return state

    }

    fun findOrderStateByUserParameters(
        orderStateCode: Int? = null,
        customerCode: Int? = null,
        stateInit: String? = null,
        stateNv1: String? = null,
        stateNv2: String? = null,
        resolve: String? = null,
        driver: String? = null,
        invoiceDateIni: String? = null,
        invoiceDateEnd: String? = null,
        purchaseDateInit: String? = null,
        purchaseDateEnd: String? = null
    ): List<State> {

        val statesList = mutableListOf<State>()

        val connection = DriverManager.getConnection(urlLocalDb)


        val sql = StringBuilder("SELECT * FROM situacao WHERE 1=1")
        val parameters = mutableListOf<Any?>()


        orderStateCode?.let {

            sql.append(" AND order_number = ?")
            parameters.add(it)

        }

        customerCode?.let {

            sql.append(" AND customer_code = ?")
            parameters.add(it)

        }


        stateInit?.let {

            sql.append(" AND state = ?")
            parameters.add(it)

        }


        stateNv1?.let {
            sql.append(" AND first_level = ?")
            parameters.add(it)

        }

        stateNv2?.let {
            sql.append(" AND second_level = ?")
            parameters.add(it)
        }


        resolve?.let {
            sql.append(" AND resolve = ?")
            parameters.add(it)

        }

        driver?.let {

            sql.append(" AND solve_driver = ?")
            parameters.add(it)

        }

        if (!invoiceDateIni.isNullOrEmpty() && !invoiceDateEnd.isNullOrEmpty()) {

            sql.append(" AND invoicing_date BETWEEN ? AND ?")
            parameters.add(invoiceDateIni)
            parameters.add(invoiceDateEnd)


        } else if (!invoiceDateIni.isNullOrEmpty()) {

            sql.append(" AND invoicing_date >= ?")
            parameters.add(invoiceDateIni)


        } else if (!invoiceDateEnd.isNullOrEmpty()) {

            sql.append(" AND invoicing_date <= ?")
            parameters.add(invoiceDateEnd)
        }

        if (!purchaseDateInit.isNullOrEmpty() && !purchaseDateEnd.isNullOrEmpty()) {

            sql.append(" AND purchase_date BETWEEN ? AND ?")
            parameters.add(purchaseDateInit)
            parameters.add(purchaseDateEnd)


        } else if (!purchaseDateInit.isNullOrEmpty()) {

            sql.append(" And purchase_date >= ?")
            parameters.add(purchaseDateInit)

        } else if (!purchaseDateEnd.isNullOrEmpty()) {
            sql.append(" And purchase_date <= ?")
            parameters.add(purchaseDateEnd)


        }


        val preparedStatement: PreparedStatement = connection.prepareStatement(sql.toString())

        for ((index, param) in parameters.withIndex()) {

            preparedStatement.setObject(index + 1, param)

        }

        val resultSet = preparedStatement.executeQuery()

        while (resultSet.next()) {

            val states = State(

                orderCodeState = resultSet.getInt("order_number"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                state = resultSet.getString("state"),
                stateNV1 = resultSet.getString("first_level"),
                stateNV2 = resultSet.getString("second_level"),
                description = resultSet.getString("description"),
                invoiceDate = resultSet.getString("invoicing_date"),
                purchaseDate = resultSet.getString("purchase_date"),
                solveDate = resultSet.getString("solve_date"),
                solveDriver = resultSet.getString("solve_driver"),
                resolve = resultSet.getString("resolve"),
                loadCode = 1,


            )
            statesList.add(states)

        }

        resultSet.close()
        preparedStatement.close()
        connection.close()

        return statesList
    }


    fun updateState(orderSateCode: Int, orderState: StateUpdateDTOtoDB): State? {

        val connection = DriverManager.getConnection(urlLocalDb)


        connection.use {

                conn ->

            val queryBuilder = StringBuilder("UPDATE situacao SET ")
            val parameters = mutableListOf<Any>()



            orderState.state?.let {

                queryBuilder.append("state = ?,")
                parameters.add(it)


            }

            orderState.stateNV1?.let {
                queryBuilder.append("first_level = ?,")
                parameters.add(it)

            }

            orderState.stateNV2?.let {
                queryBuilder.append("second_level = ?,")
                parameters.add(it)


            }

            orderState.description?.let {
                queryBuilder.append("description = ?,")
                parameters.add(it)

            }

            orderState.solveDate?.let {
                queryBuilder.append("solve_date = ?,")
                parameters.add(it)
            }

            orderState.solveDriver?.let {
                queryBuilder.append("solve_driver = ?,")
                parameters.add(it)


            }

            orderState.resolve?.let {
                queryBuilder.append("resolve = ?,")
                parameters.add(it)


            }



            if (parameters.isEmpty()) {

                println("Nenhuma Linha Atualizada")
                return null

            }
            queryBuilder.setLength(queryBuilder.length - 1)

            queryBuilder.append("WHERE order_number = ?")
            parameters.add(orderSateCode)


            val updateQuery = queryBuilder.toString()



            val updateStatement = conn.prepareStatement(updateQuery)


            parameters.forEachIndexed {

                    index, value ->
                updateStatement.setObject(index + 1, value)

            }

            val rowsUpdated = updateStatement.executeUpdate()

            if (rowsUpdated > 0) {

                val selectQuery = "SELECT * FROM situacao WHERE order_number = ?"

                val selectStatement = conn.prepareStatement(selectQuery)

                selectStatement.setInt(1, orderSateCode)

                val resultSet = selectStatement.executeQuery()

                val updateOrderState = if (resultSet.next()) {

                    State(

                        orderCodeState = resultSet.getInt("order_number"),
                        customerCode = resultSet.getInt("customer_code"),
                        customerName = resultSet.getString("customer_name"),
                        state = resultSet.getString("state"),
                        stateNV1 = resultSet.getString("first_level"),
                        stateNV2 = resultSet.getString("second_level"),
                        description = resultSet.getString("description"),
                        invoiceDate = resultSet.getString("invoicing_date"),
                        purchaseDate = resultSet.getString("purchase_date"),
                        solveDate = resultSet.getString("solve_date"),
                        solveDriver = resultSet.getString("solve_driver"),
                        resolve = resultSet.getString("resolve"),
                        loadCode = 1,


                    )


                } else {

                    null
                }

                resultSet.close()
                selectStatement.close()
                updateStatement.close()

                return updateOrderState

            } else {

                updateStatement.close()

                println("Erro")

                return null

            }


        }


    }

    fun statesListView(): List<State?> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val stateList = mutableListOf<State>()


        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM situacao")

        while (resultSet.next()) {

            val states = State(

                orderCodeState = resultSet.getInt("order_number"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                state = resultSet.getString("state"),
                stateNV1 = resultSet.getString("first_level"),
                stateNV2 = resultSet.getString("second_level"),
                description = resultSet.getString("description"),
                invoiceDate = resultSet.getString("invoicing_date"),
                purchaseDate = resultSet.getString("purchase_date"),
                solveDate = resultSet.getString("solve_date"),
                solveDriver = resultSet.getString("solve_driver"),
                resolve = resultSet.getString("resolve"),
                loadCode = 1,


            )

            stateList.add(states)


        }



        resultSet.close()
        statement.close()
        connection.close()


        return stateList
    }

    fun updateStateSync(orderStateCode: Int, sync: String) {

        val connection = DriverManager.getConnection(urlLocalDb)

        val query = "UPDATE situacao SET state_sync = ? WHERE order_number = ? "

        val statement = connection.prepareStatement(query)



        statement.setInt(2, orderStateCode)
        statement.setString(1, sync)

        println(statement)

        val rowsUpdate = statement.executeUpdate()



        if (rowsUpdate > 0){

            println("Syncronização bem sucedida")

        }else{

            println("Syncronização mal sucedida")

        }

        statement.close()
        connection.close()


    }


    fun deleteStateValidated() {

        val connection = DriverManager.getConnection(urlLocalDb)

        val parameter = "Sincronizado"

        val query = "DELETE FROM situacao WHERE state_sync = ?"

        val statement = connection.prepareStatement(query)

        statement.setString(1, parameter)

        val rowsUpdated = statement.executeUpdate()

        println(rowsUpdated)



        statement.close()

        connection.close()

    }



}