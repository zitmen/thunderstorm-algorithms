package cz.cuni.lf1.thunderstorm.algorithms.estimators

import cz.cuni.lf1.thunderstorm.algorithms.estimators.psf.SymmetricGaussianPsf
import cz.cuni.lf1.thunderstorm.algorithms.estimators.util.EstimatorTestingUtils.assertFittingMorePreciseThanDetection
import cz.cuni.lf1.thunderstorm.algorithms.estimators.util.EstimatorTestingUtils.testEstimator
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createMoleculeDetectionNano
import cz.cuni.lf1.thunderstorm.test.assertListOfMoleculesEquals
import org.junit.Test

class LeastSquaresEstimatorTests {

    @Test
    fun testLeastSquaresSymmetricGaussian() {
        val pairs = testEstimator(LeastSquaresEstimator(5, SymmetricGaussianPsf(1.0), false))

        assertFittingMorePreciseThanDetection(pairs)

        val fits = pairs.map { it.fit }.toList()
        val expected = listOf(  // results from TS
                createMoleculeDetectionNano( 1634.1840016072, 16829.8699508670),
                createMoleculeDetectionNano( 4945.5295002463,  3698.9254339077),
                createMoleculeDetectionNano( 4816.9859625047, 34756.8731718770),
                createMoleculeDetectionNano( 5850.4310231741, 36091.7201452063),
                createMoleculeDetectionNano( 7540.0100007289,  9080.8270602474),
                createMoleculeDetectionNano( 8361.0825099783, 31268.3335567894),
                createMoleculeDetectionNano( 8794.5367253196, 10091.7778558366),
                createMoleculeDetectionNano( 9810.4244157234, 14977.5475587619),
                createMoleculeDetectionNano(10187.4586688654, 23643.9943869420),
                createMoleculeDetectionNano(10271.4183717399, 14759.9757526986),
                createMoleculeDetectionNano(10361.2363039872, 22818.9790411805),
                createMoleculeDetectionNano(10473.4195848467, 15328.4608731353),
                createMoleculeDetectionNano(10752.9689448721, 19144.7150768819),
                createMoleculeDetectionNano(10831.7922323480,  8807.6999945587),
                createMoleculeDetectionNano(11347.5033959504, 25490.0587810986),
                createMoleculeDetectionNano(11876.0449376651, 24576.5467787877),
                createMoleculeDetectionNano(12732.2510442951, 23254.4515002747),
                createMoleculeDetectionNano(13587.9628217146, 21986.6925657209),
                createMoleculeDetectionNano(15345.2182550595, 28543.6957227166),
                createMoleculeDetectionNano(15621.2842116422,  7522.4579575263),
                createMoleculeDetectionNano(16283.7794542934,  8834.8178586255),
                createMoleculeDetectionNano(16436.1598322862, 28616.7579768473),
                createMoleculeDetectionNano(16778.9971498156, 18383.5452908893),
                createMoleculeDetectionNano(17695.8214871646, 17814.8400158825),
                createMoleculeDetectionNano(18385.9487272185,  7137.8107943135),
                createMoleculeDetectionNano(21518.2037749602, 15282.8482855307),
                createMoleculeDetectionNano(22288.2744442416,  6967.9597512760),
                createMoleculeDetectionNano(22688.7475645618, 24911.1815057023),
                createMoleculeDetectionNano(22950.6721321354, 31214.5141463504),
                createMoleculeDetectionNano(23666.7260883207, 13315.6618402281),
                createMoleculeDetectionNano(24898.5914326540, 21986.9179757447),
                createMoleculeDetectionNano(25444.1254695150, 17758.9525145488),
                createMoleculeDetectionNano(25708.5745060205, 26799.6790981039),
                createMoleculeDetectionNano(26775.2532974694, 10583.4006627221),
                createMoleculeDetectionNano(26734.9544658921, 19884.7266517043),
                createMoleculeDetectionNano(27398.9867957412, 20890.0832410656),
                createMoleculeDetectionNano(27725.3305971445,  7485.1442496580),
                createMoleculeDetectionNano(29131.4137951492,  2979.2039855412),
                createMoleculeDetectionNano(29366.1830618317,  2419.1076851940),
                createMoleculeDetectionNano(30424.4505606220, 18423.5345830188),
                createMoleculeDetectionNano(31703.1644427991, 11716.4840082148),
                createMoleculeDetectionNano(33584.2329828820, 10335.7736743788),
                createMoleculeDetectionNano(34169.3817277629, 10822.4287716254),
                createMoleculeDetectionNano(34641.7536762137,  9379.7234596887))

        assertListOfMoleculesEquals(expected, fits, 1e-10)
    }

    @Test
    fun testWeightedLeastSquaresSymmetricGaussian() {
        val pairs = testEstimator(LeastSquaresEstimator(5, SymmetricGaussianPsf(1.0), true))

        assertFittingMorePreciseThanDetection(pairs)

        val fits = pairs.map { it.fit }.toList()
        val expected = listOf(  // results from TS
                createMoleculeDetectionNano( 1633.7817978002, 16827.6130996627),
                createMoleculeDetectionNano( 4946.0929491707,  3697.0400430389),
                createMoleculeDetectionNano( 4817.7890607788, 34754.2734657504),
                createMoleculeDetectionNano( 5848.8949821447, 36090.7951457799),
                createMoleculeDetectionNano( 7541.7370061949,  9078.4255766070),
                createMoleculeDetectionNano( 8361.0845006867, 31268.3181210044),
                createMoleculeDetectionNano( 8796.6139384996, 10093.1082144228),
                createMoleculeDetectionNano( 9817.2091646234, 14973.4171183979),
                createMoleculeDetectionNano(10187.1810365437, 23643.4255704451),
                createMoleculeDetectionNano(10267.7229411191, 14767.7436304036),
                createMoleculeDetectionNano(10361.9323419834, 22820.5889507149),
                createMoleculeDetectionNano(10471.9671373342, 15324.0661388267),
                createMoleculeDetectionNano(10752.6383258981, 19144.5870075458),
                createMoleculeDetectionNano(10833.8633801996,  8805.9559868713),
                createMoleculeDetectionNano(11347.0500756818, 25489.4804130724),
                createMoleculeDetectionNano(11877.0668976568, 24576.7067268968),
                createMoleculeDetectionNano(12731.6499868356, 23255.1143523846),
                createMoleculeDetectionNano(13586.9346678007, 21986.9868590032),
                createMoleculeDetectionNano(15345.8785060810, 28543.0571569056),
                createMoleculeDetectionNano(15621.1924938196,  7520.3248083644),
                createMoleculeDetectionNano(16284.5919189038,  8835.4051036767),
                createMoleculeDetectionNano(16436.9669188504, 28615.7386358944),
                createMoleculeDetectionNano(16778.7266483827, 18382.7031636058),
                createMoleculeDetectionNano(17693.6107839609, 17814.2803685218),
                createMoleculeDetectionNano(18386.9334501697,  7138.2460931497),
                createMoleculeDetectionNano(21518.1217007315, 15279.6572578994),
                createMoleculeDetectionNano(22330.9021779080,  6969.2721763416),
                createMoleculeDetectionNano(22690.9617562880, 24911.1028559767),
                createMoleculeDetectionNano(22949.4594201187, 31217.0173717035),
                createMoleculeDetectionNano(23665.4716741246, 13315.0409251392),
                createMoleculeDetectionNano(24899.6708487845, 21984.6909950974),
                createMoleculeDetectionNano(25441.1431581514, 17759.6830399287),
                createMoleculeDetectionNano(25710.5108271930, 26797.6429808278),
                createMoleculeDetectionNano(26775.2252443687, 10583.3745638343),
                createMoleculeDetectionNano(26734.4058626207, 19885.5129465726),
                createMoleculeDetectionNano(27398.8874966166, 20890.8838168644),
                createMoleculeDetectionNano(27724.3815749944,  7484.5917408212),
                createMoleculeDetectionNano(29132.4263687979,  2980.3944542814),
                createMoleculeDetectionNano(29359.9787629244,  2426.5814429104),
                createMoleculeDetectionNano(30426.4331859733, 18423.6283839056),
                createMoleculeDetectionNano(31706.9105224024, 11714.6970132284),
                createMoleculeDetectionNano(33582.4471432423, 10334.4729971867),
                createMoleculeDetectionNano(34168.9857973222, 10823.0007579588),
                createMoleculeDetectionNano(34641.1556351226,  9379.7973846020))

        assertListOfMoleculesEquals(expected, fits, 1e-10)
    }
}