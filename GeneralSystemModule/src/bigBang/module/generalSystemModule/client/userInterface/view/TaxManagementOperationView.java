package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.TaxList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;
import bigBang.module.generalSystemModule.shared.Tax;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class TaxManagementOperationView extends View implements TaxManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //PX
	
	private FilterableList<Line> lineList;
	private FilterableList<SubLine> subLineList;
	private FilterableList<Coverage> coverageList;
	
	private boolean readOnly = false;
	
	private NavigationPanel navPanel;
	private PopupPanel popup;
	
	private TaxList taxList;
	private TaxForm form;
	
	public TaxManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		navPanel = new NavigationPanel();
		navPanel.setSize("100%", "100%");
		
		lineList = new FilterableList<Line>() {
			protected void onAttach() {
				ListHeader header = new ListHeader();
				header.setText("Ramos");
				header.setHeight("25px");
				setHeaderWidget(header);
				clearSelection();
				super.onAttach();
			};
		};
		lineList.setSize("100%", "100%");
		
		subLineList = new FilterableList<SubLine>() {
			protected void onAttach() {
				ListHeader header = new ListHeader();
				header.setText("Modalidades");
				header.setHeight("25px");
				setHeaderWidget(header);
				clearSelection();
				super.onAttach();
			};
		};
		subLineList.setSize("100%", "100%");
		
		subLineList.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					SubLine subLine = ((ValueSelectable<SubLine>) s).getValue();
					setCoverages(subLine.coverages);
					navPanel.navigateTo(coverageList);
				}
			}
		});
		
		coverageList = new FilterableList<Coverage>() {
			protected void onAttach() {
				ListHeader header = new ListHeader();
				header.setText("Coberturas");
				header.setHeight("25px");
				setHeaderWidget(header);
				clearSelection();
				if(!readOnly)
					taxList.getNewButton().setEnabled(true);
				super.onAttach();
			};
			
			protected void onUnload() {
				taxList.getNewButton().setEnabled(false);
				super.onUnload();
			};
		};
		coverageList.setSize("100%", "100%");
		
		lineList.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					Line line = ((ValueSelectable<Line>) s).getValue();
					setSubLines(line.subLines);
					navPanel.navigateTo(subLineList);
				}
			}
		});
		
		coverageList.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					Coverage coverage = ((ValueSelectable<Coverage>) s).getValue();
					setTaxes(coverage.taxes);
				}
			}
		});
		
		navPanel.setHomeWidget(lineList);
		wrapper.addWest(navPanel, LIST_WIDTH);
		
		form = new TaxForm();
		taxList = new TaxList();
		taxList.getNewButton().setEnabled(false);

		popup = new PopupPanel("Imposto/Coeficiente");
		popup.add(form.getNonScrollableContent());
		popup.setWidth("650px");
		
		wrapper.add(taxList);
		initWidget(wrapper);
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
			this.lineList.add(lineEntry);
			if(line.subLines == null || line.subLines.length == 0){
				lineEntry.setSelectable(false);
				//lineEntry.setRightWidget(new Label("sem modalidades"));
			}
		}
	}
	
	private void setSubLines(SubLine[] subLines) {
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
	
	private void setCoverages(Coverage[] coverages) {
		this.coverageList.clear();
		
		for(int k = 0; k < coverages.length; k++) {
			Coverage coverage = coverages[k];
			ListEntry<Coverage> coverageEntry = new ListEntry<Coverage>(coverage);
			coverageEntry.setTitle(coverage.name);
			this.coverageList.add(coverageEntry);
		}
	}
	
	private void setTaxes(Tax[] taxes) {
		this.taxList.clear();
		for(int k = 0; k < taxes.length; k++) {
			Tax tax = taxes[k];
			addTaxToList(tax, false);
		}
	}

	@Override
	public void showForm(boolean show) {
		if(!show){
			this.popup.hidePopup();
			this.form.clearInfo();
		}else{
			this.form.clearInfo();
			this.popup.center();
		}
	}


	@Override
	public HasClickHandlers getNewButton() {
		return taxList.getNewButton();
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return form.getSaveButton();
	}


	@Override
	public HasClickHandlers getDeleteButton() {
		return form.getDeleteButton();
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
	public void addTaxToList(Tax tax){
		addTaxToList(tax, true);
	}
	

	public void addTaxToList(Tax tax, boolean insertInArray) {
		ListEntry<Tax> taxEntry = new ListEntry<Tax>(tax){
			public <I extends Object> void setInfo(I info) {
				Tax tax = (Tax) info;
				setTitle(tax.name);
			};
			
			@Override
			public void setSelected(boolean selected, boolean fireEvents) {
				super.setSelected(selected, fireEvents);
				Resources r = GWT.create(Resources.class);
				setLeftWidget(new Image(selected ? r.listEditIconSmallWhite() : r.listEditIconSmallBlack()));
			}
		};
		
		taxEntry.setTitle(tax.name);
		this.taxList.add(taxEntry);
		if(insertInArray){
			Coverage c = this.getCurrentCoverage();
			Tax[] newArray = new Tax[c.taxes.length + 1];
			for(int i = 0; i < c.taxes.length; i++){
				newArray[i] = c.taxes[i];
			}
			newArray[c.taxes.length == 0 ? 0 : c.taxes.length - 1] = tax;
			c.taxes = newArray;
		}
		taxList.getScrollable().scrollToBottom();
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
	
}