package hr.fer.ztel.myFridge.data;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "food", schema = "my_fridge")
@XmlRootElement(name = "foodItem")
public class Food {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;

	@Column(name = "barcode", nullable = false, unique = true)
	private String barcode;

	@Column(name = "name")
	private String name;

	@Column(name = "manufacturer")
	private String manufacturer;

	@Lob
	@Column(name = "imageSmall")
	private byte[] imageSmall;

	@Lob
	@Column(name = "imageLarge")
	private byte[] imageLarge;

	@Column(name = "description", length = 800)
	private String description;

	@OneToMany(targetEntity = UserFood.class, mappedBy = "food", fetch = FetchType.EAGER, orphanRemoval = true, cascade = { CascadeType.REMOVE })
	private Collection<UserFood> userFoods;

	public Food() {
	}

	public Food(Integer id, String barcode, String name, String manufacturer, byte[] imageSmall,
			byte[] imageLarge, String description) {
		this.id = id;
		this.barcode = barcode;
		this.name = name;
		this.manufacturer = manufacturer;
		this.imageSmall = imageSmall;
		this.imageLarge = imageLarge;
		this.description = description;
	}

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@XmlElement
	public byte[] getImageSmall() {
		return imageSmall;
	}

	public void setImageSmall(byte[] imageSmall) {
		this.imageSmall = imageSmall;
	}

	@XmlElement
	public byte[] getImageLarge() {
		return imageLarge;
	}

	public void setImageLarge(byte[] imageLarge) {
		this.imageLarge = imageLarge;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlTransient
	public Collection<UserFood> getUserFoods() {
		return userFoods;
	}

	public void setUserFoods(Collection<UserFood> userFoods) {
		this.userFoods = userFoods;
	}

	@Override
	public String toString() {
		return "Food [id=" + id + ", barcode=" + barcode + ", name=" + name + ", manufacturer="
				+ manufacturer + ", description=" + description + "]";
	}

}
