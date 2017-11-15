package cz.cuni.lf1.thunderstorm.parser.thresholding

import org.junit.Test
import kotlin.test.assertEquals

internal class RefreshableVariableTests {

    @Test
    public fun test() {
        val variable = RefreshableVariable<Int>(5)
        assertEquals(5, variable.getValue())
        assertEquals(5, variable.getValue())
        variable.setRefresher { it + 1 }
        assertEquals(6, variable.getValue())
        assertEquals(6, variable.getValue())
    }
}