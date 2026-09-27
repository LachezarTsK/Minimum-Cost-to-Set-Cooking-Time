
public class Solution {

    private static final int[] INPUT_RANGE_SECONDS = {1, 99};
    private static final int[] INPUT_RANGE_MINUTES = {1, 99};

    private static final int SECONDS_IN_ONE_MINUTE = 60;
    private static final int TIME_COMBINATION_EXCEEDS_INPUT_RANGE = Integer.MAX_VALUE;

    public int minCostSetTime(int startAt, int moveCost, int pushCost, int targetSeconds) {
        int minutes = getMinutes(targetSeconds);
        int seconds = getSeconds(targetSeconds);

        int cookingTimeWithDigitsForMinutesSetToMaximum
                = calculateCookingTime(startAt, moveCost, pushCost, minutes, seconds);

        if (!isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(seconds)) {
            return cookingTimeWithDigitsForMinutesSetToMaximum;
        }

        --minutes;
        seconds += SECONDS_IN_ONE_MINUTE;
        int cookingTimeWithDigitsForSecondsSetToMaximum
                = calculateCookingTime(startAt, moveCost, pushCost, minutes, seconds);

        if (cookingTimeWithDigitsForMinutesSetToMaximum == TIME_COMBINATION_EXCEEDS_INPUT_RANGE) {
            return cookingTimeWithDigitsForSecondsSetToMaximum;
        }
        return Math.min(cookingTimeWithDigitsForMinutesSetToMaximum, cookingTimeWithDigitsForSecondsSetToMaximum);
    }

    private static int getMinutes(int timeInSeconds) {
        return timeInSeconds / SECONDS_IN_ONE_MINUTE;
    }

    private static int getSeconds(int timeInSeconds) {
        return timeInSeconds % SECONDS_IN_ONE_MINUTE;
    }

    private static int getFirstDigit(int time) {
        return time / 10;
    }

    private static int getSecondDigit(int time) {
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
    private static boolean isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(int seconds) {
        return seconds + SECONDS_IN_ONE_MINUTE <= INPUT_RANGE_SECONDS[1];
    }

    private static int calculateCookingTime(int startAt, int moveCost, int pushCost, int minutes, int seconds) {
        if (minutes > INPUT_RANGE_MINUTES[1]) {
            return TIME_COMBINATION_EXCEEDS_INPUT_RANGE;
        }

        int[] cookingTime = {getFirstDigit(minutes), getSecondDigit(minutes),
            getFirstDigit(seconds), getSecondDigit(seconds)};

        int digit = startAt;
        boolean leadingZeros = true;
        int currentCookingTime = 0;

        for (int i = 0; i < cookingTime.length; ++i) {
            if (cookingTime[i] != 0) {
                leadingZeros = false;
            }
            if (leadingZeros && cookingTime[i] == 0) {
                continue;
            }

            if (digit != cookingTime[i]) {
                currentCookingTime += moveCost;
                digit = cookingTime[i];
            }
            currentCookingTime += pushCost;
        }

        return currentCookingTime;
    }
}
