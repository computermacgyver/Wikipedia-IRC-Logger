package us.hale.scott.wikipedia.irc.logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;


public class CheckStreamTask extends TimerTask {
	
	private SimpleLogger logger;
	private Thread loggerThread;
	
	private static long MAX_INTERVAL = 10*60*1000;//Ten minutes in milliseconds
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void setLogger(SimpleLogger logger) {
		this.logger=logger;
	}
	
	public void setLoggerThread(Thread loggerThread) {
		this.loggerThread = loggerThread;
	}

	@Override
	public void run() {
		long lastActivity = logger.getLastActivity();
		long diff = System.currentTimeMillis()-lastActivity;
		if (diff>MAX_INTERVAL) {
			System.err.println(df.format(new Date()) + " No activity within last period. Disconnecting, reconnecting.");
		
			/*try {
				logger.disconnect();
			} catch (Exception e) {
				System.err.println("Error while disconnecting.");
				e.printStackTrace();
			}*/
			
			/*try {
				logger.connect();
			} catch (IOException e) {
				System.err.println("Could not (re-)connect to stream.");
				e.printStackTrace();
			}*/
			
			loggerThread.interrupt();
			
		} else {
			System.err.println(df.format(new Date()) + " Last activity OK. Last activity at: " + lastActivity);
		}
	}

}
