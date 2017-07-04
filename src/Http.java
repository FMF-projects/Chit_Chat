import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

public class Http {

	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException {
	}
	
	public static String uporabniki() throws ClientProtocolException, IOException {
    	String prijavljeni = Request.Get("http://chitchat.andrej.com/users")
                			  .execute()
                              .returnContent().asString();
    	return prijavljeni;
	}
	
	
	public static String prijava(String ime) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/users")
					.addParameter("username", ime)
					.build();
		
		String responseBody = Request.Post(uri)
									 .execute()
									 .returnContent()
									 .asString();
		return responseBody;	
	}
	
	
	public static String odjava(String ime) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/users")
					.addParameter("username", ime)
					.build();
		
		String responseBody = Request.Delete(uri)
									 .execute()
									 .returnContent()
									 .asString();
		return responseBody;		
	}
	
	public static String prejmi_sporocilo(String ime) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
					.addParameter("username", ime)
					.build();
		
		String responseBody = Request.Get(uri)
									.execute()
									.returnContent()
									.asString();
		return responseBody;
	}
	
	
	public static String poslji_sporocilo(String ime, String sporocilo) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
						.addParameter("username", ime)
						.build();
		
		String responseBody = Request.Post(uri)
									.bodyString(sporocilo, ContentType.APPLICATION_JSON)
									.execute()
									.returnContent()
									.asString();
		return responseBody;
	}
	
}