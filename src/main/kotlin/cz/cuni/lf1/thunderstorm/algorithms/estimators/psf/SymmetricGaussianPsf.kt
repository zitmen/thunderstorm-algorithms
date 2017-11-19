package cz.cuni.lf1.thunderstorm.algorithms.estimators.psf

import cz.cuni.lf1.thunderstorm.algorithms.estimators.SubImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule
import cz.cuni.lf1.thunderstorm.datastructures.extensions.*
import org.apache.commons.math3.analysis.MultivariateMatrixFunction
import org.apache.commons.math3.analysis.MultivariateVectorFunction
import java.lang.Math.PI
import java.lang.Math.exp
import java.util.*

public object Params {
    const val X = 0
    const val Y = 1
    const val I = 2
    const val S = 3
    const val O = 4
}

public class SymmetricGaussianPsf(private val defaultSigma: Double) : PsfModel {

    override fun getInitialParams(subImage: SubImage): DoubleArray {
        val guess = DoubleArray(5)
        Arrays.fill(guess, 0.0)
        guess[Params.X] = subImage.detectorX
        guess[Params.Y] = subImage.detectorY
        guess[Params.I] = (subImage.max - subImage.min) * 2 * PI * defaultSigma * defaultSigma
        guess[Params.S] = defaultSigma
        guess[Params.O] = subImage.min
        return guess
    }

    override fun transformParameters(params: DoubleArray): DoubleArray {
        val transformed = Arrays.copyOf(params, params.size)
        transformed[Params.X] = params[Params.X]
        transformed[Params.Y] = params[Params.Y]
        transformed[Params.I] = params[Params.I].sqr()
        transformed[Params.S] = params[Params.S].sqr()
        transformed[Params.O] = params[Params.O].sqr()
        return transformed
    }

    override fun transformParametersInverse(params: DoubleArray): DoubleArray {
        val transformed = Arrays.copyOf(params, params.size)
        transformed[Params.X] = params[Params.X]
        transformed[Params.Y] = params[Params.Y]
        transformed[Params.I] = params[Params.I].abs().sqrt()
        transformed[Params.S] = params[Params.S].abs().sqrt()
        transformed[Params.O] = params[Params.O].abs().sqrt()
        return transformed
    }

    override fun getValue(params: DoubleArray, x: Double, y: Double): Double {
        val twoSigmaSquared = params[Params.S] * params[Params.S] * 2.0
        return params[Params.O] + params[Params.I] / (twoSigmaSquared * PI) * exp(-((x - params[Params.X]) * (x - params[Params.X]) + (y - params[Params.Y]) * (y - params[Params.Y])) / twoSigmaSquared)
    }

    override fun getDegreesOfFreedom(): Int {
        return 5
    }

    override fun getValueFunction(xgrid: DoubleArray, ygrid: DoubleArray): MultivariateVectorFunction {
        return MultivariateVectorFunction { params ->
            val transformedParams = transformParameters(params)
            val retVal = DoubleArray(xgrid.size)
            for (i in xgrid.indices) {
                retVal[i] = getValue(transformedParams, xgrid[i], ygrid[i])
            }
            retVal
        }
    }

    override fun getJacobianFunction(xgrid: DoubleArray, ygrid: DoubleArray): MultivariateMatrixFunction {
        return MultivariateMatrixFunction { point ->
            //derivations by wolfram alpha:
            //d(b^2 + ((J*J)/2/PI/(s*s)/(s*s)) * e^( -( ((x0-x)^2)/(2*s*s*s*s) + (((y0-y)^2)/(2*s*s*s*s)))))/dx
            val transformedPoint = transformParameters(point)
            val sigma = transformedPoint[Params.S]
            val sigmaSquared = sigma * sigma
            val retVal = Array(xgrid.size) { DoubleArray(transformedPoint.size) }

            for (i in xgrid.indices) {
                //d()/dIntensity
                val xd = xgrid[i] - transformedPoint[Params.X]
                val yd = ygrid[i] - transformedPoint[Params.Y]
                val upper = -(xd * xd + yd * yd) / (2 * sigmaSquared)
                val expVal = exp(upper)
                val expValDivPISigmaSquared = expVal / (sigmaSquared * PI)
                val expValDivPISigmaPowEight = expValDivPISigmaSquared / sigmaSquared
                retVal[i][Params.I] = point[Params.I] * expValDivPISigmaSquared
                //d()/dx
                retVal[i][Params.X] = transformedPoint[Params.I] * xd * expValDivPISigmaPowEight * 0.5
                //d()/dy
                retVal[i][Params.Y] = transformedPoint[Params.I] * yd * expValDivPISigmaPowEight * 0.5
                //d()/dsigma
                retVal[i][Params.S] = transformedPoint[Params.I] * expValDivPISigmaPowEight / point[Params.S] * (xd * xd + yd * yd - 2 * sigmaSquared)
                //d()/dbkg
                retVal[i][Params.O] = 2 * point[Params.O]
            }
            retVal
        }
    }

    override fun createMoleculeFromParams(params: DoubleArray): Molecule {
        return createMoleculeEstimate(
                params[Params.X],
                params[Params.Y],
                params[Params.I],
                params[Params.S],
                params[Params.O])
    }
}