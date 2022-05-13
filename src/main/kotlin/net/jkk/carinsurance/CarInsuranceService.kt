package net.jkk.carinsurance

import net.sf.clipsrules.jni.*
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.util.stream.IntStream
import javax.swing.*
import javax.swing.border.TitledBorder

class CarInsuranceService(
    private val actionListener: ActionListener,
    private val env: Environment,
) {

    private val cars = getCars()
    private val carBrandNames = getCarBrands(cars)
    private val carModelNames = getCorrectModelForBrand(cars)

    private val productionYearNames = populateYears()
    private val engineCapacityName = arrayOf("", "1.0", "1.2", "1.4", "1.6", "1.8", "2.0", "2.2")

    private val licenceYearNames = arrayOf("", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val regularCustomerAgeNames = arrayOf("", "1", "2", "3", "4", "5", "6", "7", "8", "9")
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
        } else if (type == InfoType.DRIVER_INFO) {
            panel = setComboBox(licenceYearNames, CarInsurance.LICENCE_YEAR, panel)
            panel = setComboBox(maritalStatusName, CarInsurance.MARITAL_STATUS, panel)
            panel = setComboBox(regularCustomerAgeNames, CarInsurance.REGULAR_CUSTOMER_AGE, panel)
            panel = setComboBox(hadAccidentNames, CarInsurance.HAD_ACCIDENT, panel)
        } else if (type == InfoType.BUTTON_INFO) {
            panel.add(JButton("Reset"))
        }
        return panel
    }

    fun isAllComboBoxesSet(carInfoPanel: JPanel, driverInfoPanel: JPanel): Boolean {
        return carInfoPanel.components.filterIsInstance(JComboBox::class.java)
            .all { it.selectedItem != "" && it.selectedItem != null } &&
                driverInfoPanel.components.filterIsInstance(JComboBox::class.java)
                    .all { it.selectedItem != "" && it.selectedItem != null }
    }

    private fun getCar(carInfoPanel: JPanel): Car {
        var car = Car()
        carInfoPanel.components.filterIsInstance(JComboBox::class.java).forEach {
            when (it.name) {
                CarInsurance.CAR_MODEL -> car = cars.find { car -> car.carModel == it.selectedItem as String }!!
            }
        }
        return car
    }

    fun changeBrandModel(carBrand: String, panel: JPanel) {
        var box: JComboBox<*>
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

    fun getPriceInfo(): MutableList<PriceInfo> {
        val multiField: MultifieldValue = env.eval("(get-price-info)") as MultifieldValue
        val priceInfoList: MutableList<PriceInfo> = ArrayList()
        for (field in multiField) {
            val factValue: FactAddressValue = field as FactAddressValue
            val infoType: String = (factValue.getSlotValue("info-type") as StringValue).value
            val desc: String = (factValue.getSlotValue("desc") as StringValue).value
            val value: Double = (factValue.getSlotValue("value") as NumberValue).doubleValue()
            priceInfoList.add(PriceInfo(infoType, desc, value))
        }
        return priceInfoList
    }



    fun crateInfoPanel(infoPanel: JPanel, priceInfoList: MutableList<PriceInfo>): JPanel {
        infoPanel.layout = GridLayout(priceInfoList.size, 1)
        priceInfoList.forEach { priceInfo ->
            val infoTile = JPanel()
            infoTile.layout = GridLayout(1, 3)
            infoTile.border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "",
                TitledBorder.CENTER,
                TitledBorder.ABOVE_TOP)

            val infoTypeLabel = JLabel(priceInfo.infoType)
            val descLabel = JLabel(priceInfo.desc)
            val valueLabel = JLabel(priceInfo.value.toString())
            infoTypeLabel.horizontalAlignment = SwingConstants.CENTER
            descLabel.horizontalAlignment = SwingConstants.CENTER
            valueLabel.horizontalAlignment = SwingConstants.CENTER
            infoTypeLabel.verticalAlignment = SwingConstants.CENTER
            descLabel.verticalAlignment = SwingConstants.CENTER
            valueLabel.verticalAlignment = SwingConstants.CENTER

            infoTile.add(infoTypeLabel)
            infoTile.add(descLabel)
            infoTile.add(valueLabel)
            infoPanel.add(infoTile)
        }
        return infoPanel
    }

    fun getPrice(carInfoPanel: JPanel): Double {
        val car = getCar(carInfoPanel)
        env.eval("(calculate-insurance ${car.carBrand} ${car.carModel} ${car.basePrice})")
        val multiField: MultifieldValue = env.eval("(get-price-after-calculations)") as MultifieldValue
        var price: Double = 0.0
        for (field in multiField) {
            val factValue: FactAddressValue = field as FactAddressValue
            price = (factValue.getSlotValue("changed-price") as NumberValue).doubleValue()
        }
        return price
    }

    private fun getCars(): MutableList<Car> {
        val stringEval = "(get-all-cars)"
        val multiField: MultifieldValue = env.eval(stringEval) as MultifieldValue
        val cars: MutableList<Car> = ArrayList()
        for (field in multiField) {
            val factValue: FactAddressValue = field as FactAddressValue
            val carBrand: String = (factValue.getSlotValue("carBrand") as LexemeValue).value
            val carModel: String = (factValue.getSlotValue("carModel") as LexemeValue).value
            val basePrice: Double = (factValue.getSlotValue("basePrice") as NumberValue).doubleValue()
            cars.add(Car(carBrand, carModel, basePrice))
        }
        return cars
    }
}

enum class InfoType {
    CAR_INFO, DRIVER_INFO, BUTTON_INFO
}