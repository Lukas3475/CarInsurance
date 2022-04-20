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

    val productionYearNames = populateYears()

    val carBrandNames = arrayOf("", "AUDI", "BMW", "OPEL", "FORD", "MAZDA")

    val carAUDIModelNames = arrayOf("", "A1", "A3", "A4", "A5", "A6")
    val carBMWModelNames = arrayOf("", "X1", "X3", "X4", "X5", "X6")
    val carOPELModelNames = arrayOf("", "ASTRA", "CORSA", "INSYGNIA", "MOKKA", "COMBO")
    val carFORDModelNames = arrayOf("", "FIESTA", "ESCAPE", "GALAXY", "FOCUS", "MONDEO")
    val carMAZDAModelNames = arrayOf("", "CX-3", "CX-30", "MX-5", "3", "6")

    private val fuelTypeNames = arrayOf("", "GASOLINE", "OIL", "GAS", "GASOLINE/GAS")

    val engineCapacityName = arrayOf("", "1.0", "1.2", "1.4", "1.6", "1.8", "2.0", "2.2")
    val doorAmountName = arrayOf("", "3", "5")
    val bodyTypeName = arrayOf("", "SUV", "COMBI", "SEDAN", "HATCHBACK", "CABRIOLET", "COUPÉ")

    val birthYearNames = populateBirthLicenceYear()
    val licenceYearNames = populateBirthLicenceYear()

    private val residenceRegionNames = arrayOf("", "DOLNOŚLĄSKIE", "KUJAWSKO-POMORSKIE",
        "LUBELSKIE", "LUBUSKIE", "ŁÓDZKIE", "MAŁOPOLSKIE", "MAZOWIECKIE",
        "OPOLSKIE", "PODKARPACKIE", "PODLASKIE", "POMORSKIE", "ŚLĄSKIE",
        "ŚWIĘTOKRZYSKIE", "WARMIŃSKO-MAZURSKIE", "WIELKOPOLSKIE", "ZACHODNIOPOMORSKIE")

    private val maritalStatusName = arrayOf("", "MARRIED", "DIVORCED", "WIDOW", "SINGLE")

    private fun populateYears(): Array<String> {
        val years = ArrayList<String>()
        years.add("")
        IntStream.range(1990, 2023).forEach { year: Int -> years.add(year.toString()) }
        return years.toTypedArray()
    }

    private fun populateBirthLicenceYear(): Array<String> {
        val years = ArrayList<String>()
        years.add("")
        IntStream.range(1920, 2005).forEach { year: Int -> years.add(year.toString()) }
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
            panel = setComboBox(fuelTypeNames, CarInsurance.FUEL_TYPE, panel)
            panel = setComboBox(engineCapacityName, CarInsurance.ENGINE_CAPACITY, panel)
            panel = setComboBox(doorAmountName, CarInsurance.DOOR_AMOUNT, panel)
            panel = setComboBox(bodyTypeName, CarInsurance.BODY_TYPE, panel)
        } else {
            panel = setComboBox(birthYearNames, CarInsurance.BIRTH_YEAR, panel)
            panel = setComboBox(licenceYearNames, CarInsurance.LICENCE_YEAR, panel)
            panel = setComboBox(residenceRegionNames, CarInsurance.REGION, panel)
            panel = setComboBox(maritalStatusName, CarInsurance.MARITAL_STATUS, panel)
        }
        return panel
    }
}

enum class InfoType {
    CAR_INFO, DRIVER_INFO
}