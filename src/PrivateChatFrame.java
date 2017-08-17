import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.client.ClientProtocolException;

@SuppressWarnings("serial")
public class PrivateChatFrame extends JFrame implements ActionListener, KeyListener {

	private JTextArea output;
	private JTextField input;
	
	private String user = ChitChat.chatFrame.user;
	private String prejemnik;

	public PrivateChatFrame(String prejemnik) {
		super();
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		this.prejemnik = prejemnik;
		
		this.setTitle("PrivateChitChat : " + prejemnik); //naslov
		this.setMinimumSize(new Dimension(400,300));
		
		// polje za prikaz pogovora
		this.output = new JTextArea(20, 35);
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
		this.input.setText("");
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.weightx = 1.0; 
		inputConstraint.weighty = 0.0; //visina inputa se ne razteguje
		inputConstraint.fill = GridBagConstraints.BOTH; //zapolnitev prostora
		pane.add(input, inputConstraint);
		input.addKeyListener(this);
		
		
		// zapiranje okna
				this.addWindowListener(new WindowAdapter() 
					{
					@Override
					public void windowClosing(WindowEvent e) {
						// odstranimo uporabnika s seznama zasebnih pogovorov
						ChitChat.chatFrame.zasebni_pogovori.remove(prejemnik);
					}
				});
	}
	
	/**
	 * Dodamo sporocolo v okno za izpis sporocil.
	 * @param time: cas sporocila
	 * @param person: posiljatelj sporocila
	 * @param message: sporocilo
	 */
	public void addMessage(String time, String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + "[" + time + "] " +  person + ": " + message + "\n");
	}
	
	// posljemo zasebno sporocilo
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				if (this.input.getText().equals("")) {
					
				} else {
					try {
						Http.poslji_sporocilo(user, false, prejemnik, this.input.getText());
						this.addMessage(ChitChat.chatFrame.currentTime(), user, this.input.getText());
						this.input.setText(""); // ponastavimo vnosno vrstico	
		
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
				}				
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub	
	}
	
}
