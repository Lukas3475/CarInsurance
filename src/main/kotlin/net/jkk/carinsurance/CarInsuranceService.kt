package net.jkk.carinsurance

import java.awt.GridLayout
import java.awt.event.ActionListener
import java.util.stream.IntStream
import javax.swing.BorderFactory
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.TitledBorder

class CarInsuranceService(private val actionListener: ActionListener) {

    private val productionYearNames = populateYears()

    private val carBrandNames = arrayOf("", "AUDI", "BMW", "OPEL")

    private val carAUDIModelNames = arrayOf("", "A1", "A3", "A4")
    private val carBMWModelNames = arrayOf("", "X1", "X3", "X4")
    private val carOPELModelNames = arrayOf("", "ASTRA", "CORSA", "INSIGNIA")

    private val engineCapacityName = arrayOf("", "1.0", "1.2", "1.4", "1.6", "1.8", "2.0", "2.2")

    private val licenceYearNames = arrayOf("", "1", "2", "3", "4", "5", "6", "7", "8", "9", "MORE")
    private val regularCustomerAgeNames = arrayOf("", "1", "2", "3", "4", "5", "6", "7", "8", "9", "MORE")
    private val hadAccidentNames = arrayOf("", "YES", "NO")

    private val maritalStatusName = arrayOf("", "MARRIED", "DIVORCED", "WIDOW", "SINGLE")

    private fun populateYears(): Array<String> {
        val years = ArrayList<String>()
        years.add("")
        IntStream.range(2000, 2023).forEach { year: Int -> years.add(year.toString()) }
        return years.toTypedArray()
    }

    private fun setComboBox(names: Array<String>, name: String, panel: JPanel): JPanel {
        panel.add(JLabel(name))
        val comboBox: JComboBox<String> = JComboBox(names)
        comboBox.name = name
        panel.add(comboBox)
        comboBox.addActionListener(actionListener)
        return panel
    }

    fun createPanel(panelName: String, rows: Int, cols: Int, type: InfoType): JPanel {
        var panel = JPanel()
        panel.layout = GridLayout(rows, cols)
        panel.border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
            panelName,
            TitledBorder.CENTER,
            TitledBorder.ABOVE_TOP)
        panel.name = panelName
        if (type == InfoType.CAR_INFO) {
            panel = setComboBox(productionYearNames, CarInsurance.PRODUCTION_YEAR, panel)
            panel = setComboBox(carBrandNames, CarInsurance.CAR_BRAND, panel)
            panel = setComboBox(arrayOf(), CarInsurance.CAR_MODEL, panel)
            panel = setComboBox(engineCapacityName, CarInsurance.ENGINE_CAPACITY, panel)
        } else {
            panel = setComboBox(licenceYearNames, CarInsurance.LICENCE_YEAR, panel)
            panel = setComboBox(maritalStatusName, CarInsurance.MARITAL_STATUS, panel)
        }
        return panel
    }
}

enum class InfoType {
    CAR_INFO, DRIVER_INFO
}