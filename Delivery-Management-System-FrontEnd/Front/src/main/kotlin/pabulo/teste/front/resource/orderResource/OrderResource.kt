package pabulo.teste.front.resource.orderResource

import pabulo.teste.front.dtos.orders.OrderUpdateDTOtoDb
import pabulo.teste.front.dtos.orders.SaverOrderDTOtoDb
import pabulo.teste.front.entity.Order
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class OrderResource {
    private val tempUrl =
        "C:/Users/USUARIO/Desktop/ProjectFront/Delivery-Management-System-FrontEnd/Front/src/main/resources/pabulo/teste/front/DB/deliverySystemDb.db"

    private val urlLocalDb =
        "jdbc:sqlite:$tempUrl"


    fun saveOrderOnLocalDb(orderSaverOrderDTOtoDb: SaverOrderDTOtoDb) {


        val connection = DriverManager.getConnection(urlLocalDb)


        val statement = connection.prepareStatement(

            "INSERT INTO pedido (order_code, customer_code, customer_name, load_number, order_type, status, purchase_date, invoicing_date,sellers_rca,seller_name, order_adress) VALUES(?,?,?,?,?,?,?,?,?,?,?)"


        )

        statement.setInt(1, orderSaverOrderDTOtoDb.orderCode)
        statement.setInt(2, orderSaverOrderDTOtoDb.customerCode)
        statement.setString(3, orderSaverOrderDTOtoDb.customerName)
        statement.setInt(4, orderSaverOrderDTOtoDb.loadNumber)
        statement.setString(5, orderSaverOrderDTOtoDb.orderType)
        statement.setString(6, orderSaverOrderDTOtoDb.status)
        statement.setString(7, orderSaverOrderDTOtoDb.purchaseDate)
        statement.setString(8, orderSaverOrderDTOtoDb.invoicingDate)
        statement.setInt(9, orderSaverOrderDTOtoDb.sellerRCA)
        statement.setString(10, orderSaverOrderDTOtoDb.sellerName)
        statement.setString(11, orderSaverOrderDTOtoDb.orderAddress)

        statement.executeUpdate()

        statement.close()

        connection.close()


    }

    fun updateLoadByCode(loadCode: Int, newLoadNumber: OrderUpdateDTOtoDb): List<Order>? {

        val connection = DriverManager.getConnection(urlLocalDb)

        connection.use {

                conn ->

            val query = (
                    "UPDATE pedido SET load_number = ? WHERE load_number = ?")
            val updateStatement = conn.prepareStatement(query)

            updateStatement.setInt(1, newLoadNumber.loadNumber!!)
            updateStatement.setInt(2, loadCode)


            val rowsUpdated = updateStatement.executeUpdate()

            if (rowsUpdated > 0) {

                val selectQuery = "SELECT * FROM pedido WHERE load_number = ?"
                val selectStatement = conn.prepareStatement(selectQuery)
                selectStatement.setInt(1, newLoadNumber.loadNumber!!)

                val resultSet = selectStatement.executeQuery()
                val updatedOrders = mutableListOf<Order>()

                while (resultSet.next()) {

                    val order = Order(
                        orderCode = resultSet.getInt("order_code"),
                        customerCode = resultSet.getInt("customer_code"),
                        customerName = resultSet.getString("customer_name"),
                        loadCode = resultSet.getInt("load_number"),
                        orderType = resultSet.getString("order_type"),
                        orderStatus = resultSet.getString("status"),
                        purchaseDate = resultSet.getString("purchase_date"),
                        invoiceDate = resultSet.getString("invoicing_date"),
                        orderSellerRca = resultSet.getInt("sellers_rca"),
                        sellerName = resultSet.getString("seller_name"),
                        orderAddress = resultSet.getString("order_adress")
                    )

                    updatedOrders.add(order)


                }
                resultSet.close()
                selectStatement.close()
                updateStatement.close()
                connection.close()

                return updatedOrders


            } else {

                updateStatement.close()
                connection.close()

                println("erro ao atualizar os carregamentos")

                return null

            }

        }

    }


    fun findALlOrdersByLoad(loadCode: Int): List<Order> {

        val connection = DriverManager.getConnection(urlLocalDb)

        val ordersList = mutableListOf<Order>()

        val query = "SELECT * FROM pedido WHERE load_number = ?"
        val statement = connection.prepareStatement(query)

        statement.setInt(1, loadCode)

        val resultSet: ResultSet = statement.executeQuery()

        while (resultSet.next()) {

            val orders = Order(

                orderCode = resultSet.getInt("order_code"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                loadCode = resultSet.getInt("load_number"),
                orderType = resultSet.getString("order_type"),
                orderStatus = resultSet.getString("status"),
                purchaseDate = resultSet.getString("purchase_date"),
                invoiceDate = resultSet.getString("invoicing_date"),
                orderSellerRca = resultSet.getInt("sellers_rca"),
                sellerName = resultSet.getString("seller_name"),
                orderAddress = resultSet.getString("order_adress")

            )

            ordersList.add(orders)


        }
        resultSet.close()
        statement.close()
        connection.close()

        return ordersList

    }

    fun findOrderByCode(orderCode: Int): Order? {

        val connection = DriverManager.getConnection(urlLocalDb)

        val query =
            "SELECT order_code, customer_code, customer_name, load_number, order_type, status, purchase_date, invoicing_date,sellers_rca,seller_name, order_adress FROM pedido WHERE order_code = ?"


        val statement = connection.prepareStatement(query)

        statement.setInt(1, orderCode)

        val resultSet: ResultSet = statement.executeQuery()

        val order: Order? = if (resultSet.next()) {

            Order(

                orderCode = resultSet.getInt("order_code"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                loadCode = resultSet.getInt("load_number"),
                orderType = resultSet.getString("order_type"),
                orderStatus = resultSet.getString("status"),
                purchaseDate = resultSet.getString("purchase_date"),
                invoiceDate = resultSet.getString("invoicing_date"),
                orderSellerRca = resultSet.getInt("sellers_rca"),
                sellerName = resultSet.getString("seller_name"),
                orderAddress = resultSet.getString("order_adress")

            )


        } else {

            null

        }


        resultSet.close()
        statement.close()
        connection.close()

        return order

    }


    fun findOrderByUserParameters(
        customerCode: Int? = null,
        customerName: String? = null,
        loadCode: Int? = null,
        orderType: String? = null,
        purchaseDateInit: String? = null,
        purchaseDateEnd: String? = null,
        invoiceDateInit: String? = null,
        invoiceDateEnd: String? = null,
    ): List<Order> {


        val orderslist = mutableListOf<Order>()

        val connection = DriverManager.getConnection(urlLocalDb)


        val sql = StringBuilder("SELECT * FROM pedido WHERE 1=1")
        val parameters = mutableListOf<Any?>()



        customerCode?.let {

            sql.append(" AND customer_code = ?")
            parameters.add(it)

        }

        customerName?.let {

            sql.append(" AND customer_name = ?")
            parameters.add(it)

        }

        loadCode?.let {
            sql.append(" AND load_number = ?")
            parameters.add(it)

        }


        orderType?.let {

            sql.append(" AND order_type = ?")
            parameters.add(it)

        }

        if (!purchaseDateInit.isNullOrEmpty() && !purchaseDateEnd.isNullOrEmpty()) {

            sql.append(" AND purchase_date BETWEEN ? AND ?")
            parameters.add(purchaseDateInit)
            parameters.add(purchaseDateEnd)
        } else if (!invoiceDateInit.isNullOrEmpty()) {

            sql.append(" AND purchase_date >= ?")
            parameters.add(purchaseDateInit)

        } else if (!purchaseDateEnd.isNullOrEmpty()) {

            sql.append(" AND purchase_date <= ?")
            parameters.add(purchaseDateEnd)

        }


        if (!invoiceDateInit.isNullOrEmpty() && !invoiceDateEnd.isNullOrEmpty()) {

            sql.append(" AND invoicing_date BETWEEN ? AND ?")
            parameters.add(invoiceDateInit)
            parameters.add(invoiceDateEnd)

        } else if (!invoiceDateInit.isNullOrEmpty()) {

            sql.append(" AND invoicing_date >= ?")
            parameters.add(invoiceDateInit)

        } else if (!invoiceDateEnd.isNullOrEmpty()) {

            sql.append(" AND invoicing_date <= ?")
            parameters.add(invoiceDateEnd)

        }


        val preparedStatement: PreparedStatement = connection.prepareStatement(sql.toString())

        for ((index, param) in parameters.withIndex()) {

            preparedStatement.setObject(index + 1, param)

        }

        val resultSet = preparedStatement.executeQuery()


        while (resultSet.next()) {

            val orders = Order(


                orderCode = resultSet.getInt("order_code"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                loadCode = resultSet.getInt("load_number"),
                orderType = resultSet.getString("order_type"),
                orderStatus = resultSet.getString("status"),
                purchaseDate = resultSet.getString("purchase_date"),
                invoiceDate = resultSet.getString("invoicing_date"),
                orderSellerRca = resultSet.getInt("sellers_rca"),
                sellerName = resultSet.getString("seller_name"),
                orderAddress = resultSet.getString("order_adress")

            )

            orderslist.add(orders)

        }

        resultSet.close()
        preparedStatement.close()
        connection.close()


        return orderslist

    }


    fun updateOrder(originalCode: Int, orderUpdateDTO: OrderUpdateDTOtoDb): Order? {

        val connection = DriverManager.getConnection(urlLocalDb)

        connection.use {

                conn ->

            val queryBuilder = StringBuilder("UPDATE pedido SET ")
            val parameters = mutableListOf<Any>()

            orderUpdateDTO.orderCode?.let {
                queryBuilder.append("order_code = ?,")
                parameters.add(it)
            }

            orderUpdateDTO.customerCode?.let {

                queryBuilder.append("customer_code = ?,")
                parameters.add(it)

            }

            orderUpdateDTO.customerName?.let {

                queryBuilder.append("customer_name = ?,")
                parameters.add(it)

            }

            orderUpdateDTO.loadNumber?.let {

                queryBuilder.append("load_number = ?,")
                parameters.add(it)

            }

            orderUpdateDTO.orderType?.let {

                queryBuilder.append("order_type = ?,")
                parameters.add(it)
            }

            orderUpdateDTO.status?.let {
                queryBuilder.append("status = ?,")
                parameters.add(it)

            }

            orderUpdateDTO.purchaseDate?.let {

                queryBuilder.append("purchase_date = ?,")
                parameters.add(it)

            }

            orderUpdateDTO.invoicingDate?.let {

                queryBuilder.append("invoicing_date = ?,")
                parameters.add(it)

            }

            orderUpdateDTO.sellerRCA?.let {

                queryBuilder.append("sellers_rca = ?,")
                parameters.add(it)

            }

            orderUpdateDTO.sellerName?.let {

                queryBuilder.append("seller_name = ?,")
                parameters.add(it)

            }

            orderUpdateDTO.orderAddress?.let {

                queryBuilder.append("order_adress = ?,")
                parameters.add(it)

            }

            if (parameters.isEmpty()) {

                println("Nenhuma Linha Atualizada")
                return null

            }
            queryBuilder.setLength(queryBuilder.length - 1)

            queryBuilder.append("WHERE order_code = ?")
            parameters.add(originalCode)

            println(parameters)

            val updateQuery = queryBuilder.toString()
            val updateStatement = conn.prepareStatement(updateQuery)

            println(updateQuery)

            parameters.forEachIndexed {

                    index, value ->
                updateStatement.setObject(index + 1, value)

            }

            val rowsUpdated = updateStatement.executeUpdate()

            if (rowsUpdated > 0) {

                val selectQuery = "SELECT * FROM pedido WHERE order_code = ?"

                val selectStatement = conn.prepareStatement(selectQuery)

                selectStatement.setInt(1, orderUpdateDTO.orderCode ?: originalCode)

                val resultSet = selectStatement.executeQuery()

                val updateOrder = if (resultSet.next()) {

                    Order(

                        orderCode = resultSet.getInt("order_code"),
                        customerCode = resultSet.getInt("customer_code"),
                        customerName = resultSet.getString("customer_name"),
                        loadCode = resultSet.getInt("load_number"),
                        orderType = resultSet.getString("order_type"),
                        orderStatus = resultSet.getString("status"),
                        purchaseDate = resultSet.getString("purchase_date"),
                        invoiceDate = resultSet.getString("invoicing_date"),
                        orderSellerRca = resultSet.getInt("sellers_rca"),
                        sellerName = resultSet.getString("seller_name"),
                        orderAddress = resultSet.getString("order_adress")
                    )

                } else {

                    null

                }

                resultSet.close()
                selectStatement.close()
                updateStatement.close()


                return updateOrder

            } else {

                updateStatement.close()

                println("Erro")

                return null

            }

        }


    }

    fun ordersProblems(customerNoFounded: String): List<Order?> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val ordersList = mutableListOf<Order>()


        val query = "SELECT * FROM pedido WHERE customer_name = ?"

        val statement = connection.prepareStatement(query)

        statement.setString(1, customerNoFounded)

        val resultSet: ResultSet = statement.executeQuery()

        while (resultSet.next()) {

            val orders = Order(

                orderCode = resultSet.getInt("order_code"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                loadCode = resultSet.getInt("load_number"),
                orderType = resultSet.getString("order_type"),
                orderStatus = resultSet.getString("status"),
                purchaseDate = resultSet.getString("purchase_date"),
                invoiceDate = resultSet.getString("invoicing_date"),
                orderSellerRca = resultSet.getInt("sellers_rca"),
                sellerName = resultSet.getString("seller_name"),
                orderAddress = resultSet.getString("order_adress")
            )

            ordersList.add(orders)

        }

        resultSet.close()
        statement.close()


        connection.close()

        return ordersList


    }


    fun ordersToTableView(): List<Order?> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val ordersList = mutableListOf<Order>()

        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM pedido")

        while (resultSet.next()) {

            val orders = Order(

                orderCode = resultSet.getInt("order_code"),
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                loadCode = resultSet.getInt("load_number"),
                orderType = resultSet.getString("order_type"),
                orderStatus = resultSet.getString("status"),
                purchaseDate = resultSet.getString("purchase_date"),
                invoiceDate = resultSet.getString("invoicing_date"),
                orderSellerRca = resultSet.getInt("sellers_rca"),
                sellerName = resultSet.getString("seller_name"),
                orderAddress = resultSet.getString("order_adress")
            )

            ordersList.add(orders)

        }

        resultSet.close()
        statement.close()


        connection.close()

        return ordersList

    }

    fun updateOrderSync(orderCode: Int, sync: String) {

        val connection = DriverManager.getConnection(urlLocalDb)

        val query = "UPDATE pedido SET orderSCN = ? WHERE order_code = ? "

        val statement = connection.prepareStatement(query)



        statement.setInt(2, orderCode)
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

    fun deleteOrderValidated() {

        val connection = DriverManager.getConnection(urlLocalDb)

        val parameter = "Sincronizado"

        val query = "DELETE FROM pedido WHERE orderSCN = ?"

        val statement = connection.prepareStatement(query)

        statement.setString(1, parameter)

        val rowsUpdated = statement.executeUpdate()

        println(rowsUpdated)



        statement.close()

        connection.close()

    }




}