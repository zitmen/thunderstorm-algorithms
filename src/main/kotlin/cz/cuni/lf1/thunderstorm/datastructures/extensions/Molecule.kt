package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.datastructures.Distance
import cz.cuni.lf1.thunderstorm.datastructures.Molecule

internal fun createMoleculeDetection(x: Double, y: Double)
        = Molecule(Distance.fromPixels(x), Distance.fromPixels(y))
