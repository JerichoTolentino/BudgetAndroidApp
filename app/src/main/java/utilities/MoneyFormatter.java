package utilities;

import java.text.ParseException;

/**
 * A class that provides method to handle string formatting of currency values.
 */
public class MoneyFormatter
{

    //region Long To Money

    /**
     * Converts a long to a string currency.
     * @param amount The amount to convert.
     * @param addDollarSign Value that indicates whether to add a dollar sign to the resulting currency.
     * @return A string representation of the currency.
     */
    public static String formatLongToMoney(long amount, boolean addDollarSign)
    {
        String money = "";

        if (amount < 0) {
            money += "-";
            amount = Math.abs(amount);
        }

        if (addDollarSign)
            money += "$";

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

    /**
     * Formats the dollar amount of a currency with comma separators for every group of three digits.
     * @param dollarPart The dollar amount of a currency.
     * @return A comma-separated dollar portion of a currency.
     */
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

    /**
     * Prefixes a leading zero to a number if it is only 1 digit.
     * @param twoDigit The number to format.
     * @return A numeric string with at least 2 digits.
     */
    private static String twoDigitToString(long twoDigit)
    {
        String result;

        if(twoDigit < 10)
            result = "0" + String.valueOf(twoDigit);
        else
            result = String.valueOf(twoDigit);

        return result;
    }

    /**
     * Prefixes leading zeroes to ensure a number contains at least 3 digits
     * @param threeDigit The number to format.
     * @return A numeric string with at least 3 digits.
     */
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

    /**
     * Parses a string representation of currency to a long value.
     * @param money The string representation of currency.
     * @return The value of the currency as a long.
     * @throws ParseException Thrown when the string input is not a valid currency.
     */
    public static long formatMoneyToLong(String money) throws ParseException
    {
        if (Utility.countOccurrences(money, "$") > 1 || Utility.countOccurrences(money, ".") > 1)
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
