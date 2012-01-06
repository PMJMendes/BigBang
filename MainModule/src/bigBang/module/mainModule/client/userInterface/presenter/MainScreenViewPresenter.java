package bigBang.module.mainModule.client.userInterface.presenter;

import java.util.HashMap;

import bigBang.library.client.EventBus;
import bigBang.library.client.event.LogoutEvent;
import bigBang.library.client.event.ShowMeRequestEvent;
import bigBang.library.client.event.ShowMeRequestEventHandler;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

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
	private HashMap<String, SectionViewPresenter> sectionPresenters;

	public MainScreenViewPresenter(EventBus eventBus, final View view){
		this.sections = new HashMap<String, MenuSection>();
		this.sectionPresenters = new HashMap<String, SectionViewPresenter>();
		this.setView(view);
		this.setEventBus(eventBus);
	}

	public void setService(Service service) {
		return;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
		registerEventHandlers(eventBus);
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
		this.sectionPresenters.put(sectionPresenter.getSection().getId(), sectionPresenter);
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
		if(this.eventBus == null) {setEventBus(eventBus);}
		eventBus.addHandler(ShowMeRequestEvent.TYPE, new ShowMeRequestEventHandler() {
			
			@Override
			public void onShowMeRequest(ShowMeRequestEvent event) {
				Object source = event.getMe();
				if(sections.containsValue(source)){
					try {
						view.showSection((MenuSection) source);
					} catch (Exception e) {
					}
				}
			}
		});
	}

	public void setUsername(String username) {
		view.setUsername(username);
	}

	public void setDomain(String domain) {
		view.setDomain(domain);
	}

}
