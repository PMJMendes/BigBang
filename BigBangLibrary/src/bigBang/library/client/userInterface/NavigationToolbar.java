package bigBang.library.client.userInterface;

import bigBang.library.client.event.NavigationEvent;
import bigBang.library.client.event.NavigationEvent.Navigation;
import bigBang.library.client.event.NavigationEventHandler;

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
				fireEvent(new NavigationEvent(Navigation.NEXT));
			}
		});

		prevButton = new Button("Anterior");
		prevButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new NavigationEvent(Navigation.PREVIOUS));
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
	
	public HandlerRegistration addNavigationEventHandler(NavigationEventHandler h) {
		return addHandler(h, NavigationEvent.TYPE);
	}
}
