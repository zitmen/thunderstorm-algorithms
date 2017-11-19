package cz.cuni.lf1.thunderstorm.algorithms.estimators.psf

import cz.cuni.lf1.thunderstorm.algorithms.estimators.SubImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule
import org.apache.commons.math3.analysis.MultivariateMatrixFunction
import org.apache.commons.math3.analysis.MultivariateVectorFunction

public interface PsfModel {

    public fun getInitialParams(subImage: SubImage): DoubleArray
    public fun transformParameters(params: DoubleArray): DoubleArray
    public fun transformParametersInverse(params: DoubleArray): DoubleArray
    public fun getValue(params: DoubleArray, x: Double, y: Double): Double
    public fun getDegreesOfFreedom(): Int
    public fun getValueFunction(xgrid: DoubleArray, ygrid: DoubleArray): MultivariateVectorFunction
    public fun getJacobianFunction(xgrid: DoubleArray, ygrid: DoubleArray): MultivariateMatrixFunction
    public fun createMoleculeFromParams(params: DoubleArray): Molecule
}