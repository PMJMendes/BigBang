package bigBang.module.tasksModule.client.userInterface.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Task;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.tasksModule.client.userInterface.form.DismissTaskForm;
import bigBang.module.tasksModule.client.userInterface.presenter.DismissTaskViewPresenter;

public class DismissTaskView extends View implements DismissTaskViewPresenter.Display{

	private DismissTaskForm form;
	private Button dismissButton;
	
	public DismissTaskView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		form = new DismissTaskForm();
		form.setSize("100%", "100%");
		dismissButton = new Button("Tomei Conhecimento");
		form.addWidget(dismissButton);
		
		wrapper.add(form);
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	@Override
	public HasClickHandlers getDismissButton() {
		return dismissButton;
	}
	
	@Override
	public HasEditableValue<Task> getForm() {
		return this.form;
	}
	
}
