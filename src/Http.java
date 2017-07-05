import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class Http {

	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException {
	}
	
	// SEZNAM UPORABNIKOV
	public static List<Uporabnik> uporabniki() throws ClientProtocolException, IOException {
    	String responseBody = Request.Get("http://chitchat.andrej.com/users")
                			  .execute()
                              .returnContent().asString();
    	
    	ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		
		TypeReference<List<Uporabnik>> t = new TypeReference<List<Uporabnik>>() { };
		//System.out.print(Uporabnik.ListToString(mapper.readValue(responseBody, t)));
		return mapper.readValue(responseBody, t);
	}
	
	// PRIJAVA
	public static void prijava(String ime) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/users")
					.addParameter("username", ime)
					.build();
		
		String responseBody = Request.Post(uri)
									 .execute()
									 .returnContent()
									 .asString();
		System.out.println(responseBody);	
	}
	
	// ODJAVA
	public static void odjava(String ime) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/users")
					.addParameter("username", ime)
					.build();
		
		String responseBody = Request.Delete(uri)
									 .execute()
									 .returnContent()
									 .asString();
		System.out.println(responseBody);		
	}
	
	// PREJMI SPOROCILO
	public static List<Sporocilo> prejmi_sporocilo(String ime) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
					.addParameter("username", ime)
					.build();
		
		String responseBody = Request.Get(uri)
									.execute()
									.returnContent()
									.asString();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		
		TypeReference<List<Sporocilo>> t = new TypeReference<List<Sporocilo>>() { };
		List<Sporocilo> sporocila = mapper.readValue(responseBody, t);
		
		return sporocila;
	}
	
	// POSLJI SPOROCILO
	public static void poslji_sporocilo(String ime, String tekst) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
						.addParameter("username", ime)
						.build();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		
		Sporocilo sporocilo = new Sporocilo(true, tekst);
		String sporocilo_str = mapper.writeValueAsString(sporocilo);
		
		String responseBody = Request.Post(uri)
									.bodyString(sporocilo_str , ContentType.APPLICATION_JSON)
									.execute()
									.returnContent()
									.asString();
		System.out.println(responseBody);
	}
	
}