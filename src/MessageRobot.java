import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

public class MessageRobot extends TimerTask {
	private ChatFrame chat;
	private Timer timer;
	private String uporabnik = ChatFrame.user.getText();
	
	
	public MessageRobot(ChatFrame chat) {
		this.chat = chat;
	}
	

	/**
	 * Activate the robot!
	 */
	public void activate() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}
	
	public void deactivate() {
		timer.cancel();
	}
	
	@Override
	public void run() {
		try {
			List<Sporocilo> sporocila = Http.prejmi_sporocilo(uporabnik);
			
			for (Sporocilo sporocilo : sporocila) {
				chat.addMessage(sporocilo.getSender(), sporocilo.getText());	
			}
			
			
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
}
