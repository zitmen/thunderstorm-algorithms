package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.algorithms.IDetector
import cz.cuni.lf1.thunderstorm.datastructures.Image
import cz.cuni.lf1.thunderstorm.datastructures.Point

public class EmptyDetector : IDetector {

    public override fun detect(image: Image)
            = listOf<Point>()
}
