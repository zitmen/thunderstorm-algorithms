package cz.cuni.lf1.thunderstorm.algorithms.estimators

import cz.cuni.lf1.thunderstorm.algorithms.estimators.SubImage.createSubImage
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createMoleculeDetection
import cz.cuni.lf1.thunderstorm.datastructures.extensions.floor
import ij.plugin.filter.Convolver
import ij.process.FloatProcessor

/**
 * Rewritten to java from the matlab implementation in supplement of the article
 * "Rapid, accurate particle tracking by calculation of radial symmetry centers"
 * by Raghuveer Parthasarathy
 *
 */
public class RadialSymmetryEstimator(private val fittingRadius: Int) : Estimator {

    public override fun estimatePosition(image: GrayScaleImage, initialEstimate: Molecule): Molecule? {
        val img = createSubImage(fittingRadius, image, initialEstimate)
        if (img != null) {
            return fit(img)
        }
        return null
    }

    private fun fit(img: SubImage): Molecule {
        val dIdu = computeGradientImage(img, false)
        val dIdv = computeGradientImage(img, true)

        smooth(dIdu, img.size_x)
        smooth(dIdv, img.size_y)

        val m = calculateSlope(dIdu, dIdv)
        val xMesh = createMesh(img.size_x, true)
        val yMesh = createMesh(img.size_y, false)

        val yInterceptB = calculateYIntercept(xMesh, yMesh, m)

        val weights = calculateWeights(dIdu, dIdv, xMesh, yMesh)

        val coordinates = lsRadialCenterFit(m, yInterceptB, weights)

        return createMoleculeDetection(coordinates[0] + img.detectorX.floor() + 0.5, coordinates[1] + img.detectorY.floor() + 0.5)
    }

    /**
     * Computes gradient image along 45-degree shifted coordinates.
     *
     * @param img
     * @param mainDiagonalDirection specifies the direction in which the
     * gradient will be computed, true for main diagonal and false for
     * antidiagonal
     * @return
     */
    private fun computeGradientImage(img: SubImage, mainDiagonalDirection: Boolean): FloatArray {
        val dI = FloatArray((img.size_x - 1) * (img.size_y - 1))

        var idx1 = if (mainDiagonalDirection) 0 else 1
        var idx2 = if (mainDiagonalDirection) img.size_x + 1 else img.size_x
        var resIdx = 0

        for (i in 0 until img.size_x - 1) {
            for (j in 0 until img.size_y - 1) {
                dI[resIdx++] = (img.values[idx1++] - img.values[idx2++]).toFloat()
            }
            idx1++
            idx2++
        }

        return dI
    }

    /**
     * smoothing by 3*3 box filter
     */
    private fun smooth(dIdu: FloatArray, size: Int) {
        val kernel = floatArrayOf(1f / 3f, 1f / 3f, 1f / 3f)

        val convolver = Convolver()
        val imp = FloatProcessor(size - 1, size - 1, dIdu, null)
        convolver.convolve(imp, kernel, kernel.size, 1)
        convolver.convolve(imp, kernel, 1, kernel.size)
    }

    private fun calculateSlope(dIdu: FloatArray, dIdv: FloatArray): FloatArray {
        val m = FloatArray(dIdu.size)

        for (i in m.indices) {
            var `val` = -(dIdu[i] + dIdv[i]) / (dIdu[i] - dIdv[i])
            `val` = if (java.lang.Float.isNaN(`val`)) 0f else `val`
            `val` = if (java.lang.Float.isInfinite(`val`)) java.lang.Float.MAX_VALUE / 1e5f else `val` //replace inf by some big value - Not max_value because it could overflow to infinity in next step
            m[i] = `val`
        }
        return m
    }

    private fun createMesh(size: Int, xMesh: Boolean): FloatArray {
        val mesh = FloatArray((size - 1) * (size - 1))
        val smallSize = (size - 1) / 2

        var idx = 0
        for (i in 0 until size - 1) {
            val iVal = (-smallSize).toFloat() + 0.5f + i.toFloat()
            for (j in 0 until size - 1) {
                val jVal = (-smallSize).toFloat() + 0.5f + j.toFloat()
                mesh[idx++] = if (xMesh) jVal else iVal
            }
        }
        return mesh
    }

    /**
     * b in original matlab implementation
     */
    private fun calculateYIntercept(xMesh: FloatArray, yMesh: FloatArray, m: FloatArray): FloatArray {
        val intercept = FloatArray(m.size)
        for (i in intercept.indices) {
            intercept[i] = yMesh[i] - m[i] * xMesh[i]
        }
        return intercept
    }

    /**
     * weight by square of gradient magnitude and inverse distance to gradient
     * intensity centroid.
     */
    private fun calculateWeights(dIdu: FloatArray, dIdv: FloatArray, xMesh: FloatArray, yMesh: FloatArray): FloatArray {
        var gradientMagnitudeSum = 0f
        var xCentroid = 0f
        var yCentroid = 0f
        for (i in dIdu.indices) {
            val gradientMagnitude = dIdu[i] * dIdu[i] + dIdv[i] * dIdv[i]
            gradientMagnitudeSum += gradientMagnitude

            xCentroid += xMesh[i] * gradientMagnitude
            yCentroid += yMesh[i] * gradientMagnitude
        }
        xCentroid /= gradientMagnitudeSum
        yCentroid /= gradientMagnitudeSum

        val weights = FloatArray(dIdu.size)

        for (i in weights.indices) {
            val gradientMagnitude = dIdu[i] * dIdu[i] + dIdv[i] * dIdv[i]
            val distanceToCentroid = Math.sqrt(((xMesh[i] - xCentroid) * (xMesh[i] - xCentroid) + (yMesh[i] - yCentroid) * (yMesh[i] - yCentroid)).toDouble())
            weights[i] = (gradientMagnitude / distanceToCentroid).toFloat()
        }
        return weights
    }

    /**
     * least-squares minimization to determine the translated coordinate system
     * origin (xc, yc) such that lines y = mx+b have the minimal total
     * distance^2 to the origin:
     */
    private fun lsRadialCenterFit(m: FloatArray, b: FloatArray, weights: FloatArray): DoubleArray {
        var sw = 0.0
        var smmw = 0.0
        var smw = 0.0
        var smbw = 0.0
        var sbw = 0.0

        for (i in m.indices) {
            val weighted = (weights[i] / (m[i] * m[i] + 1)).toDouble() //wm2p1
            sw += weighted
            val mw = weighted * m[i]
            smw += mw
            smmw += mw * m[i]
            smbw += mw * b[i]
            sbw += weighted * b[i]
        }
        val det = smw * smw - smmw * sw
        val xc = (smbw * sw - smw * sbw) / det    // relative to image center
        val yc = (smbw * smw - smmw * sbw) / det // relative to image center

        return doubleArrayOf(xc, yc)
    }
}
