
import kotlin.math.min

class Solution {

    private companion object {
        val INPUT_RANGE_SECONDS = intArrayOf(1, 99)
        val INPUT_RANGE_MINUTES = intArrayOf(1, 99)

        const val SECONDS_IN_ONE_MINUTE = 60
        const val TIME_COMBINATION_EXCEEDS_INPUT_RANGE = Integer.MAX_VALUE
    }

    fun minCostSetTime(startAt: Int, moveCost: Int, pushCost: Int, targetSeconds: Int): Int {
        var minutes = getMinutes(targetSeconds)
        var seconds = getSeconds(targetSeconds)

        val cookingTimeWithDigitsForMinutesSetToMaximum =
            calculateCookingTime(startAt, moveCost, pushCost, minutes, seconds)

        if (!isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(seconds)) {
            return cookingTimeWithDigitsForMinutesSetToMaximum
        }

        --minutes
        seconds += SECONDS_IN_ONE_MINUTE
        val cookingTimeWithDigitsForSecondsSetToMaximum =
            calculateCookingTime(startAt, moveCost, pushCost, minutes, seconds)

        if (cookingTimeWithDigitsForMinutesSetToMaximum == TIME_COMBINATION_EXCEEDS_INPUT_RANGE) {
            return cookingTimeWithDigitsForSecondsSetToMaximum
        }
        return Math.min(cookingTimeWithDigitsForMinutesSetToMaximum, cookingTimeWithDigitsForSecondsSetToMaximum)
    }

    private fun getMinutes(timeInSeconds: Int): Int {
        return timeInSeconds / SECONDS_IN_ONE_MINUTE
    }

    private fun getSeconds(timeInSeconds: Int): Int {
        return timeInSeconds % SECONDS_IN_ONE_MINUTE
    }

    private fun getFirstDigit(time: Int): Int {
        return time / 10
    }

    private fun getSecondDigit(time: Int): Int {
        return time % 10
    }

    /*
    Example of possible transfer of one minute from digits for minutes to digits for seconds:
    cookingTimeWithDigitsForMinutesSetToMaximum = 10:00
    cookingTimeWithDigitsForSecondsSetToMaximum = 09:60
    (0 + 60) seconds: possible since it does not exceed the maximum of 99 seconds
    ------------------------------------------------------
    Example of impossible transfer of one minute from digits for minutes to digits for seconds:
    cookingTimeWithDigitsForMinutesSetToMaximum = 10:50
    cookingTimeWithDigitsForSecondsSetToMaximum = 09:(50+60)
    (50 + 60) seconds: impossible since it exceeds the maximum of 99 seconds
     */
    private fun isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(seconds: Int): Boolean {
        return seconds + SECONDS_IN_ONE_MINUTE <= INPUT_RANGE_SECONDS[1]
    }

    private fun calculateCookingTime(startAt: Int, moveCost: Int, pushCost: Int, minutes: Int, seconds: Int): Int {
        if (minutes > INPUT_RANGE_MINUTES[1]) {
            return TIME_COMBINATION_EXCEEDS_INPUT_RANGE
        }

        val cookingTime = intArrayOf(
            getFirstDigit(minutes), getSecondDigit(minutes),
            getFirstDigit(seconds), getSecondDigit(seconds)
        )

        var digit = startAt
        var leadingZeros = true
        var currentCookingTime = 0

        for (i in cookingTime.indices) {
            if (cookingTime[i] != 0) {
                leadingZeros = false
            }
            if (leadingZeros && cookingTime[i] == 0) {
                continue
            }

            if (digit != cookingTime[i]) {
                currentCookingTime += moveCost
                digit = cookingTime[i]
            }
            currentCookingTime += pushCost
        }

        return currentCookingTime
    }
}
