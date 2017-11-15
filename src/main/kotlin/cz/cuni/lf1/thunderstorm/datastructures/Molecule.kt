package cz.cuni.lf1.thunderstorm.datastructures

public class Molecule(
        public val xPos: Distance,
        public val yPos: Distance,
        public val zPos: Distance = Distance.fromNanoMeters(0.0),
        public val params: Map<String, PhysicalQuantity> = mapOf()
)
