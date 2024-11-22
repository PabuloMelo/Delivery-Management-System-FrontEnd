package pabulo.teste.front.adapters

import pabulo.teste.front.dtos.driver.SaveDriverDtoToDb
import pabulo.teste.front.dtos.load.LoadDTOtoDB
import pabulo.teste.front.resource.loadResource.LoadResource

class AutoInsertOrdersTypeOnDb {

    val loadResource = LoadResource()

    private fun autoInsertLoadTypeAndLoadsOnLocalDb() {

        val driverPhotoPath = " "
        val retiraPosterior = "Retira Posterior"
        val entregaFutura = "Entrega Futura"
        val retiraImediata = "Retira Imediata"
        val defaultDriver = "Default"
        val departureDateDefault = "2024-01-01"
        val loadValidateDefault = "DEFAULT"


        val retiraPosteriorDTO = SaveDriverDtoToDb(retiraPosterior, driverPhotoPath)
        val entregaFuturaDTO = SaveDriverDtoToDb(entregaFutura, driverPhotoPath)
        val retiraImediataDTO = SaveDriverDtoToDb(retiraImediata, driverPhotoPath)
        val defaultDriverDTO = SaveDriverDtoToDb(defaultDriver,driverPhotoPath)

        loadResource.saveANewDriverOnLocalDb(defaultDriverDTO)
        loadResource.saveANewDriverOnLocalDb(retiraImediataDTO)
        loadResource.saveANewDriverOnLocalDb(entregaFuturaDTO)
        loadResource.saveANewDriverOnLocalDb(retiraPosteriorDTO)

        /*----------------------------------CreateLoadRetiraPosteriorToInit-------------------------------------------*/

        val loadCodeRP = 1


        val loadDTORP = LoadDTOtoDB(loadCodeRP, retiraPosterior, departureDateDefault, loadValidateDefault)

        loadResource.saveLoadOnLocalDb(loadDTORP)

        /*----------------------------------CreateLoadEntregaFuturaToInit-------------------------------------------*/

        val loadCodeFD = 2


        val loadDTOFD = LoadDTOtoDB(loadCodeFD,entregaFutura,departureDateDefault,loadValidateDefault)

        loadResource.saveLoadOnLocalDb(loadDTOFD)

        /*----------------------------------CreateLoadRetiraImediataToInit-------------------------------------------*/

        val loadCodeRI = 3

        val loadDTORI = LoadDTOtoDB(loadCodeRI,retiraImediata,departureDateDefault,loadValidateDefault)

        loadResource.saveLoadOnLocalDb(loadDTORI)

        val loadCodeDefault = 4

        val loadDefaultDefaultDTO = LoadDTOtoDB(loadCodeDefault,defaultDriver,departureDateDefault,loadValidateDefault)

        loadResource.saveLoadOnLocalDb(loadDefaultDefaultDTO)

    }

    fun checkLoadTypeOnLocalDb() {

        val orderType = loadResource.findDriverByName("Entrega Futura")



        if (orderType == null) {

            autoInsertLoadTypeAndLoadsOnLocalDb()

        }

    }


}