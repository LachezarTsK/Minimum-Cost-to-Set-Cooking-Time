
package main
import "math"

var INPUT_RANGE_SECONDS = []int{1, 99}
var INPUT_RANGE_MINUTES = []int{1, 99}

const SECONDS_IN_ONE_MINUTE = 60
const TIME_COMBINATION_EXCEEDS_INPUT_RANGE = math.MaxInt

func minCostSetTime(startAt int, moveCost int, pushCost int, targetSeconds int) int {
    var minutes = getMinutes(targetSeconds)
    var seconds = getSeconds(targetSeconds)

    cookingTimeWithDigitsForMinutesSetToMaximum :=
        calculateCookingTime(startAt, moveCost, pushCost, minutes, seconds)

    if !isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(seconds) {
        return cookingTimeWithDigitsForMinutesSetToMaximum
    }

    minutes--
    seconds += SECONDS_IN_ONE_MINUTE
    cookingTimeWithDigitsForSecondsSetToMaximum :=
        calculateCookingTime(startAt, moveCost, pushCost, minutes, seconds)

    if cookingTimeWithDigitsForMinutesSetToMaximum == TIME_COMBINATION_EXCEEDS_INPUT_RANGE {
        return cookingTimeWithDigitsForSecondsSetToMaximum
    }
    return min(cookingTimeWithDigitsForMinutesSetToMaximum, cookingTimeWithDigitsForSecondsSetToMaximum)
}

func getMinutes(timeInSeconds int) int {
    return timeInSeconds / SECONDS_IN_ONE_MINUTE
}

func getSeconds(timeInSeconds int) int {
    return timeInSeconds % SECONDS_IN_ONE_MINUTE
}

func getFirstDigit(time int) int {
    return time / 10
}

func getSecondDigit(time int) int {
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
func isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(seconds int) bool {
    return seconds+SECONDS_IN_ONE_MINUTE <= INPUT_RANGE_SECONDS[1]
}

func calculateCookingTime(startAt int, moveCost int, pushCost int, minutes int, seconds int) int {
    if minutes > INPUT_RANGE_MINUTES[1] {
        return TIME_COMBINATION_EXCEEDS_INPUT_RANGE
    }

    cookingTime := []int{
        getFirstDigit(minutes), getSecondDigit(minutes),
        getFirstDigit(seconds), getSecondDigit(seconds),
    }

    var digit = startAt
    var leadingZeros = true
    var currentCookingTime = 0

    for i := range cookingTime {
        if cookingTime[i] != 0 {
            leadingZeros = false
        }
        if leadingZeros && cookingTime[i] == 0 {
            continue
        }

        if digit != cookingTime[i] {
            currentCookingTime += moveCost
            digit = cookingTime[i]
        }
        currentCookingTime += pushCost
    }

    return currentCookingTime
}
