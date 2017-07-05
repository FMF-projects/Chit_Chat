import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sporocilo {
	
	private Boolean global;
	private String recipient;
	private String sender;
	private String text;
	private Date sent_at;
	
	private Sporocilo() {}
	
	// prejeta sporocila
	public Sporocilo(Boolean global, String recipient, 
			String sender, String text, Date sent_at) {
		this.global = global;
		this.recipient = recipient;
		this.sender = sender;
		this.text = text;
		this.sent_at = sent_at;
	}
	
	// poslana javna sporocila
	public Sporocilo(Boolean global, String text) {
		this.global = global;
		this.text = text;
	}
	
	// poslana zasebna sporocila
	public Sporocilo(Boolean global, String recipient, String text) {
		this.global = global;
		this.recipient = recipient;
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "Sporocilo [global=" + global + ", recipient=" + 
				recipient + ", sender=" + sender + ", text=" + 
				text + ", sent at=" + sent_at + "]";
	}
	
	public static String ListToString(List<Sporocilo> seznam) {
		String sporocila = "[ ";
		for (Sporocilo sporocilo : seznam) {
			sporocila += sporocilo.toString() + ", ";
		}
		sporocila += " ]";
		return sporocila;
	}
	
	
	@JsonProperty("global")
	public Boolean getGlobal() {
		return global;
	}
	
	public void setGlobal(Boolean vrednost) {
		this.global = vrednost;
	}
	
	@JsonProperty("recipient")
	public String getRecipient() {
		return recipient;
	}
	
	public void setRecipient(String prejemnik) {
		this.recipient = prejemnik;
	}
	
	@JsonProperty("sender")
	public String getSender() {
		return sender;
	}
	
	public void setSender(String posiljatelj) {
		this.sender = posiljatelj;
	}
	
	@JsonProperty("text")
	public String getText() {
		return text;
	}
	
	public void setText(String sporocilo) {
		this.text = sporocilo;
	}
	
	@JsonProperty("sent_at")
	public Date getSentAt() {
		return sent_at;
	}
	
	public void setSentAt(Date cas_sporocila) {
		this.sent_at = cas_sporocila;
	}
	
}
