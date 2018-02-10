package com.budget_app.utilities;

import java.text.ParseException;

/**
 * Created by Jericho on 11/5/2017.
 */

public class MoneyFormatter
{

    //region Long To Money

    public static String formatLongToMoney(long amount, boolean addDollarSign)
    {
        String money = "";

        if (addDollarSign)
            money += "$";

        if (amount < 0) {
            money += "-";
            amount = Math.abs(amount);
        }

        if(amount < 10)
            money += "0." + twoDigitToString(amount);
        else
        {
            long dollarPart = amount / 100;
            int centsPart = (int)(amount % 100);

            money += formatDollarPart(dollarPart) + "." + twoDigitToString(centsPart);
        }

        return money;
    }

    private static String formatDollarPart(long dollarPart)
    {
        String formattedDollarPart = "";

        if(dollarPart > 1000)
        {
            //iteratively add commas until most significant digits
            while(dollarPart > 1000)
            {
                if(formattedDollarPart.equals(""))
                    formattedDollarPart = threeDigitToString(dollarPart % 1000);
                else
                    formattedDollarPart = threeDigitToString(dollarPart % 1000) + "," + formattedDollarPart;
                dollarPart /= 1000;
            }
            //add most significant digits as they are (without leading zeroes)
            formattedDollarPart = String.valueOf(dollarPart) + "," + formattedDollarPart;
        }
        else
            formattedDollarPart = String.valueOf(dollarPart);

        return formattedDollarPart;
    }

    private static String twoDigitToString(long twoDigit)
    {
        String result;

        if(twoDigit < 10)
            result = "0" + String.valueOf(twoDigit);
        else
            result = String.valueOf(twoDigit);

        return result;
    }

    private static String threeDigitToString(long threeDigit)
    {
        String result;

        if(threeDigit < 10)
            result = "00" + String.valueOf(threeDigit);
        else if(threeDigit < 100)
            result = "0" + String.valueOf(threeDigit);
        else
            result = String.valueOf(threeDigit);

        return result;
    }

    //endregion

    //region Money To Long

    public static long formatMoneyToLong(String money) throws ParseException
    {
        if (StringHelper.countOccurrences(money, "$") > 1 || StringHelper.countOccurrences(money, ".") > 1)
            throw new ParseException("Money parsing error", 0);

        money = money.replace("$", "").replace(",", "");

        int length = money.length();
        int decimalIndex = money.indexOf(".");


        if (length == 0)
        {
            return 0;
        }
        else if (decimalIndex == -1)
        {
            return Long.parseLong(money) * 100;
        }
        else if (decimalIndex == (length - 1) - 2)
        {
            money = money.replace(".", "");
            return Long.parseLong(money);
        }
        else
        {
            throw new ParseException("Money decimal parsing error", decimalIndex);
        }

    }

    //endregion

}
