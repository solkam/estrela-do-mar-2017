package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.MegaEventType;
import com.cb.mundo.model.util.FormatUtil;

/**
 * Representa um mega evento sendo acampamento ou estrela do mar.
 * 
 * @author Solkam
 * @since 26 MAI 2014
 */
@Entity
@Table(name="mega_event")
@Audited
public class MegaEvent implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private MegaEventType type;

	@Column(length=90, nullable=false)
	private String name;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date beginDate;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date endDate;
	
	
	@OneToMany(mappedBy="megaEvent")
	private List<EventWeek> weeks;
	
	
	/**
	 * Link para carrinho de compra (usado hoje)
	 */
	@Column(length=500)
	private String linkToCart;
	
	private Boolean flagActive = true;
	
	private Boolean flagCurrent = false;
	
	private Boolean flagHostingOpen = false;
	
	@Column(length=12000)
	private String saleConditionTerm;
	@Column(length=12000)
	private String releaseOfLiabilityTerm;
	
	@Column(nullable=false)
	private String adminEmail;
	

//eventos para dependentes...	
	private BigDecimal babysitterWeekValue = new BigDecimal("890.00");
	private BigDecimal childWeekValue      = new BigDecimal("990.00");
	private BigDecimal teenagerWeekValue   = new BigDecimal("1090.00");
	private BigDecimal youngWeekValue      = new BigDecimal("1290.00");
	


//acessores...	
	private static final long serialVersionUID = 8226009858775124329L;
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getFlagHostingOpen() {
		return flagHostingOpen;
	}
	public void setFlagHostingOpen(Boolean flagHostingOpen) {
		this.flagHostingOpen = flagHostingOpen;
	}
	public List<EventWeek> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<EventWeek> weeks) {
		this.weeks = weeks;
	}
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	public String getName() {
		return name;
	}
	public String getSaleConditionTerm() {
		return saleConditionTerm;
	}
	public void setSaleConditionTerm(String saleConditionTerm) {
		this.saleConditionTerm = saleConditionTerm;
	}
        public String getReleaseOfLiabilityTerm() {
            return releaseOfLiabilityTerm;
        }
        public void setReleaseOfLiabilityTerm(String releaseOfLiabilityTerm) {
            this.releaseOfLiabilityTerm = releaseOfLiabilityTerm;
        }
	public void setName(String name) {
		this.name = name;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
//            GregorianCalendar temp = new GregorianCalendar();
//            temp.setTimeInMillis(beginDate.getTime());
//            temp.add(GregorianCalendar.DATE, 1);
//            this.beginDate = temp.getTime();
            this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
//            GregorianCalendar temp = new GregorianCalendar();
//            temp.setTimeInMillis(endDate.getTime());
//            temp.add(GregorianCalendar.DATE, 1);
//            this.endDate = temp.getTime();
            this.endDate = endDate;
	}
	public Boolean getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(Boolean flagActive) {
		this.flagActive = flagActive;
	}
	public MegaEventType getType() {
		return type;
	}
	public void setType(MegaEventType type) {
		this.type = type;
	}
	public String getLinkToCart() {
		return linkToCart;
	}
	public void setLinkToCart(String linkToCart) {
		this.linkToCart = linkToCart;
	}
	public Boolean getFlagCurrent() {
		return flagCurrent;
	}
	public void setFlagCurrent(Boolean flagCurrent) {
		this.flagCurrent = flagCurrent;
	}
	public BigDecimal getChildWeekValue() {
		return childWeekValue;
	}
	public void setChildWeekValue(BigDecimal childWeekValue) {
		this.childWeekValue = childWeekValue;
	}
	public BigDecimal getBabysitterWeekValue() {
		return babysitterWeekValue;
	}
	public void setBabysitterWeekValue(BigDecimal babysitterWeekValue) {
		this.babysitterWeekValue = babysitterWeekValue;
	}
	public BigDecimal getTeenagerWeekValue() {
		return teenagerWeekValue;
	}
	public void setTeenagerWeekValue(BigDecimal teenagerWeekValue) {
		this.teenagerWeekValue = teenagerWeekValue;
	}
	public BigDecimal getYoungWeekValue() {
		return youngWeekValue;
	}
	public void setYoungWeekValue(BigDecimal youngWeekValue) {
		this.youngWeekValue = youngWeekValue;
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
		MegaEvent other = (MegaEvent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MegaEvent [id=" + id + ", name=" + name + ", beginDate=" + beginDate + ", endDate=" + endDate + "]";
	}
	
	public boolean isTransient() {
		return getId()==null;
	}
	
	
	
	public String getDescDates() {
		final String PATTERN = "%s - %s";
		
		String beginDescDate = FormatUtil.toLongFormat( beginDate );
		String endDescDate   = FormatUtil.toLongFormat( endDate );
		return String.format(PATTERN, beginDescDate, endDescDate);
	}
	
	

}
