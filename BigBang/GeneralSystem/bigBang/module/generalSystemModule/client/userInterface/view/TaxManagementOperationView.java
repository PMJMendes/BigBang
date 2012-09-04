package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.TaxList;
import bigBang.module.generalSystemModule.client.userInterface.TaxList.Entry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaxManagementOperationView extends View implements TaxManagementOperationViewPresenter.Display {


	private static final int LIST_WIDTH = 400; //PX

	@Override
	public FilterableList<Line> getLineList() {
		return lineList;
	}

	@Override
	public FilterableList<SubLine> getSubLineList() {
		return subLineList;
	}

	@Override
	public FilterableList<Coverage> getCoverageList() {
		return coverageList;
	}

	@Override
	public NavigationPanel getNavPanel() {
		return navPanel;
	}

	@Override
	public TaxList getTaxList() {
		return taxList;
	}


	@Override
	public TaxForm getForm() {
		return form;
	}

	private FilterableList<Line> lineList;
	private FilterableList<SubLine> subLineList;
	private FilterableList<Coverage> coverageList;

	private boolean readOnly = false;

	private NavigationPanel navPanel;
	private PopupPanel popup;

	private TaxList taxList;
	private TaxForm form;

	private ActionInvokedEventHandler<Action> handler;
	private BigBangOperationsToolBar toolbar;
	private MenuItem delete;
	private VerticalPanel popupWrapper;

	public TaxManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		navPanel = new NavigationPanel(){

			@Override
			public boolean navigateBack() {
				taxList.clear();
				return super.navigateBack();
			}


		};


		this.form = new TaxForm();
		this.popup = new PopupPanel();

		toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT_TAX);

			}

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE_TAX);

			}

			@Override
			public void onCancelRequest() {
				fireAction(Action.CANCEL_EDIT_TAX);

			}

		};

		toolbar.hideAll();
		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				fireAction(Action.DELETE_TAX);

			}
		});

		toolbar.addItem(SUB_MENU.ADMIN, delete);
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);

		popupWrapper = new VerticalPanel();
		popupWrapper.setSize("100%", "100%");
		toolbar.setWidth("100%");
		popupWrapper.add(toolbar);
		popupWrapper.add(form.getNonScrollableContent());
		popupWrapper.setCellHeight(form.getNonScrollableContent(),"100%");
		popup.add(popupWrapper);


		navPanel.setSize("100%", "100%");

		lineList = new FilterableList<Line>() {
			protected void onAttach() {
				fireAction(Action.LINE_LIST_ATTACH);
				super.onAttach();
			};
		};
		lineList.setSize("100%", "100%");
		lineList.showFilterField(false);

		subLineList = new FilterableList<SubLine>() {
			protected void onAttach() {
				fireAction(Action.SUB_LINE_LIST_ATTACH);
				super.onAttach();
			};
		};
		subLineList.setSize("100%", "100%");
		subLineList.showFilterField(false);
		coverageList = new FilterableList<Coverage>() {
			protected void onAttach() {
				fireAction(Action.COVERAGE_LIST_ATTACH);
				super.onAttach();
			};

			protected void onUnload() {
				taxList.getNewButton().setEnabled(false);
				super.onUnload();
			};
		};
		coverageList.showFilterField(false);
		coverageList.setSize("100%", "100%");

		navPanel.setHomeWidget(lineList);
		wrapper.addWest(navPanel, LIST_WIDTH);

		taxList = new TaxList();
		taxList.getNewButton().setEnabled(false);

		wrapper.add(taxList);
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}


	@Override
	public void setLines(Line[] lines) {
		this.navPanel.navigateToFirst();
		this.lineList.clear();

		for(int i = 0; i < lines.length; i++){
			Line line = lines[i];
			ListEntry<Line> lineEntry = (line.subLines == null || line.subLines.length == 0)
					? new ListEntry<Line>(line) : new NavigationListEntry<Line>(line);
					lineEntry.setTitle(line.name);
					lineEntry.setText(line.categoryName);
					lineEntry.setHeight("40px");
					this.lineList.add(lineEntry);
					if(line.subLines == null || line.subLines.length == 0){
						lineEntry.setSelectable(false);
						//lineEntry.setRightWidget(new Label("sem modalidades"));
					}
		}
	}

	public void setSubLines(SubLine[] subLines) {
		this.subLineList.clear();

		for(int j = 0; j < subLines.length; j++) {
			SubLine subLine = subLines[j];
			ListEntry<SubLine> subLineEntry = (subLine.coverages == null || subLine.coverages.length == 0)
					? new ListEntry<SubLine>(subLine) : new NavigationListEntry<SubLine>(subLine);
					subLineEntry.setTitle(subLine.name);
					this.subLineList.add(subLineEntry);
					if(subLine.coverages == null || subLine.coverages.length == 0){
						subLineEntry.setSelectable(false);
					}
		}
	}

	public void setCoverages(Coverage[] coverages) {
		this.coverageList.clear();

		for(int k = 0; k < coverages.length; k++) {
			Coverage coverage = coverages[k];
			ListEntry<Coverage> coverageEntry = new ListEntry<Coverage>(coverage);
			coverageEntry.setTitle(coverage.name);
			this.coverageList.add(coverageEntry);
		}
	}

	@Override
	public void setTaxes(Tax[] taxes) {
		this.taxList.clear();
		for(int k = 0; k < taxes.length; k++) {
			Tax tax = taxes[k];
			taxList.add(new Entry(tax));
		}
	}


	@Override
	public HasClickHandlers getNewButton() {
		return taxList.getNewButton();
	}

	@Override
	public HasValue<Tax> getTaxForm() {
		return form;
	}

	@Override
	public String getCurrentCoverageId() {
		Coverage c = getCurrentCoverage();
		return c == null ? null : c.id; 
	}

	private Coverage getCurrentCoverage(){
		for(ValueSelectable<Coverage> c : this.coverageList.getSelected())
			return c.getValue();
				return null; 
	}


	@Override
	public void lockForm(boolean lock) {
		this.form.setReadOnly(lock);
	}


	@Override
	public void removeTaxFromList(Tax tax) {
		for(ValueSelectable<Tax> s : this.taxList) {
			if(s.getValue().id.equals(tax.id))
				taxList.remove(s);
		}
	}


	@Override
	public void updateTaxInList(Tax tax) {
		for(ValueSelectable<Tax> s : this.taxList) {
			if(s.getValue().id.equals(tax.id))
				s.setValue(tax);
		}
	}

	@Override
	public void clear(){
		this.coverageList.clear();
		this.coverageList.clearFilters();
		this.form.clearInfo();
		this.lineList.clear();
		this.lineList.clearFilters();
		this.subLineList.clear();
		this.subLineList.clearFilters();
		this.taxList.clear();
		this.taxList.clearFilters();
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
		taxList.registerActionHandler(handler);
	}

	protected void fireAction(Action action){
		if(this.handler != null) {
			handler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}




	@Override
	public void showForm(boolean b) {
		if(!b){
			popup.hidePopup();
			return;
		}
		popup.center();
	}

	@Override
	public String getClickedTax() {
		return taxList.getClickedTax();
	}

	@Override
	public BigBangOperationsToolBar getToolBar() {
		return toolbar;
	}


}