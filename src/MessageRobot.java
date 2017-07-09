import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

public class MessageRobot extends TimerTask {
	private static ChatFrame chat;
	private Timer timer;
	
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
		List<Sporocilo> sporocila;
		List<Uporabnik> prijavljeni_uporabniki;
		
		try {
			String user = ChatFrame.user;

			sporocila = Http.prejmi_sporocilo(user);
			izpisi_sporocila(sporocila);
			
			prijavljeni_uporabniki = Http.uporabniki();
			chat.izpisi_uporabnike(prijavljeni_uporabniki);
			
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
	public static void izpisi_sporocila(List<Sporocilo> sporocila) {
		for (Sporocilo sporocilo : sporocila) {
			String cas = Sporocilo.TimeFromDate(sporocilo.getSentAt());
			String posiljatelj = sporocilo.getSender();
			String tekst = sporocilo.getText();
			Boolean zasebno = sporocilo.getGlobal();
			
			if (zasebno == false) {
				chat.addMessage(cas, posiljatelj, tekst);
			} else {
				
			}
		}
	}
	
}
