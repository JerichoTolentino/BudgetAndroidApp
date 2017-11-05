package com.budget_app.utilities;

/**
 * Created by Jericho on 11/5/2017.
 */

public class MoneyFormatter
{

    public static String formatLongToMoney(long amount)
    {
        String money;

        if(amount < 10)
            money = "$0." + twoDigitToString(amount);
        else
        {
            long dollarPart = amount / 100;
            int centsPart = (int)(amount % 100);

            money = "$" + formatDollarPart(dollarPart) + "." + twoDigitToString(centsPart);
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

}
