import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

public class MessageRobot extends TimerTask {
	private ChatFrame chat;
	private Timer timer;
	
	
	public MessageRobot(ChatFrame chat) {
		this.chat = chat;
	}
	

	/**
	 * Activate the robot!
	 */
	public void activate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}
	
	public void deactivate() {
		timer.cancel();
	}
	
	@Override
	public void run() {
		System.out.println("zahtevam sporocila");
		List<Sporocilo> sporocila;
		
		try {
			String uporabnik = ChatFrame.user.getText();
			sporocila = Http.prejmi_sporocilo(uporabnik);
			System.out.println("sporocila:");
			System.out.println(Sporocilo.ListToString(sporocila));
			
			for (Sporocilo sporocilo : sporocila) {
				System.out.println(sporocilo.toString());
				chat.addMessage(sporocilo.getSender(), sporocilo.getText());	
			}			
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
}
