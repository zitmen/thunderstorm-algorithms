package cz.cuni.lf1.thunderstorm.algorithms.estimators

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule

public interface Estimator {

    public fun estimatePosition(image: GrayScaleImage, initialEstimate: Molecule): Molecule
}