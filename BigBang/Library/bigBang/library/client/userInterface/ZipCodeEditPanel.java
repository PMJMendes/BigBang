package bigBang.library.client.userInterface;


import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.ZipCode;
import bigBang.library.client.userInterface.view.View;

public class ZipCodeEditPanel extends View implements HasValue<ZipCode>{


	public abstract class ZipCodeEditOperationsToolbar extends BigBangOperationsToolBar{

		private MenuItem confirm;
		private MenuItem cancel;

		public ZipCodeEditOperationsToolbar(){
			super();
			confirm = new MenuItem("Confirmar", new Command() {

				@Override
				public void execute() {
					onConfirmRequest();
				}
			});

			cancel = new MenuItem("Cancelar", new Command() {

				@Override
				public void execute() {
					onCancelRequest();
				}

			});


			this.hideAll();
			this.addItem(confirm);
			this.addSeparator();
			this.addItem(cancel);
		}


		protected abstract void onConfirmRequest();

		@Override
		public void onEditRequest() {
			return;
		}
		@Override
		public void onSaveRequest() {
			return;
		}
		@Override
		public abstract void onCancelRequest();

	}

	private ZipCodeForm form;
	private VerticalPanel mainWrapper;
	private ZipCodeEditOperationsToolbar toolbar;
	
	public ZipCodeEditPanel() {

		
		mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);

		form = new ZipCodeForm();
		
		toolbar = new ZipCodeEditOperationsToolbar() {
			
			@Override
			protected void onConfirmRequest() {
				onConfirm();
			}
			
			@Override
			public void onCancelRequest() {
				onCancel();
			}
		};
		
		mainWrapper.add(toolbar);
		mainWrapper.add(form.getNonScrollableContent());
		mainWrapper.setCellWidth(form.getNonScrollableContent(), "100%");
		
	}

	protected void onCancel() {
		ValueChangeEvent.fire(this, null);
	}

	protected void onConfirm() {
		ValueChangeEvent.fire(this, form.getInfo());
	}

	@Override
	protected void initializeView() {
		return;
	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<ZipCode> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void setValue(ZipCode value) {
		setValue(value, true);
	}

	@Override
	public void setValue(ZipCode value, boolean fireEvents) {
		form.setValue(value);
		
		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public ZipCode getValue() {
		return form.getValue();
	}

	public HasValue<ZipCode> getForm() {
		return form;
	}

}
