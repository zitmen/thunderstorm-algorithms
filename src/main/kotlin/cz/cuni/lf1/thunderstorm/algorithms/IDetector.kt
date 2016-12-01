package cz.cuni.lf1.thunderstorm.algorithms

import cz.cuni.lf1.thunderstorm.datastructures.Image
import cz.cuni.lf1.thunderstorm.datastructures.Point

public interface IDetector {

    public fun detect(image: Image): List<Point>
}
