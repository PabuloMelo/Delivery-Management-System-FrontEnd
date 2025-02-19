package pabulo.teste.front.resource.loadResource

import pabulo.teste.front.dtos.driver.SaveDriverDtoToDb
import pabulo.teste.front.dtos.load.LoadDTOtoDB
import pabulo.teste.front.dtos.load.LoadUpdateDto
import pabulo.teste.front.entity.Driver
import pabulo.teste.front.entity.Load
import java.nio.file.Paths
import java.sql.DriverManager
import java.sql.ResultSet

class LoadResource {


    private val dbUrl: String = getDbUrl()

    private fun getDbUrl(): String {
        val resourceUrl = javaClass.getResource("/pabulo/teste/front/DB/deliverySystemDb.db")
            ?: throw IllegalArgumentException("Database file not found")
        return "jdbc:sqlite:${Paths.get(resourceUrl.toURI())}"
    }


    private val urlLocalDb = dbUrl

    fun saveLoadOnLocalDb(load: LoadDTOtoDB) {

        val connection = DriverManager.getConnection(urlLocalDb)

        val statement = connection.prepareStatement(


            "INSERT INTO carregamento(load_code,driver,departure_date,load_validate) VALUES(?,?,?,?)"

        )

        statement.setInt(1, load.loadCode)
        statement.setString(2, load.loadDriver)
        statement.setString(3, load.loadDepartureDate)
        statement.setString(4, load.loadValidate)

        statement.executeUpdate()

        statement.close()

        connection.close()


    }


    fun findLoadByLoadCode(loadCode: Int): Load? {

        val connection = DriverManager.getConnection(urlLocalDb)

        val query = "SELECT * FROM carregamento WHERE load_code = ?"

        val statement = connection.prepareStatement(query)

        statement.setInt(1, loadCode)

        val resultSet: ResultSet = statement.executeQuery()

        val load: Load? = if (resultSet.next()) {

            Load(
                loadCode = resultSet.getInt("load_code"),
                driver = resultSet.getString("driver"),
                departureDate = resultSet.getString("departure_date"),
                loadValidate = resultSet.getString("load_validate")

            )

        } else {

            null

        }


        connection.close()
        resultSet.close()
        statement.close()

        return load


    }

    fun findLoadToTable(): List<Load?> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val loadsList = mutableListOf<Load>()

        val query = "SELECT * FROM carregamento"

        val statement = connection.prepareStatement(query)

        val resultSet: ResultSet = statement.executeQuery()

        while (resultSet.next()) {

            val load = Load(

                loadCode = resultSet.getInt("load_code"),
                driver = resultSet.getString("driver"),
                departureDate = resultSet.getString("departure_date"),
                loadValidate = resultSet.getString("load_validate")
            )
            loadsList.add(load)
        }
        connection.close()
        resultSet.close()
        statement.close()

        return loadsList
    }


    fun findNotValidateLoadCode(loadValidate: String): List<String?> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val loadsList = mutableListOf<String>()

        val query = "SELECT load_code FROM carregamento WHERE  load_validate = ?"

        val statement = connection.prepareStatement(query)


        statement.setString(1, loadValidate)

        val resultSet: ResultSet = statement.executeQuery()

        while (resultSet.next()) {
            loadsList.add(resultSet.getString("load_code"))
        }
        connection.close()
        resultSet.close()
        statement.close()

        return loadsList


    }


    fun updateLoadByCode(loadCode: Int, newLoad: LoadUpdateDto): Load? {

        val connection = DriverManager.getConnection(urlLocalDb)

        connection.use {

                conn ->

            val statement = connection.prepareStatement(
                "UPDATE carregamento SET load_code = ?, load_validate = ? WHERE load_code = ?"
            )
            statement.setInt(1, newLoad.loadCode)
            statement.setString(2, newLoad.loadValidate)
            statement.setInt(3, loadCode)


            val updateStatement = statement.executeUpdate()

            if (updateStatement > 0) {

                val selectedQuery = "SELECT * FROM carregamento WHERE load_code = ?"
                val selectStatement = conn.prepareStatement(selectedQuery)

                selectStatement.setInt(1, newLoad.loadCode)

                val resultSet = selectStatement.executeQuery()
                val updatedLoad = if (resultSet.next()) {

                    Load(

                        loadCode = resultSet.getInt("load_code"),
                        driver = resultSet.getString("driver"),
                        departureDate = resultSet.getString("departure_date"),
                        loadValidate = resultSet.getString("load_validate")

                    )
                } else {

                    null
                }


                resultSet.close()
                selectStatement.close()
                statement.close()




                return updatedLoad

            } else {
                statement.close()

                println("Erro")

                return null

            }


        }


    }


    fun saveANewDriverOnLocalDb(driverDto: SaveDriverDtoToDb) {

        val connection = DriverManager.getConnection(urlLocalDb)

        val statement = connection.prepareStatement(


            "INSERT INTO motoristas(driver_Name,driver_PhotoPath) VALUES(?,?)"

        )

        statement.setString(1, driverDto.driverName)
        statement.setString(2, driverDto.driverImagePath)

        statement.executeUpdate()

        statement.close()
        connection.close()


    }


    fun findDriverByName(driverName: String): Driver? {

        val connection = DriverManager.getConnection(urlLocalDb)

        val query = "SELECT driver_Name,driver_PhotoPath, driverId FROM motoristas WHERE driver_Name = ? "

        val statement = connection.prepareStatement(query)

        statement.setString(1, driverName)

        val resultSet: ResultSet = statement.executeQuery()

        val driver: Driver? = if (resultSet.next()) {


            Driver(
                driverID = resultSet.getInt("driverId"),
                driverName = resultSet.getString("driver_Name"),
                driverPhotoPath = resultSet.getString("driver_PhotoPath")


            )

        } else {

            null
        }
        resultSet.close()
        statement.close()
        connection.close()

        return driver

    }

    fun getDrivers(): List<String> {
        val drivers = mutableListOf<String>()
        val connection = DriverManager.getConnection(urlLocalDb)


        val statement = connection.createStatement()
        val resultSet =
            statement.executeQuery("SELECT driver_Name FROM motoristas WHERE driver_Name NOT IN ('Retira Posterior', 'Entrega Futura', 'Retira Imediata')")

        while (resultSet.next()) {
            drivers.add(resultSet.getString("driver_Name"))
        }

        resultSet.close()
        statement.close()


        connection.close()

        return drivers
    }


    fun loadToTableView(): List<Load?> {


        val connection = DriverManager.getConnection(urlLocalDb)

        val loadsList = mutableListOf<Load>()

        val statment = connection.createStatement()

        val resultSet = statment.executeQuery("SELECT * FROM carregamento")

        while (resultSet.next()) {

            val loads = Load(

                loadCode = resultSet.getInt("load_code"),
                driver = resultSet.getString("driver"),
                departureDate = resultSet.getString("departure_date"),
                loadValidate = resultSet.getString("load_validate")
            )

            loadsList.add(loads)

        }

        connection.close()
        statment.close()
        resultSet.close()

        return loadsList


    }

    fun updateLoadSync(loadCode: Int, sync: String) {

        val connection = DriverManager.getConnection(urlLocalDb)

        val query = "UPDATE carregamento SET load_sync = ? WHERE load_code = ? "

        val statement = connection.prepareStatement(query)

        statement.setInt(2, loadCode)
        statement.setString(1, sync)

        val rowsUpdate = statement.executeUpdate()

        if (rowsUpdate > 0) {

            println("Syncronização bem sucedida")

        } else {

            println("Syncronização mal sucedida")

        }

        statement.close()
        connection.close()


    }

    fun ignoreLoadsDefault(): List<Load?> {

        val connection = DriverManager.getConnection(urlLocalDb)
        val defaultloadsList = mutableListOf<Load>()

       // val loadSync = "Não Sincronizado"


        val query =
            "SELECT * FROM carregamento WHERE driver NOT IN('Retira Posterior', 'Entrega Futura', 'Retira Imediata') AND load_validate NOT IN ('DEFAULT')"


        val statement = connection.prepareStatement(query)
       // statement.setString(1, loadSync)


        val resultSet = statement.executeQuery()

        while (resultSet.next()) {

            val load = Load(

                loadCode = resultSet.getInt("load_code"),
                driver = resultSet.getString("driver"),
                departureDate = resultSet.getString("departure_date"),
                loadValidate = resultSet.getString("load_validate")

            )


            defaultloadsList.add(load)

        }

        resultSet.close()
        statement.close()
        connection.close()


        return defaultloadsList

    }

    fun deleteLoadValidated() {

        val connection = DriverManager.getConnection(urlLocalDb)

        val parameter = "Sincronizado"
        val validade = "VALIDADO"

        val query = "DELETE FROM carregamento WHERE load_sync = ? AND load_validate = ?"

        val statement = connection.prepareStatement(query)

        statement.setString(1, parameter)
        statement.setString(2, validade)

        val rowsUpdated = statement.executeUpdate()

        println(rowsUpdated)



        statement.close()

        connection.close()

    }


}






