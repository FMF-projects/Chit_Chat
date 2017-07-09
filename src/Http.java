import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class Http {
	
	/**
	 * @return seznam prijavljenih uporabnikov
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static List<Uporabnik> uporabniki() throws ClientProtocolException, IOException {
    	String responseBody = Request.Get("http://chitchat.andrej.com/users")
                			  .execute()
                              .returnContent().asString();

    	ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		
		TypeReference<List<Uporabnik>> t = new TypeReference<List<Uporabnik>>() { };
		return mapper.readValue(responseBody, t);
	}
	
	/**
	 * @param ime: uporabnik, ki ga zelimo prijaviti na streznik
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
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

	/**
	 * @param ime: uporabnik, ki ga zelimo odjaviti s streznika
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
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
	
	/**
	 * S streznika pridobi nova sporocila.
	 * @param ime: uporabnik za katerega zahtevamo sporocila
	 * @return seznam sporocil
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
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
		return mapper.readValue(responseBody, t);
	}
	
	
	/**
	 * Strezniku poslje sporocilo.
	 * @param ime: posiljatelj sporocila
	 * @param tekst: sporocilo
	 * @param zasebno: true, ce je sporocilo zasebno, sicer false
	 * @param prejemnik: uporabnik, kateremu je sporocilo namenjeno
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void poslji_sporocilo(String ime, Boolean zasebno, String prejemnik, String tekst) 
			throws URISyntaxException, ClientProtocolException, IOException {
		
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
						.addParameter("username", ime)
						.build();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new ISO8601DateFormat());
		
		Sporocilo sporocilo;
		if (zasebno == false) {
			sporocilo = new Sporocilo(true, tekst);
		} else {
			sporocilo = new Sporocilo(false, prejemnik, tekst);
		}
		
		String sporocilo_str = mapper.writeValueAsString(sporocilo);
		
		String responseBody = Request.Post(uri)
									.bodyString(sporocilo_str , ContentType.APPLICATION_JSON)
									.execute()
									.returnContent()
									.asString();
		System.out.println(responseBody);
	}
	
}