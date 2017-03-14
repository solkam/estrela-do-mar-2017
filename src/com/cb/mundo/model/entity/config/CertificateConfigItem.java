package com.cb.mundo.model.entity.config;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.enumeration.School;

/**
 * Represents a item on certificate with all details like
 * color, font, Y position, etc.
 * 
 * @author solkam
 * @since 02 out 2011
 */
@Entity
public class CertificateConfigItem implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@Enumerated(EnumType.STRING)
	@Column(length=3, nullable=false)
	private School school = School.LCB;

	
	@ManyToOne
	private Module module;
	
	
	private int numberOfFacilitator = 1;
	
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable=false)
	private CertificateItemAttribute attribute;
	
	private Integer displayOrder;


	@Enumerated(EnumType.STRING)
	@Column(length=10, nullable=false)
	private CertificateItemAlign align = CertificateItemAlign.CENTER;
	
	
	private int leftMargin;
	private int rightMargin;
	private int yposition;
	
	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	private CertificateItemColor color = CertificateItemColor.BLACK;

	
	@Column(length=200)
	private String text = "";

	
	@Column(length=20)
	private String fontName = CertificateConfigConstant.FONT_FAMILY_CALIBRI;

	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	private CertificateItemFontStyle fontStyle = CertificateItemFontStyle.BOLD;
	
	private int fontSize;


	//listener
	@PrePersist @PreUpdate 
	public void assignDisplayOrder() {
		this.displayOrder = attribute.getDisplayOrder();
	}
	
	
    //acessores...	
	private static final long serialVersionUID = -5026229002257689190L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	public int getNumberOfFacilitator() {
		return numberOfFacilitator;
	}
	public void setNumberOfFacilitator(int numberOfFacilitator) {
		this.numberOfFacilitator = numberOfFacilitator;
	}
	public CertificateItemAttribute getAttribute() {
		return attribute;
	}
	public void setAttribute(CertificateItemAttribute attribute) {
		this.attribute = attribute;
	}
	public int getLeftMargin() {
		return leftMargin;
	}
	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}
	public int getRightMargin() {
		return rightMargin;
	}
	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}
	public int getYposition() {
		return yposition;
	}
	public void setYposition(int yposition) {
		this.yposition = yposition;
	}
	public CertificateItemColor getColor() {
		return color;
	}
	public void setColor(CertificateItemColor color) {
		this.color = color;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public CertificateItemFontStyle getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(CertificateItemFontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}
	public CertificateItemAlign getAlign() {
		return align;
	}
	public void setAlign(CertificateItemAlign align) {
		this.align = align;
	}
	public Font getAwtFont() {
		return new Font(fontName, fontStyle.getCode(), fontSize);
	}
	public Color getAwtColor() {
		return this.color.getAwtColor();
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CertificateConfigItem other = (CertificateConfigItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CertificateConfigItem [id=" + id + ", school=" + school
				+ ", module=" + module + ", numberOfFacilitator="
				+ numberOfFacilitator + ", attribute=" + attribute + ", align="
				+ align + ", leftMargin=" + leftMargin + ", rightMargin="
				+ rightMargin + ", yposition=" + yposition + ", color=" + color
				+ ", text=" + text + ", fontName=" + fontName + ", fontStyle="
				+ fontStyle + ", fontSize=" + fontSize + "]";
	}

/*
insert into CertificateConfigItem (school, attribute, color, fontName, fontSize, fontStyle, leftMargin,Margin, module, numberOfFacilitator, rightMargin, text, yposition, displayOrder)
select                            'KF'   , attribute, color, fontName, fontSize, fontStyle, leftMargin,Margin, module, numberOfFacilitator, rightMargin, text, yposition, displayOrder
from CertificateConfigItem where school='LCB';



 */
	
	
}
