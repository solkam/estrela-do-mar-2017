package com.cb.estreladomar.controller.adapter;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.service.EventService;

@ManagedBean(name="inscricaoMenuButtonAdapter")
@RequestScoped
public class MenuButtonAdapter {
	
	@EJB
	private EventService eventService;
	
//	private MethodExpression buildMethodExpression(String expression) {
//		FacesContext facesCtx = FacesContext.getCurrentInstance();
//		ELContext elCtx = facesCtx.getELContext();
//		ExpressionFactory expFactory = facesCtx.getApplication().getExpressionFactory();
//		return expFactory.createMethodExpression(elCtx, expression, String.class, new Class[0]);
//	}
	
	public MenuModel getMegaEventModel() {
		MenuModel model = new DefaultMenuModel();
		List<MegaEvent> activeMegaEvents = eventService.searchActiveMegaEvent();
		for (MegaEvent megaEvent : activeMegaEvents) {
			DefaultMenuItem item = new DefaultMenuItem();
			item.setValue( megaEvent.getName() );
			item.setAjax( false );
			item.setAsync( false );
			
			String command = String.format("#{sessionHolder.setSessionMegaEventById( %s )}", megaEvent.getId() );
			item.setCommand( command );
			
			model.addElement( item );
		}
		return model;
	}
	
	

}
