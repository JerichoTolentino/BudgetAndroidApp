package jericho.budgetapp.Presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.support.v7.widget.AppCompatEditText;

/**
 * A custom EditText that hides the on-screen input keyboard whenever the 'Enter' key is pressed.
 */
public class InputMultilineEditText extends AppCompatEditText
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

    /**
     * Hides the on-screen input keyboard whenever the 'Enter' key is pressed.
     * @param keyCode
     * @param event
     * @return
     * @see AppCompatEditText#onKeyDown(int, KeyEvent)
     */
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

    /**
     * Sets the context for this EditText.
     * @param context The desired context.
     */
    public static void setContext(Context context)
    {
        if (context != null)
        {
            m_context = context;
        }
    }
}
