package hr.fer.ztel.myFridge.data;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "user")
@Entity
@Table(name = "user", schema = "my_fridge")
public class User {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email", unique = true, nullable = false)
	private String eMail;

	@Column(name = "notifications")
	private boolean notifications = true;

	@OneToMany(targetEntity = UserFood.class, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true, cascade = { CascadeType.REMOVE })
	private Collection<UserFood> userFoods;

	public User() {
	}

	public User(Integer id, String username, String password, String eMail, boolean notifications,
			Collection<UserFood> userFoods) {

		this.id = id;
		this.username = username;
		this.password = password;
		this.eMail = eMail;
		this.userFoods = userFoods;
		this.notifications = notifications;
	}

	@XmlTransient
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	@XmlElement
	public boolean isNotifications() {
		return notifications;
	}

	public void setNotifications(boolean notifications) {
		this.notifications = notifications;
	}

	@XmlTransient
	public Collection<UserFood> getUserFoods() {
		return userFoods;
	}

	public void setUserFoods(Collection<UserFood> userFoods) {
		this.userFoods = userFoods;
	}
}
