package pabulo.teste.front.sqlite

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object SqliteHelper {
    private var connection: Connection? = null
    private const val URL = "C:/Users/USUARIO/Desktop/ProjectFront/Delivery-Management-System-FrontEnd/Front/src/main/resources/pabulo/teste/front/DB/deliverySystemDb.db"


    private const val URLDBLOCAL =
        "jdbc:sqlite:$URL"

    private fun connect(): Connection? {

        if (connection == null || connection!!.isClosed) {


            try {
                connection =
                    DriverManager.getConnection(URLDBLOCAL)

            } catch (e: SQLException) {
                ("Erro ao tentar se conectar ao banco de dados ${e.message}")

                return null

            }

        }
        return connection
    }

    private fun close() {

        connection?.close()

    }


    fun createTables() {

        val conn = connect()

        if (conn != null) {
            try {
                val createTableSql = listOf(


                    """ 
                CREATE TABLE IF NOT EXISTS clientes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_code INTEGER NOT NULL,
                    customer_name TEXT, 
                    phone TEXT,
                    customer_type TEXT,
                    customer_registered TEXT,
                 UNIQUE(customer_code)
                );""",


                    """CREATE TABLE IF NOT EXISTS vendedores (
                    seller_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sellers_name TEXT NOT NULL,
                    sellers_rca INTEGER NOT NULL,
                    UNIQUE(sellers_rca)
                );""",

                    """ CREATE TABLE IF NOT EXISTS pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_code INTEGER NOT NULL,
                    customer_code INTEGER NOT NULL,
                    load_number INTEGER NOT NULL,
                    customer_name TEXT NOT NULL,
                    order_type TEXT,
                    status TEXT,
                    purchase_date TEXT,
                    invoicing_date TEXT,
                    sellers_rca INTEGER NOT NULL,
                    seller_name TEXT NOT NULL,
                    days_until_delivery INTEGER,
                    order_future_del_state TEXT,
                   UNIQUE(order_code)
                );""",

                    """CREATE TABLE IF NOT EXISTS situacao (
                    state_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id INTEGER,
                    order_number INTEGER NOT NULL,
                    customer_name TEXT NOT NULL,
                    customer_code INTEGER NOT NULL,
                    state TEXT,
                    first_level TEXT,
                    second_level TEXT,
                    description TEXT,
                    invoicing_date TEXT,
                    purchase_date TEXT,
                    solve_date TEXT,
                    solve_driver TEXT,
                    days_until_solve TEXT,
                    resolve TEXT,
                    UNIQUE(order_number)
                );""",

                    """CREATE TABLE IF NOT EXISTS motoristas (
                    driverId INTEGER PRIMARY KEY AUTOINCREMENT,
                    driver_Name TEXT NOT NULL,
                    driver_PhotoPath TEXT);""",


                    """CREATE TABLE IF NOT EXISTS carregamento (
                    load_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    load_code INTEGER NOT NULL,
                    driver TEXT,
                    departure_date TEXT
                    );""",



                    """CREATE TABLE IF NOT EXISTS endereco (
                    address_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    address TEXT
                    );""",


                    """DELETE FROM carregamento;""",

                    """DELETE FROM pedido;""",

                    """DELETE FROM motoristas""",


                )

                //




                // """DELETE FROM carregamento;""",

                // """DELETE FROM pedido;""",

                // """DELETE FROM situacao""",

                // """DELETE FROM clientes""",

             //   """ALTER TABLE situacao ADD COLUMN state_sync TEXT;""",


                connect().use { conn ->

                    conn!!.createStatement().use { stmt ->
                        for (createTableSql in createTableSql) {
                            stmt.execute(createTableSql)
                        }
                    }
                }
                println("Tabelas Criadas")
            } catch (e: SQLException) {
                println("Erro ao criar as tabelas ${e.message}")
            }


        } else {

            println("erro de conecção")
        }
        close()

    }

}

fun main() {
    SqliteHelper.createTables()

}


