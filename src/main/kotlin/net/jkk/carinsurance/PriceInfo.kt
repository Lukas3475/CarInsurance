package net.jkk.carinsurance

data class PriceInfo(
    var infoType: String = "",
    var desc: String = "",
    var value: Double = 0.0
){
    override fun toString(): String {
        return "PriceInfo(infoType='$infoType', desc='$desc', value=$value)"
    }
}
