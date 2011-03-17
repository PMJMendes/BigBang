package bigBang.module.mainModule.client.userInterface.presenter;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Service;
import bigBang.library.shared.userInterface.MenuSection;
import bigBang.library.shared.userInterface.presenter.SectionViewPresenter;
import bigBang.library.shared.userInterface.presenter.ViewPresenter;
import bigBang.library.shared.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class MainScreenViewPresenter implements ViewPresenter {
	
	public interface Display {
		Widget asWidget();
		void createMenuSection(SectionViewPresenter sectionPresenter);
		void showSection(MenuSection section) throws Exception;
	}
	
	private Display view;
	private EventBus eventBus;

	public MainScreenViewPresenter(EventBus eventBus, View view){
		this.setView(view);
		this.setEventBus(eventBus);
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
	}

	public void bind() {
		
	}

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub
		
	}

}
