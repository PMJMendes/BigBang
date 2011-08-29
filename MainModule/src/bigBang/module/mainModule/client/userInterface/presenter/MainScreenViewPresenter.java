package bigBang.module.mainModule.client.userInterface.presenter;

import java.util.HashMap;

import bigBang.library.client.EventBus;
import bigBang.library.client.event.LogoutEvent;
import bigBang.library.client.event.ScreenInvokedEvent;
import bigBang.library.client.event.ScreenInvokedEventHandler;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

public class MainScreenViewPresenter implements ViewPresenter {

	public interface Display {
		Widget asWidget();
		void createMenuSection(SectionViewPresenter sectionPresenter);
		void showSection(MenuSection section) throws Exception;
		void setUsername(String username);

		MenuItem getLogoutButton();
		void setDomain(String domain);
		void showFirstSection();
	}

	private Display view;
	private EventBus eventBus;

	private HashMap<String, MenuSection> sections;

	public MainScreenViewPresenter(EventBus eventBus, final View view){
		this.sections = new HashMap<String, MenuSection>();
		this.setView(view);
		this.setEventBus(eventBus);

		eventBus.addHandler(ScreenInvokedEvent.TYPE, new ScreenInvokedEventHandler() {

			@Override
			public void onScreenInvoked(ScreenInvokedEvent event) {
				try {
					((Display) view).showSection(sections.get(event.getSectionId()));
				} catch (Exception e) {
					GWT.log("Nonexistent section");
				}
			}
		});
	} 

	public void setService(Service service) {
		return;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setView(View view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());		
	}

	public void addMenuSectionPresenter(SectionViewPresenter sectionPresenter){
		this.view.createMenuSection(sectionPresenter);
		this.sections.put(sectionPresenter.getSection().getId(), sectionPresenter.getSection());
	}
	
	public void renderPresenters() {
		this.view.showFirstSection();
	}

	public void bind() {
		view.getLogoutButton().setCommand(new Command() {

			@Override
			public void execute() {
				eventBus.fireEvent(new LogoutEvent());
			}
		});
	}

	public void registerEventHandlers(EventBus eventBus) {
		setEventBus(eventBus);
	}

	public void setUsername(String username) {
		view.setUsername(username);
	}

	public void setDomain(String domain) {
		view.setDomain(domain);
	}

}
