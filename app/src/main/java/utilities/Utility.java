package utilities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jericho.budgetapp.Model.Priceable;
import jericho.budgetapp.R;

/**
 * A class that provides useful methods used throughout the application.
 */
public class Utility
{

    /**
     * The list of all menu item IDs for the action bar.
     */
    private static final int[] ALL_MENU_ITEM_IDS = new int[] {R.id.view_history,
            R.id.add_new,
            R.id.remove,
            R.id.empty_star,
            R.id.filled_star,
            R.id.quick_add_expense};

    /**
     * Displays the specified menu items on the menu.
     * @param menu The menu to display the items on.
     * @param visible_item_ids The IDs of the menu items to display.
     */
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

    /**
     * Gets the 1-indexed day of the week. (1 = sunday, 7 = saturday)
     */
    public static int getDayOfTheWeek()
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Returns a new instance of a Comparator that compares Priceables by their names.
     * @return A new instance of a Comparator that compares Priceables by their names.
     */
    public static Comparator<Priceable> getPrioeableNameComparator()
    {
        return new Comparator<Priceable>() {
            @Override
            public int compare(Priceable priceable, Priceable t1) {
                return priceable.getName().compareTo(t1.getName());
            }
        };
    }

    /**
     * Extracts all the extras within an Intent object.
     * @param intent The intent to extract extras from.
     * @return The collection of the name-value pairs of extras extracted from the intent.
     */
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

    /**
     * Counts the number of times a string occurs within another.
     * @param source The string to search.
     * @param find The string to look for and count in the search string.
     * @return The number of times find occurs in source.
     */
    public static int countOccurrences(String source, String find)
    {
        String[] split = source.split(find);
        return split.length - 1;
    }

    /**
     * Iterates over each item in an Iterable and returns the count.
     * @param iterable The Iterable to count.
     * @return The number of items in the Iterable.
     */
    public static int count(Iterable<?> iterable)
    {
        int count = 0;
        for (Object o : iterable)
            count++;

        return count;
    }

}
