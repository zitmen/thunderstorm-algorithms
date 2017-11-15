package cz.cuni.lf1.thunderstorm.parser.thresholding

public class RefreshableVariable<T>(private var value: T) {

    private var refresher: (T) -> T = { it }

    public fun setRefresher(refresh: (oldValue: T) -> T) {
        refresher = refresh
    }

    public fun getValue(): T {
        value = refresher(value)
        refresher = { it }
        return value
    }
}