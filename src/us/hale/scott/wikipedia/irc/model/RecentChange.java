package us.hale.scott.wikipedia.irc.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

public class RecentChange {
	//private static Logger logger = LoggerFactory.getLogger(RecentChange.class);
	
	
	private Date datetime;
	private String channel;
	private String user;
	private String article;
	private String url;
	private String comment;
	private int lineCount;
	
	private boolean minor;
	private boolean bot;
	private boolean newPage;
	
	//getLanguage, isAnonymous()
	
	private Pattern ipv6;
	private Pattern ipv4;
	public RecentChange() {
		//IPv6 pattern from http://home.deds.nl/~aeron/regex/java.html
		ipv6=Pattern.compile("^([\\dA-F]{1,4}:|((?=.*(::))(?!.*\\3.+\\3))\\3?)([\\dA-F]{1,4}(\\3|:\\b)|\\2){5}(([\\dA-F]{1,4}(\\3|:\\b|$)|\\2){2}|(((2[0-4]|1\\d|[1-9])?\\d|25[0-5])\\.?\\b){4})\\z"
, Pattern.CASE_INSENSITIVE);
		ipv4=Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
		
	}
	
	
	public static RecentChange parse(String irc) {
		RecentChange rc = new RecentChange();
		
		//Date
		String[] parts = irc.split("\t");
		rc.setDatetime(new Date(Long.valueOf(parts[0])));
		
		//Split on control characters that change color
		parts = parts[1].split("\003([0-9]){0,2}(,[0-9]{1,2})?");
		
		/*for (int i=0; i<parts.length;i++) {
			System.out.println(i + ": " + parts[i]);
		}*/
		
		//Channel
		int hash = parts[0].indexOf("#");
		rc.setChannel(parts[0].substring(hash,parts[0].indexOf(" ",hash)));
		
		//Page
		rc.setArticle(parts[2]);
		
		//Url
		rc.setUrl(parts[6]);
		
		//User
		rc.setUser(parts[10]);
		
		String lineCount = parts[13].replaceAll("\002", "");
		int start = lineCount.indexOf("(");
		int end = lineCount.indexOf(")");
		if (start!=-1) {
			rc.setLineCount(Integer.valueOf(lineCount.substring(start+1,end)));
		}
		
		if (parts.length>14) {
			rc.setComment(parts[14]);
		}
		
		//Flags
		String flags = parts[4];
		
		if (flags.contains("M")) {
			rc.setMinor(true);
		}
		if (flags.contains("B")) {
			rc.setBot(true);
		} /*else if (rc.getUser().toLowerCase().contains("bot")) {
			logger.debug("Edit not flagged as bot, but believe it is bot because user is " + rc.getUser());
			rc.setBot(true);
		} /*else if (rc.getComment()!=null && rc.getComment().toLowerCase().contains("bot")) {
			System.err.println("Edit not flagged as bot, but believe it is bot because comment is " + rc.getComment());
			rc.setBot(true);
		}*/
		
		if (flags.contains("N")) {
			rc.setNewPage(true);
		}
		
		//String info = irc.replaceAll("\003([0-9]){0,2}(,[0-9]{1,2})?", "");//control characters
		return rc;
	}
	
	/*public JSONObject toJson() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("language", getLanguage());
		map.put("article", getArticle());
		map.put("datetime", String.valueOf(getDatetime().getTime()));
		map.put("url", getUrl());
		map.put("lineCount", String.valueOf(getLineCount()));
		return new JSONObject(map);
	}*/
	
	public static void main(String args[]) throws IOException {
		//RecentChange rc = RecentChange.parse("1370552809744\t:rc-pmtpa!~rc-pmtpa@special.user PRIVMSG #en.wikipedia :\00314[[\00307User:Mimich/Sandbox 6\00314]]\0034 \00310 \00302http://en.wikipedia.org/w/index.php?diff=558662916&oldid=558355812\003 \0035*\003 \00303Mimich\003 \0035*\003 (+12) \00310\003");
		//System.out.println(rc);
		
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String line = br.readLine();
		int count=0;
		while (line!=null && count>-1) {
			if (line.contains("PRIVMSG #")) {
				//System.out.println(line.replaceAll("\003([0-9]){0,2}(,[0-9]{1,2})?", ""));
				//System.out.println(RecentChange.parse(line));
				RecentChange rc = RecentChange.parse(line);
				System.out.println(rc.getNamespace());
				count++;
			}
			line=br.readLine();
		}
		br.close();
	}
	
	public String getLanguage() {
		return channel.substring(1,channel.indexOf("."));
	}
	
	public boolean isAnonymous() {
		return ipv4.matcher(user).matches() || ipv6.matcher(user).matches(); 
	}
	
	public String getNamespace() {
		if (getArticle()!=null && getArticle().indexOf(":")!=-1) {
			return getArticle().substring(0,getArticle().indexOf(":"));
		} else {
			return null;
		}
	}
	
	
	//Auto gen from here
	
	
	public Date getDatetime() {
		return datetime;
	}




	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}




	public String getChannel() {
		return channel;
	}




	public void setChannel(String channel) {
		this.channel = channel;
	}




	public String getUser() {
		return user;
	}




	public void setUser(String user) {
		this.user = user;
	}




	public String getArticle() {
		return article;
	}




	public void setArticle(String article) {
		this.article = article;
	}




	public String getUrl() {
		return url;
	}




	public void setUrl(String url) {
		this.url = url;
	}




	public String getComment() {
		return comment;
	}




	public void setComment(String comment) {
		this.comment = comment;
	}




	public int getLineCount() {
		return lineCount;
	}




	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}




	public boolean isMinor() {
		return minor;
	}




	public void setMinor(boolean minor) {
		this.minor = minor;
	}




	public boolean isBot() {
		return bot;
	}




	public void setBot(boolean bot) {
		this.bot = bot;
	}
	
	public boolean isNewPage() {
		return newPage;
	}

	public void setNewPage(boolean newPage) {
		this.newPage = newPage;
	}

	@Override
	public String toString() {
		return "RecentChange [datetime=" + datetime + ", channel=" + channel
				+ ", user=" + user + ", article=" + article + ", url=" + url
				+ ", comment=" + comment + ", lineCount=" + lineCount
				+ ", minor=" + minor + ", bot=" + bot + ", newPage=" + newPage
				+ "]";
	}



	
	
}
