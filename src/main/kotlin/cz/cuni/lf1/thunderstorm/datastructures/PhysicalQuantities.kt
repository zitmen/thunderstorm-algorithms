package cz.cuni.lf1.thunderstorm.datastructures

public enum class PhysicalUnits {
    NANOMETERS,
    PIXELS,
    PHOTONS,
    AD_COUNTS
}

public interface PhysicalQuantity {

    public fun getValue(): Double
    public fun getUnit(): PhysicalUnits
}

public class Distance private constructor(
        private val value: Double,
        private val unit: PhysicalUnits)
    : PhysicalQuantity {

    public override fun getValue() = value
    public override fun getUnit() = unit

    companion object {
        public fun fromNanoMeters(value: Double) = Distance(value, PhysicalUnits.NANOMETERS)
        public fun fromPixels(value: Double) = Distance(value, PhysicalUnits.PIXELS)
    }
}

public class Intensity private constructor(
        private val value: Double,
        private val unit: PhysicalUnits)
    : PhysicalQuantity {

    public override fun getValue() = value
    public override fun getUnit() = unit

    companion object {
        public fun fromPhotons(value: Double) = Intensity(value, PhysicalUnits.PHOTONS)
        public fun fromAdCounts(value: Double) = Intensity(value, PhysicalUnits.AD_COUNTS)
    }
}