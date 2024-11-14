package pabulo.teste.front.resource.customerResouce

import pabulo.teste.front.dtos.customer.SaveCustomerDtoToDb
import pabulo.teste.front.dtos.customer.UpdateCustomerDtoToDb
import pabulo.teste.front.entity.Customer
import java.sql.DriverManager
import java.sql.ResultSet


class CustomerResource {

    private val tempUrl =
        "C:/Users/USUARIO/Desktop/ProjectFront/Delivery-Management-System-FrontEnd/Front/src/main/resources/pabulo/teste/front/DB/deliverySystemDb.db"

    private val urlLocalDb =

        "jdbc:sqlite:$tempUrl"

    fun saveCustomerToLocalDB(customerDtoToDb: SaveCustomerDtoToDb) {


        val connection =
            DriverManager.getConnection(urlLocalDb)

        val statement = connection.prepareStatement(

            "INSERT INTO clientes(customer_code ,customer_name ,phone , customer_type ,customer_registered, customerSCN) VALUES(?,?,?,?,?,?)"
        )


        statement.setInt(1, customerDtoToDb.customerCode)
        statement.setString(2, customerDtoToDb.customerName)
        statement.setString(3, customerDtoToDb.phone)
        statement.setString(4, customerDtoToDb.customerRegistered)
        statement.setString(5, customerDtoToDb.customerType)
        statement.setString(6, customerDtoToDb.customerSync)


        statement.executeUpdate()

        statement.close()

        connection.close()


    }

    fun findCustomerByCodeInLocalDb(customerCode: Int): Customer? {

        val connection =
            DriverManager.getConnection(urlLocalDb)

        val query =
            "SELECT customer_code ,customer_name ,phone , customer_type ,customer_registered, customerSCN FROM clientes WHERE customer_code = ?"

        val statement = connection.prepareStatement(query)


        statement.setInt(1, customerCode)

        val resultSet: ResultSet = statement.executeQuery()


        val customer: Customer? = if (resultSet.next()) {

            Customer(
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                phone = resultSet.getString("phone"),
                customerRegistered = resultSet.getString("customer_registered"),
                customerType = resultSet.getString("customer_type"),
                customerSync = resultSet.getString("customerSCN")
            )
        } else {

            null

        }

        resultSet.close()
        statement.close()
        connection.close()

        return customer


    }


    fun findCustomerByNameInLocalDb(customerName: String): Customer? {

        val connection =
            DriverManager.getConnection(urlLocalDb)

        val query =
            "SELECT customer_code ,customer_name ,phone , customer_type ,customer_registered, customerSCN FROM clientes WHERE customer_name = ?"

        val statement = connection.prepareStatement(query)


        statement.setString(1, customerName)

        val resultSet: ResultSet = statement.executeQuery()


        val customer: Customer? = if (resultSet.next()) {

            Customer(
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                phone = resultSet.getString("phone"),
                customerRegistered = resultSet.getString("customer_registered"),
                customerType = resultSet.getString("customer_type"),
                customerSync = resultSet.getString("customerSCN")
            )
        } else {

            null

        }

        resultSet.close()
        statement.close()
        connection.close()

        return customer


    }


    fun updateCustomerOnLocalDb(originalCustomerCode: Int, updateCustomerDto: UpdateCustomerDtoToDb): Customer? {

        val connection = DriverManager.getConnection(urlLocalDb)

        connection.use {

                conn ->

            val queryBuilder = StringBuilder("UPDATE clientes SET ")
            val paramenters = mutableListOf<Any>()

            updateCustomerDto.customerCode?.let {
                queryBuilder.append("customer_code = ?,")
                paramenters.add(it)
            }
            updateCustomerDto.customerName?.let {

                queryBuilder.append("customer_name = ?,")
                paramenters.add(it)

            }
            updateCustomerDto.phone?.let {

                queryBuilder.append("phone = ?,")
                paramenters.add(it)

            }

            updateCustomerDto.customerType?.let {
                queryBuilder.append("customer_type = ?,")
                paramenters.add(it)

            }
            updateCustomerDto.customerRegistered?.let {
                queryBuilder.append("customer_registered = ?,")
                paramenters.add(it)
            }

            if (paramenters.isEmpty()) {

                println("Nenhuma linha atualizada")
                return null
            }
            queryBuilder.setLength(queryBuilder.length - 1)

            queryBuilder.append(" WHERE customer_code = ?")
            paramenters.add(originalCustomerCode)


            val updateQuery = queryBuilder.toString()
            val updateStatement = conn.prepareStatement(updateQuery)

            paramenters.forEachIndexed { index, value ->

                updateStatement.setObject(index + 1, value)
            }


            val rowsUpdated = updateStatement.executeUpdate()

            if (rowsUpdated > 0) {

                val selectQuery = "SELECT * FROM clientes WHERE customer_code = ?"

                val selectStatement = conn.prepareStatement(selectQuery)
                selectStatement.setInt(1, updateCustomerDto.customerCode ?: originalCustomerCode)

                val resultSet = selectStatement.executeQuery()

                val updatedCustomer = if (resultSet.next()) {

                    Customer(
                        customerCode = resultSet.getInt("customer_code"),
                        customerName = resultSet.getString("customer_name"),
                        phone = resultSet.getString("phone"),
                        customerType = resultSet.getString("customer_type"),
                        customerRegistered = resultSet.getString("customer_registered"),
                        customerSync = resultSet.getString("customerSCN")
                    )
                } else {
                    null
                }
                resultSet.close()
                selectStatement.close()
                updateStatement.close()

                return updatedCustomer

            } else {

                updateStatement.close()

                println("Erro")

                return null

            }
        }

    }

    fun customerListToView(): List<Customer?> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val customersList = mutableListOf<Customer>()


        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM clientes")

        while (resultSet.next()) {

            val customers = Customer(
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                phone = resultSet.getString("phone"),
                customerRegistered = resultSet.getString("customer_registered"),
                customerType = resultSet.getString("customer_type"),
                customerSync = resultSet.getString("customerSCN")
            )

            customersList.add(customers)

        }

        resultSet.close()
        statement.close()


        connection.close()

        return customersList

    }

    fun findCustomerSync(): List<Customer?> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val customersList = mutableListOf<Customer>()
        val parameter = "Não Sincronizado"

        val query = "SELECT * FROM clientes WHERE customerSCN = ?"

        val statement = connection.prepareStatement(query)

        statement.setString(1, parameter)

        val resultSet = statement.executeQuery()



        while (resultSet.next()) {

            val customers = Customer(
                customerCode = resultSet.getInt("customer_code"),
                customerName = resultSet.getString("customer_name"),
                phone = resultSet.getString("phone"),
                customerRegistered = resultSet.getString("customer_registered"),
                customerType = resultSet.getString("customer_type"),
                customerSync = resultSet.getString("customerSCN")
            )

            customersList.add(customers)

        }

        resultSet.close()
        statement.close()


        connection.close()

        return customersList

    }


    fun deleteCustomerValidated(){

        val connection = DriverManager.getConnection(urlLocalDb)

        val parameter = "Sincronizado"

        val query = "DELETE FROM clientes WHERE customerSCN = ?"

        val statement = connection.prepareStatement(query)

        statement.setString(1,parameter)

        val rowsUpdated = statement.executeUpdate()

        println(rowsUpdated)



        statement.close()

        connection.close()

    }


}