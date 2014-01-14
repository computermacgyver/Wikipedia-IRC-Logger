import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger implements Runnable {
	
	private Socket socket;
	private BufferedReader ircReader;
	private BufferedWriter ircWriter;
	private OutputStreamWriter fileWriter;
	private long lastActivity;
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	
	private static Charset UTF8 = Charset.forName("UTF-8");
	
	public SimpleLogger() {
	}
		
	public boolean connect() throws IOException {
		
		if (socket!=null || ircWriter!=null || ircReader!=null) {
			disconnect();
		}
		
		String[] channels = getChannels();

		// The server to connect to and our details.
		String server = "irc.wikimedia.org";
		String nick = "academic_record"+(int)(Math.random()*1000);
		String login = "academic_study";

		// Connect directly to the IRC server.
		socket = new Socket(server, 6667);
		ircWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),UTF8));
		ircReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),UTF8));


		// Log on to the server.
		ircWriter.write("NICK " + nick + "\r\n");
		ircWriter.write("USER " + login
				+ " 8 * : Academic study of Wikipedia Changes\r\n");
		ircWriter.flush();

		// Read lines from the server until it tells us we have connected.
		String line = null;
		while ((line = ircReader.readLine()) != null) {
			if (line.indexOf("004") >= 0) {
				// We are now logged in.
				break;
			} else if (line.indexOf("433") >= 0) {
				System.err.println("Nickname " + nick +  " is already in use. Cannot connect.");
				return false;
			}
		}

		// Join the channels.
		for (String channel : channels) {
			ircWriter.write("JOIN " + channel + "\r\n");
		}
		ircWriter.flush();
		
		return true;
	}
	
	
	
	public void closeOutputFiles() {
		if (fileWriter!=null) {
			try {
				fileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void rollOutputFiles() throws FileNotFoundException {
		Date d = new Date();
		String nextFile = "irc-logger--"+df.format(d)+".txt";

		if (fileWriter==null) {
			FileOutputStream outStream = new FileOutputStream(nextFile,true);//Open for appending
			fileWriter = new OutputStreamWriter(outStream,UTF8); //Force UTF-8 encoding
		} else {
			synchronized (fileWriter) {//No writing while we close and open next file
				closeOutputFiles();
				FileOutputStream outStream = new FileOutputStream(nextFile,true);//Open for appending
				fileWriter = new OutputStreamWriter(outStream,UTF8); //Force UTF-8 encoding
			}
		}
	}
	
	public void readLines() throws IOException {
		String uLine,line;
		while ((line = ircReader.readLine()) != null) {
			lastActivity=System.currentTimeMillis();
			uLine = line.toUpperCase();
			if (uLine.startsWith("PING ")) {
				// We must respond to PINGs to avoid being disconnected.
				ircWriter.write("PONG " + line.substring(5) + "\r\n");
				ircWriter.flush();
			} /*else if (uLine.contains("PRIVMSG")) {
				fileWriter.write(lastActivity + "\t" + line + "\n");
			}*/ else {
				fileWriter.write(lastActivity + "\t" + line + "\n");
			}
		}
	}
	
	public void disconnect() {
		try {
			ircWriter.write("QUIT :leaving now.\r\n");
			ircWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ircWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ircReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	

	public static String[] getChannels() {
		return new String[] { "#simple.wikipedia", "#ca.wikipedia",
				"#cs.wikipedia", "#gl.wikipedia", "#pt.wikipedia",
				"#tr.wikipedia", "#lt.wikipedia", "#war.wikipedia",
				"#pl.wikipedia", "#ceb.wikipedia", "#hr.wikipedia",
				"#de.wikipedia", "#hu.wikipedia", "#hi.wikipedia",
				"#he.wikipedia", "#uz.wikipedia", "#ms.wikipedia",
				"#eo.wikipedia", "#en.wikipedia", "#zh.wikipedia",
				"#vi.wikipedia", "#it.wikipedia", "#vo.wikipedia",
				"#ar.wikipedia", "#eu.wikipedia", "#et.wikipedia",
				"#id.wikipedia", "#es.wikipedia", "#ru.wikipedia",
				"#nl.wikipedia", "#nn.wikipedia", "#no.wikipedia",
				"#ro.wikipedia", "#fr.wikipedia", "#bg.wikipedia",
				"#uk.wikipedia", "#fa.wikipedia", "#fi.wikipedia",
				"#da.wikipedia", "#ja.wikipedia", "#kk.wikipedia",
				"#sr.wikipedia", "#ko.wikipedia", "#sv.wikipedia",
				"#sk.wikipedia", "#sl.wikipedia" };
	}

	public long getLastActivity() {
		return lastActivity;
	}

	@Override
	public void run() {
		while (true) {
			while (true) {
				try {
					connect();
					break;
				} catch (Exception e) {
					System.err.println("Error in main while loop while reconnecting.");
					try {
						Thread.sleep(5*60*1000);
					} catch (InterruptedException ie) {
						System.err.println("Sleep interrupted.");
					}
				}
			}
			
			try {
				rollOutputFiles();
				readLines();//Blocks until disconnect
			} catch (Exception e) {
				System.err.println("Error while reading irc output. Will disconnect and re-connect.");
				e.printStackTrace();
			}
			
			disconnect();
		}
		
	}

}