import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JList;

import org.apache.http.client.ClientProtocolException;

public class UserRobot extends TimerTask {
	private static ChatFrame chat;
	private Timer timer;
	
	public UserRobot(ChatFrame chat) {
		this.chat = chat;
	}

	public void activate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 5000);
	}
	
	public void deactivate() {
		timer.cancel();
	}

	@Override
	public void run() {	
		List<Uporabnik> prijavljeni_uporabniki;
		
		try {
			prijavljeni_uporabniki = Http.uporabniki();
			chat.izpisi_uporabnike(prijavljeni_uporabniki);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
