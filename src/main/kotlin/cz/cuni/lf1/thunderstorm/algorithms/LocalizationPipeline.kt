package cz.cuni.lf1.thunderstorm.algorithms

import cz.cuni.lf1.thunderstorm.algorithms.detectors.Detector
import cz.cuni.lf1.thunderstorm.algorithms.estimators.Estimator
import cz.cuni.lf1.thunderstorm.algorithms.filters.Filter
import cz.cuni.lf1.thunderstorm.datastructures.Distance
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule

public object LocalizationPipeline {

    public fun runLocalization(rawImage: GrayScaleImage, filter: Filter, detector: Detector, estimator: Estimator): List<Molecule>
        = detector.detect(filter.filter(rawImage))
                .map {
                    estimator.estimatePosition(
                            rawImage,
                            Molecule(Distance.fromPixels(it.getX()),
                                    Distance.fromPixels(it.getY())))
                }
}