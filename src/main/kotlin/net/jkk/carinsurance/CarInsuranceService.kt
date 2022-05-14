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


    //populating array for combo box
    private fun populateYears(): Array<String> {
        val years = ArrayList<String>()
        years.add("")
        IntStream.range(2000, 2023).forEach { year: Int -> years.add(year.toString()) }
        return years.toTypedArray()
    }

    //setting combo box for window
    private fun setComboBox(names: Array<String>, name: String, panel: JPanel): JPanel {
        panel.add(JLabel(name))
        val comboBox: JComboBox<String> = JComboBox(names)
        comboBox.name = name
        panel.add(comboBox)
        comboBox.addActionListener(actionListener)
        return panel
    }

    //creating panels for window
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

    //checking if all combo boxes are set in panels
    fun isAllComboBoxesSet(carInfoPanel: JPanel, driverInfoPanel: JPanel): Boolean {
        return carInfoPanel.components.filterIsInstance(JComboBox::class.java)
            .all { it.selectedItem != "" && it.selectedItem != null } &&
                driverInfoPanel.components.filterIsInstance(JComboBox::class.java)
                    .all { it.selectedItem != "" && it.selectedItem != null }
    }

    //getting car from panel
    private fun getCar(carInfoPanel: JPanel): Car {
        var car = Car()
        carInfoPanel.components.filterIsInstance(JComboBox::class.java).forEach {
            when (it.name) {
                CarInsurance.CAR_MODEL -> car = cars.find { car -> car.carModel == it.selectedItem as String }!!
            }
        }
        return car
    }

    //setting band models for given brand in panel
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

    //getting model names for given brand model
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

    //getting car brands from given car list
    private fun getCarBrands(cars: MutableList<Car>): Array<String> {
        val carBrands: MutableList<String> = ArrayList()
        carBrands.add("")
        carBrands.addAll(cars.map { it.carBrand }.distinct())
        return carBrands.toTypedArray()
    }

    //creating info panel for displaying insurance info
    fun crateInfoPanel(infoPanel: JPanel, priceInfoList: MutableList<PriceInfo>, carInfoPanel: JPanel): JPanel {
        infoPanel.layout = GridLayout(priceInfoList.size + 1, 1)
        val priceLabel = JLabel("Insurance price: $${getPrice(carInfoPanel)}")
        priceLabel.verticalAlignment = SwingConstants.CENTER
        priceLabel.horizontalAlignment = SwingConstants.CENTER
        infoPanel.add(priceLabel)
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

    //removing duplicated attributes in CLIPs environment
    fun removeDuplicatedAttributes() {
        val variableArray = arrayListOf(CarInsurance.PRODUCTION_YEAR,
            CarInsurance.ENGINE_CAPACITY,
            CarInsurance.LICENCE_YEAR,
            CarInsurance.MARITAL_STATUS,
            CarInsurance.HAD_ACCIDENT,
            CarInsurance.REGULAR_CUSTOMER_AGE)
        variableArray.forEach {
            env.eval("(remove-duplicated-attributes $it)")
        }
    }

    //getting price info from CLIPs environment
    fun getPriceInfo(): MutableList<PriceInfo> {
        val multiField: MultifieldValue = env.eval("(get-price-info)") as MultifieldValue
        val priceInfoList: MutableList<PriceInfo> = ArrayList()
        for (field in multiField) {
            val factValue: FactAddressValue = field as FactAddressValue
            val infoType: String = (factValue.getSlotValue("info-type") as StringValue).value
            val desc: String = (factValue.getSlotValue("desc") as StringValue).value
            val value: String = (factValue.getSlotValue("value") as StringValue).value
            priceInfoList.add(PriceInfo(infoType, desc, value))
        }
        return priceInfoList
    }

    //getting price after calculating in CLIPs environment
    fun getPrice(carInfoPanel: JPanel): String {
        val car = getCar(carInfoPanel)
        env.eval("(calculate-insurance ${car.carBrand} ${car.carModel} ${car.basePrice})")
        val multiField: MultifieldValue = env.eval("(get-price-after-calculations)") as MultifieldValue
        var price = 0.0
        for (field in multiField) {
            val factValue: FactAddressValue = field as FactAddressValue
            price = (factValue.getSlotValue("changed-price") as NumberValue).doubleValue()
        }
        return String.format("%.2f", price)
    }

    //getting list of cars from CLIPs environment
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