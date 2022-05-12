package net.jkk.carinsurance

import net.sf.clipsrules.jni.*
import java.awt.FlowLayout
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class CarInsurance() : ActionListener {
    companion object {
        //Car Info
        const val CAR_BRAND = "CarBrand"
        const val CAR_MODEL = "CarModel"
        const val PRODUCTION_YEAR = "ProductionYear"
        const val ENGINE_CAPACITY = "EngineCapacity"

        //Driver Info
        const val LICENCE_YEAR = "LicenceYear"
        const val MARITAL_STATUS = "MaritalStatus"
        const val REGULAR_CUSTOMER_AGE = "RegularCustomerAge"
        const val HAD_ACCIDENT = "HadAccident"

        //Window Info
        const val HEIGHT = 600
        const val WIDTH = 800
    }

    private val env: Environment = Environment()
    private var carInsuranceService: CarInsuranceService
    lateinit var executionThread: Thread

    private var winFrame: JFrame = JFrame("Car Insurance")

    var isExecuting = false

    init {
        try {
            env.loadFromResource("/carinsurence.clp")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        env.reset()
        carInsuranceService = CarInsuranceService(this, getCars())

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
        try {
            runCarInsurance(e)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    private fun runCarInsurance(event: ActionEvent?) {
        if (event != null && event.source is JComboBox<*>) {
            val box = event.source as JComboBox<*>
            when (box.name) {
                CAR_BRAND -> {
                    carInsuranceService.changeBrandModel(box.selectedItem as String, box.parent as JPanel)
                }
                CAR_MODEL -> println()
                PRODUCTION_YEAR -> println(box.selectedItem)
                ENGINE_CAPACITY -> println(box.selectedItem)
                LICENCE_YEAR -> println(box.selectedItem)
                MARITAL_STATUS -> println(box.selectedItem)
                REGULAR_CUSTOMER_AGE -> println(box.selectedItem)
                HAD_ACCIDENT -> println(box.selectedItem)
            }
        }
    }

    private fun getCars(): MutableList<Car> {
        val stringEval = "(get-all-cars)"
        val multiField: MultifieldValue = env.eval(stringEval) as MultifieldValue
        val cars: MutableList<Car> = ArrayList()
        for (field in multiField) {
            val factValue: FactAddressValue = field as FactAddressValue
            val carBrand: String = (factValue.getSlotValue("carBrand") as LexemeValue).value
            val carModel: String = (factValue.getSlotValue("carModel") as LexemeValue).value
            val basePrice: Int = (factValue.getSlotValue("basePrice") as NumberValue).intValue()
            cars.add(Car(carBrand, carModel, basePrice))
        }
        return cars
    }

}

fun main() {
    SwingUtilities.invokeLater {
        CarInsurance()
    }
}



