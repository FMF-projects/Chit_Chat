public class ChitChat {

	public static ChatFrame chatFrame = new ChatFrame();
	public static MessageRobot robot = new MessageRobot(chatFrame);
	
	public static void main(String[] args) {
		chatFrame.pack();
		chatFrame.setVisible(true);
	}
}
