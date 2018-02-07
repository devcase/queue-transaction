package queuetransaction;

import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

public class Main {
	final static int TIMEOUT = 10;
	
	public static void main(String[] args) throws IOException, ParseException {
		// create Options object
		Options options = new Options();
		options.addOption("u", true, "authentication");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);


		HttpClientContext context = HttpClientContext.create();
		HttpClientConnectionManager connMrg = new BasicHttpClientConnectionManager();
		
		RequestConfig requestConfig = RequestConfig.custom()
			    .setConnectionRequestTimeout(TIMEOUT * 1000)
			    .setConnectTimeout(TIMEOUT * 1000)
			    .setSocketTimeout(TIMEOUT * 1000)
			    .build();
		
		CloseableHttpClient client = HttpClients.custom()
				.disableAutomaticRetries()
				.disableRedirectHandling()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(connMrg)
				.build();
		
		Scanner scanner = new Scanner(System.in);
		try {
			while(scanner.hasNext()) {
				String data = scanner.nextLine();
				HttpPost post = new HttpPost(cmd.getArgs()[0]);
				
				if(options.hasOption("u")) {
					String encodedauth = Base64.encodeBase64String(cmd.getOptionValue("u").getBytes());
					post.setHeader("Authorization", "Basic " + encodedauth);
				}
				
				post.setHeader("content-type", "application/x-www-form-urlencoded");
				HttpEntity entity = new UrlEncodedFormEntity(Collections.singletonList(new BasicNameValuePair("body", data)));
				post.setEntity(entity);
					
				CloseableHttpResponse response = client.execute(post);
				System.out.println(data);
				System.out.println(response.getStatusLine().toString());
				response.close();
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally {
			client.close();
			scanner.close();
		}
		
	}

}
 