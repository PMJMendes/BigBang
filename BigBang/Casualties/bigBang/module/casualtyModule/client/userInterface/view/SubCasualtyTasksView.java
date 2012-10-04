package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.module.casualtyModule.client.userInterface.SubCasualtyTasksOperationsToolbar;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyTasksViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyTasksViewPresenter.Action;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;

public class SubCasualtyTasksView extends View implements SubCasualtyTasksViewPresenter.Display{

	protected SubCasualtyForm form;
	protected SubCasualtyTasksOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<Action> handler;
	
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	
	public SubCasualtyTasksView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		toolbar = new SubCasualtyTasksOperationsToolbar() {
			
			@Override
			public void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CLOSE));
			}

			@Override
			public void onRejectClose() {
				handler.onActionInvoked(new ActionInvokedEvent<Action>(Action.REJECT_CLOSE));
			}

			@Override
			protected void onGoToProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<SubCasualtyTasksViewPresenter.Action>(Action.GO_TO_PROCESS));
			}
		};
		wrapper.add(toolbar);
		
		form = new SubCasualtyForm();
		form.setReadOnly(true);
		form.setSize("100%", "100%");
		wrapper.add(form);
		wrapper.setCellHeight(form, "100%");
		
		this.overlayContainer = new SimplePanel();
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<SubCasualty> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.hideAll();
		toolbar.setGoToProcessVisible();
	}

	@Override
	public void allowClose(boolean allow) {
		toolbar.allowClose(allow);
	}

	@Override
	public void allowRejectClose(boolean allow) {
		toolbar.allowRejectClose(allow);
	}

	@Override
	public HasWidgets getOverlayViewContainer() {
		return this.overlayContainer;
	}

	@Override
	public void showOverlayViewContainer(boolean show) {
		if(show && this.popupPanel == null){
			this.popupPanel = new PopupPanel(){
				@Override
				protected void onDetach() {
					super.onDetach();
					SubCasualtyTasksView.this.popupPanel = null;
				}
			};
			this.popupPanel.add((Widget)this.overlayContainer);
		}

		if(this.popupPanel != null){
			if(show && !this.popupPanel.isAttached()){
				this.popupPanel.center();
			}else if(this.popupPanel.isAttached() && !show){
				this.popupPanel.hidePopup();
				this.popupPanel.remove((Widget)this.overlayContainer);
				this.popupPanel = null;
			}
		}
	}

}
