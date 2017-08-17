import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

public class MessageRobot extends TimerTask {
	
	private ChatFrame chat;
	private Timer timer;
	
	private List<Sporocilo> sporocila;
	private List<Uporabnik> uporabniki;
	
	
	public MessageRobot(ChatFrame chat) {
		this.chat = chat;
	}
	
	public void activate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}
	
	public void deactivate() {
		timer.cancel();
	}
	
	@Override
	public void run() {		
		try {
			String user = this.chat.user;

			sporocila = Http.prejmi_sporocilo(user);
			izpisi_sporocila(sporocila);
			
			uporabniki = Http.uporabniki();
			this.chat.izpisi_uporabnike(uporabniki);
			
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
	 * V okno za sporocila doda podana sporocila.
	 * @param sporocila: sporocila, ki jih zelimo prikazati v oknu
	 */
	public void izpisi_sporocila(List<Sporocilo> sporocila) {
		for (Sporocilo sporocilo : sporocila) {
			
			String cas = Sporocilo.TimeFromDate(sporocilo.getSentAt());
			String posiljatelj_str = sporocilo.getSender();
			String tekst = sporocilo.getText();
			Boolean javno = sporocilo.getGlobal();
			
			if (javno.equals(true)) {
				chat.addMessage(cas, posiljatelj_str, tekst);
			} else {
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

