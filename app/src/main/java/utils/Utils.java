package utils;

import android.content.ClipData;
import android.view.Menu;
import android.view.MenuItem;

import jericho.budgetapp.R;

/**
 * Created by tolenti4-INS on 2018-01-24.
 */

public class Utils
{

    private static final int[] ALL_MENU_ITEM_IDS = new int[] {R.id.view_history,
                                                              R.id.add_new,
                                                              R.id.remove};

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

}
