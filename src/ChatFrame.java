import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
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
	
	public static JTextField user_field; // polje za vpis vzdevka
	public static JPanel prijavljeni_uporabniki_plosca;
	
	// belezimo s katerimi uporabniki imamo odprt zasebni pogovor
	public static List<String> zasebni_pogovori = new ArrayList();
	
	public static String user = System.getProperty("user.name");
	public static Boolean prijavljen = false; // stanje nase prijave
	

	public ChatFrame() {
		super();
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		this.setTitle("ChitChat"); //naslov
		this.setMinimumSize(new Dimension(600,500));
		
	
		// zapiranje okna
		// v primeru, da se nismo rocno odjavili, se to zgodi ko zapremo okno
		// prav tako se prekine delovanje robota
		this.addWindowListener(new WindowAdapter() 
			{
			@Override
			public void windowClosing(WindowEvent e) {
				if (prijavljen == true) {
					ChitChat.robot_sporocila.deactivate();

					try {
						Http.odjava(user);
						
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
		});
		
		
		//vrstica za dolocanje vzdevka
		JPanel nickname = new JPanel();
		GridBagConstraints nicknameConstraint = new GridBagConstraints();
		nicknameConstraint.gridx = 0;
		nicknameConstraint.gridy = 0;
		nicknameConstraint.weightx = 1.0;
		nicknameConstraint.weighty = 0.0;
		nicknameConstraint.gridwidth = 2; // razteza se cez 2 stolpca 
		nicknameConstraint.fill = GridBagConstraints.BOTH; 
		pane.add(nickname, nicknameConstraint);
		nickname.addKeyListener(this);
		nickname.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//besedilo "vzdevek"
		JLabel vzdevek = new JLabel("Vzdevek:");
		nickname.add(vzdevek);
		
		// polje za vpis vzdevka
		user_field = new JTextField(user, 20);
		nickname.add(user_field);
		user_field.setEnabled(true);
		
		//gumb za prijavo
		prijava_gumb = new JButton("Prijava");
		nickname.add(prijava_gumb);
		prijava_gumb.addActionListener(this);
		
		//gumb za odjavo
		odjava_gumb = new JButton("Odjava");
		nickname.add(odjava_gumb);
		odjava_gumb.addActionListener(this);
		
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
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.weightx = 1.0; 
		inputConstraint.weighty = 0.0; //visina inputa se ne razteguje
		inputConstraint.fill = GridBagConstraints.BOTH; //zapolnitev prostora
		inputConstraint.gridwidth = 2;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);
		input.setEnabled(false); // urejanje bomo omogocili, ko se bo uporabnik prijavil
		
		// polje za aktivne uporabnike
		this.prijavljeni_uporabniki_plosca = new JPanel();
		this.prijavljeni_uporabniki_plosca.setMinimumSize(new Dimension(150,400));
		this.prijavljeni_uporabniki_plosca.setLayout(
				new BoxLayout(prijavljeni_uporabniki_plosca, BoxLayout.Y_AXIS)); // vertikalno dodajanje elementov
		JScrollPane scrollbar_uporabniki = new JScrollPane(prijavljeni_uporabniki_plosca); //drsnik
		GridBagConstraints uporabnikiConstraint = new GridBagConstraints();
		uporabnikiConstraint.gridx = 1;
		uporabnikiConstraint.gridy = 1;
		uporabnikiConstraint.weightx = 1.0;
		uporabnikiConstraint.weighty = 1.0;
		uporabnikiConstraint.fill = GridBagConstraints.BOTH;
		pane.add(scrollbar_uporabniki, uporabnikiConstraint);
		
	}

	/**
	 * Dodamo sporocilo v okno za izpis sporocil.
	 * @param time: cas sporocila
	 * @param person: posiljatelj sporocila
	 * @param message: sporocilo
	 */
	public void addMessage(String time, String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + "[" + time + "] " +  person + ": " + message + "\n");
	}
	
	/**
	 * Na plosco "prijavljeni_uporabniki_plosca" izpise prijavljene
	 * uporabnike v obliki gumbov, ki ob kliku odprejo okno 
	 * za zasebni pogovor z uporabnikom
	 * @param uporabniki: seznam trenutno prijavljenih uporabnikov
	 */
	public static void izpisi_uporabnike(List<Uporabnik> uporabniki) {
		prijavljeni_uporabniki_plosca.removeAll();
		for (Uporabnik uporabnik : uporabniki) {
			// za vsakega prijavljenega uporabnika ustvarimo gumb, 
			// ki nam bo ob kliku odprl novo okno za zasebni pogovor
			JButton uporabnik_gumb = new JButton(uporabnik.getUsername());
			prijavljeni_uporabniki_plosca.add(uporabnik_gumb);
			uporabnik_gumb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					zasebno_okno(uporabnik);
				}
			});
			uporabnik_gumb.setAlignmentX(Component.CENTER_ALIGNMENT);
			uporabnik_gumb.setBackground(Color.white);
			prijavljeni_uporabniki_plosca.revalidate();
			}
		}
	
	/**
	 * Preveri, ce okno za zasebni pogovor z danim uporabnikom
	 * ze obstaja, sicer ga ustvari
	 * @param prejemnik: uporabnik s katerim si zelimo zasebnega pogovora
	 */
	public static void zasebno_okno(Uporabnik prejemnik) {
		if (zasebni_pogovori.contains(prejemnik.getUsername())) {
			
		} else {
			prejemnik.CreateChat();
			zasebni_pogovori.add(prejemnik.getUsername());
		}		
	}
	
	// prijava in odjava
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.prijava_gumb) {
					try {
						user = user_field.getText(); // preberemo vzdevek, ki ga je uporabnik vpisal
						Http.prijava(user);
						
						prijavljen = true; // spremenimo stanje prijave
						
						// aktiviramo robota za sporocila in uporabnike						
						ChitChat.robot_sporocila.activate();
						ChitChat.robot_sporocila.run();
						
						// omogocimo urejanje vrstice za vnos sporocila
						input.setEnabled(true);
						
						// onemogocimo urejanje vrstice z vzdevkom, saj 
						// smo trenutno z njim prijavljeni v chat
						user_field.setEnabled(false);
						
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
		if (e.getSource() == this.odjava_gumb) {
				try {
					// deaktiviramo robota in ustvarimo novega, saj bi 
					// morali sicer pred ponovno prijavo na novo zagnati program
					ChitChat.robot_sporocila.deactivate();
					ChitChat.robot_sporocila = new MessageRobot(ChitChat.chatFrame);;
					
					Http.odjava(user);
					
					prijavljen = false;
					
					user_field.setEnabled(true);
					input.setEnabled(false);
					
					// izbrisemo sporocila in prijavljene uporabnike
					this.output.setText("");  
					prijavljeni_uporabniki_plosca.removeAll();
					prijavljeni_uporabniki_plosca.revalidate();
					prijavljeni_uporabniki_plosca.repaint();
					
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

	
	// posiljanje sporocila
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				try {
					Http.poslji_sporocilo(user, false, this.input.getText(), null);
					this.addMessage(CurrentTime(), user, this.input.getText());
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

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static String CurrentTime() {
		Date cas = new Date();
		SimpleDateFormat date_format = new SimpleDateFormat("HH:mm");
		String time = date_format.format(cas);
		return time;
	}
	

}
