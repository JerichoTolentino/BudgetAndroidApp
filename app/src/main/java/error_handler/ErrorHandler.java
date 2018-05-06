package error_handler;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A class that contains functionality for common error handling.
 */
public class ErrorHandler
{

	//region Constants

	private final String m_logFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "\\BudgetBuddy_ErrorLog\\error_log.txt";

	//endregion

	//region Members

	private File m_logFile = new File(m_logFilePath);

	//endregion

	//region Constructor

	/**
	 * Initializes a new instance of an ErrorHandler.
	 */
	public ErrorHandler()
	{
		m_logFile.mkdirs();
	}

	//endregion

	//region Public Methods

	/**
	 * Logs an exception to the error log file.
	 * @param ex The exception to log.
	 */
	public void logException(Exception ex)
	{
		String msg = ex.getMessage() + writeStackTraceToString(ex);

		writeToLog(msg);
	}

	//endregion

	//region Helper Methods

	/**
	 * Writes a message to the log file.
	 * @param msg The message to write.
	 */
	private void writeToLog(String msg)
	{
		try (FileWriter writer = new FileWriter(m_logFile))
		{
			writer.write(msg);
		}
		catch (IOException ex)
		{
			// Failed to write to error log (this should never happen)
			System.out.println(ex.toString());
			assert false;
		}
	}

	/**
	 * Writes an exception's stack trace to a string.
	 * @param ex The exception containing the stack trace to write to a string.
	 * @return A stack trace as a string.
	 */
	private String writeStackTraceToString(Exception ex)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		ex.printStackTrace(pw);

		return sw.toString();
	}

	//endregion

}