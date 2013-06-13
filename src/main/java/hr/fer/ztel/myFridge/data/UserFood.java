package hr.fer.ztel.myFridge.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "user_food", schema = "my_fridge")
@XmlRootElement(name = "userFood")
public class UserFood {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@Column(name = "date_expiry", nullable = false)
	private Date dateExpiry;

	@Column(name = "date_opened")
	private Date dateOpened;

	@Column(name = "valid_after_opening")
	private Integer validAfterOpening;

	@Column(name = "is_notified", nullable = false)
	private boolean isNotified;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "food_id")
	private Food food;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	public UserFood() {
	}

	public UserFood(Integer id, Date dateAdded, Date dateExpiry, Date dateOpened, Integer validAfterOpening,
			boolean isNotified, Food food, User user) {

		this.id = id;
		this.dateExpiry = dateExpiry;
		this.dateOpened = dateOpened;
		this.validAfterOpening = validAfterOpening;
		this.isNotified = isNotified;
		this.food = food;
		this.user = user;
	}

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public Date getDateExpiry() {
		return dateExpiry;
	}

	public void setDateExpiry(Date dateExpiry) {
		this.dateExpiry = dateExpiry;
	}

	@XmlElement
	public Date getDateOpened() {
		return dateOpened;
	}

	public void setDateOpened(Date dateOpened) {
		this.dateOpened = dateOpened;
	}

	public Integer getValidAfterOpening() {
		return validAfterOpening;
	}

	public void setValidAfterOpening(Integer validAfterOpening) {
		this.validAfterOpening = validAfterOpening;
	}

	@XmlTransient
	public boolean isNotified() {
		return isNotified;
	}

	public void setNotified(boolean isNotified) {
		this.isNotified = isNotified;
	}

	@XmlElement
	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	@XmlTransient
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
