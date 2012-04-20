package bigBang.module.expenseModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.expenseModule.client.dataAccess.ExpenseSearchDataBroker;
import bigBang.module.expenseModule.client.resources.Resources;
import bigBang.module.expenseModule.shared.ExpenseSortParameter;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExpenseStub;

import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;

public class ExpenseSearchPanel extends SearchPanel<ExpenseStub> {

	public static class Entry extends ListEntry<ExpenseStub>{

		protected Label numberLabel;
		protected Label clientLabel;
		protected Label policyCoverageLabel;
		protected Label objectLabel;
		protected Image statusIcon;
		protected Label dateLabel;
		protected Label valueLabel;

		
		public Entry(ExpenseStub value) {
			super(value);
			setHeight("55px");
			this.titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}
		
		public <I extends Object> void setInfo(I info){
			ExpenseStub value = (ExpenseStub)info;
			
			if(value.id != null){
				if(numberLabel == null){
					numberLabel = getFormatedLabel();
					numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
					numberLabel.setWordWrap(false);
					clientLabel = getFormatedLabel();
					clientLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					policyCoverageLabel = getFormatedLabel();
					policyCoverageLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					objectLabel = getFormatedLabel();
					objectLabel.getElement().getStyle().setFontSize(10, Unit.PX);
					VerticalPanel container = new VerticalPanel();
					container.setSize("100%", "100%");
					
					container.add(numberLabel);
					container.add(clientLabel);
					container.add(policyCoverageLabel);
					
					setWidget(container);
					
					HorizontalPanel rightWrapper = new HorizontalPanel();
					VerticalPanel rightContainer = new VerticalPanel();
					rightContainer.setSize("100%", "100%");
					dateLabel = getFormatedLabel();
					rightContainer.add(dateLabel);
					rightContainer.setCellVerticalAlignment(dateLabel, HasVerticalAlignment.ALIGN_TOP);
					statusIcon = new Image();
					statusIcon.setTitle(value.isOpen ? "Aberta" : "Fechada");
					rightContainer.add(statusIcon);
					rightContainer.setCellVerticalAlignment(statusIcon, HasVerticalAlignment.ALIGN_MIDDLE);
					rightWrapper.add(rightContainer);
					VerticalPanel iconContainer = new VerticalPanel();
					valueLabel = getFormatedLabel();
					iconContainer.add(valueLabel);
					iconContainer.setCellVerticalAlignment(valueLabel, HasVerticalAlignment.ALIGN_BOTTOM);
					
					rightWrapper.add(rightContainer);
					rightWrapper.add(iconContainer);
					setRightWidget(rightWrapper);
					
				}
				
				numberLabel.setText("#" + value.number);
				clientLabel.setText("Cliente #" + value.clientNumber + "-" + value.clientName);
				policyCoverageLabel.setText("Apólice #" + value.referenceNumber + "-" + " / " + value.coverageName);
				policyCoverageLabel.setTitle("Apólice / Cobertura");
				objectLabel.setText(value.insuredObjectName);
				
				dateLabel.setText(value.expenseDate);
				Resources resources = GWT.create(Resources.class);
				statusIcon.setResource(value.isOpen ? resources.active() : resources.inactive());
				valueLabel.setText(value.value + "€");
			}
			
			
		}
	
		@Override
		public void setSelected(boolean selected, boolean b) {
			super.setSelected(selected, b);
			if(numberLabel != null){
				if(selected){
					numberLabel.getElement().getStyle().setColor("white");
					policyCoverageLabel.getElement().getStyle().setColor("white");
					objectLabel.getElement().getStyle().setColor("white");
					clientLabel.getElement().getStyle().setColor("white");
					dateLabel.getElement().getStyle().setColor("white");
					valueLabel.getElement().getStyle().setColor("white");
				}
				else{
					dateLabel.getElement().getStyle().setColor("#0066FF");
					objectLabel.getElement().getStyle().setColor("#0066FF");
					policyCoverageLabel.getElement().getStyle().setColor("gray");
					clientLabel.getElement().getStyle().setColor("gray");
				}
			}
		}
		
	}
	
	protected static enum Filters{
		EXPENSE_DATE_FROM,
		EXPENSE_DATE_TO,
	}
	
	protected int dataVersion;
	protected FiltersPanel filtersPanel;
	protected Map<String, ExpenseStub> expensesToUpdate;
	protected Map<String, Void> expensesToRemove;
	
	public ExpenseSearchPanel() {
		super((ExpenseSearchDataBroker) ((ExpenseDataBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE)).getSearchBroker());
		expensesToUpdate = new HashMap<String, ExpenseStub>();
		expensesToRemove = new HashMap<String, Void>();
		
		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(ExpenseSortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(ExpenseSortParameter.SortableField.NUMBER, "Número");
		sortOptions.put(ExpenseSortParameter.SortableField.DATE, "Data da Despesa");
		
		filtersPanel = new FiltersPanel(sortOptions);
		//filtersPanel.addTypifiedListField(Filters.CLIENT, BigBangConstants.EntityIds.CLIENT, "Cliente");
		//TODO
		
	}
	
	@Override
	public void doSearch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(Collection<ExpenseStub> results) {
		// TODO Auto-generated method stub
		
	}

}
