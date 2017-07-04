import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.client.ClientProtocolException;

@SuppressWarnings("serial")
public class ChatFrame extends JFrame implements ActionListener, KeyListener {
	
	private static final Icon String = null;
	private JTextArea output;
	private JTextField input;
	
	private JButton prijava_gumb;
	private JButton odjava_gumb;
	public static JTextField user;

	public ChatFrame() {
		super();
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		this.setTitle("ChitChat"); //naslov
		
		//vrstica za dolocanje vzdevka
		JPanel nickname = new JPanel();
		GridBagConstraints nicknameConstraint = new GridBagConstraints();
		nicknameConstraint.gridx = 0;
		nicknameConstraint.gridy = 0;
		nicknameConstraint.weightx = 1.0;
		nicknameConstraint.weighty = 0.0;
		nicknameConstraint.fill = GridBagConstraints.BOTH;
		pane.add(nickname, nicknameConstraint);
		nickname.addKeyListener(this);
		
		nickname.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//besedilo "vzdevek"
		JLabel vzdevek = new JLabel("Vzdevek:");
		nickname.add(vzdevek);
		
		// polje za vpis vzdevka
		user = new JTextField(System.getProperty("user.name"), 20);
		nickname.add(user);
		user.setEnabled(true);
		
		//gumb za prijavo
		prijava_gumb = new JButton("Prijava");
		nickname.add(prijava_gumb);
		prijava_gumb.addActionListener(this);
		
		//gumb za odjavo
		odjava_gumb = new JButton("Odjava");
		nickname.add(odjava_gumb);
		odjava_gumb.addActionListener(this);
		
		// polje za prikaz pogovara
		this.output = new JTextArea(20, 40);
		this.output.setEditable(false);
		JScrollPane scrollbar = new JScrollPane(output); //drsnik
		GridBagConstraints outputConstraint = new GridBagConstraints();
		outputConstraint.gridx = 0;
		outputConstraint.gridy = 1;
		outputConstraint.weightx = outputConstraint.weighty = 1.0;
		outputConstraint.fill = GridBagConstraints.BOTH;
		pane.add(scrollbar, outputConstraint);
		
		// polje za vnos besedila
		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.weightx = 1.0; 
		inputConstraint.weighty = 0.0; //visina inputa se ne razteguje
		inputConstraint.fill = GridBagConstraints.BOTH; //zapolnitev prostora
		pane.add(input, inputConstraint);
		input.addKeyListener(this);
		
		input.setEnabled(false);
		
	}

	/**
	 * @param person - the person sending the message
	 * @param message - the message content
	 */
	public void addMessage(String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.prijava_gumb) {
				try {
					Http.prijava(user.getText());
					input.setEnabled(true);
					user.setEnabled(false);
				} catch (Exception e1) {
					System.out.println("Vpiši vzdevek.");
				}
		}
		if (e.getSource() == this.odjava_gumb) {
			try {
				input.setEnabled(false);
				Http.odjava(user.getText());
				user.setEnabled(true);
			} catch (Exception e1) {
				System.out.println("Vpiši vzdevek.");
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				try {
					Http.poslji_sporocilo(user.getText(), this.input.getText());
					System.out.println("uspesno");
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.addMessage(user.getText(), this.input.getText());
				this.input.setText("");
			}
		}		
	}


	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}


//za focus
// dodamo na konec konstruktorja v chatframu
// addWindowListener(new WindowAdapter(){
//		public void windowOpened(WindowEvent e){
//           input.requestFocuslnWindow();
//      }
// });

// public void windowOpened(WindowEvent e){
//  }
