import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public class ChitChat {

	public static ChatFrame chatFrame = new ChatFrame();
	public static MessageRobot robot_sporocila = new MessageRobot(chatFrame);
	public static UserRobot robot_uporabniki = new UserRobot(chatFrame);
	
	public static void main(String[] args) {
		chatFrame.pack();
		chatFrame.setVisible(true);
	}
}
