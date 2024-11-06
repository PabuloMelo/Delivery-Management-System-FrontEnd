package pabulo.teste.front.resource.sellerResource

import pabulo.teste.front.dtos.sellers.SaveSellerDtoToDb
import pabulo.teste.front.entity.Seller
import java.sql.DriverManager
import java.sql.ResultSet

class SellerResource {

    private val tempUrl = "C:/Users/pabul/projects/FrontEnd/Delivery-Management-System-FrontEnd/Front/src/main/resources/pabulo/teste/front/DB/deliverySystemDb.db"

    private val urlLocalDb =
        "jdbc:sqlite:$tempUrl"

    fun saveSellerOnDb(saveSellerToDb: SaveSellerDtoToDb) {


        val connection =
            DriverManager.getConnection(urlLocalDb)

        val statement = connection.prepareStatement(

            "INSERT INTO vendedores( sellers_rca, sellers_name, seller_photo) VALUES(?,?,?)"
        )


        statement.setInt(1, saveSellerToDb.sellerRca)
        statement.setString(2, saveSellerToDb.sellerName)
        statement.setString(3, saveSellerToDb.sellerImagePath)

        statement.executeUpdate()

        statement.close()

        connection.close()


    }


    fun findSellerInLocalDbByRca(sellerRca: Int): Seller? {

        val connection = DriverManager.getConnection(urlLocalDb)


        val query =
            "SELECT sellers_rca, sellers_name, seller_photo FROM vendedores WHERE sellers_rca = ?"

        val statement = connection.prepareStatement(query)

        statement.setInt(1, sellerRca)

        val resultset: ResultSet = statement.executeQuery()

        val seller: Seller? = if (resultset.next()) {

            Seller(

                sellerRca = resultset.getInt("sellers_rca"),
                sellerName = resultset.getString("sellers_name"),
                sellerImagePath = resultset.getString("seller_photo")


            )


        } else {

            null
        }

        resultset.close()
        statement.close()
        connection.close()

        return seller


    }

    fun findSellerInLocalDbByName(sellerName: String): Seller? {

        val connection = DriverManager.getConnection(urlLocalDb)


        val query =
            "SELECT sellers_rca, sellers_name, seller_photo FROM vendedores WHERE sellers_name = ?"

        val statement = connection.prepareStatement(query)

        statement.setString(1, sellerName)

        val resultset: ResultSet = statement.executeQuery()

        val seller: Seller? = if (resultset.next()) {

            Seller(

                sellerRca = resultset.getInt("sellers_rca"),
                sellerName = resultset.getString("sellers_name"),
                sellerImagePath = resultset.getString("seller_photo")


            )


        } else {

            null
        }

        resultset.close()
        statement.close()
        connection.close()

        return seller


    }


}