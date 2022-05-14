package net.jkk.carinsurance

import net.sf.clipsrules.jni.Environment
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class CarInsurance() : ActionListener {
    companion object {
        //Car Info
        const val CAR_BRAND = "carBrand"
        const val CAR_MODEL = "carModel"
        const val PRODUCTION_YEAR = "productionYear"
        const val ENGINE_CAPACITY = "engineCapacity"

        //Driver Info
        const val LICENCE_YEAR = "licenceYear"
        const val MARITAL_STATUS = "maritalStatus"
        const val REGULAR_CUSTOMER_AGE = "regularCustomerAge"
        const val HAD_ACCIDENT = "hadAccident"

        //Window Info
        const val HEIGHT = 600
        const val WIDTH = 900
    }

    private val env: Environment = Environment()
    private var executionThread: Thread? = null
    private var carInsuranceService: CarInsuranceService

    private var carInfoPanel: JPanel
    private var driverInfoPanel: JPanel
    private var buttonPanel: JPanel
    private var infoPanel: JPanel
    private var winFrame: JFrame = JFrame("Car Insurance")

    var isExecuting = false

    init {
        try {
            env.loadFromResource("/carinsurence.clp")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        env.reset()
        carInsuranceService = CarInsuranceService(this, env)

        winFrame.contentPane.layout = GridLayout(2, 1)
        winFrame.setSize(WIDTH, HEIGHT)
        winFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        winFrame.setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2)

        infoPanel = JPanel()
        infoPanel.name = "Info Panel"

        carInfoPanel = carInsuranceService.createPanel("Car Info", 4, 2, InfoType.CAR_INFO)
        driverInfoPanel = carInsuranceService.createPanel("Driver Info", 4, 2, InfoType.DRIVER_INFO)
        buttonPanel = carInsuranceService.createPanel("Button Info", 1, 1, InfoType.BUTTON_INFO)
        setBtnActionEvent()
        val insuranceInfo = JPanel()
        insuranceInfo.layout = FlowLayout()
        insuranceInfo.add(carInfoPanel)
        insuranceInfo.add(driverInfoPanel)
        insuranceInfo.add(buttonPanel)

        winFrame.contentPane.add(insuranceInfo)
        winFrame.contentPane.add(infoPanel)

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
        if (isExecuting) return
        if (event != null && event.source is JComboBox<*>) {
            val box = event.source as JComboBox<*>
            if (box.selectedItem == "") return
            when (box.name) {
                CAR_BRAND -> {
                    carInsuranceService.changeBrandModel(box.selectedItem as String, box.parent as JPanel)
                }
                PRODUCTION_YEAR -> {
                    env.assertString("(attribute (variable ${box.name}) (value ${box.selectedItem}))")
                }
                ENGINE_CAPACITY -> {
                    env.assertString("(attribute (variable ${box.name}) (value ${box.selectedItem}))")
                }
                LICENCE_YEAR -> {
                    env.assertString("(attribute (variable ${box.name}) (value ${box.selectedItem}))")
                }
                MARITAL_STATUS -> {
                    env.assertString("(attribute (variable ${box.name}) (value ${box.selectedItem}))")
                }
                REGULAR_CUSTOMER_AGE -> {
                    env.assertString("(attribute (variable ${box.name}) (value ${box.selectedItem}))")
                }
                HAD_ACCIDENT -> {
                    env.assertString("(attribute (variable ${box.name}) (value ${box.selectedItem}))")
                }
            }

            if (carInsuranceService.isAllComboBoxesSet(carInfoPanel, driverInfoPanel)) {
                carInsuranceService.removeDuplicatedAttributes()
                val runningThread = Runnable {
                    try {
                        env.run()
                        isExecuting = false
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                isExecuting = true
                executionThread = Thread(runningThread)
                executionThread!!.start()
                while (isExecuting) {
                    Thread.sleep(2000)
                }
                carInsuranceService.getPrice(carInfoPanel)

                infoPanel =
                    carInsuranceService.crateInfoPanel(infoPanel, carInsuranceService.getPriceInfo(), carInfoPanel)
                winFrame.contentPane.revalidate()
                winFrame.contentPane.repaint()

                carInfoPanel.components.filterIsInstance(JComboBox::class.java).forEach { it.isEnabled = false }
                driverInfoPanel.components.filterIsInstance(JComboBox::class.java).forEach { it.isEnabled = false }
                env.reset()
            }
        }
    }

    //button event for clearing CLIPs environment and window controls
    private fun setBtnActionEvent() {
        val btn = buttonPanel.components.filterIsInstance(JButton::class.java).first()
        btn.addActionListener {
            carInfoPanel.components.filterIsInstance(JComboBox::class.java).forEach {
                if (it.name == CAR_MODEL) it.removeAllItems()
                it.selectedItem = ""
                it.isEnabled = true
            }
            driverInfoPanel.components.filterIsInstance(JComboBox::class.java).forEach {
                it.selectedItem = ""
                it.isEnabled = true
            }
            val panel =
                winFrame.contentPane.components.filterIsInstance(JPanel::class.java).first { it.name == "Info Panel" }
            panel.removeAll()
            winFrame.contentPane.revalidate()
            winFrame.contentPane.repaint()
        }
        env.reset()
    }
}

fun main() {
    SwingUtilities.invokeLater {
        CarInsurance()
    }
}



