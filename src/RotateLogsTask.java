import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class RotateLogsTask extends TimerTask {
	
	SimpleLogger logger;
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void setLogger(SimpleLogger logger) {
		this.logger = logger;
	}
	
	public RotateLogsTask() {
		
	}

	

	@Override
	public void run() {
		if (logger!=null) {
			try {
				System.err.println("Rotating log file now at : " + df.format(new Date()));
				logger.rollOutputFiles();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	

}
