package net.jkk.carinsurance

import net.sf.clipsrules.jni.Environment
import java.awt.FlowLayout
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*

class CarInsurance() : ActionListener {
    companion object {
        //Car Info
        const val PRODUCTION_YEAR = "ProductionYear"
        const val CAR_BRAND = "CarBrand"
        const val CAR_MODEL = "CarModel"
        const val FUEL_TYPE = "FuelType"
        const val ENGINE_CAPACITY = "EngineCapacity"
        const val DOOR_AMOUNT = "DoorAmount"
        const val BODY_TYPE = "BodyType"
        //Driver Info
        const val BIRTH_YEAR = "Birth Year"
        const val LICENCE_YEAR = "LicenceYear"
        const val REGION = "Region"
        const val MARITAL_STATUS = "MaritalStatus"
        //Window Info
        const val HEIGHT = 600
        const val WIDTH = 800
    }

    lateinit var env: Environment
    lateinit var resourceBundle: ResourceBundle
    lateinit var executionThread: Thread
    private val carInsuranceService = CarInsuranceService(this)

    private var winFrame: JFrame = JFrame("Car Insurance")

    var isExecuting = false

    init {
        winFrame.contentPane.layout = BoxLayout(winFrame.contentPane, BoxLayout.Y_AXIS)
        winFrame.setSize(WIDTH, HEIGHT)
        winFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        winFrame.setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2)

        val carInfoPanel = carInsuranceService.createPanel("Car Info", 7, 2, InfoType.CAR_INFO)
        val driverInfoPanel = carInsuranceService.createPanel("Driver Info", 7, 2, InfoType.DRIVER_INFO)

        val insuranceInfo = JPanel()
        insuranceInfo.layout = FlowLayout()
        insuranceInfo.add(carInfoPanel)
        insuranceInfo.add(driverInfoPanel)

        winFrame.contentPane.add(insuranceInfo)


        winFrame.isVisible = true
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (e != null && e.source is JComboBox<*>) {
            val box = e.source as JComboBox<*>
            if (box.name == PRODUCTION_YEAR){
                println(box.selectedItem)
            }
        }
    }

}

fun main() {
    SwingUtilities.invokeLater {
        CarInsurance()
    }
}



