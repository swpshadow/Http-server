import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.StringTokenizer;
public class HttpServer implements Runnable{

	static int serverPort = 16405;
	static Socket client = null;
	public HttpServer(Socket s)
	{
		client = s;
	}
	public static void main(String[] args) {
		
		
		if (args.length > 0)//set port to value from terminal
		{
			try 
			{
				serverPort = Integer.parseInt(args[0]);
			}
			catch(Exception e)
			{
				System.out.println("value from terminal is not a number: ");
				System.out.println(args[0]);
				System.out.println("using default port value");
				
			}
		}
		try
		{
			ServerSocket server = new ServerSocket(serverPort);
			while (true) {
				HttpServer serv = new HttpServer(server.accept());
				Thread t = new Thread(serv);
				t.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		BufferedReader input = null;
		BufferedOutputStream dataOut = null;
		PrintWriter output = null;
		String message = null;
		try {
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new PrintWriter(client.getOutputStream());
			dataOut = new BufferedOutputStream(client.getOutputStream());
			
			message = input.readLine();
			
			StringTokenizer parser = new StringTokenizer(message);
			String type = parser.nextToken().toUpperCase();
			
			String folder = "./public_html";
			
			if (!type.equals("GET") && !type.equals("HEAD"))
			{
				output.println("HTTP1.1 501 Not Implemented");
				System.out.println("HTTP1.1 501 Not Implemented");
			}
			else //combined head and get as get is just adding the body
			{
				String fname = parser.nextToken();
				
				if (!fname.contains(".."))
				{
					
					if(fname.contains("favicon"))
					{
						return;
					}
					else
					{
						if(fname.equals("/"))
						{
							fname = "/index.html";
						}
						System.out.println(message);
						try
						{
							
							File file = new File(folder + fname);				
							FileInputStream f = new FileInputStream(file);
							
							output.print("HTTP1.1 200 OK\n");
							System.out.println("HTTP1.1 200 OK");
							
							output.print("Server: cs4333httpserver/1.1\n");
							System.out.println("Server: cs4333httpserver/1.1");
							output.printf("Content length: %s\n", file.length());
							System.out.printf("Content length: %s\n", file.length());
							String t = getTypeOf(fname);
							output.printf("Content-type: %s\n", t);
							System.out.printf("Content-type: %s\n", t);
							dataOut.flush();
							
							if (type.equals("GET"))
							{
								byte[] data = f.readAllBytes();
								dataOut.write(data);
								dataOut.flush();
							}
							dataOut.close();
							output.close();
							f.close();
						}
						catch(FileNotFoundException e)
						{
							output.print("HTTP1.1 404 Not Found\n");
							System.out.println("HTTP1.1 404 Not Found");
						}
						catch(Exception e)
						{
							
						}
					}
				}
				else
				{
					System.out.println(message);
					output.write("HTTP1.1 404 Bad Request\n");
					System.out.println("HTTP1.1 404 Bad Request");
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.printf("there was an error: %s", e);
			e.printStackTrace();
		}
		

	}

	public static String getTypeOf(String s)
	{
		int idx = s.lastIndexOf('.');
		String type = s.substring(idx+1);
		if(type.equals("html") || type.equals("htm"))
		{
			return type + " text/html";
		}
		else if(type.equals("gif"))
		{
			return type + " image/gif";
		}
		else if (type.equals("jpg") || type.equals("jpeg"))
		{
			return type + " image/jpeg";
		}
		else if (type.equals("pdf"))
		{
			return type + " application/pdf";
		}
		else
		{
			return type + " unknown";
		}
		
	}

}
