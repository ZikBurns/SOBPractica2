
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"id","username","sex","age","smoker","haspets","room"})
@Entity
public class Renter implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String username;
    private TypeSex sex;
    private int age;
    private boolean smoker;
    private boolean haspets;

    @OneToOne 
    @JoinColumn(name="ROOM_ID") 
    private Room room;
    
    
    public Renter() {
    }

    public Renter(int id, String username, TypeSex sex, int age, boolean smoker, boolean haspets) {
        this.id = id;
        this.username = username;
        this.sex = sex;
        this.age = age;
        this.smoker = smoker;
        this.haspets = haspets;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeSex getSex() {
        return sex;
    }

    public void setSex(TypeSex sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public boolean isHaspets() {
        return haspets;
    }

    public void setHaspets(boolean haspets) {
        this.haspets = haspets;
    }

    @Override
    public String toString() {
        if(room==null) return "Renter{" + "id=" + id + ", username=" + username + ", sex=" + sex + ", age=" + age + ", smoker=" + smoker + ", haspets=" + haspets + '}';
        else return "Renter{" + "id=" + id + ", username=" + username + ", sex=" + sex + ", age=" + age + ", smoker=" + smoker + ", haspets=" + haspets + ", room=" + room + '}';
    }

   
    
    
}
