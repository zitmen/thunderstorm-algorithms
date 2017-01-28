package cz.cuni.lf1.thunderstorm.datastructures

import cz.cuni.lf1.thunderstorm.datastructures.extensions.div
import cz.cuni.lf1.thunderstorm.datastructures.extensions.plus
import cz.cuni.lf1.thunderstorm.datastructures.extensions.normalize

internal object BSpline {

    /**
     * Generate B-Spline shifted so it is symmetric around zero
     *
     * @param k order of b-spline
     * @param s scale of samples
     * @param t samples
     * @return the B-spline of order `k` sampled at points `t`
     */
    public fun create(k: Int, s: Double, t: Array<Double>)
            // scale and align to center, then evaluate, and finally normalize sum to 1
            = N(k, (t / s) + (k.toDouble() / 2.0)).normalize()

    /**
     * The actual recursive blending function for generating B-Splines
     *
     * Note: although the algorithm could be optimized to call the recursion
     * just once, it is not such issue, because in our application it is
     * evaluated at only few spots
     *
     * @param k order
     * @param t samples
     */
    private fun N(k: Int, t: Array<Double>): Array<Double> {
        if (k <= 1) {
            return haar(t)
        } else {
            val res = Array(t.size) { 0.0 }
            for (i in t.indices) {
                val Nt = N(k - 1, arrayOf(t[i]))
                val Nt_1 = N(k - 1, arrayOf(t[i] - 1))
                res[i] = t[i] / (k - 1) * Nt[0] + (k - t[i]) / (k - 1) * Nt_1[0]
            }
            return res
        }
    }

    /**
     * Generate Haar basis (no scaling)
     */
    private fun haar(t: Array<Double>)
            = t.map { if (it >= 0.0 && it < 1.0) 1.0 else 0.0 }.toTypedArray()
}
