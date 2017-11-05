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
            money = "$0.0" + String.valueOf(amount);
        else if(amount < 100)
            money = "$0." + String.valueOf(amount);
        else
        {
            long dollarPart = amount / 100;
            int centsPart = (int)(amount % 100);

            if(centsPart < 10)
                money = "$" + dollarPart + ".0" + centsPart;
            else
                money = "$" + dollarPart + "." + centsPart;
        }

        return money;
    }

}
