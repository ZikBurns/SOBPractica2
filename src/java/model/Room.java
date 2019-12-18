
package model;
import java.io.Serializable;
import javax.persistence.Embedded;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"id","address","city","description","dimension","furniture","location","price","req"})
@Entity
public class Room implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String description;
    private String address;
    private String city;
    private TypeDimension dimension;
    private TypeLocation location;
    private boolean furniture;
    private double price;
    
    @Embedded 
    private Requeriments req;

    @OneToOne(mappedBy="room", cascade = CascadeType.PERSIST)
    private Renter renter;
    
    public Room(int id, String description, String address, String city, TypeDimension dimension, TypeLocation location, boolean furniture, double price, Requeriments req) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.city = city;
        this.dimension = dimension;
        this.location = location;
        this.furniture = furniture;
        this.price = price;
        this.req = req;
    }

    public Room() {
    }

    public Renter getRenter() {
        return renter;
    }
    
    @XmlTransient
    public void setRenter(Renter renter) {
        this.renter = renter;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    

    public Requeriments getReq() {
        return req;
    }

    public void setReq(Requeriments req) {
        this.req = req;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public TypeDimension getDimension() {
        return dimension;
    }

    public void setDimension(TypeDimension dimension) {
        this.dimension = dimension;
    }

    public TypeLocation getLocation() {
        return location;
    }

    public void setLocation(TypeLocation location) {
        this.location = location;
    }

    public boolean isFurniture() {
        return furniture;
    }

    public void setFurniture(boolean furniture) {
        this.furniture = furniture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", description=" + description + ", address=" + address + ", city=" + city + ", dimension=" + dimension + ", location=" + location + ", furniture=" + furniture + ", price=" + price + ", req=" + req + '}';
    }
    
    
    
    
}
