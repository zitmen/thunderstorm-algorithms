package cz.cuni.lf1.thunderstorm.algorithms.estimators

import cz.cuni.lf1.thunderstorm.datastructures.Distance
import cz.cuni.lf1.thunderstorm.datastructures.Molecule
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class CentroidEstimatorTests {

    @Test
    public fun testEstimate1() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(1.0, 1.0, 1.0),
                arrayOf(3.0, 4.0, 0.0),
                arrayOf(3.0, 3.0, 1.0)))

        val result = CentroidEstimator(1).estimatePosition(image,
                Molecule(Distance.fromPixels(1.5), Distance.fromPixels(1.5)))!!

        assertEquals(1.206, result.xPos.getValue(), 1e-3)
        assertEquals(1.735, result.yPos.getValue(), 1e-3)
    }

    @Test
    public fun testEstimate2() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(9.0, 9.0, 7.0, 7.0, 6.0),
                arrayOf(4.0, 6.0, 7.0, 5.0, 6.0),
                arrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
                arrayOf(2.0, 3.0, 4.0, 0.0, 1.0),
                arrayOf(2.0, 3.0, 3.0, 1.0, 2.0)))

        val result = CentroidEstimator(1).estimatePosition(image,
                Molecule(Distance.fromPixels(2.5), Distance.fromPixels(3.5)))!!

        assertEquals(2.206, result.xPos.getValue(), 1e-3)
        assertEquals(3.735, result.yPos.getValue(), 1e-3)
    }

    @Test
    fun testEstimate3() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(1.4175035112951352E-7, 1.5056067251874795E-6, 1.0387350450247402E-5, 4.6596555689039867E-5, 1.3603475690306496E-4, 2.5864120226361595E-4, 3.203992064538993E-4, 2.5864120226361595E-4, 1.3603475690306496E-4, 4.6596555689039867E-5, 1.0387350450247402E-5),
                arrayOf(9.779516450051282E-7, 1.0387350450247402E-5, 7.166350121265529E-5, 3.2147488824231735E-4, 9.385191163983373E-4, 0.0017843947983501222, 0.002210470228208766, 0.0017843947983501222, 9.385191163983373E-4, 3.2147488824231735E-4, 7.166350121265529E-5),
                arrayOf(4.3869876640759965E-6, 4.6596555689039867E-5, 3.2147488824231735E-4, 0.00144210235366173, 0.004210097510616101, 0.008004606371066857, 0.00991593569322976, 0.008004606371066857, 0.004210097510616101, 0.00144210235366173, 3.2147488824231735E-4),
                arrayOf(1.2807444490144886E-5, 1.3603475690306496E-4, 9.385191163983373E-4, 0.004210097510616101, 0.01229102844461037, 0.02336878049655781, 0.02894874699531102, 0.02336878049655781, 0.01229102844461037, 0.004210097510616101, 9.385191163983373E-4),
                arrayOf(2.4350635942371895E-5, 2.5864120226361595E-4, 0.0017843947983501222, 0.008004606371066857, 0.02336878049655781, 0.04443077358068976, 0.05503989493087988, 0.04443077358068976, 0.02336878049655781, 0.008004606371066857, 0.0017843947983501222),
                arrayOf(3.0165048585846606E-5, 3.203992064538993E-4, 0.002210470228208766, 0.00991593569322976, 0.02894874699531102, 0.05503989493087988, 0.06818224824514224, 0.05503989493087988, 0.02894874699531102, 0.00991593569322976, 0.002210470228208766),
                arrayOf(2.4350635942371895E-5, 2.5864120226361595E-4, 0.0017843947983501222, 0.008004606371066857, 0.02336878049655781, 0.04443077358068976, 0.05503989493087988, 0.04443077358068976, 0.02336878049655781, 0.008004606371066857, 0.0017843947983501222),
                arrayOf(1.2807444490144886E-5, 1.3603475690306496E-4, 9.385191163983373E-4, 0.004210097510616101, 0.01229102844461037, 0.02336878049655781, 0.02894874699531102, 0.02336878049655781, 0.01229102844461037, 0.004210097510616101, 9.385191163983373E-4),
                arrayOf(4.3869876640759965E-6, 4.6596555689039867E-5, 3.2147488824231735E-4, 0.00144210235366173, 0.004210097510616101, 0.008004606371066857, 0.00991593569322976, 0.008004606371066857, 0.004210097510616101, 0.00144210235366173, 3.2147488824231735E-4),
                arrayOf(9.779516450051282E-7, 1.0387350450247402E-5, 7.166350121265529E-5, 3.2147488824231735E-4, 9.385191163983373E-4, 0.0017843947983501222, 0.002210470228208766, 0.0017843947983501222, 9.385191163983373E-4, 3.2147488824231735E-4, 7.166350121265529E-5),
                arrayOf(1.4175035112951352E-7, 1.5056067251874795E-6, 1.0387350450247402E-5, 4.6596555689039867E-5, 1.3603475690306496E-4, 2.5864120226361595E-4, 3.203992064538993E-4, 2.5864120226361595E-4, 1.3603475690306496E-4, 4.6596555689039867E-5, 1.0387350450247402E-5)))

        val estimate = CentroidEstimator(5).estimatePosition(image, Molecule(Distance.fromPixels(5.5), Distance.fromPixels(5.5)))!!

        assertEquals(6.5, estimate.xPos.getValue(), 1e-2)
        assertEquals(5.5, estimate.yPos.getValue(), 1e-2)
    }

    @Test
    public fun testBoundaries() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(1.0, 1.0, 1.0),
                arrayOf(3.0, 4.0, 0.0),
                arrayOf(3.0, 3.0, 1.0)))

        val estimator = CentroidEstimator(1)

        assertNotNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(1.5), Distance.fromPixels(1.5))))
        assertNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(0.5), Distance.fromPixels(1.5))))
        assertNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(2.5), Distance.fromPixels(1.5))))
        assertNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(1.5), Distance.fromPixels(0.5))))
        assertNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(1.5), Distance.fromPixels(2.5))))
        assertNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(-1.0), Distance.fromPixels(1.5))))
        assertNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(3.0), Distance.fromPixels(1.5))))
        assertNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(1.5), Distance.fromPixels(-1.0))))
        assertNull(estimator.estimatePosition(image, Molecule(Distance.fromPixels(1.5), Distance.fromPixels(3.0))))
    }
}
