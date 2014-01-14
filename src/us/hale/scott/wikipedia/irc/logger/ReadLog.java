package us.hale.scott.wikipedia.irc.logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ReadLog {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String line = br.readLine();
		while (line!=null) {
			System.out.println(line.replaceAll("\003([0-9]){0,2}(,[0-9]{1,2})?", ""));
			line=br.readLine();
		}
		br.close();

	}

}
