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
		
		try {
			String user = ChatFrame.user;

			sporocila = Http.prejmi_sporocilo(user);
			izpisi_sporocila(sporocila);
			
		} catch (ClientProtocolException e) {
			System.out.println("napaka1");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			System.out.println("napaka2");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("napaka3");
			e.printStackTrace();
		}
	}
	
	public static void izpisi_sporocila(List<Sporocilo> sporocila) {
		for (Sporocilo sporocilo : sporocila) {
			String cas = Sporocilo.TimeFromDate(sporocilo.getSentAt());
			String posiljatelj = sporocilo.getSender();
			String tekst = sporocilo.getText();
			chat.addMessage(cas, posiljatelj, tekst);
		}
	}
	
}
