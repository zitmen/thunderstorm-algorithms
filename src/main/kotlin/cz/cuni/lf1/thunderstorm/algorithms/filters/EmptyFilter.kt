package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.IFilter
import cz.cuni.lf1.thunderstorm.datastructures.Image

public class EmptyFilter : IFilter {

    public override fun filter(image: Image)
        = image
}
