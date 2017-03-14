package com.cb.mundo.controller.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.mundo.model.entity.RoomType;
import com.cb.mundo.model.entity.config.CertificateItemAlign;
import com.cb.mundo.model.entity.config.CertificateItemAttribute;
import com.cb.mundo.model.entity.config.CertificateItemColor;
import com.cb.mundo.model.entity.config.CertificateItemFontStyle;
import com.cb.mundo.model.entity.enumeration.AccountReceivableStatus;
import com.cb.mundo.model.entity.enumeration.CBRole;
import com.cb.mundo.model.entity.enumeration.DocumentType;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.EventType;
import com.cb.mundo.model.entity.enumeration.FacilitatorCategory;
import com.cb.mundo.model.entity.enumeration.FinancialStatus;
import com.cb.mundo.model.entity.enumeration.Gender;
import com.cb.mundo.model.entity.enumeration.Idiom;
import com.cb.mundo.model.entity.enumeration.IncomingCategory;
import com.cb.mundo.model.entity.enumeration.InformeStatus;
import com.cb.mundo.model.entity.enumeration.InformeType;
import com.cb.mundo.model.entity.enumeration.IntegrantRole;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.entity.enumeration.MegaEventType;
import com.cb.mundo.model.entity.enumeration.OutcomingCategory;
import com.cb.mundo.model.entity.enumeration.PaymentType;
import com.cb.mundo.model.entity.enumeration.Profile;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.entity.enumeration.SaleReceipt;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.entity.enumeration.SeminarPaymentMethod;
import com.cb.mundo.model.entity.enumeration.StaffRole;
import com.cb.mundo.model.entity.enumeration.TrainnerLevel;
import com.cb.mundo.model.entity.enumeration.TrainnerType;
import com.cb.mundo.model.entity.enumeration.TransportDirection;
import com.cb.mundo.model.entity.enumeration.TshirtSize;
import com.cb.mundo.model.entity.enumeration.WayOfPaymentToAdmin;

/**
 * Helper para enumeration serem acessiveis 
 * nas paginas (para construir combos)
 * [04ABR2015] Movido para mundocb-base
 * 
 * @author Solkam
 * @since 04 abr 2013
 */
@ManagedBean(name="enumHelper")
@ViewScoped
public class EnumHelper implements Serializable {
	
	//comuns
	
	public Gender[] getGenders() {
		return Gender.values();
	}
	
	public Profile[] getProfiles() {
		return Profile.values();
	}
	
	public School[] getSchools() {
		return School.values();
	}
	
	/**
	 * Somente idiomas liberados: pt e es
	 * @return
	 */
	public Idiom[] getIdioms() {
		return new Idiom[] {Idiom.pt, Idiom.es};
	}
	
	public DocumentType[] getDocumentTypes() {
		return DocumentType.values();
	}
	
	
	
	//seminario
	
	public SeminarPaymentMethod[] getSeminarPaymentMethods() {
		return SeminarPaymentMethod.values();
	}
	
	public StaffRole[] getStaffRoles() {
		return StaffRole.values();
	}

	public IntegrantRole[] getIntegrantRoles() {
		return IntegrantRole.values();
	}

	public FinancialStatus[] getFinancialStatusList() {
		return FinancialStatus.values();
	}

	public IncomingCategory[] getIncomingCategories() {
		return IncomingCategory.values();
	}

	public OutcomingCategory[] getOutcomingCategories() {
		return OutcomingCategory.values();
	}

	public CertificateItemColor[] getCertificateItemColors() {
		return CertificateItemColor.values();
	}

	public CertificateItemAlign[] getCertificateItemAligns() {
		return CertificateItemAlign.values();
	}

	public CertificateItemAttribute[] getCertificateItemAttributes() {
		return CertificateItemAttribute.values();
	}

	public CertificateItemFontStyle[] getCertificateItemFontStyle() {
		return CertificateItemFontStyle.values();
	}

	public WayOfPaymentToAdmin[] getWaysOfPaymentToAdmin() {
		return WayOfPaymentToAdmin.values();
	}

	public InformeType[] getInformeTypes() {
		return InformeType.values();
	}

	public InformeStatus[] getInformeStatusList() {
		return InformeStatus.values();
	}

	public FacilitatorCategory[] getFacilitatorCategories() {
		return FacilitatorCategory.values();
	}

	public List<Profile> getSeminarProfiles() {
		List<Profile> profiles = new ArrayList<>();
		for (Profile profile : Profile.values()) {
			if (profile.getFlagSeminar()) {
				profiles.add( profile );
			}
		}
		return profiles;
	}
	
	
	
	//inscricao
	
	public MegaEventType[] getMegaEventTypes() {
		return MegaEventType.values();
	}
	
	
	
	
	public MegaEventPaymentMethod[] getMegaEventPaymentMethods() {
		return MegaEventPaymentMethod.values();
	}
	
	public EventType[] getEventTypes() {
		return EventType.values();
	}
	
	
	public EventPresence[] getEventPresences() {
		return EventPresence.values();
	}
	
	public EventPresence[] getEventPresencesParticipantAndStaff() {
		EventPresence[] presences = new EventPresence[2];
		presences[0] = EventPresence.PARTICIPANT;
		presences[1] = EventPresence.STAFF;
		return presences;
	}
	
	public EventPresence[] getEventPresencesFamiliar() {
		EventPresence[] presences = new EventPresence[2];
		presences[0] = EventPresence.DEPENDENT;
		return presences;
	}
	
	public TrainnerType[] getTrainnerTypes() {
		return TrainnerType.values();
	}
	
	public TrainnerLevel[] getTrainnerLevel() {
		return TrainnerLevel.values();
	}
	public TransportDirection[] getTransportDirection() {
		return TransportDirection.values();
	}
	
	public RegisterStatus[] getRegisterStatusList() {
		return RegisterStatus.values();
	}
	
    public RegisterStatus[] getRegisterStatusForChangePurpose() {
         return new RegisterStatus[]{
             RegisterStatus.CANCELED, RegisterStatus.CHECKEDIN, RegisterStatus.CHECKEDOUT, RegisterStatus.EXPIRED, 
             RegisterStatus.MEGAEVENTOUT, RegisterStatus.TRANSFERED, RegisterStatus.REGISTERED
         };
	}
        
	public TshirtSize[] getTshirtSizes() {
		return TshirtSize.values();
	}
	
	public RoomType[] getRoomTypes() {
//		return RoomType.values();
		return null;
	}
        
    public SaleReceipt[] getSaleReceipts(){
        return SaleReceipt.values();
    }
        
    public MegaEventPaymentMethod[] getMegaEventPaymentMethodForAccountReceivable(){
         return new MegaEventPaymentMethod[]{
             MegaEventPaymentMethod.CH, 
             MegaEventPaymentMethod.DC, 
             MegaEventPaymentMethod.DP, 
             MegaEventPaymentMethod.CC,
             MegaEventPaymentMethod.BT
         };
    }
        
    public AccountReceivableStatus[] getAccountReceivableStatus(){
         return AccountReceivableStatus.values();
    }
        
    public PaymentType[] getPaymentType(){
         return new PaymentType[]{
             PaymentType.IC, PaymentType.AF, PaymentType.MF,
         };
    }
        
    public PaymentType[] getPaymentTypeWithoutCash(){
         return new PaymentType[]{
            PaymentType.AF, PaymentType.MF,
         };
    }
        
    public PaymentType getNonePaymentType(){
        return PaymentType.NO;
    }
    
    public CBRole[] getCBRoles() {
    	return CBRole.values();
    }
    

    private static final long serialVersionUID = -8673804423121889389L;
}
