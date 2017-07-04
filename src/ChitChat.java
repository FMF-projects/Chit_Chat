public class ChitChat {

	public static ChatFrame chatFrame = new ChatFrame();
	static MessageRobot robot = new MessageRobot(chatFrame);
	
	public static void main(String[] args) {
		chatFrame.pack();
		chatFrame.setVisible(true);
	}

}
