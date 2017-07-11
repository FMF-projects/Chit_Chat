import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

public class MessageRobot extends TimerTask {
	private static ChatFrame chat;
	private Timer timer;
	
	private static List<Sporocilo> sporocila;
	private static List<Uporabnik> uporabniki;
	
	public MessageRobot(ChatFrame chat) {
		this.chat = chat;
	}
	
	public void activate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 3000);
	}
	
	public void deactivate() {
		timer.cancel();
	}
	
	@Override
	public void run() {		
		try {
			String user = ChatFrame.user;

			sporocila = Http.prejmi_sporocilo(user);
			izpisi_sporocila(sporocila);
			
			uporabniki = Http.uporabniki();
			ChitChat.chatFrame.izpisi_uporabnike(uporabniki);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param uporabniki: seznam uporabnikov
	 * @param uporabnik_str: ime uporabnika, ki ga zelimo
	 * @return objekt z imenom uporabnik_str
	 */
	public static Uporabnik StringToUporabnik(List<Uporabnik> uporabniki, String uporabnik_str) {
		Uporabnik iskan_uporabnik = null;
		for (Uporabnik uporabnik : uporabniki) {
			if (uporabnik.getUsername() == uporabnik_str) {
				iskan_uporabnik = uporabnik;
			} else {
				
			}
		}	
		if (iskan_uporabnik == null) {
			iskan_uporabnik = new Uporabnik(uporabnik_str, new Date());
		}
		return iskan_uporabnik;
	}
	
	/**
	 * V okno za sporocila doda podana sporocila.
	 * @param sporocila: sporocila, ki jih zelimo prikazati v oknu
	 */
	public static void izpisi_sporocila(List<Sporocilo> sporocila) {
		for (Sporocilo sporocilo : sporocila) {
			
			System.out.println(sporocilo.toString());
			
			String cas = Sporocilo.TimeFromDate(sporocilo.getSentAt());
			String posiljatelj_str = sporocilo.getSender();
			String tekst = sporocilo.getText();
			Boolean javno = sporocilo.getGlobal();
			
			if (javno == true) {
				chat.addMessage(cas, posiljatelj_str, tekst);
			} else {
				System.out.println("zaznal_sem_sporocilo");
				Map<String, PrivateChatFrame> pogovori = ChitChat.chatFrame.zasebni_pogovori;
				if (pogovori.containsKey(posiljatelj_str)) {
					
				} else {
					ChitChat.chatFrame.zasebno_okno(posiljatelj_str);
					pogovori = ChitChat.chatFrame.zasebni_pogovori; // osvezimo
				}
				pogovori.get(posiljatelj_str).addMessage(cas, posiljatelj_str, tekst);
			}
		}
	}
	
	
}

