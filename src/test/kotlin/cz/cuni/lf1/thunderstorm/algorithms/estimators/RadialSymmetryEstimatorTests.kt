package cz.cuni.lf1.thunderstorm.algorithms.estimators

import cz.cuni.lf1.thunderstorm.algorithms.estimators.util.EstimatorTestingUtils.testEstimator
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createMoleculeDetectionNano
import cz.cuni.lf1.thunderstorm.test.assertListOfMoleculesEquals
import org.junit.Test

internal class RadialSymmetryEstimatorTests {

    @Test
    public fun testEstimate() {
        val pairs = testEstimator(RadialSymmetryEstimator(5))
        //assertFittingMorePreciseThanDetection(pairs)  // didn't pass even in TS

        val fits = pairs.map { it.fit }.toList()
        val expected = listOf(  // results from TS
                createMoleculeDetectionNano( 1637.1368380010, 16826.3165945947),
                createMoleculeDetectionNano( 4944.4300916746,  3696.8578637361),
                createMoleculeDetectionNano( 4828.7577412292, 34741.7086442097),
                createMoleculeDetectionNano( 5850.1442899334, 36087.9272881884),
                createMoleculeDetectionNano( 7546.3337674895,  9074.9838920948),
                createMoleculeDetectionNano( 8388.1644170548, 31239.3288336342),
                createMoleculeDetectionNano( 8816.9932248900, 10072.6915469235),
                createMoleculeDetectionNano(10048.3101651647, 15036.9660402506),
                createMoleculeDetectionNano(10196.9503862263, 23572.6297462793),
                createMoleculeDetectionNano(10031.6667449541, 14951.0185682388),
                createMoleculeDetectionNano(10358.6595233969, 22858.3338927463),
                createMoleculeDetectionNano( 9987.3259666141, 14897.3939987913),
                createMoleculeDetectionNano(10750.3306411167, 19147.5393435775),
                createMoleculeDetectionNano(10847.0956998360,  8799.4628270536),
                createMoleculeDetectionNano(11390.5789688604, 25478.4358724876),
                createMoleculeDetectionNano(11868.1613604771, 24597.9924086763),
                createMoleculeDetectionNano(12733.2313260429, 23262.5567103180),
                createMoleculeDetectionNano(13588.6366167370, 21991.4577470785),
                createMoleculeDetectionNano(15357.9807967751, 28554.2203078009),
                createMoleculeDetectionNano(15620.7384113415,  7519.7942280380),
                createMoleculeDetectionNano(16281.0029249527,  8830.2949793552),
                createMoleculeDetectionNano(16429.8209663786, 28610.0914875425),
                createMoleculeDetectionNano(16779.9836701923, 18372.9942051725),
                createMoleculeDetectionNano(17607.2529578394, 18053.5214957029),
                createMoleculeDetectionNano(18394.3191867204,  7139.6653757967),
                createMoleculeDetectionNano(21517.2795311150, 15277.0794379355),
                createMoleculeDetectionNano(22261.0492331570,  6990.8119463254),
                createMoleculeDetectionNano(22684.1199620768, 24910.3097469239),
                createMoleculeDetectionNano(22921.0499346917, 31205.1901887701),
                createMoleculeDetectionNano(23659.2551126333, 13314.5661158108),
                createMoleculeDetectionNano(24895.2363957988, 21985.5665528751),
                createMoleculeDetectionNano(25437.7496216502, 17759.9131463272),
                createMoleculeDetectionNano(25695.0809704546, 26785.0823151817),
                createMoleculeDetectionNano(26778.3411421733, 10584.3282002853),
                createMoleculeDetectionNano(26730.5908159078, 19886.7687362302),
                createMoleculeDetectionNano(27279.2043526413, 20867.0960031132),
                createMoleculeDetectionNano(27728.1913046934,  7485.7445308080),
                createMoleculeDetectionNano(29259.5085775643,  2845.3880753618),
                createMoleculeDetectionNano(29224.7076180210,  2716.4478408251),
                createMoleculeDetectionNano(30429.9552218267, 18421.6195321046),
                createMoleculeDetectionNano(31723.6163991214, 11705.2198729620),
                createMoleculeDetectionNano(33989.4800938878, 10630.4415753243),
                createMoleculeDetectionNano(33955.1926558698, 10653.8114054139),
                createMoleculeDetectionNano(34643.6369932576,  9380.1698722160))

        assertListOfMoleculesEquals(expected, fits, 1e-10)
    }
}
