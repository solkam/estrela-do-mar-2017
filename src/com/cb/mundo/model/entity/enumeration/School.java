package com.cb.mundo.model.entity.enumeration;

import com.cb.mundo.model.calculum.CoachingRendicionCalculum;
import com.cb.mundo.model.calculum.DefaultRendicionCalculum;
import com.cb.mundo.model.calculum.RendicionCalculum;

/**
 * Represent a CB school and is internacionalizable
 * Upgraded with kaiwoman and bodhisattva
 * 
 * script produção:
insert into user_x_school (user_id, school) values (1, 'ME');

 * 
 * @author solkam
 * @since 17 ago 2011
 */
public enum School {
	
	BD("SCHOOL_BODHISATTVA"    , "logo_school_vertical_bodhisattva_120.png"   , "Bodhisattva"      , new DefaultRendicionCalculum() ),
	CE("SCHOOL_COACHINGEXPRESS", "logo_school_vertical_coaching_120.png"      , "Coaching Express" , new CoachingRendicionCalculum() ),
	FM("SCHOOL_FREEMIND"       , "logo_school_vertical_freemind_120.png"      , "Free Mind"        , new DefaultRendicionCalculum() ),
	KW("SCHOOL_KAIWOMAN"       , "logo_school_vertical_kaiwoman_120.png"      , "Kai Woman"        , new DefaultRendicionCalculum() ),
	KF("SCHOOL_KINFOREST"      , "logo_school_vertical_kinforest_120.png"     , "Kin Forest"       , new DefaultRendicionCalculum() ),
	LCB("SCHOOL_LIDERAZGO"     , "logo_school_vertical_liderazgo_120.png"     , "Liderança"        , new DefaultRendicionCalculum() ),
	KA("SCHOOL_KAINAPI"        , "logo_school_vertical_kainapi_120.png"       , "Guia de Kainapi"  , new DefaultRendicionCalculum() ),
	ME("SCHOOL_MEDITACION"     , "logo_school_vertical_meditacion_120.png"    , "Meditación"       , new DefaultRendicionCalculum() ),
	ND("SCHOOL_NATURALDETOX"   , "logo_school_vertical_naturaldetox_120.png"  , "Natural Detox"    , new DefaultRendicionCalculum() ),
	NU("SCHOOL_NUMEROLOGY"     , "logo_school_vertical_numerologia_120.png"   , "Numerologia"      , new DefaultRendicionCalculum() ),
	TA("SCHOOL_TALENTOS"       , "logo_school_vertical_talentos_120.png"      , "Talentos"         , new DefaultRendicionCalculum() ),
	VH("SCHOOL_VISION_HALCON"  , "logo_cb_principal.png"                      , "Vision del Halcon", new DefaultRendicionCalculum() ),
	YK("SCHOOL_YAIKIN"         , "logo_cb_principal.png"                      , "Yaikin"           , new DefaultRendicionCalculum() ),
	;
	
//inativada mas voltara	
//	BF("SCHOOL_BARDOFENIX",      "logo_cb_principal.png",   "Bardo Fenix"      , new DefaultRendicionCalculum() ),

	private final String key;
	private final String image;
	private final String description;
	private final RendicionCalculum rendicionCalculum;//strategy para calculo de rendiciones

	private School(String key, String image, String description, RendicionCalculum rc) {
		this.key = key;
		this.image = image;
		this.description = description;
		this.rendicionCalculum = rc;//todas as escolas seguem a mesma regra de rendicion
	}
	
	public String getKey() {
		return key;
	}
	public String getImage() {
		return image;
	}
	public String getDescription() {
		return description;
	}
	public RendicionCalculum getRendicionCalculum() {
		return rendicionCalculum;
	}

	
	@Override
	public String toString() {
		return name();
	}
	
}
