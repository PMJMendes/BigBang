package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ConfirmCancelToolbar;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.quoteRequestModule.client.userInterface.form.ChooseSublineForm;

public class ChooseSublinePanel extends PopupPanel implements HasValue<String>{

	ConfirmCancelToolbar toolbar;
	ChooseSublineForm form;
	
	public ChooseSublinePanel() {
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		initWidget(wrapper);
		toolbar = new ConfirmCancelToolbar() {
			
			@Override
			public void onConfirm() {
				if(form.validate()){
					ChooseSublinePanel.this.hidePopup();
				}
				else{
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem problemas no preenchimento do formulário."), TYPE.ERROR_TRAY_NOTIFICATION));
				}
			}
			
			@Override
			public void onCancelRequest() {
				form.setValue(null);
				hidePopup();
			}
		};
		
		form = new ChooseSublineForm();
		wrapper.add(form.getNonScrollableContent());
		
	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return null;
	}

	@Override
	public String getValue() {
		return form.getInfo();
	}

	@Override
	public void setValue(String value) {
		form.setValue(value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		form.setValue(value, fireEvents);
	}

}
