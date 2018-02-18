package utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.budget_app.jt_interfaces.Priceable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jericho.budgetapp.R;

public class Utils
{

    private static final int[] ALL_MENU_ITEM_IDS = new int[] {R.id.view_history,
                                                              R.id.add_new,
                                                              R.id.remove,
                                                              R.id.empty_star,
                                                              R.id.filled_star,
                                                              R.id.quick_add_expense};

    public static void showMenuItems(Menu menu, int[] visible_item_ids)
    {
        for (int id : ALL_MENU_ITEM_IDS)
        {
            menu.findItem(id).setVisible(false);
        }

        if (visible_item_ids == null) return;

        for (int i = 0; i < visible_item_ids.length; i++)
        {
            MenuItem item = menu.findItem(visible_item_ids[i]);
            if (item != null)
            {
                item.setVisible(true);
            }
        }
    }

    // Get the zero-indexed day of the week (0 = sunday, 6 = saturday)
    public static int getDayOfTheWeek()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static Comparator<Priceable> getPrioeableNameComparator()
    {
        return new Comparator<Priceable>() {
            @Override
            public int compare(Priceable priceable, Priceable t1) {
                return priceable.getName().compareTo(t1.getName());
            }
        };
    }

    public static Map<String, Object> getExtrasFromIntent(Intent intent)
    {
        Map<String, Object> entries = new HashMap<>();

        if (intent != null && intent.getExtras() != null)
        {
            for (String key : intent.getExtras().keySet())
            {
                Object value = intent.getExtras().get(key);
                entries.put(key, value);
            }

            for (String key : entries.keySet())
                intent.removeExtra(key);

        }

        return entries;
    }

}
