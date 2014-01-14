package us.hale.scott.wikipedia.irc.logger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;

public class WikipediaLogger {
	private SimpleLogger logger;
	private Thread loggerThread;
	private Timer streamTimer;
	private static long DELAY = 10*60*1000;// ten minutes (milliseconds)
	private Timer logTimer;

	public void setupTimers() {

		streamTimer = new Timer();
		CheckStreamTask checkTask = new CheckStreamTask();
		checkTask.setLogger(logger);
		checkTask.setLoggerThread(loggerThread);
		streamTimer.scheduleAtFixedRate(checkTask, DELAY, DELAY);

		// Set up rotate log task to happen daily at midnight.
		// Starts next midnight and repeat every 24 hours (argument in
		// milliseconds)
		logTimer = new Timer();
		RotateLogsTask rotateTask = new RotateLogsTask();
		rotateTask.setLogger(logger);
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 5);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		logTimer.scheduleAtFixedRate(rotateTask, cal.getTime(),24*60*60*1000);
		//logTimer.scheduleAtFixedRate(rotateTask, DELAY / 2, DELAY / 2);
	}

	public void run() {
		logger = new SimpleLogger();
		loggerThread = new Thread(logger, "loggerThread");
		
		setupTimers();// Timers run to check stream health and roll output files
		
		loggerThread.start();

	}

	public static void main(String[] args) throws Exception {
		WikipediaLogger wikiLog = new WikipediaLogger();
		wikiLog.run();
	}

}
