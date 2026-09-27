
class Util {
    static INPUT_RANGE_SECONDS = [1, 99];
    static INPUT_RANGE_MINUTES = [1, 99];

    static SECONDS_IN_ONE_MINUTE = 60;
    static TIME_COMBINATION_EXCEEDS_INPUT_RANGE = Number.MAX_SAFE_INTEGER;
}

function minCostSetTime(startAt: number, moveCost: number, pushCost: number, targetSeconds: number): number {
    let minutes = getMinutes(targetSeconds);
    let seconds = getSeconds(targetSeconds);

    const cookingTimeWithDigitsForMinutesSetToMaximum
        = calculateCookingTime(startAt, moveCost, pushCost, minutes, seconds);

    if (!isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(seconds)) {
        return cookingTimeWithDigitsForMinutesSetToMaximum;
    }

    --minutes;
    seconds += Util.SECONDS_IN_ONE_MINUTE;
    const cookingTimeWithDigitsForSecondsSetToMaximum
        = calculateCookingTime(startAt, moveCost, pushCost, minutes, seconds);

    if (cookingTimeWithDigitsForMinutesSetToMaximum === Util.TIME_COMBINATION_EXCEEDS_INPUT_RANGE) {
        return cookingTimeWithDigitsForSecondsSetToMaximum;
    }
    return Math.min(cookingTimeWithDigitsForMinutesSetToMaximum, cookingTimeWithDigitsForSecondsSetToMaximum);
};

function getMinutes(timeInSeconds: number): number {
    return Math.floor(timeInSeconds / Util.SECONDS_IN_ONE_MINUTE);
}

function getSeconds(timeInSeconds: number): number {
    return timeInSeconds % Util.SECONDS_IN_ONE_MINUTE;
}

function getFirstDigit(time: number): number {
    return Math.floor(time / 10);
}

function getSecondDigit(time: number): number {
    return time % 10;
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
function isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(seconds: number): boolean {
    return seconds + Util.SECONDS_IN_ONE_MINUTE <= Util.INPUT_RANGE_SECONDS[1];
}

function calculateCookingTime(startAt: number, moveCost: number, pushCost: number, minutes: number, seconds: number): number {
    if (minutes > Util.INPUT_RANGE_MINUTES[1]) {
        return Util.TIME_COMBINATION_EXCEEDS_INPUT_RANGE;
    }

    const cookingTime = [getFirstDigit(minutes), getSecondDigit(minutes),
    getFirstDigit(seconds), getSecondDigit(seconds)];

    let digit = startAt;
    let leadingZeros = true;
    let currentCookingTime = 0;

    for (let i = 0; i < cookingTime.length; ++i) {
        if (cookingTime[i] !== 0) {
            leadingZeros = false;
        }
        if (leadingZeros && cookingTime[i] === 0) {
            continue;
        }

        if (digit !== cookingTime[i]) {
            currentCookingTime += moveCost;
            digit = cookingTime[i];
        }
        currentCookingTime += pushCost;
    }

    return currentCookingTime;
}
