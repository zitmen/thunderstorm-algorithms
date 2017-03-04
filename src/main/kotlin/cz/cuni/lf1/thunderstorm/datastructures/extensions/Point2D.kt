package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.datastructures.Point2D
import cz.cuni.lf1.thunderstorm.datastructures.Point2DImpl

internal fun createPoint2D(x: Double, y: Double): Point2D
        = Point2DImpl(x, y)

internal operator fun Point2D.plus(pt: Point2D): Point2D
        = Point2DImpl(getX() + pt.getX(), getY() + pt.getY())