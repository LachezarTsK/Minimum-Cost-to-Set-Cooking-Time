
#include <array>
#include <limits>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {

    inline static const array<int, 2> INPUT_RANGE_SECONDS{ 1, 99 };
    inline static const array<int, 2> INPUT_RANGE_MINUTES{ 1, 99 };

    static const int SECONDS_IN_ONE_MINUTE = 60;
    static const int TIME_COMBINATION_EXCEEDS_INPUT_RANGE = numeric_limits<int>::max();

public:
    int minCostSetTime(int startAt, int moveCost, int pushCost, int targetSeconds) {
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
        return min(cookingTimeWithDigitsForMinutesSetToMaximum, cookingTimeWithDigitsForSecondsSetToMaximum);
    }

private:
    static int getMinutes(int timeInSeconds) {
        return timeInSeconds / SECONDS_IN_ONE_MINUTE;
    }

    static int getSeconds(int timeInSeconds) {
        return timeInSeconds % SECONDS_IN_ONE_MINUTE;
    }

    static int getFirstDigit(int time) {
        return time / 10;
    }

    static int getSecondDigit(int time) {
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
    static bool isPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(int seconds) {
        return seconds + SECONDS_IN_ONE_MINUTE <= INPUT_RANGE_SECONDS[1];
    }

    static int calculateCookingTime(int startAt, int moveCost, int pushCost, int minutes, int seconds) {
        if (minutes > INPUT_RANGE_MINUTES[1]) {
            return TIME_COMBINATION_EXCEEDS_INPUT_RANGE;
        }

        vector<int> cookingTime = { getFirstDigit(minutes), getSecondDigit(minutes),
                                    getFirstDigit(seconds), getSecondDigit(seconds) };

        int digit = startAt;
        bool leadingZeros = true;
        int currentCookingTime = 0;

        for (int i = 0; i < cookingTime.size(); ++i) {
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
};
