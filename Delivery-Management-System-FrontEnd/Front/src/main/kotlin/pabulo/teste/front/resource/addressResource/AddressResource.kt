package pabulo.teste.front.resource.addressResource

import pabulo.teste.front.dtos.address.SaveAddressDTO
import pabulo.teste.front.dtos.address.UpdateAddressDTO
import pabulo.teste.front.entity.Address
import java.sql.DriverManager
import java.sql.ResultSet

class AddressResource {

    private val tempUrl =
        "C:/Users/USUARIO/Desktop/ProjectFront/Delivery-Management-System-FrontEnd/Front/src/main/resources/pabulo/teste/front/DB/deliverySystemDb.db"

    private val urlLocalDb =
        "jdbc:sqlite:$tempUrl"





    fun saveNewAddressOnDb(saveAddressDTO: SaveAddressDTO): Address {

        val connection = DriverManager.getConnection(urlLocalDb)

        val statement = connection.prepareStatement(

            "INSERT INTO endereco(address, city) VALUES(?,?)"

        )

        statement.setString(1, saveAddressDTO.district)
        statement.setString(2, saveAddressDTO.city)

        statement.executeUpdate()


        val selectQuery = "SELECT * FROM endereco WHERE address = ?"
        val selectStatement = connection.prepareStatement(selectQuery)

        selectStatement.setString(1, saveAddressDTO.district)

        val resultSet = selectStatement.executeQuery()

        val address = Address(

            resultSet.getInt("address_id"),
            resultSet.getString("address"),
            resultSet.getString("city")

        )


        selectStatement.close()
        resultSet.close()
        statement.close()
        connection.close()

        return address

    }

    fun findAddressByName(addressName: String): Address? {

        val connection = DriverManager.getConnection(urlLocalDb)

        val selectQuery = "SELECT * FROM endereco WHERE address = ?"

        val selectStatement = connection.prepareStatement(selectQuery)

        selectStatement.setString(1, addressName)

        val resultSet = selectStatement.executeQuery()

        val address: Address? = if (resultSet.next()) {

            Address(

                addressId = resultSet.getInt("address_id"),
                district = resultSet.getString("address"),
                city = resultSet.getString("city")

            )

        } else {

            null

        }

        selectStatement.close()
        resultSet.close()
        connection.close()

        return address

    }


    fun updateAddress(updateAddressDTO: UpdateAddressDTO, originalDistrictName: String): Address? {

        val connection = DriverManager.getConnection(urlLocalDb)

        println(originalDistrictName)

        connection.use {

            val queryBuilder = StringBuilder("UPDATE endereco SET ")
            val parameters = mutableListOf<Any>()



            updateAddressDTO.city?.let {

                queryBuilder.append("city = ?,")
                parameters.add(it)

            }

            updateAddressDTO.district?.let {

                queryBuilder.append("address = ?,")
                parameters.add(it)
            }

            if (parameters.isEmpty()) {

                println("Nenhuma linha atualizada")

                return null
            }

            queryBuilder.setLength(queryBuilder.length - 1)

            queryBuilder.append(" WHERE address = ?")
            parameters.add(originalDistrictName)


            val updateQuery = queryBuilder.toString()
            val updateStatement = connection.prepareStatement(updateQuery)



            parameters.forEachIndexed { index, value ->

                updateStatement.setObject(index + 1, value)

            }



            val rowsUpdate = updateStatement.executeUpdate()

            if (rowsUpdate > 0) {

                val selectQuery = "SELECT * FROM endereco WHERE address = ?"

                val selectStatement = connection.prepareStatement(selectQuery)

                selectStatement.setString(1, updateAddressDTO.district ?: originalDistrictName)

                val resultSet = selectStatement.executeQuery()

                val addressUpdated = if (resultSet.next()) {

                    Address(

                        addressId = resultSet.getInt("address_id"),
                        district = resultSet.getString("address"),
                        city = resultSet.getString("city")

                    )

                } else {

                    null

                }

                resultSet.close()
                selectStatement.close()
                updateStatement.close()
                connection.close()

                return addressUpdated

            } else {

                updateStatement.close()
                connection.close()

                println("Erro ao atualizar o endereço")

                return null

            }

        }


    }


    fun getAddress(): List<String> {

        val connection = DriverManager.getConnection(urlLocalDb)

        val address = mutableListOf<String>()

        val statement = connection.createStatement()

        val resultSet = statement.executeQuery("SELECT address FROM endereco")

        while (resultSet.next()) {

            address.add(resultSet.getString("address"))

        }

        resultSet.close()
        statement.close()
        connection.close()

        return address
    }


    fun findAddressByDistrictCode(districtCode: Int): Address? {

        val connection = DriverManager.getConnection(urlLocalDb)

        val query = "SELECT * FROM endereco WHERE address_id = ?"

        val statement = connection.prepareStatement(query)

        statement.setInt(1, districtCode)

        val resultSet: ResultSet = statement.executeQuery()

        val address: Address? = if (resultSet.next()) {

            Address(

                addressId = resultSet.getInt("address_id"),
                district = resultSet.getString("address"),
                city = resultSet.getString("city")

            )

        } else {

            null

        }


        resultSet.close()
        statement.close()
        connection.close()


        return address

    }

    fun findAddressByDistrictName(districtName: String): Address? {

        val connection = DriverManager.getConnection(urlLocalDb)

        val query = "SELECT * FROM endereco WHERE address = ?"

        val statement = connection.prepareStatement(query)

        statement.setString(1, districtName)

        val resultSet: ResultSet = statement.executeQuery()

        val address: Address? = if (resultSet.next()) {

            Address(

                addressId = resultSet.getInt("address_id"),
                district = resultSet.getString("address"),
                city = resultSet.getString("city")

            )

        } else {

            null

        }


        resultSet.close()
        statement.close()
        connection.close()


        return address

    }



}