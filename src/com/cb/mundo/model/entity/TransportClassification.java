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
import org.hibernate.envers.Audited;

/**
 *
 * @author Lancelot
 */
@Entity
@Audited
public class TransportClassification implements Serializable {
    
    private static final long serialVersionUID = 4496703902472236328L;
        
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(length=50)
    private String classification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getI18nKey() {
        return new StringBuilder("label_transport_class_").append(classification).toString().toLowerCase();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 17 * hash + (this.classification != null ? this.classification.hashCode() : 0);
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
        final TransportClassification other = (TransportClassification) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.classification == null) ? (other.classification != null) : !this.classification.equals(other.classification)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TransportClassification{" + "id=" + id + ", classification=" + classification + '}';
    }
    
    
}
