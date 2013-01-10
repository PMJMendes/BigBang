package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.MedicalFileTasksToolbar;
import bigBang.module.casualtyModule.client.userInterface.form.MedicalFileForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileTasksViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.MedicalFileTasksViewPresenter.Action;

public class MedicalFileTasksView extends View implements MedicalFileTasksViewPresenter.Display{

	protected MedicalFileForm form;
	protected MedicalFileTasksToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;

	public MedicalFileTasksView() {
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		wrapper.setSize("100%", "100%");
		
		toolbar = new MedicalFileTasksToolbar() {
			
			@Override
			public void onSaveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<MedicalFileTasksViewPresenter.Action>(Action.SAVE));

			}
			
			@Override
			public void onEditRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<MedicalFileTasksViewPresenter.Action>(Action.EDIT));
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<MedicalFileTasksViewPresenter.Action>(Action.CANCEL));

			}
			
			@Override
			protected void onNavigateToAuxiliaryProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<MedicalFileTasksViewPresenter.Action>(Action.GO_TO_PROCESS));
			}
		};
		
		form = new MedicalFileForm();
		form.setReadOnly(true);
		form.setSize("100%", "100%");
		
		wrapper.add(toolbar);
		wrapper.add(form);
		
		wrapper.setCellHeight(form, "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<MedicalFile> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.lockAll();
	}

	@Override
	public void allowEdit(boolean b) {
		toolbar.allowEdit(b);
	}

}
