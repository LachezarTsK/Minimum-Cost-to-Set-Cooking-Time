
using System;

public class Solution
{
    private static readonly int[] INPUT_RANGE_SECONDS = { 1, 99 };
    private static readonly int[] INPUT_RANGE_MINUTES = { 1, 99 };

    private static readonly int SECONDS_IN_ONE_MINUTE = 60;
    private static readonly int TIME_COMBINATION_EXCEEDS_INPUT_RANGE = int.MaxValue;

    public int MinCostSetTime(int startAt, int moveCost, int pushCost, int targetSeconds)
    {
        int minutes = GetMinutes(targetSeconds);
        int seconds = GetSeconds(targetSeconds);

        int cookingTimeWithDigitsForMinutesSetToMaximum
                = CalculateCookingTime(startAt, moveCost, pushCost, minutes, seconds);

        if (!IsPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(seconds))
        {
            return cookingTimeWithDigitsForMinutesSetToMaximum;
        }

        --minutes;
        seconds += SECONDS_IN_ONE_MINUTE;
        int cookingTimeWithDigitsForSecondsSetToMaximum
                = CalculateCookingTime(startAt, moveCost, pushCost, minutes, seconds);

        if (cookingTimeWithDigitsForMinutesSetToMaximum == TIME_COMBINATION_EXCEEDS_INPUT_RANGE)
        {
            return cookingTimeWithDigitsForSecondsSetToMaximum;
        }
        return Math.Min(cookingTimeWithDigitsForMinutesSetToMaximum, cookingTimeWithDigitsForSecondsSetToMaximum);
    }

    private static int GetMinutes(int timeInSeconds)
    {
        return timeInSeconds / SECONDS_IN_ONE_MINUTE;
    }

    private static int GetSeconds(int timeInSeconds)
    {
        return timeInSeconds % SECONDS_IN_ONE_MINUTE;
    }

    private static int GetFirstDigit(int time)
    {
        return time / 10;
    }

    private static int GetSecondDigit(int time)
    {
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
    private static bool IsPossibleToTransferOneMinuteFromDigitsForMinutesToDigitsForSeconds(int seconds)
    {
        return seconds + SECONDS_IN_ONE_MINUTE <= INPUT_RANGE_SECONDS[1];
    }

    private static int CalculateCookingTime(int startAt, int moveCost, int pushCost, int minutes, int seconds)
    {
        if (minutes > INPUT_RANGE_MINUTES[1])
        {
            return TIME_COMBINATION_EXCEEDS_INPUT_RANGE;
        }

        int[] cookingTime = {GetFirstDigit(minutes), GetSecondDigit(minutes),
            GetFirstDigit(seconds), GetSecondDigit(seconds)};

        int digit = startAt;
        bool leadingZeros = true;
        int currentCookingTime = 0;

        for (int i = 0; i < cookingTime.Length; ++i)
        {
            if (cookingTime[i] != 0)
            {
                leadingZeros = false;
            }
            if (leadingZeros && cookingTime[i] == 0)
            {
                continue;
            }

            if (digit != cookingTime[i])
            {
                currentCookingTime += moveCost;
                digit = cookingTime[i];
            }
            currentCookingTime += pushCost;
        }

        return currentCookingTime;
    }
}
