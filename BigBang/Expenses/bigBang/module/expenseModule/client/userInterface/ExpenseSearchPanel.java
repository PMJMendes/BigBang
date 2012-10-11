package bigBang.module.expenseModule.client.userInterface;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.expenseModule.client.dataAccess.ExpenseSearchDataBroker;
import bigBang.module.expenseModule.client.resources.Resources;
import bigBang.module.expenseModule.shared.ExpenseSearchParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter.SortableField;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.ExpenseDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;

public class ExpenseSearchPanel extends SearchPanel<ExpenseStub> implements ExpenseDataBrokerClient{

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
			setHeight("70px");
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
					clientLabel.getElement().getStyle().setProperty("whiteSpace", "");
					clientLabel.setHeight("1.2em");
					policyCoverageLabel = getFormatedLabel();
					policyCoverageLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					policyCoverageLabel.getElement().getStyle().setProperty("whiteSpace", "");
					policyCoverageLabel.setHeight("1.2em");
					objectLabel = getFormatedLabel();
					objectLabel.getElement().getStyle().setFontSize(10, Unit.PX);
					VerticalPanel container = new VerticalPanel();
					container.setSize("100%", "100%");

					container.add(numberLabel);
					container.add(clientLabel);
					container.add(policyCoverageLabel);
					container.add(objectLabel);

					setWidget(container);

					HorizontalPanel rightWrapper = new HorizontalPanel();
					VerticalPanel valuesContainer = new VerticalPanel();
					valuesContainer.setSize("100%", "100%");
					dateLabel = getFormatedLabel();
					valuesContainer.add(dateLabel);
					valuesContainer.setCellVerticalAlignment(dateLabel, HasVerticalAlignment.ALIGN_TOP);
					valueLabel = getFormatedLabel();
					valuesContainer.setHeight("50px");
					valuesContainer.add(valueLabel);
					valuesContainer.setCellVerticalAlignment(valueLabel, HasVerticalAlignment.ALIGN_BOTTOM);
					valuesContainer.setCellHorizontalAlignment(valueLabel, HasHorizontalAlignment.ALIGN_RIGHT);
					valuesContainer.setCellHorizontalAlignment(dateLabel, HasHorizontalAlignment.ALIGN_RIGHT);
					
					statusIcon = new Image();
					statusIcon.setTitle(value.isOpen ? "Aberta" : "Fechada");
					VerticalPanel statusContainer = new VerticalPanel();
					statusContainer.add(statusIcon);
					statusContainer.setHeight("50px");
					
					statusContainer.setCellVerticalAlignment(statusIcon, HasVerticalAlignment.ALIGN_MIDDLE);
					rightWrapper.add(valuesContainer);
					rightWrapper.add(statusContainer);
					setRightWidget(rightWrapper);

				}

				numberLabel.setText("#" + value.number);
				clientLabel.setText("Cliente #" + value.clientNumber + "-" + value.clientName);
				policyCoverageLabel.setText("Apólice #" + value.referenceNumber + (value.coverageName !=null ?  "- / " + value.coverageName : ""));
				policyCoverageLabel.setTitle("Apólice / Cobertura");
				objectLabel.setText(value.insuredObjectName == null ? "-" : value.insuredObjectName);

				dateLabel.setText(value.expenseDate);
				Resources resources = GWT.create(Resources.class);
				statusIcon.setResource(value.isOpen ? resources.active() : resources.inactive());
				valueLabel.setText(value.value + "€");
			}

			setMetaData(new String[]{
					value.number,
					value.referenceNumber,
					value.coverageName,
					value.clientNumber,
					value.clientName
				});

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
					objectLabel.getElement().getStyle().setColor("gray");
					policyCoverageLabel.getElement().getStyle().setColor("#0066FF");
					clientLabel.getElement().getStyle().setColor("black");
					valueLabel.getElement().getStyle().setColor("black");
					numberLabel.getElement().getStyle().setColor("black");
				}
			}
		}
		
		

	}

	protected static enum Filters{
		INSURER,
		MANAGER,
		EXPENSE_DATE_FROM,
		EXPENSE_DATE_TO,
		INSURED_OBJECT
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
		filtersPanel.addTypifiedListField(Filters.INSURER, BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor");
		filtersPanel.addDateField(Filters.EXPENSE_DATE_FROM, "De");
		filtersPanel.addDateField(Filters.EXPENSE_DATE_TO, "Até");
		filtersPanel.addTextField(Filters.INSURED_OBJECT, "Unidade de Risco");

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch(false);
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);

		((ExpenseDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.EXPENSE)).registerClient(this);

	}

	@Override
	public void doSearch(boolean keepState) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}

		SearchParameter[] parameters = new SearchParameter[1];
		ExpenseSearchParameter p = new ExpenseSearchParameter();

		p.freeText = this.getFreeText();

		Date dateFrom = (Date) filtersPanel.getFilterValue(Filters.EXPENSE_DATE_FROM);
		p.dateFrom = dateFrom == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(dateFrom);
		Date dateTo = (Date) filtersPanel.getFilterValue(Filters.EXPENSE_DATE_TO);
		p.dateTo = dateTo == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(dateTo);
		p.insurerId = (String) filtersPanel.getFilterValue(Filters.INSURER);
		p.managerId = (String) filtersPanel.getFilterValue(Filters.MANAGER);
		p.insuredObject = (String) filtersPanel.getFilterValue(Filters.INSURED_OBJECT);
	
		parameters[0] = p;
		
		SortParameter[] sorts = new SortParameter[]{
				new ExpenseSortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
		};
		
		doSearch(parameters, sorts, keepState);
	}

	@Override
	public void onResults(Collection<ExpenseStub> results) {
		for(ExpenseStub s : results){
			if(!this.expensesToRemove.containsKey(s.id)){
				if(this.expensesToUpdate.containsKey(s.id)){
					s = this.expensesToUpdate.get(s.id);
				}
				addSearchResult(s);
			}
		}
	}

	private Entry addSearchResult(SearchResult r) {
		Entry entry = null;
		if(r instanceof ExpenseStub){
			entry = new Entry((ExpenseStub)r);
			add(entry);
		}
		return entry;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		dataVersion = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return dataVersion;
	}

	@Override
	public void addExpense(Expense expense) {
		ExpenseStub newExpense = expense;
		
		if(!this.expensesToRemove.containsKey(expense.id)){
			if(this.expensesToUpdate.containsKey(expense.id)){
				newExpense = this.expensesToUpdate.get(expense.id);
			}
			Entry entry = new Entry(newExpense);
			add(0, entry);
		}
	}

	@Override
	public void updateExpense(Expense expense) {
		for(ValueSelectable<ExpenseStub> vs : this){
			if(vs.getValue().id.equals(expense.id)){
				vs.setValue(expense);
				return;
			}
		}
		this.expensesToUpdate.put(expense.id, null);
	}

	@Override
	public void deleteExpense(String id) {
		for(ValueSelectable<ExpenseStub> vs : this){
			if(vs.getValue().id.equals(id)){
				remove(vs);
				return;
			}
		}
		this.expensesToRemove.put(id, null);
	}

}
