package bigBang.library.client.userInterface;

import bigBang.library.client.userInterface.NavigationToolbar.NavigationEvent.Navigation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;

public class NavigationToolbar extends ListHeader {
	
	public interface NavigationEventHandler extends EventHandler {
		public void onNavigationEvent(Navigation n);
	}
	
	public static class NavigationEvent extends GwtEvent<NavigationEventHandler> {
		
		public enum Navigation {
			NEXT,
			PREVIOUS
		} 
		
		private Navigation navigation; 
		
		public NavigationEvent(Navigation n){
			this.navigation = n;
		}
		
		public static final Type<NavigationEventHandler> TYPE = new Type<NavigationEventHandler>(); 
		

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<NavigationEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(NavigationEventHandler handler) {
			handler.onNavigationEvent(navigation);
		}
	}
	
	public NavigationToolbar(){
		super();
	}
	
	public void showNext(boolean show) {
		if(!show){
			setRightWidget(new SimplePanel());
			return;
		}
		Button nextButton = new Button("Próximo");
		nextButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new NavigationEvent(Navigation.NEXT));
			}
		});
		setRightWidget(nextButton);
	}
	
	public void showPrevious(boolean show) {
		if(!show){
			setLeftWidget(new SimplePanel());
			return;
		}
		Button prevButton = new Button("Anterior");
		prevButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new NavigationEvent(Navigation.PREVIOUS));
			}
		});
		setLeftWidget(prevButton);
	}
	
	public HandlerRegistration addNavigationEventHandler(NavigationEventHandler h) {
		return addHandler(h, NavigationEvent.TYPE);
	}
}
