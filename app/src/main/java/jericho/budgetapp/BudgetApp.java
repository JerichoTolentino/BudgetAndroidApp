package jericho.budgetapp;

import android.app.Application;
import android.content.Context;

public class BudgetApp extends Application
{

    public static final String DB_NAME = "BudgetApp.db";

    private static Context m_context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        BudgetApp.m_context = getApplicationContext();
    }

    public static Context getContext()
    {
        return BudgetApp.m_context;
    }

}
