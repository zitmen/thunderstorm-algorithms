package cz.cuni.lf1.thunderstorm.algorithms.estimators

import cz.cuni.lf1.thunderstorm.algorithms.estimators.psf.PsfModel
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Intensity
import cz.cuni.lf1.thunderstorm.datastructures.Molecule
import cz.cuni.lf1.thunderstorm.datastructures.extensions.minus
import cz.cuni.lf1.thunderstorm.datastructures.extensions.stddev
import org.apache.commons.math3.optim.*
import org.apache.commons.math3.optim.nonlinear.vector.ModelFunction
import org.apache.commons.math3.optim.nonlinear.vector.ModelFunctionJacobian
import org.apache.commons.math3.optim.nonlinear.vector.Target
import org.apache.commons.math3.optim.nonlinear.vector.Weight
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer
import java.util.*

private const val MAX_ITERATIONS = 1000

public class LeastSquaresEstimator(
        private val fittingRadius: Int,
        private val psfModel: PsfModel,
        private val useWeighting: Boolean,
        private val maxIter: Int = MAX_ITERATIONS)
    : Estimator {

    override fun estimatePosition(image: GrayScaleImage, initialEstimate: Molecule): Molecule? {
        val subimage = SubImage.createSubImage(fittingRadius, image, initialEstimate)

        // init
        val weights = calcWeights(useWeighting, subimage)
        val observations = subimage.values

        // fit
        val optimizer = LevenbergMarquardtOptimizer(
                SimplePointChecker(10e-10, 10e-10, maxIter))

        val pv: PointVectorValuePair
        pv = optimizer.optimize(
                MaxEval.unlimited(),
                MaxIter(MAX_ITERATIONS + 1),
                ModelFunction(psfModel.getValueFunction(subimage.xgrid, subimage.ygrid)),
                ModelFunctionJacobian(psfModel.getJacobianFunction(subimage.xgrid, subimage.ygrid)),
                Target(observations),
                InitialGuess(psfModel.transformParametersInverse(psfModel.getInitialParams(subimage))),
                Weight(weights))

        // correct position
        pv.pointRef[0] = subimage.correctXPosition(pv.pointRef[0])
        pv.pointRef[1] = subimage.correctYPosition(pv.pointRef[1])

        // estimate background and return an instance of the `Molecule`
        val fittedParameters = psfModel.createMoleculeFromParams(pv.pointRef)
        fittedParameters.background = Intensity.fromAdCounts((observations.toTypedArray() - psfModel.getValueFunction(subimage.xgrid, subimage.ygrid).value(pv.pointRef).toTypedArray()).stddev())
        return fittedParameters
    }

    fun calcWeights(useWeighting: Boolean, subimage: SubImage): DoubleArray {
        val weights = DoubleArray(subimage.values.size)
        if (!useWeighting) {
            Arrays.fill(weights, 1.0)
        } else {
            val minWeight = 1.0 / subimage.max
            val maxWeight = 1000 * minWeight
            for (i in 0 until subimage.values.size) {
                var weight = 1 / subimage.values[i]
                if (weight.isInfinite() || weight.isNaN() || weight > maxWeight) {
                    weight = maxWeight
                }
                weights[i] = weight
            }
        }
        return weights
    }
}