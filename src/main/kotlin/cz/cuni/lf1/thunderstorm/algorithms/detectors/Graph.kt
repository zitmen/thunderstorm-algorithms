package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Point2D
import cz.cuni.lf1.thunderstorm.datastructures.Point2DImpl
import cz.cuni.lf1.thunderstorm.datastructures.extensions.mean

import java.util.LinkedList
import java.util.Vector

/**
 * Representation of a connected component.

 * A connected component consists of nodes, int this particular case
 * those are pixels. All the pixels are stored in Point structures.
 */
class ConnectedComponent {


    public val points = Vector<Point2D>()

    /**
     * Calculate centroid of all points stored in the component.

     * The centroid is calculated simply as mean value of X,Y position
     * and the intensity of the centroid is calculated as sum of all the
     * nodes in the component.

     * @return a **new instance** of Point class representing
     * *         the calculated centroid
     */
    fun centroid(): Point2D {
        val npts = points.size
        val xarr = Array<Double>(npts, { 0.0 })
        val yarr = Array<Double>(npts, { 0.0 })

        var i = 0
        val im = npts
        while (i < im) {
            val p = points.elementAt(i)
            xarr[i] = p.getX()
            yarr[i] = p.getY()
            i++
        }
        return Point2DImpl(xarr.mean(), yarr.mean())
    }
}

/**
 * South, North, East, and West pixels are connected.
 * <pre>
 * `.|.
 * - -
 * .|.`
</pre> *
 * Note that the dots are here used just for sake of formatting.
 */
val CONNECTIVITY_4 = 4
/**
 * South, North, East, West, SouthWest, SouthEast, NorthWest, and NorthEast pixels are connected.
 * <pre>
 * `\|/
 * - -
 * /|\`
</pre> *
 */
val CONNECTIVITY_8 = 8

/**
 * Get connected components in image.
 *
 * Take an input `image` as an undirected graph where the pixels with value greater than 0 are
 * considered to be nodes. The edges between them are created accorging to the specified `connectivity` model.
 * Then find [connected components](http://en.wikipedia.org/wiki/Connected_component_(graph_theory))
 * as defined in graph theory.
 * @param connectivity one of the connectivity models (`CONNECTIVITY_4` or `CONNECTIVITY_8`)
 * @return Vector of ConnectedComponents
 *
 * @see ConnectedComponent
 *
 * @todo This method is much slower than it could be because of too many allocations!
 */
fun GrayScaleImage.getConnectedComponents(connectivity: Int): Vector<ConnectedComponent> {
    assert(connectivity == CONNECTIVITY_4 || connectivity == CONNECTIVITY_8)

    val map = Array(getWidth()) { IntArray(getHeight()) }
    for (x in map.indices) {
        for (y in 0..map[x].size - 1) {
            map[x][y] = 0 // member of no component
        }
    }

    var p: Point2D
    var c: ConnectedComponent
    val components = Vector<ConnectedComponent>()
    val queue = LinkedList<Point2D>()

    var counter = 0
    var n: Boolean
    var s: Boolean
    var w: Boolean
    var e: Boolean
    for (x in map.indices) {
        for (y in 0..map[x].size - 1) {
            if (map[x][y] > 0) continue // already member of another component
            if (getValue(y, x) == 0.0) continue    // disabled pixel

            // new component
            counter++
            queue.clear()
            queue.push(Point2DImpl(x.toDouble(), y.toDouble()))
            c = ConnectedComponent()

            while (!queue.isEmpty()) {
                p = queue.pop()
                val px = p.getX().toInt()
                val py = p.getY().toInt()

                if (map[px][py] > 0) continue // already member of another component
                if (getValue(py, px) == 0.0) continue    // disabled pixel

                map[px][py] = counter

                c.points.add(Point2DImpl(p.getX(), p.getY()))

                w = px > 0  // west
                n = py > 0  // north
                e = px < map.size - 1  // east
                s = py < map[px].size - 1  // south

                if (w) queue.push(Point2DImpl((px - 1).toDouble(), py.toDouble()))  // west
                if (n) queue.push(Point2DImpl(px.toDouble(), (py - 1).toDouble()))  // north
                if (e) queue.push(Point2DImpl((px + 1).toDouble(), py.toDouble()))  // east
                if (s) queue.push(Point2DImpl(px.toDouble(), (py + 1).toDouble()))  // south

                if (connectivity == CONNECTIVITY_8) {
                    if (n && w) queue.push(Point2DImpl((px - 1).toDouble(), (py - 1).toDouble()))  // north west
                    if (n && e) queue.push(Point2DImpl((px + 1).toDouble(), (py - 1).toDouble()))  // north east
                    if (s && w) queue.push(Point2DImpl((px - 1).toDouble(), (py + 1).toDouble()))  // south west
                    if (s && e) queue.push(Point2DImpl((px + 1).toDouble(), (py + 1).toDouble()))  // south east
                }
            }
            components.add(c)
        }
    }
    return components
}