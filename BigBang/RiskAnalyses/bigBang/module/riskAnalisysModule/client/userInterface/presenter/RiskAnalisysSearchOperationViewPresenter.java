package bigBang.module.riskAnalisysModule.client.userInterface.presenter;

import bigBang.definitions.shared.RiskAnalysis;
import bigBang.definitions.shared.RiskAnalysisStub;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.RiskAnalisysSearchPanel;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class RiskAnalisysSearchOperationViewPresenter implements ViewPresenter {

	public interface Display {
		Widget asWidget();
		
		void addEntryToList(RiskAnalisysSearchPanel.Entry entry);

		HasValueSelectables<RiskAnalysisStub> getList();
	}

	protected Display view;
	protected boolean bound;

	public RiskAnalisysSearchOperationViewPresenter(Display view) {
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		//TODO 
		//TODO ENSURE LIST SELECTED
	}
	
	protected void ensureListedAndSelected(RiskAnalysis value) {
		boolean found = false;
		for(ValueSelectable<RiskAnalysisStub> stub : view.getList().getAll()){
			if(stub.getValue().id.equals(value.id)){
				found  = true;
				stub.setSelected(true, false);
			}
			else{
				stub.setSelected(false,false);
			}
		}
		
		if(!found){
			RiskAnalisysSearchPanel.Entry entry = new RiskAnalisysSearchPanel.Entry(value);
			view.addEntryToList(entry);
		}
	}
	
	public void bind() {
		if(bound)
			return;
		//APPLICATION-WIDE EVENTS
		bound = true;
	}
}
