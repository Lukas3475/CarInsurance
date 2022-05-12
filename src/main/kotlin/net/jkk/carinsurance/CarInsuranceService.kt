package net.jkk.carinsurance

import net.sf.clipsrules.jni.*
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.util.stream.IntStream
import javax.swing.*
import javax.swing.border.TitledBorder

class CarInsuranceService(private val actionListener: ActionListener, private val cars: MutableList<Car>) {

    private val carBrandNames = getCarBrands(cars)
    private val carModelNames = getCorrectModelForBrand(cars)

    private val productionYearNames = populateYears()
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
            panel = setComboBox(carBrandNames, CarInsurance.CAR_BRAND, panel)
            panel = setComboBox(carModelNames, CarInsurance.CAR_MODEL, panel)
            panel = setComboBox(productionYearNames, CarInsurance.PRODUCTION_YEAR, panel)
            panel = setComboBox(engineCapacityName, CarInsurance.ENGINE_CAPACITY, panel)
        } else {
            panel = setComboBox(licenceYearNames, CarInsurance.LICENCE_YEAR, panel)
            panel = setComboBox(maritalStatusName, CarInsurance.MARITAL_STATUS, panel)
            panel = setComboBox(regularCustomerAgeNames, CarInsurance.REGULAR_CUSTOMER_AGE, panel)
            panel = setComboBox(hadAccidentNames, CarInsurance.HAD_ACCIDENT, panel)
        }
        return panel
    }

    fun changeBrandModel(carBrand: String, panel: JPanel) {
        var box : JComboBox<*>
        panel.components.forEach {
            if (it.name == CarInsurance.CAR_MODEL) {
                box = it as JComboBox<*>
                box.removeAllItems()
                val carModels = DefaultComboBoxModel(getCorrectModelForBrand(cars, carBrand))
                box.model = carModels
            }
        }
    }

    private fun getCorrectModelForBrand(cars: MutableList<Car>, carBrand: String = ""): Array<String> {
        val carModels: MutableList<String> = ArrayList()
        carModels.add("")
        cars.forEach { car ->
            if (car.carBrand == carBrand) {
                carModels.add(car.carModel)
            }
        }
        return carModels.toTypedArray()
    }

    private fun getCarBrands(cars: MutableList<Car>): Array<String> {
        val carBrands: MutableList<String> = ArrayList()
        carBrands.add("")
        carBrands.addAll(cars.map { it.carBrand }.distinct())
        return carBrands.toTypedArray()
    }
}

enum class InfoType {
    CAR_INFO, DRIVER_INFO
}