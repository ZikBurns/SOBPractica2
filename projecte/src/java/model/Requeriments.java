
package model;
import javax.persistence.*;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"maxage","minage","ownermail","ownerphone","pets","sex","smokers"})
@Embeddable @Access(AccessType.FIELD)
public class Requeriments implements Serializable{
    private TypeSex sex;
    private String ownermail;
    private int ownerphone;
    private int maxage;
    private int minage;
    private boolean smokers;
    private boolean pets;

    public Requeriments(String ownermail, int ownerphone, TypeSex sex, int maxage, int minage, boolean smokers, boolean pets) {
        this.ownermail = ownermail;
        this.ownerphone = ownerphone;
        this.sex = sex;
        this.maxage = maxage;
        this.minage = minage;
        this.smokers = smokers;
        this.pets = pets;
    }

    public String getOwnermail() {
        return ownermail;
    }

    public void setOwnermail(String ownermail) {
        this.ownermail = ownermail;
    }

    public int getOwnerphone() {
        return ownerphone;
    }

    public void setOwnerphone(int ownerphone) {
        this.ownerphone = ownerphone;
    }

    

    public Requeriments() {
    }
    
    
    
    public TypeSex getSex() {
        return sex;
    }

    public void setSex(TypeSex sex) {
        this.sex = sex;
    }

    public int getMaxage() {
        return maxage;
    }

    public void setMaxage(int maxage) {
        this.maxage = maxage;
    }

    public int getMinage() {
        return minage;
    }

    public void setMinage(int minage) {
        this.minage = minage;
    }

    public boolean isSmokers() {
        return smokers;
    }

    public void setSmokers(boolean smokers) {
        this.smokers = smokers;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    @Override
    public String toString() {
        return "Requeriments{" + "ownermail=" + ownermail + ", ownerphone=" + ownerphone + ", sex=" + sex + ", maxage=" + maxage + ", minage=" + minage + ", smokers=" + smokers + ", pets=" + pets + '}';
    }

    
    
    
    
}
