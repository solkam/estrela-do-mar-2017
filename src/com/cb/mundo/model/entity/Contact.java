package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.cb.mundo.model.entity.enumeration.CBRole;
import com.cb.mundo.model.entity.enumeration.ContactMaturity;
import com.cb.mundo.model.entity.enumeration.DocumentType;
import com.cb.mundo.model.entity.enumeration.Gender;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.entity.enumeration.TrainnerLevel;
import com.cb.mundo.model.entity.enumeration.TrainnerType;
import com.cb.mundo.model.entity.enumeration.TshirtSize;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * Represents all person contact
 * 
 * @author Solkam
 * @since 07 out 2011
 */
@Entity
@Table(name = "contact")
@Audited
public class Contact implements Serializable, Comparable<Contact> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 90)
	private String name;

	@Column(nullable = false, length = 90)
	private String civilName;

	@Column(length = 60)
	private String identityDocument;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private DocumentType identityDocumentType = DocumentType.CPF;

	/**
	 * Representa a escola raiz do contact
	 */
	@Enumerated(EnumType.STRING)
	private School rootSchool;

	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private Gender gender = Gender.M;

	@Column(length = 60)
	private String companyName;

	@ManyToMany
	@JoinTable(name = "contact_x_profession", 
		joinColumns = @JoinColumn(name = "contact_id"), 
		inverseJoinColumns = @JoinColumn(name = "profession_id")
	)
	private List<Profession> professions;

	@OneToOne(mappedBy="contact")
	private Account account;
	
	
	// address
	
	@Column(length = 90)
	private String address;

	@Column(length = 90)
	private String neighborhood;

	@Column(length = 90)
	private String city;

	@Column(length = 90)
	private String state;

	@Column(length = 90)
	@NotNull
	private String country;

	@Column(length = 90)
	private String zipPostal;

	// digital contacts

	@Column(nullable = false, length = 90)
	private String email;

	@Column(nullable = false, length = 30)
	private String telephone1;

	@Column(length = 30)
	private String telephone2;

	@Column(length = 30)
	private String telephone3;

	@Column(length = 60)
	private String skype;

	@Column(length = 90)
	private String indicateBy;

	// produtor e produzidos (auto-relacionamento)

	@ManyToOne
	private Contact productorContact;
	
	@ManyToOne
	private Contact productorContact2;//comissao compartilhada

	
	@OneToMany(mappedBy = "productorContact")
	private List<Contact> produtedOnes = new ArrayList<Contact>();

	// treinador e treinados (auto-relacionamento)

	@ManyToOne
	private Contact trainnerContact;

	@OneToMany(mappedBy = "trainnerContact")
	private List<Contact> trainnedOnes;

	@Enumerated(EnumType.STRING)
	private TrainnerType trainnerType;

	private Boolean flagProductor = false;

	private Boolean flagConsultant = false;

	// created by yamarty
	@Enumerated(EnumType.STRING)
	private TrainnerLevel trainnerLevel;

	@Enumerated(EnumType.STRING)
	private TshirtSize tshirtSize;

	@Enumerated(EnumType.STRING)
	private CBRole cbRole;

	/**
	 * Participante de producoes
	 */
	@OneToMany(mappedBy = "contact")
	private List<Participant> participants;

	/**
	 * Staffs de producoes
	 */
	@OneToMany(mappedBy = "contact")
	private List<Staff> staffs;

	/**
	 * Produtores em producoes
	 */
	@OneToMany(mappedBy = "contact")
	private List<Integrant> integrants;

	/**
	 * Observacoes sobre o contato
	 */
	@OneToMany(mappedBy = "contact", cascade = CascadeType.REMOVE)
	private List<ContactObservation> observations;

	// log
	@Size(max = 100)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Size(max = 100)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	
	
	// constructors
	public Contact() {
	}

	public Contact(String civilName, String email, String telephone1) {
		super();
		this.civilName = civilName;
		this.name = civilName;
		this.email = email;
		this.telephone1 = telephone1;
	}

	public Contact(Long id) {// para loader
		super();
		this.id = id;
	}

	
	
	// acessores..
	private static final long serialVersionUID = 6154724908915867971L;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Contact getProductorContact2() {
		return productorContact2;
	}
	public void setProductorContact2(Contact productorContact2) {
		this.productorContact2 = productorContact2;
	}
	public CBRole getCbRole() {
		return cbRole;
	}
	public void setCbRole(CBRole cbRole) {
		this.cbRole = cbRole;
	}
	public List<Contact> getTrainnedOnes() {
		return trainnedOnes;
	}
	public void setTrainnedOnes(List<Contact> trainnedOnes) {
		this.trainnedOnes = trainnedOnes;
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	public List<Integrant> getIntegrants() {
		return integrants;
	}
	public void setIntegrants(List<Integrant> integrants) {
		this.integrants = integrants;
	}
	public List<Staff> getStaffs() {
		return staffs;
	}
	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}
	public TshirtSize getTshirtSize() {
		return tshirtSize;
	}
	public void setTshirtSize(TshirtSize tshirtSize) {
		this.tshirtSize = tshirtSize;
	}
	public School getRootSchool() {
		return rootSchool;
	}
	public void setRootSchool(School rootSchool) {
		this.rootSchool = rootSchool;
	}
	public List<ContactObservation> getObservations() {
		return observations;
	}
	public void setObservations(List<ContactObservation> observations) {
		this.observations = observations;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCivilName() {
		return civilName;
	}
	public void setCivilName(String civilName) {
		this.civilName = civilName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone1() {
		return telephone1;
	}
	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}
	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public String getTelephone3() {
		return telephone3;
	}

	public void setTelephone3(String telephone3) {
		this.telephone3 = telephone3;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getIndicateBy() {
		return indicateBy;
	}

	public void setIndicateBy(String indicateBy) {
		this.indicateBy = indicateBy;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipPostal() {
		return zipPostal;
	}

	public void setZipPostal(String zipPostal) {
		this.zipPostal = zipPostal;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIdentityDocument() {
		return identityDocument;
	}

	public void setIdentityDocument(String identityDocument) {
		this.identityDocument = identityDocument;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public List<Profession> getProfessions() {
		return professions;
	}

	public void setProfessions(List<Profession> professions) {
		this.professions = professions;
	}

	public DocumentType getIdentityDocumentType() {
		return identityDocumentType;
	}

	public void setIdentityDocumentType(DocumentType identityDocumentType) {
		this.identityDocumentType = identityDocumentType;
	}

	public Contact getTrainnerContact() {
		return trainnerContact;
	}

	public void setTrainnerContact(Contact trainnerContact) {
		this.trainnerContact = trainnerContact;
	}

	public Boolean isFlagProductor() {
		return flagProductor;
	}

	public void setFlagProductor(Boolean flagProductor) {
		this.flagProductor = flagProductor;
	}

	public TrainnerType getTrainnerType() {
		return trainnerType;
	}

	public void setTrainnerType(TrainnerType trainnerType) {
		this.trainnerType = trainnerType;
	}

	public Contact getProductorContact() {
		return productorContact;
	}

	public void setProductorContact(Contact productorContact) {
		this.productorContact = productorContact;
	}

	public List<Contact> getProdutedOnes() {
		return produtedOnes;
	}

	public void setProdutedOnes(List<Contact> produtedOnes) {
		this.produtedOnes = produtedOnes;
	}

	public Boolean getFlagConsultant() {
		return flagConsultant;
	}

	public void setFlagConsultant(Boolean flagConsultant) {
		this.flagConsultant = flagConsultant;
	}

	public Boolean getFlagProductor() {
		return flagProductor;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public TrainnerLevel getTrainnerLevel() {
		return trainnerLevel;
	}

	public void setTrainnerLevel(TrainnerLevel trainnerLevel) {
		this.trainnerLevel = trainnerLevel;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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
		Contact other = (Contact) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", civilName=" + civilName + "]";
	}

	@Override
	public int compareTo(Contact that) {
		if (this.id!=null) {
			return this.id.compareTo( that.id );
		} else {
			return -1;
		}
	}
	
	
	
	public boolean isTransient() {
		return getId() == null;
	}

	// metodos especiais...

	/**
	 * Contato sera treinador que um tiver um tipo associado
	 * 
	 * @return
	 */
	public Boolean getFlagTrainner() {
		return getTrainnerType() != null;
	}

	/**
	 * Prepara uma descricao curta do contato, retorna o nome novo (se tiver) ou
	 * nome civil
	 * 
	 * @return
	 */
	public String getShortDesc() {
		return name == null || name.trim().isEmpty() ? civilName : name;
	}

	/**
	 * Prepara uma descricao completo do contato
	 * 
	 * @return
	 */
	public String getFullDesc() {
		if (name != null && !name.trim().isEmpty()) {
			return String.format("%s (%s)", civilName, name);
		}
		return civilName;
	}

	/**
	 * Monta o documento de identidade completo
	 * 
	 * @return
	 */
	public String getFullIdentityDocument() {
		return String.format("%s %s", getIdentityDocumentType(),
				getIdentityDocument());
	}

	/**
	 * Monta os dados de endereco numa unica string
	 * 
	 * @return
	 */
	public String getFullAddress() {
		return String.format("%s - %s - %s", getAddress(), getCity(), getCountry() );
	}

	/**
	 * Mostra civilName, name e email. (usado na tela signup para mostra que
	 * contato ja tem usuario)
	 * 
	 * @return
	 */
	public String getMoreFullDesc() {
		if (name != null && !name.trim().isEmpty()) {
			return String.format("%s (%s) [%s]", getCivilName(), getName(),
					getEmail());
		}
		return String.format("%s [%s]", getCivilName(), getEmail());
	}

	/**
	 * Mostra o formato para envio de email
	 * 
	 * @return
	 */
	public String getMailingDesc() {
		return String.format("%s (%s)", getShortDesc(), getEmail());
	}

	/**
	 * Preeenche os campos obrigatorios com um texto default Em check-in,
	 * primeiro se cria um contato no banco e depois se preenche com os dados
	 * reais.
	 */
	public void fillRequiredAttributes() {
		String RANDOM_CONTENT = "?";

		this.civilName = RANDOM_CONTENT;
		this.telephone1 = RANDOM_CONTENT;
	}

	/**
	 * Usado zerar os campos obrigatorios. preenche seus campos.
	 */
	public void resetRequiredAttributes() {
		this.civilName = null;
		this.telephone1 = null;
	}

	// metodos para idade e maturidade...

	/**
	 * Retorna se o calculo de idade pode ser realizado
	 * 
	 * @return
	 */
	public boolean getFlagAgingOK() {
		return getBirthDate() != null;
	}

	/**
	 * Calcula idade
	 * 
	 * @return
	 */
	public int getAge() {
		return CalendarUtil.calculateAge(getBirthDate());
	}

	/**
	 * Calcula a maturidade conforme enum
	 * 
	 * @return
	 */
	public ContactMaturity getMaturity() {
		int age = CalendarUtil.calculateAge(getBirthDate());
		return ContactMaturity.getByAge(age);
	}

	/**
	 * Retorna o nome do produtor de maneira segura (evitando NPE)
	 * 
	 * @return
	 */
	public String getSafetyProductorName() {
		return getProductorContact() != null ? getProductorContact()
				.getShortDesc() : "";
	}

	/**
	 * Flag para verificar se tem nome novo
	 * 
	 * @return
	 */
	public Boolean getFlagHasNewName() {
		return getName() != null && !getName().trim().isEmpty();
	}

	/**
	 * Faz o clone do objeto
	 * 
	 * @return
	 */
	public Contact builtTransientClone() {
		Contact clone = new Contact();
		clone.setAddress(address);
		clone.setBirthDate(birthDate);
		clone.setCbRole(cbRole);
		clone.setCity(city);
		clone.setCivilName(civilName);
		clone.setCompanyName(companyName);
		clone.setCountry(country);
		clone.setCreatedBy(createdBy);
		clone.setEmail(email);
		clone.setFlagConsultant(flagConsultant);
		clone.setFlagProductor(flagProductor);
		clone.setGender(gender);
		clone.setIdentityDocument(identityDocument);
		clone.setIdentityDocumentType(identityDocumentType);
		clone.setIndicateBy(indicateBy);
		clone.setName(name);
		clone.setNeighborhood(neighborhood);
		clone.setProductorContact(productorContact);
		clone.setSkype(skype);
		clone.setState(state);
		clone.setTelephone1(telephone1);
		clone.setTelephone2(telephone2);
		clone.setTelephone3(telephone3);
		clone.setTrainnerContact(trainnerContact);
		clone.setTrainnerType(trainnerType);
		clone.setZipPostal(zipPostal);
		return clone;
	}


}
