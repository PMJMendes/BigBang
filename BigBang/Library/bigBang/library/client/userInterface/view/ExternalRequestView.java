package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.userInterface.ExternalInfoRequestForm;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.ExternalRequestViewPresenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class ExternalRequestView<T> extends View implements ExternalRequestViewPresenter.Display{

	private FormView<T> ownerForm;
	private ExternalInfoRequestForm form;
	protected ListHeader ownerHeader;

	public ExternalRequestView(FormView<T> ownerForm){

		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);

		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel left = new VerticalPanel();
		left.setSize("100%", "100%");
		ownerHeader = new ListHeader();
		left.add(ownerHeader);
		left.setCellWidth(ownerHeader,"100%");
		left.add(ownerForm);
		left.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(left, 650);

		VerticalPanel right = new VerticalPanel();
		right.setSize("100%", "100%");
		ListHeader externalRequestHeader = new ListHeader("Pedido de Informação Externo");
		right.add(externalRequestHeader);
		right.setCellWidth(externalRequestHeader, "100%");
		
		//TODO METER TOOLBAR
		
		form = new ExternalInfoRequestForm();
		right.add(form);
		right.setCellHeight(form, "100%");
		mainWrapper.add(right);
	}


	@SuppressWarnings("unchecked")
	public HasValue<ProcessBase> getOwnerForm(){
		return (HasValue<ProcessBase>) ownerForm;
	}


	public abstract void setParentHeaderTitle(String title);

	
	@Override
	public HasEditableValue<ExternalInfoRequest> getForm(){
		return form;
	}

	@Override
	public void disableToolbar() {
		//TODO
	}
	@Override
	public void setToolbarSaveMode(boolean b) {
		//TODO
	}
	@Override
	public void allowEdit(boolean b) {
		//TODO
	}

}
