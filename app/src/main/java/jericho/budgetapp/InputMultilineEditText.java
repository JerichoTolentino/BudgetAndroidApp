package jericho.budgetapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by tolenti4-INS on 2018-01-27.
 */

public class InputMultilineEditText extends android.support.v7.widget.AppCompatEditText
{
    private static Context m_context;

    public InputMultilineEditText(Context context) {
        super(context);
    }

    public InputMultilineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputMultilineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_ENTER)
        {
            InputMethodManager imm = (InputMethodManager) m_context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void setContext(Context context)
    {
        if (context != null)
        {
            m_context = context;
        }
    }
}
