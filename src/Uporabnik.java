import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Uporabnik {
	private String username;
	private Date lastActive;
	
	private Uporabnik() {}
	
	public Uporabnik(String username, Date lastActive) {
		this.username = username;
		this.lastActive = lastActive;
	}

	@Override
	public String toString() {
		return "Uporabnik [username=" + username + ", lastActive=" + lastActive + "]";
	}
	
	public static String ListToString(List<Uporabnik> seznam) {
		String uporabniki = "[ ";
		for (Uporabnik uporabnik : seznam) {
			uporabniki += uporabnik.toString() + ", ";
		}
		uporabniki += " ]";
		return uporabniki;
	}

	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty("last_active")
	public Date getLastActive() {
		return this.lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}
	
}


