package cz.cuni.lf1.thunderstorm.datastructures

public class Molecule(
        public val xPos: Distance,
        public val yPos: Distance,
        public val zPos: Distance = Distance.fromNanoMeters(0.0),
        public val params: MutableMap<String, PhysicalQuantity> = hashMapOf()) {

    public fun getIntensity() = params["I"]

    public fun setIntensity(intensity: PhysicalQuantity) {
        params["I"] = intensity
    }
}
