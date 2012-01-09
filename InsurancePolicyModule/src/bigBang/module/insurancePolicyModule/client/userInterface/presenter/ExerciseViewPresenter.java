package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ExerciseDataBrokerClient;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseForm;

public abstract class ExerciseViewPresenter implements ViewPresenter{

	public static enum Action{
		EDIT,
		SAVE,
		CANCEL,
		DELETE
	}

	public static interface Display{

		void setExercise(Exercise exercise);
		void setInsurancePolicy(InsurancePolicy policy);
		Widget asWidget();
		void setReadOnly(boolean readOnly);
		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);
		ExerciseForm getExerciseForm();
		void showError(String string);
		void showMessage(String string);

	}

	protected Display view;
	protected boolean bound = false;
	private boolean readOnly = true;
	@Override
	public void setView(View view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}


	@Override
	public void bind() {
		if(bound){
			return;
		}
		this.view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SAVE:
					Exercise value = view.getExerciseForm().getInfo();
					saveExercise(value);
					break;
				case EDIT:{
					readOnly = false;
					view.setReadOnly(readOnly);
					break;}
				case CANCEL:
					readOnly = true;
					view.setReadOnly(readOnly);
					setExercise(view.getExerciseForm().getInfo());
					break;
				case DELETE:
					deleteExercise();
					break;
				}
			}});

		bound = true;
	}

	protected void deleteExercise() {
		
		//TODO server call
		
		boolean success = true;
		if(success){
			view.getExerciseForm().clearInfo();
			
		}
		else{
			view.showError("Problema ao apagar o exercício.");
		}
	}

	protected void saveExercise(Exercise value) {
		
		boolean success = true;
		if(success){
			readOnly = true;
			view.setReadOnly(readOnly);
		}
		else{
			view.showError("Problema ao gravar o exercício.");
		}
		
	}

	@Override
	public void setService(Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEventBus(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {		//view.getInsurancePolicyForm().setValue(policy);

		// TODO Auto-generated method stub

	}

	public void setPolicy(InsurancePolicy policy){
		//view.getInsurancePolicyForm().setValue(policy);
	}

	public void setExercise(Exercise exercise){
		view.setExercise(exercise);
		view.setReadOnly(readOnly);
	}

	
	public void clearView(){
		view.getExerciseForm().clearInfo();
	}

	public Exercise getExerciseViaBroker() {
		
		Exercise exercise = new Exercise();
		ExerciseDataBrokerClient broker = new ExerciseDataBrokerClient() {
			
			
			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int getDataVersion(String dataElementId) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void updateExercise(Exercise exercise) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeExercise(String id) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addExercise(Exercise exercise) {
				// TODO Auto-generated method stub
				
			}
		};
		
		return exercise;
	}

}
