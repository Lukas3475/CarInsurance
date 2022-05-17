package net.jkk.carinsurance

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import net.sf.clipsrules.jni.Environment
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.FileOutputStream
import javax.swing.*

class CarInsurance : ActionListener {
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
                val totalPrice = carInsuranceService.getPrice(carInfoPanel)
                val priceInfoList = carInsuranceService.getPriceInfo()
                infoPanel =
                    carInsuranceService.crateInfoPanel(infoPanel, priceInfoList, carInfoPanel)
                winFrame.contentPane.revalidate()
                winFrame.contentPane.repaint()

                generatePDF(priceInfoList, carInfoPanel, totalPrice)

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

    private fun generatePDF(priceInfoList: MutableList<PriceInfo>, carInfoPanel: JPanel, price: String) {
        val car = carInsuranceService.getCar(carInfoPanel)
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream("recipe.pdf"))
        document.open()
        val font1 = FontFactory.getFont(FontFactory.HELVETICA, 16.0f, BaseColor.BLACK)
        val font2 = FontFactory.getFont(FontFactory.HELVETICA, 14.0f, BaseColor.BLACK)
        val font3 = FontFactory.getFont(FontFactory.HELVETICA, 11.0f, BaseColor.BLACK)
        val boldFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14.0f, BaseColor.BLACK)
        val title = Paragraph("Receipt for your car insurance", font1)
        title.alignment = Element.ALIGN_CENTER
        document.add(title)
        document.add(Chunk.NEWLINE)
        document.add(Chunk.NEWLINE)
        val carInfoTable = PdfPTable(3)

        carInfoTable.addCell(PdfPCell(Paragraph("Car brand", boldFont2)))
        carInfoTable.addCell(PdfPCell(Paragraph("Car model", boldFont2)))
        carInfoTable.addCell(PdfPCell(Paragraph("Base price", boldFont2)))

        carInfoTable.addCell(Paragraph(car.carBrand, font2))
        carInfoTable.addCell(Paragraph(car.carModel, font2))
        carInfoTable.addCell(Paragraph(car.basePrice.toString(), font2))

        carInfoTable.rows.forEach { row ->
            row.cells.forEach {
                it.horizontalAlignment = Element.ALIGN_CENTER
                it.setPadding(7f)
            }
        }
        document.add(carInfoTable)

        document.add(Chunk.NEWLINE)
        document.add(Chunk.NEWLINE)

        val insuranceTableInfo = PdfPTable(3)
        insuranceTableInfo.addCell(PdfPCell())
        insuranceTableInfo.addCell(PdfPCell(Paragraph("Description", boldFont2)))
        insuranceTableInfo.addCell(PdfPCell(PdfPCell(Paragraph("Value", boldFont2))))

        priceInfoList.forEach {
            insuranceTableInfo.addCell(PdfPCell(Paragraph(it.infoType, font3)))
            insuranceTableInfo.addCell(PdfPCell(Paragraph(it.desc, font3)))
            insuranceTableInfo.addCell(PdfPCell(Paragraph(it.value, font3)))
        }

        insuranceTableInfo.rows.forEach { row ->
            row.cells.forEach {
                it.setPadding(12.5f)
                it.horizontalAlignment = Element.ALIGN_CENTER
                it.verticalAlignment = Element.ALIGN_CENTER
            }
        }

        document.add(insuranceTableInfo)

        document.add(Chunk.NEWLINE)
        document.add(Chunk.NEWLINE)

        val totalPrice = Paragraph("Total price is: $", boldFont2)
        totalPrice.add(Chunk(price, font2))
        totalPrice.alignment = Element.ALIGN_RIGHT
        document.add(totalPrice)

        document.close()
    }
}

fun main() {
    SwingUtilities.invokeLater {
        CarInsurance()
    }
}



