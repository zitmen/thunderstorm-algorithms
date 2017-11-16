package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.datastructures.Distance
import cz.cuni.lf1.thunderstorm.datastructures.Intensity
import cz.cuni.lf1.thunderstorm.datastructures.Molecule

internal fun createMoleculeDetection(x: Double, y: Double)
        = Molecule(Distance.fromPixels(x), Distance.fromPixels(y))

internal fun createMoleculeDetection(x: Double, y: Double, intensity: Double)
        = Molecule(Distance.fromPixels(x), Distance.fromPixels(y)).also {
            it.setIntensity(Intensity.fromAdCounts(intensity))
        }