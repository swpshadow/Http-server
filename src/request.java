import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class request {

	public static void main(String[] args) {
		try {
			URL url = new URL("http://localhost/public_html/file1.html");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			
			try (BufferedInputStream in = new BufferedInputStream(url.openStream());
					  FileOutputStream fileOutputStream = new FileOutputStream("/public_html/file2.htm")) {
					    byte dataBuffer[] = new byte[1024];
					    int bytesRead;
					    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
					        fileOutputStream.write(dataBuffer, 0, bytesRead);
					    }
					    System.out.println(Arrays.toString(dataBuffer));
					} catch (IOException e) {
					    // handle exception
					}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
