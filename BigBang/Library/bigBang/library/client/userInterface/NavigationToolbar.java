package bigBang.library.client.userInterface;

import bigBang.library.client.event.NavigationRequestEvent;
import bigBang.library.client.event.NavigationRequestEvent.Navigation;
import bigBang.library.client.event.NavigationRequestEventHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;

public class NavigationToolbar extends ListHeader {
	
	protected Button nextButton, prevButton;
	
	public NavigationToolbar(){
		super();
		
		nextButton = new Button("Próximo");
		nextButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new NavigationRequestEvent(Navigation.NEXT));
			}
		});

		prevButton = new Button("Anterior");
		prevButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new NavigationRequestEvent(Navigation.PREVIOUS));
			}
		});
		showNext(true);
		showPrevious(true);
	}
	
	public void showNext(boolean show) {
		if(!show){
			setRightWidget(new SimplePanel());
			return;
		}
		setRightWidget(nextButton);
	}
	
	public void showPrevious(boolean show) {
		if(!show){
			setLeftWidget(new SimplePanel());
			return;
		}
		setLeftWidget(prevButton);
	}
	
	public HandlerRegistration addNavigationEventHandler(NavigationRequestEventHandler h) {
		return addHandler(h, NavigationRequestEvent.TYPE);
	}
}
