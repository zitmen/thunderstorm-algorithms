package cz.cuni.lf1.thunderstorm.datastructures.extensions.watershed

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray
import ij.gui.Roi
import ij.process.ByteProcessor
import ij.process.ImageProcessor
import java.util.*

public val DIR_X_OFFSET = intArrayOf(0, 1, 1, 1, 0, -1, -1, -1)
public val DIR_Y_OFFSET = intArrayOf(-1, -1, 0, 1, 1, 1, 0, -1)

private val MAXIMUM = 1            // marks local maxima (irrespective of noise tolerance)
private val LISTED = 2             // marks points currently in the list
private val PROCESSED = 4          // marks points processed previously
private val MAX_AREA = 8           // marks areas near a maximum, within the tolerance
private val EQUAL = 16             // marks contigous maximum points of equal level
private val MAX_POINT = 32         // marks a single point standing for a maximum
private val ELIMINATED = 64        // marks maxima that have been eliminated before watershed


public fun isWithin(x: Int, y: Int, direction: Int, width: Int, height: Int)
        = when (direction) {
            0 -> y > 0   // top
            1 -> x < width - 1 && y > 0 // top right
            2 -> x < width - 1    // right
            3 -> x < width - 1 && y < height - 1  // bottom right
            4 -> y < height - 1    // bottom
            5 -> x > 0 && y < height - 1 // bottom left
            6 -> x > 0   // left
            7 -> x > 0 && y > 0    // top left
            else -> false
        }


object WatershedFunctions {

    /**
     * After watershed, set all pixels in the background and segmentation lines to 0
     */
    public fun watershedPostProcess(ip: ImageProcessor) {
        val pixels = ip.pixels as ByteArray
        val size = ip.width * ip.height
        (0..size - 1)
                .filter { pixels[it].toInt() and 255 < 255 }
                .forEach { pixels[it] = 0.toByte() }
    }

    /** delete a line starting at x, y up to the next (4-connected) vertex  */
    public fun removeLineFrom(pixels: Array<Array<Double>>, xx: Int, yy: Int, width: Int, height: Int) {

        var x = xx
        var y = yy
        pixels[y][x] = 255.0                        //delete the first point
        var continues: Boolean
        do {
            continues = false
            val isInner = y != 0 && y != height - 1 && x != 0 && x != width - 1 //not necessary, but faster than isWithin
            var d = 0
            while (d < 8) {                          //analyze 4-connected neighbors
                if (isInner || isWithin(x, y, d, width, height)) {
                    val v = pixels[y + DIR_Y_OFFSET[d]][x + DIR_X_OFFSET[d]].toInt()
                    if (v != 255 && v != 0) {
                        val nRadii = nRadii(pixels, x + DIR_X_OFFSET[d], y + DIR_Y_OFFSET[d], width, height)
                        if (nRadii <= 1) {                        //found a point or line end
                            x += DIR_X_OFFSET[d]
                            y += DIR_Y_OFFSET[d]
                            pixels[y][x] = 255.0    //delete the point
                            continues = nRadii == 1              //continue along that line
                            break
                        }
                    }
                }
                d += 2
            } // for directions d
        } while (continues)
    } // void removeLineFrom


    /**
     * Create an 8-bit image by scaling the pixel values of ip to 1-254 (<lower threshold 0) and mark maximum areas as 255. For use as input for watershed segmentation @param ip The original image that should be segmented * @param typeP Pixel types in ip * @param globalMin The minimum pixel value of ip * @param globalMax The maximum pixel value of ip * @return The 8-bit output image.></lower>
     */
    public fun make8bit(ip: GrayScaleImage, typeP: GrayScaleImage, globalMin: Double, globalMax: Double): GrayScaleImage {
        val offset = globalMin - (globalMax - globalMin) * (1.0 / 253.0 / 2.0 - 1e-6) //everything above minValue should become >(byte)0
        val factor = 253 / (globalMax - globalMin)
        val pixels = create2DDoubleArray(ip.getWidth(), ip.getHeight(), 0.0)

        //convert possibly calibrated image to byte without damaging threshold (setMinAndMax would kill threshold)
        var v: Long
        var y = 0
        while (y < ip.getHeight()) {
            var x = 0
            while (x < ip.getWidth()) {
                val rawValue = ip.getValue(y, x)
                if (typeP.getValue(y, x).toInt() and MAX_AREA != 0)
                    pixels[y][x] = 255.0  //prepare watershed by setting "true" maxima+surroundings to 255
                else {
                    v = 1 + Math.round((rawValue - offset) * factor)
                    if (v < 1)
                        pixels[y][x] = 1.0
                    else if (v <= 254)
                        pixels[y][x] = (v and 255).toDouble()
                    else
                        pixels[y][x] = 254.toDouble()
                }
                x++
            }
            y++
        }
        return GrayScaleImageImpl(pixels)
    } // byteProcessor make8bit


    public fun findLocalMaxima(ip: GrayScaleImage, typeP: GrayScaleImage, globalMin: Double): GrayScaleImage {

        val types = create2DDoubleArray(typeP.getHeight(), typeP.getWidth(), { r, c -> typeP.getValue(r, c) })
        for (y in 0..(ip.getHeight()-1)) {         // find local maxima now
            for (x in 0..(ip.getWidth()-1)) {      // for better performance with rois, restrict search to roi
                val v = ip.getValue(y, x)
                if (v == globalMin) {
                    continue
                }
                /* check wheter we have a local maximum.
                 Note: For an EDM, we need all maxima: those of the EDM-corrected values
                 (needed by findMaxima) and those of the raw values (needed by cleanupMaxima) */
                val isInner = y != 0 && y != ip.getHeight() - 1 && x != 0 && x != ip.getWidth() - 1 //not necessary, but faster than isWithin
                val isMax = (0..7)
                        .filter { // compare with the 8 neighbor pixels
                            isInner || isWithin(x, y, it, ip.getWidth(), ip.getHeight())
                        }
                        .map { ip.getValue(x + DIR_X_OFFSET[it], y + DIR_Y_OFFSET[it]) }
                        .none { it > v && it > v }
                if (isMax) {
                    types[y][x] = MAXIMUM.toDouble()
                }
            }
        }
        return GrayScaleImageImpl(types)
    }


    /** Find all local maxima (irrespective whether they finally qualify as maxima or not)
     * @param ip    The image to be analyzed
     * *
     * @param typeP A byte image, same size as ip, where the maximum points are marked as MAXIMUM
     * *              (do not use it as output: for rois, the points are shifted w.r.t. the input image)
     * *
     * @param globalMin The minimum value of the image or roi
     * *
     * @return          Maxima sorted by value. In each array element (long, i.e., 64-bit integer), the value
     * *                  is encoded in the upper 32 bits and the pixel offset in the lower 32 bit
     * * Note: Do not use the positions of the points marked as MAXIMUM in typeP, they are invalid for images with a roi.
     */
    public fun getSortedMaxPoints(ip: GrayScaleImage, typeP: GrayScaleImage, globalMin: Double, globalMax: Double): Array<Long> {
        val vFactor = (2e9 / (globalMax - globalMin)).toFloat() //for converting float values into a 32-bit int
        val maxPoints = ArrayList<Long>()                  //value (int) is in the upper 32 bit, pixel offset in the lower
        for (y in 0..(ip.getHeight()-1))
        {   //enter all maxima into an array
            for (x in 0..(ip.getWidth()-1)) {
                if (typeP.getValue(y, x).toInt() == MAXIMUM) {
                    val iValue = ((ip.getValue(y, x) - globalMin).toFloat() * vFactor).toInt() //32-bit int, linear function of float value
                    val p = x + y * ip.getWidth()
                    maxPoints.add(iValue.toLong() shl 32 or p.toLong())
                }
            }
        }
        maxPoints.sort()
        return maxPoints.toTypedArray()
    } //getSortedMaxPoints

    /** Creates the lookup table used by the watershed function for dilating the particles.
     * The algorithm allows dilation in both straight and diagonal directions.
     * There is an entry in the table for each possible 3x3 neighborhood:
     * x-1          x          x+1
     * y-1    128            1          2
     * y       64     pxl_unset_yet     4
     * y+1     32           16          8
     * (to find throws entry, sum up the numbers of the neighboring pixels set; e.g.
     * entry 6=2+4 if only the pixels (x,y-1) and (x+1, y-1) are set.
     * A pixel is added on the 1st pass if bit 0 (2^0 = 1) is set,
     * on the 2nd pass if bit 1 (2^1 = 2) is set, etc.
     * pass gives the direction of rotation, with 0 = to top left (x--,y--), 1 to top,
     * and clockwise up to 7 = to the left (x--).
     * E.g. 4 = add on 3rd pass, 3 = add on either 1st or 2nd pass.
     */
    public fun makeFateTable(): IntArray {
        val table = IntArray(256)
        val isSet = BooleanArray(8)
        for (item in 0..255) {        //dissect into pixels
            run {
                var i = 0
                var mask = 1
                while (i < 8) {
                    isSet[i] = item and mask == mask
                    mask *= 2
                    i++
                }
            }
            run {
                var i = 0
                var mask = 1
                while (i < 8) {       //we dilate in the direction opposite to the direction of the existing neighbors
                    if (isSet[(i + 4) % 8]) table[item] = table[item] or mask
                    mask *= 2
                    i++
                }
            }
            run {
                var i = 0
                while (i < 8) {
                    //if side pixels are set, for counting transitions it is as good as if the adjacent edges were also set
                    if (isSet[i]) {
                        isSet[(i + 1) % 8] = true
                        isSet[(i + 7) % 8] = true
                    }
                    i += 2
                }
            }
            val transitions = (0..7).count { isSet[it] != isSet[(it + 1) % 8] }
            if (transitions >= 4) {                   //if neighbors contain more than one region, dilation ito this pixel is forbidden
                table[item] = 0
            }
        }
        return table
    } // int[] makeFateTable

    /** dilate the UEP on one level by one pixel in the direction specified by step, i.e., set pixels to 255
     * @param pass gives direction of dilation, see makeFateTable
     * *
     * @param ip the EDM with the segmeted blobs successively getting set to 255
     * *
     * @param fateTable             The fateTable
     * *
     * @param levelStart        offsets of the level in pixelPointers[]
     * *
     * @param levelNPoints      number of points in the current level
     * *
     * @param coordinates   list of pixel coordinates (x+y*width) sorted by level (in sequence of y, x within each level)
     * *
     * @param setPointList      list of x Coorinates for the current level only (no offset levelStart)
     * *
     * @return                  number of pixels that have been changed
     */
    public fun processLevel(pass: Int, ip: ImageProcessor, fateTable: IntArray,
                             levelStart: Int, levelNPoints: Int, coordinates: IntArray, setPointList: IntArray): Int {

        val width = ip.width
        var shift = 0
        var mult = 1
        do {
            shift++
            mult *= 2
        } while (mult < width)
        val intEncodeXMask = mult - 1
        val intEncodeYMask = intEncodeXMask.inv()
        val intEncodeShift = shift


        val xmax = ip.width - 1
        val ymax = ip.height - 1
        val pixels = ip.pixels as ByteArray
        var nChanged = 0
        var nUnchanged = 0
        run {
            var i = 0
            var p = levelStart
            while (i < levelNPoints) {
                val xy = coordinates[p]
                val x = xy and intEncodeXMask
                val y = xy and intEncodeYMask shr intEncodeShift
                val offset = x + y * ip.width
                var index = 0      //neighborhood pixel ocupation: index in fateTable
                if (y > 0 && pixels[offset - ip.width].toInt() and 255 == 255)
                    index = index xor 1
                if (x < xmax && y > 0 && pixels[offset - ip.width + 1].toInt() and 255 == 255)
                    index = index xor 2
                if (x < xmax && pixels[offset + 1].toInt() and 255 == 255)
                    index = index xor 4
                if (x < xmax && y < ymax && pixels[offset + ip.width + 1].toInt() and 255 == 255)
                    index = index xor 8
                if (y < ymax && pixels[offset + ip.width].toInt() and 255 == 255)
                    index = index xor 16
                if (x > 0 && y < ymax && pixels[offset + ip.width - 1].toInt() and 255 == 255)
                    index = index xor 32
                if (x > 0 && pixels[offset - 1].toInt() and 255 == 255)
                    index = index xor 64
                if (x > 0 && y > 0 && pixels[offset - ip.width - 1].toInt() and 255 == 255)
                    index = index xor 128
                val mask = 1 shl pass
                if (fateTable[index] and mask == mask)
                    setPointList[nChanged++] = offset  //remember to set pixel to 255
                else
                    coordinates[levelStart + nUnchanged++] = xy //keep this pixel for future passes
                i++
                p++

            }
        } // for pixel i
        for (i in 0..nChanged - 1)
            pixels[setPointList[i]] = 255.toByte()
        return nChanged
    } //processLevel

    /** Do watershed segmentation on a byte image, with the start points (maxima)
     * set to 255 and the background set to 0. The image should not have any local maxima
     * other than the marked ones. Local minima will lead to artifacts that can be removed
     * later. On output, all particles will be set to 255, segmentation lines remain at their
     * old value.
     * @param ip  The byteProcessor containing the image, with size given by the class variables width and height
     * *
     * @return    false if canceled by the user (note: can be cancelled only if called by "run" with a known ImagePlus)
     */
    public fun watershedSegment(ip: ByteProcessor) {

        val width = ip.width

        val dirOffset = intArrayOf(-width, -width + 1, +1, +width + 1, +width, +width - 1, -1, -width - 1)

        var shift = 0
        var mult = 1
        do {
            shift++
            mult *= 2
        } while (mult < width)
        val intEncodeXMask = mult - 1
        val intEncodeYMask = intEncodeXMask.inv()
        val intEncodeShift = shift


        val pixels = ip.pixels as ByteArray
        // Create an array with the coordinates of all points between value 1 and 254
        // This method, suggested by Stein Roervik (stein_at_kjemi-dot-unit-dot-no),
        // greatly speeds up the watershed segmentation routine.
        val histogram = ip.histogram
        val arraySize = ip.width * ip.height - histogram[0] - histogram[255]
        val coordinates = IntArray(arraySize)    //from pixel coordinates, low bits x, high bits y
        var highestValue = 0
        var maxBinSize = 0
        var offset = 0
        val levelStart = IntArray(256)
        for (v in 1..254) {
            levelStart[v] = offset
            offset += histogram[v]
            if (histogram[v] > 0) highestValue = v
            if (histogram[v] > maxBinSize) maxBinSize = histogram[v]
        }
        val levelOffset = IntArray(highestValue + 1)
        run {
            var y = 0
            var i = 0
            while (y < ip.height) {
                var x = 0
                while (x < ip.width) {
                    val v = pixels[i].toInt() and 255
                    if (v > 0 && v < 255) {
                        offset = levelStart[v] + levelOffset[v]
                        coordinates[offset] = x or (y shl intEncodeShift)
                        levelOffset[v]++
                    }
                    x++
                    i++
                } //for x
                y++
            }
        } //for y
        // Create an array of the points (pixel offsets) that we set to 255 in one pass.
        // If we remember this list we need not create a snapshot of the ImageProcessor.
        val setPointList = IntArray(Math.min(maxBinSize, (ip.width * ip.height + 2) / 3))
        // now do the segmentation, starting at the highest level and working down.
        // At each level, dilate the particle (set pixels to 255), constrained to pixels
        // whose values are at that level and also constrained (by the fateTable)
        // to prevent features from merging.
        val table = WatershedFunctions.makeFateTable()
        val directionSequence = intArrayOf(7, 3, 1, 5, 0, 4, 2, 6) // diagonal directions first
        for (level in highestValue downTo 1) {
            var remaining = histogram[level]  //number of points in the level that have not been processed
            var idle = 0
            while (remaining > 0 && idle < 8) {
                var dIndex = 0
                do {                        // expand each level in 8 directions
                    val n = WatershedFunctions.processLevel(directionSequence[dIndex % 8], ip, table,
                            levelStart[level], remaining, coordinates, setPointList)
                    remaining -= n         // number of points processed
                    if (n > 0) idle = 0    // nothing processed in this direction?
                    dIndex++
                } while (remaining > 0 && idle++ < 8)
            }
            if (remaining > 0 && level > 1) {   // any pixels that we have not reached?
                var nextLevel = level      // find the next level to process
                do
                    nextLevel--
                while (nextLevel > 1 && histogram[nextLevel] == 0)
                // in principle we should add all unprocessed pixels of this level to the
                // tasklist of the next level. This would make it very slow for some images,
                // however. Thus we only add the pixels if they are at the border (of the
                // image or a thresholded area) and correct unprocessed pixels at the very
                // end by CleanupExtraLines
                if (nextLevel > 0) {
                    var newNextLevelEnd = levelStart[nextLevel] + histogram[nextLevel]
                    var i = 0
                    var p = levelStart[level]
                    while (i < remaining) {
                        val xy = coordinates[p]
                        val x = xy and intEncodeXMask
                        val y = xy and intEncodeYMask shr intEncodeShift
                        val pOffset = x + y * ip.width
                        var addToNext = false
                        if (x == 0 || y == 0 || x == ip.width - 1 || y == ip.height - 1)
                            addToNext = true           //image border
                        else
                            for (d in 0..7)
                                if (isWithin(x, y, d, ip.width, ip.height) && pixels[pOffset + dirOffset[d]].toInt() == 0) {
                                    addToNext = true       //border of area below threshold
                                    break
                                }
                        if (addToNext)
                            coordinates[newNextLevelEnd++] = xy
                        i++
                        p++
                    }
                    //tasklist for the next level to process becomes longer by this:
                    histogram[nextLevel] = newNextLevelEnd - levelStart[nextLevel]
                }
            }
        }
    } // boolean watershedSegment

    /** eliminate unmarked maxima for use by watershed. Starting from each previous maximum,
     * explore the surrounding down to successively lower levels until a marked maximum is
     * touched (or the plateau of a previously eliminated maximum leads to a marked maximum).
     * Then set all the points above this value to this value
     * @param outIp     the image containing the pixel values
     * *
     * @param typeP     the types of the pixels are marked here
     * *
     * @param maxPoints array containing the coordinates of all maxima that might be relevant
     */
    public fun cleanupMaxima(outIp: ByteProcessor, typeP: ByteProcessor, maxPoints: Array<Long>, width: Int, height: Int) {

        val dirOffset = intArrayOf(-width, -width + 1, +1, +width + 1, +width, +width - 1, -1, -width - 1)

        val pixels = outIp.pixels as ByteArray
        val types = typeP.pixels as ByteArray
        val nMax = maxPoints.size
        val pList = IntArray(width * height)
        for (iMax in nMax - 1 downTo 0) {
            val offset0 = maxPoints[iMax].toInt()     //type cast gets lower 32 bits where pixel offset is encoded
            if (types[offset0].toInt() and (MAX_AREA or ELIMINATED) != 0) continue
            val level = pixels[offset0].toInt() and 255
            var loLevel = level + 1
            pList[0] = offset0                     //we start the list at the current maximum
            types[offset0] = (types[offset0].toInt() or LISTED).toByte()               //mark first point as listed
            var listLen = 1                        //number of elements in the list
            var lastLen = 1
            var listI: Int                          //index of current element in the list
            var saddleFound = false
            while (!saddleFound && loLevel > 0) {
                loLevel--
                lastLen = listLen                  //remember end of list for previous level
                listI = 0                          //in each level, start analyzing the neighbors of all pixels
                do {                                //for all pixels listed so far
                    val offset = pList[listI]
                    val x = offset % width
                    val y = offset / width
                    val isInner = y != 0 && y != height - 1 && x != 0 && x != width - 1 //not necessary, but faster than isWithin
                    for (d in 0..7) {       //analyze all neighbors (in 8 directions) at the same level
                        val offset2 = offset + dirOffset[d]
                        if ((isInner || isWithin(x, y, d, width, height)) && types[offset2].toInt() and LISTED == 0) {
                            if (types[offset2].toInt() and MAX_AREA != 0 || types[offset2].toInt() and ELIMINATED != 0 && pixels[offset2].toInt() and 255 >= loLevel) {
                                saddleFound = true //we have reached a point touching a "true" maximum...
                                break              //...or a level not lower, but touching a "true" maximum
                            } else if (pixels[offset2].toInt() and 255 >= loLevel && types[offset2].toInt() and ELIMINATED == 0) {
                                pList[listLen] = offset2
                                listLen++          //we have found a new point to be processed
                                types[offset2] = (types[offset2].toInt() or LISTED).toByte()
                            }
                        } // if isWithin & not LISTED
                    } // for directions d
                    if (saddleFound) break         //no reason to search any further
                    listI++
                } while (listI < listLen)
            }
            listI = 0
            while (listI < listLen) {
                //reset attribute since we may come to this place again
                types[pList[listI]] = (types[pList[listI]].toInt() and LISTED.inv()).toByte()
                listI++
            }
            listI = 0
            while (listI < lastLen) { //for all points higher than the level of the saddle point
                val offset = pList[listI]
                pixels[offset] = loLevel.toByte()     //set pixel value to the level of the saddle point
                types[offset] = (types[offset].toInt() or ELIMINATED).toByte()        //mark as processed: there can't be a local maximum in this area
                listI++
            }
        } // for all maxima iMax
    } // void cleanupMaxima

    /** Check all maxima in list maxPoints, mark type of the points in typeP
     * @param ip             the image to be analyzed
     * *
     * @param typeP          8-bit image, here the point types are marked by type: MAX_POINT, etc.
     * *
     * @param maxPoints      input: a list of all local maxima, sorted by height. Lower 32 bits are pixel offset
     * *
     * @param tolerance      minimum pixel value difference for two separate maxima
     * *
     * @param maxSortingError sorting may be inaccurate, sequence may be reversed for maxima having values
     * *                       not deviating from each other by more than this (this could be a result of
     * *                       precision loss when sorting ints instead of floats, or because sorting does not
     */
    public fun analyzeAndMarkMaxima(ip: ImageProcessor, typeP: GrayScaleImage, maxPoints: Array<Long>,
                                     tolerance: Double, maxSortingError: Double): GrayScaleImage {

        val dirOffset = intArrayOf(-ip.width, -ip.width + 1, +1, +ip.width + 1, +ip.width, +ip.width - 1, -1, -ip.width - 1)

        val types = create2DDoubleArray(typeP.getHeight(), typeP.getWidth(), { r, c -> typeP.getValue(r, c) })
        val nMax = maxPoints.size
        val pList = IntArray(ip.width * ip.height)       //here we enter points starting from a maximum
        val xyVector: ArrayList<IntArray>? = null
        val roi: Roi? = null
        val displayOrCount = false

        for (iMax in nMax - 1 downTo 0) {    //process all maxima now, starting from the highest
            var offset0 = maxPoints[iMax].toInt()     //type cast gets 32 lower bits, where pixel index is encoded
            //int offset0 = maxPoints[iMax].offset;
            var x0 = offset0 % ip.width
            var y0 = offset0 / ip.width

            if (types[offset0/ip.width][offset0%ip.width].toInt() and PROCESSED != 0)
            //this maximum has been reached from another one, skip it
                continue
            //we create a list of connected points and start the list at the current maximum
            var v0 = ip.getPixelValue(x0, y0)
            var sortingError: Boolean
            do {                                    //repeat if we have encountered a sortingError
                pList[0] = offset0
                types[offset0/ip.width][offset0%ip.width] = (types[offset0/ip.width][offset0%ip.width].toInt() or (EQUAL or LISTED)).toDouble()   //mark first point as equal height (to itself) and listed
                var listLen = 1                    //number of elements in the list
                var listI = 0                      //index of current element in the list
                sortingError = false       //if sorting was inaccurate: a higher maximum was not handled so far
                var maxPossible = true         //it may be a true maximum
                var xEqual = x0.toDouble()                 //for creating a single point: determine average over the
                var yEqual = y0.toDouble()                 //  coordinates of contiguous equal-height points
                var nEqual = 1                     //counts xEqual/yEqual points that we use for averaging
                do {                                //while neigbor list is not fully processed (to listLen)
                    val offset = pList[listI]
                    val x = offset % ip.width
                    val y = offset / ip.width
                    val isInner = y != 0 && y != ip.height - 1 && x != 0 && x != ip.width - 1 //not necessary, but faster than isWithin
                    for (d in 0..7) {       //analyze all neighbors (in 8 directions) at the same level
                        val offset2 = offset + dirOffset[d]
                        if ((isInner || isWithin(x, y, d, ip.width, ip.height)) && types[offset2/ip.width][offset2%ip.width].toInt() and LISTED == 0) {
                            if (types[offset2/ip.width][offset2%ip.width].toInt() and PROCESSED != 0) {
                                maxPossible = false //we have reached a point processed previously, thus it is no maximum now
                                break
                            }
                            val x2 = x + DIR_X_OFFSET[d]
                            val y2 = y + DIR_Y_OFFSET[d]
                            val v2 = ip.getPixelValue(x2, y2)
                            if (v2 > v0 + maxSortingError) {
                                maxPossible = false    //we have reached a higher point, thus it is no maximum
                                break
                            } else if (v2 >= v0 - tolerance.toFloat()) {
                                if (v2 > v0) {          //maybe this point should have been treated earlier
                                    sortingError = true
                                    offset0 = offset2
                                    v0 = v2
                                    x0 = x2
                                    y0 = y2

                                }
                                pList[listLen] = offset2
                                listLen++              //we have found a new point within the tolerance
                                types[offset2/ip.width][offset2%ip.width] = (types[offset2/ip.width][offset2%ip.width].toInt() or LISTED).toDouble()
                                if (v2 == v0) {           //prepare finding center of equal points (in case single point needed)
                                    types[offset2/ip.width][offset2%ip.width] = (types[offset2/ip.width][offset2%ip.width].toInt() or EQUAL).toDouble()
                                    xEqual += x2.toDouble()
                                    yEqual += y2.toDouble()
                                    nEqual++
                                }
                            }
                        } // if isWithin & not LISTED
                    } // for directions d
                    listI++
                } while (listI < listLen)

                if (sortingError) {                  //if x0,y0 was not the true maximum but we have reached a higher one
                    listI = 0
                    while (listI < listLen) {
                        types[pList[listI]/ip.width][pList[listI]%ip.width] = 0.0
                        listI++
                    }    //reset all points encountered, then retry
                } else {
                    val resetMask = (if (maxPossible) LISTED else LISTED or EQUAL).inv()
                    xEqual /= nEqual.toDouble()
                    yEqual /= nEqual.toDouble()
                    var minDist2 = 1e20
                    var nearestI = 0
                    listI = 0
                    while (listI < listLen) {
                        val offset = pList[listI]
                        val x = offset % ip.width
                        val y = offset / ip.width
                        types[offset/ip.width][offset%ip.width] = (types[offset/ip.width][offset%ip.width].toInt() and resetMask).toDouble()        //reset attributes no longer needed
                        types[offset/ip.width][offset%ip.width] = (types[offset/ip.width][offset%ip.width].toInt() or PROCESSED).toDouble()        //mark as processed
                        if (maxPossible) {
                            types[offset/ip.width][offset%ip.width] = (types[offset/ip.width][offset%ip.width].toInt() or MAX_AREA).toDouble()
                            if (types[offset/ip.width][offset%ip.width].toInt() and EQUAL != 0) {
                                val dist2 = (xEqual - x) * (xEqual - x) + (yEqual - y) * (yEqual - y)
                                if (dist2 < minDist2) {
                                    minDist2 = dist2    //this could be the best "single maximum" point
                                    nearestI = listI
                                }
                            }
                        }
                        listI++
                    } // for listI
                    if (maxPossible) {
                        val offset = pList[nearestI]
                        types[offset/ip.width][offset%ip.width] = (types[offset/ip.width][offset%ip.width].toInt() or MAX_POINT).toDouble()
                        if (displayOrCount) {
                            val x = offset % ip.width
                            val y = offset / ip.width
                            if (roi == null || roi.contains(x, y))
                                xyVector!!.add(intArrayOf(x, y))
                        }
                    }
                } //if !sortingError
            } while (sortingError)                //redo if we have encountered a higher maximum: handle it now.
        } // for all maxima iMax
        return GrayScaleImageImpl(types)
    } //void analyzeAndMarkMaxima
}

/** Delete extra structures form watershed of non-EDM images, e.g., foreground patches,
 * single dots and lines ending somewhere within a segmented particle
 * Needed for post-processing watershed-segmented images that can have local minima
 * @param ip 8-bit image with background = 0, lines between 1 and 254 and segmented particles = 255
 */
public fun GrayScaleImage.cleanupExtraLines(): GrayScaleImage {
    val pixels = create2DDoubleArray(getHeight(), getWidth(), 0.0)
    for (y in 0..(getHeight() - 1)) {
        for (x in 0..(getWidth() - 1)) {
            val v = pixels[y][x].toInt()
            if (v != 255.toByte().toInt() && v != 0) {
                val nRadii = nRadii(pixels, x, y, getWidth(), getHeight())     //number of lines radiating
                if (nRadii == 0) //single point or foreground patch?
                    pixels[y][x] = 255.0
                else if (nRadii == 1)
                    WatershedFunctions.removeLineFrom(pixels, x, y, getWidth(), getHeight())
            }
        }
    }
    return GrayScaleImageImpl(pixels)
}

/** Analyze the neighbors of a pixel (x, y) in a byte image; pixels <255 ("non-white") are
 * considered foreground. Edge pixels are considered foreground.
 * @param   x coordinate of the point
 * *
 * @param   y coordinate of the point
 * *
 * @return  Number of 4-connected lines emanating from this point. Zero if the point is
 * *          embedded in either foreground or background
 */
public fun nRadii(pixels: Array<Array<Double>>, x: Int, y: Int, width: Int, height: Int): Int {

    var countTransitions = 0
    var prevPixelSet = true
    var firstPixelSet = true           //initialize to make the compiler happy
    val isInner = y != 0 && y != height - 1 && x != 0 && x != width - 1 //not necessary, but faster than isWithin
    for (d in 0..7) {               //walk around the point and note every no-line->line transition
        var pixelSet = prevPixelSet
        if (isInner || isWithin(x, y, d, width, height)) {
            val isSet = pixels[y+DIR_Y_OFFSET[d]][x+DIR_X_OFFSET[d]] != 255.0
            if (d and 1 == 0)
                pixelSet = isSet //non-diagonal directions: always regarded
            else if (!isSet)
            //diagonal directions may separate two lines,
                pixelSet = false           //    but are insufficient for a 4-connected line
        } else {
            pixelSet = true
        }
        if (pixelSet && !prevPixelSet)
            countTransitions++
        prevPixelSet = pixelSet
        if (d == 0)
            firstPixelSet = pixelSet
    }
    if (firstPixelSet && !prevPixelSet)
        countTransitions++
    return countTransitions
} // int nRadii