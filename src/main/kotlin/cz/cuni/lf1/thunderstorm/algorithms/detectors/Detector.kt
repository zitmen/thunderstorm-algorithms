package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule

public interface Detector {

    public fun detect(image: GrayScaleImage): List<Molecule>
}
