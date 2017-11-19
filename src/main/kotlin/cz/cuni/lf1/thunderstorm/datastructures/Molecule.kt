package cz.cuni.lf1.thunderstorm.datastructures

public class Molecule(
        public val xPos: Distance,
        public val yPos: Distance,
        public val zPos: Distance = Distance.fromNanoMeters(0.0),
        public val params: MutableMap<String, PhysicalQuantity?> = hashMapOf()) {

    public var intensity: PhysicalQuantity?
        get() = params["I"]
        set(value) { params["I"] = value }

    public var sigma: PhysicalQuantity?
        get() = params["S"]
        set(value) { params["S"] = value }

    public var offset: PhysicalQuantity?
        get() = params["O"]
        set(value) { params["O"] = value }

    public var background: PhysicalQuantity?
        get() = params["B"]
        set(value) { params["B"] = value }
}
