/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.envers.Audited;

/**
 *
 * @author Lancelot
 */
@Entity
@Table(name = "company")
@Audited
public class Company implements Serializable{
    private static final long serialVersionUID = 2804508408992934889L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=90)
    private String name;

    @Column(nullable = false, length=90, unique = true)
    private String identitySerie;

    @Column(length=90)
    private String address;

    @Column(length=90)
    private String city;

    @Column(nullable=false, length=50)
    private String country;

    @Column(length=90)
    private String zipPostal;

    //digital contacts
    @Column(nullable=false, length=90)
    private String email;

    @Column(nullable = false)
    private Long telephone;

    @ManyToOne
    private Contact createdByContact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentitySerie() {
        return identitySerie;
    }

    public void setIdentitySerie(String identitySerie) {
        this.identitySerie = identitySerie.toUpperCase();
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipPostal() {
        return zipPostal;
    }

    public void setZipPostal(String zipPostal) {
        this.zipPostal = zipPostal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public Contact getCreatedByContact() {
        return createdByContact;
    }

    public void setCreatedByContact(Contact createdByContact) {
        this.createdByContact = createdByContact;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 89 * hash + (this.identitySerie != null ? this.identitySerie.hashCode() : 0);
        hash = 89 * hash + (this.address != null ? this.address.hashCode() : 0);
        hash = 89 * hash + (this.city != null ? this.city.hashCode() : 0);
        hash = 89 * hash + (this.country != null ? this.country.hashCode() : 0);
        hash = 89 * hash + (this.zipPostal != null ? this.zipPostal.hashCode() : 0);
        hash = 89 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 89 * hash + (this.telephone != null ? this.telephone.hashCode() : 0);
        hash = 89 * hash + (this.createdByContact != null ? this.createdByContact.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Company other = (Company) obj;
        if ((this.id == null || !this.id.equals(other.id)) && this.id != other.id) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.identitySerie == null) ? (other.identitySerie != null) : !this.identitySerie.equals(other.identitySerie)) {
            return false;
        }
        if ((this.address == null) ? (other.address != null) : !this.address.equals(other.address)) {
            return false;
        }
        if ((this.city == null) ? (other.city != null) : !this.city.equals(other.city)) {
            return false;
        }
        if ((this.country == null) ? (other.country != null) : !this.country.equals(other.country)) {
            return false;
        }
        if ((this.zipPostal == null) ? (other.zipPostal != null) : !this.zipPostal.equals(other.zipPostal)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if ((this.telephone == null) ? (other.telephone != null) : !this.telephone.equals(other.telephone)) {
            return false;
        }
        if (this.createdByContact != other.createdByContact && (this.createdByContact == null || !this.createdByContact.equals(other.createdByContact))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Company{" + "id=" + id + ", name=" + name + ", identitySerie=" + identitySerie + ", address=" + address + ", city=" + city + ", country=" + country + ", zipPostal=" + zipPostal + ", email=" + email + ", telephone=" + telephone + ", createdByContact=" + createdByContact + '}';
    }
    
    
}
