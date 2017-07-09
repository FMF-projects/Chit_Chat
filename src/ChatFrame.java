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
	
	public static JTextField user_field;
	public static JPanel prijavljeni_uporabniki_plosca;
	
	private static List<String> zasebni_pogovori = new ArrayList();
	
	public static String user = System.getProperty("user.name");
	public static Boolean prijavljen = false;
	

	public ChatFrame() {
		super();
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		this.setTitle("ChitChat"); //naslov
		this.setMinimumSize(new Dimension(600,500));
		
	
		// zapiranje okna
		this.addWindowListener(new WindowAdapter() 
			{
			@Override
			public void windowClosing(WindowEvent e) {
				if (prijavljen == true) {
					ChitChat.robot_sporocila.deactivate();
					ChitChat.robot_uporabniki.deactivate();

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
		nicknameConstraint.gridwidth = 2;
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
		
		input.setEnabled(false);
		
		// polje za aktivne uporabnike
		this.prijavljeni_uporabniki_plosca = new JPanel();
		this.prijavljeni_uporabniki_plosca.setMinimumSize(new Dimension(150,400));
		this.prijavljeni_uporabniki_plosca.setLayout(
				new BoxLayout(prijavljeni_uporabniki_plosca, BoxLayout.Y_AXIS));
		JScrollPane scrollbar_uporabniki = new JScrollPane(prijavljeni_uporabniki_plosca); //drsnik
		GridBagConstraints uporabnikiConstraint = new GridBagConstraints();
		uporabnikiConstraint.gridx = 1;
		uporabnikiConstraint.gridy = 1;
		uporabnikiConstraint.weightx = 1.0;
		uporabnikiConstraint.weighty = 1.0;
		uporabnikiConstraint.fill = GridBagConstraints.BOTH;
		pane.add(scrollbar_uporabniki, uporabnikiConstraint);
		
	}

	// dodamo sporocilo v okno s sporocilo
	public void addMessage(String time, String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + "[" + time + "] " +  person + ": " + message + "\n");
	}
	
	// izpisemo prijavljene uporabnike
	public static void izpisi_uporabnike(List<Uporabnik> uporabniki) {
		prijavljeni_uporabniki_plosca.removeAll();
		for (Uporabnik uporabnik : uporabniki) {
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
	
	public static void zasebno_okno(Uporabnik prejemnik) {
		if (zasebni_pogovori.contains(prejemnik.getUsername())) {
			
		} else {
			prejemnik.CreateChat();
		}		
	}
	
	// prijava in odjava
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.prijava_gumb) {
					try {
						user = user_field.getText();
						Http.prijava(user);
						
						prijavljen = true;
						
						ChitChat.robot_sporocila.activate();
						ChitChat.robot_sporocila.run();
						
						ChitChat.robot_uporabniki.activate();
						ChitChat.robot_uporabniki.run();
						
						input.setEnabled(true);
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
					ChitChat.robot_sporocila.deactivate();
					ChitChat.robot_sporocila = new MessageRobot(ChitChat.chatFrame);
					
					ChitChat.robot_uporabniki.deactivate();
					ChitChat.robot_uporabniki = new UserRobot(ChitChat.chatFrame);
					
					Http.odjava(user);
					
					prijavljen = false;
					
					user_field.setEnabled(true);
					input.setEnabled(false);
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
					Http.poslji_sporocilo(user, this.input.getText());
					this.addMessage(CurrentTime(), user, this.input.getText());
					this.input.setText("");
					
					
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
