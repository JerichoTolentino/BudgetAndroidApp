package com.budget_app.utilities;

/**
 * Created by tolenti4-INS on 2018-02-04.
 */

public class StringHelper
{

    public static int countOccurrences(String source, String find)
    {
        String[] split = source.split(find);
        return split.length - 1;
    }

}
