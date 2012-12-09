package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;


public abstract class ConversationTasksOperationsToolbar extends BigBangOperationsToolBar{

	protected MenuItem goToProcess;
	
	protected ConversationTasksOperationsToolbar(){
		hideAll();
		
		goToProcess = new MenuItem("Navegar para processo auxiliar", new Command() {

			@Override
			public void execute() {
				onGoToProcess();
			}
			
		});
		addItem(goToProcess);
	}
	@Override
	public void onEditRequest() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onSaveRequest() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCancelRequest() {
		// TODO Auto-generated method stub
	}
	
	protected abstract void onGoToProcess();

}
