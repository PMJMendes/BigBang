package bigBang.module.tasksModule.client.userInterface.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Task;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.tasksModule.client.userInterface.presenter.DismissTaskPresenter;

public class DismissTaskView extends View implements DismissTaskPresenter.Display{


	private Label longDescFF;
	private Label datePFF;
	private Button dismissButton;
	
	
	public DismissTaskView(){
		
		longDescFF = new Label();
		datePFF = new Label();
		dismissButton = new Button("Tomei Conhecimento");
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSpacing(10);
		wrapper.setSize("100%", "100%");
		wrapper.add(new Label("Data do Aviso"));
		wrapper.add(datePFF);
		wrapper.add(new Label("Descrição"));
		wrapper.add(longDescFF);
		wrapper.add(dismissButton);
		initWidget(wrapper);
	}
	
	@Override
	public void showTask(Task dismissTask) {
		
		datePFF.setText(dismissTask.timeStamp);
		longDescFF.setText(dismissTask.longDesc);
		
		
	}

	@Override
	public HasClickHandlers getDismissButton() {
		
		return dismissButton;
				
	}
	
	public void clear(){
		
		datePFF.setText("");
		longDescFF.setText("");
		
	}

	@Override
	public void lockView(boolean b) {
		this.dismissButton.setEnabled(!b);
		
	}



}
