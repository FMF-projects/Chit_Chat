import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

public class UserRobot extends TimerTask {
	private static ChatFrame chat;
	private Timer timer;
	
	public static Boolean active = false;
	
	public UserRobot(ChatFrame chat) {
		this.chat = chat;
	}

	public void activate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 5000);
		active = true;
	}
	
	public void deactivate() {
		timer.cancel();
		active = false;
	}

	@Override
	public void run() {
		List<Uporabnik> uporabniki;
		
		try {
			uporabniki = Http.uporabniki();
			izpisi_uporabnike(uporabniki);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void izpisi_uporabnike(List<Uporabnik> uporabniki) {
		chat.uporabniki_polje.setText("");
		for (Uporabnik uporabnik : uporabniki) {
			chat.addUser(uporabnik.getUsername());
		}
	}

}