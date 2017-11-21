package bigBang.module.receiptModule.client.userInterface;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.library.client.Session;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.receiptModule.client.resources.Resources;
import bigBang.module.receiptModule.shared.ModuleConstants;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter.SortableField;

public class ReceiptSearchPanel extends SearchPanel<ReceiptStub> implements ReceiptDataBrokerClient {
	
	public static class Entry extends ListEntry<ReceiptStub> {

		protected Label numberLabel;
		protected Label clientLabel;
		protected Label policyNumberLabel;
		protected Label lineLabel;
		protected Label premiumLabel;
		protected Label maturityDateLabel;
		protected Label descriptionLabel;
		protected Image statusIcon;
		protected boolean initialized;
		private NumberFormat nf;

		public Entry(ReceiptStub value) {
			super(value);
			setHeight("80px");
		}

		@Override
		public <I extends Object> void setInfo(I info) {
			if(!initialized){
				nf = NumberFormat.getFormat("#,##0.00");
				numberLabel = getFormatedLabel();
				numberLabel.setWordWrap(false);
				numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
				this.policyNumberLabel = getFormatedLabel();
				this.policyNumberLabel.getElement().getStyle().setFontSize(11, Unit.PX);
				this.policyNumberLabel.getElement().getStyle().setProperty("whiteSpace", "");
				this.policyNumberLabel.setHeight("1.2em");
				lineLabel = getFormatedLabel();
				this.lineLabel.getElement().getStyle().setFontSize(11, Unit.PX);
				this.lineLabel.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);
				lineLabel.getElement().getStyle().setProperty("whiteSpace", "");
				lineLabel.setHeight("1.2em");
				clientLabel = getFormatedLabel();
				this.clientLabel.getElement().getStyle().setFontSize(10, Unit.PX);
				clientLabel.getElement().getStyle().setProperty("whiteSpace", "");
				clientLabel.setHeight("1.2em");
				descriptionLabel = getFormatedLabel();
				descriptionLabel.getElement().getStyle().setProperty("whiteSpace", "");
				descriptionLabel.getElement().getStyle().setFontSize(10, Unit.PX);
				descriptionLabel.setHeight("1.2em");
				
				VerticalPanel container = new VerticalPanel();
				container.setSize("100%", "100%");
				container.add(numberLabel);
				container.setCellHeight(numberLabel, "100%");
				container.setCellVerticalAlignment(numberLabel, HasVerticalAlignment.ALIGN_TOP);
				container.add(policyNumberLabel);
				container.add(lineLabel);
				container.add(descriptionLabel);
				container.add(clientLabel);
				setWidget(container);

				VerticalPanel rightContainer = new VerticalPanel();
				rightContainer.setSize("100%", "100%");
				rightContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

				maturityDateLabel = getFormatedLabel();
				rightContainer.add(maturityDateLabel);
				rightContainer.setCellVerticalAlignment(maturityDateLabel, HasVerticalAlignment.ALIGN_TOP);

				premiumLabel = getFormatedLabel();
				this.premiumLabel.getElement().getStyle().setFontSize(12, Unit.PX);

				statusIcon = new Image();
				statusIcon.setTitle(value.statusText);
				
				rightContainer.add(statusIcon);
				rightContainer.setCellVerticalAlignment(statusIcon, HasVerticalAlignment.ALIGN_MIDDLE);
				
				rightContainer.add(premiumLabel);
				rightContainer.setCellVerticalAlignment(premiumLabel, HasVerticalAlignment.ALIGN_BOTTOM);

				((UIObject) rightWidgetContainer).setSize("100%", "100%");
				setRightWidget(rightContainer);
			}

			ReceiptStub r = (ReceiptStub) info;

			this.numberLabel.setText("#" + (r.number == null ? "" : r.number) + " (" + r.typeName + ")");
			this.numberLabel.setTitle("Número e tipo de Recibo");

			this.policyNumberLabel.setText("Apólice " + r.insurerName + " #"+ r.ownerNumber);;
			
			this.lineLabel.setText(r.categoryName + " / " + r.lineName + " / " + r.subLineName);
			this.lineLabel.setTitle("Categoria / Ramo / Modalidade");
			
			this.clientLabel.setText((r.clientNumber == null ? "" : "Cliente #" + r.clientNumber + " - ") +
										(r.clientName == null ? "" : r.clientName));
			this.clientLabel.setTitle("Cliente");
			
			this.descriptionLabel.setText(r.description == null ? "" : r.description);
			this.descriptionLabel.setTitle("Descrição");
			
			this.premiumLabel.setText(nf.format(r.totalPremium)+Session.getCurrency());
			this.premiumLabel.setTitle("Prémio total");
			this.maturityDateLabel.setText((r.maturityDate == null ? "" : r.maturityDate) + " / " + (r.endDate == null ? "" : r.endDate));
			this.maturityDateLabel.setTitle("Vigência");
			initialized = true;
			setSelected(this.isSelected(), false);
			
			Resources resources = GWT.create(Resources.class);
			
			switch(r.statusIcon){
			case GRAY:
				statusIcon.setResource(resources.greyIcon());
				break;
			case GREEN:
				statusIcon.setResource(resources.greenIcon());
				break;
			case ORANGE:
				statusIcon.setResource(resources.orangeIcon());
				break;
			case RED:
				statusIcon.setResource(resources.redIcon());
				break;
			case WHITE:
				statusIcon.setResource(resources.whiteIcon());
				break;
			case YELLOW:
				statusIcon.setResource(resources.yelloIcon());
				break;
			case ERROR:
				break;
			}
			
			setMetaData(new String[]{
					value.number,
					value.ownerNumber,
					value.categoryName,
					value.lineName,
					value.subLineName,
					value.description,
					value.clientNumber,
					value.clientName
				});
		};

		@Override
		public void setSelected(boolean selected, boolean b) {
			super.setSelected(selected, b);
			if(!initialized) {
				return;
			}
			if(selected){
				this.maturityDateLabel.getElement().getStyle().setColor("white");
				this.lineLabel.getElement().getStyle().setColor("white");
				this.policyNumberLabel.getElement().getStyle().setColor("white");
				this.clientLabel.getElement().getStyle().setColor("white");
			}else{
				this.maturityDateLabel.getElement().getStyle().setColor("#0066FF");
				this.clientLabel.getElement().getStyle().setColor("#0066FF");
				this.lineLabel.getElement().getStyle().setColor("gray");
				this.policyNumberLabel.getElement().getStyle().setColor("gray");
			}
		}
	}
	
	protected static enum Filters {
		BARCODE,
		AGENCY,
		TYPES,
		EMITED_FROM,
		EMITED_TO,
		MATURITY_FROM,
		MATURITY_TO,
		PAYMENT_FROM,
		PAYMENT_TO,
		CATEGORY,
		LINE,
		SUB_LINE, MANAGER, MEDIATOR,
		IS_INTERNAL
	}
	
	protected int dataVersion;
	protected FiltersPanel filtersPanel;
	protected Map<String, Receipt> receiptsToUpdate;
	protected Map<String, Void> receiptsToRemove;
	
	public ReceiptSearchPanel() {
		super(((ReceiptDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT)).getSearchBroker());
		receiptsToRemove = new HashMap<String, Void>();
		receiptsToUpdate = new HashMap<String, Receipt>();

		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>();
		sortOptions.put(ReceiptSortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(ReceiptSortParameter.SortableField.TYPE, "Tipo");
		sortOptions.put(ReceiptSortParameter.SortableField.NUMBER, "Número");
		sortOptions.put(ReceiptSortParameter.SortableField.CLIENT, "Cliente");
		sortOptions.put(ReceiptSortParameter.SortableField.SUB_LINE, "Ramo");
		sortOptions.put(ReceiptSortParameter.SortableField.EMISSION_DATE, "Data de Emissão");
		sortOptions.put(ReceiptSortParameter.SortableField.LIMIT_DATE, "Data Limite");
		sortOptions.put(ReceiptSortParameter.SortableField.MATURITY_DATE, "Vigência");
		sortOptions.put(ReceiptSortParameter.SortableField.PAYMENT_DATE, "Data de Pagamento");

		filtersPanel = new FiltersPanel(sortOptions);
		filtersPanel.addTextField(Filters.BARCODE, "Código Barras");
		filtersPanel.addTypifiedListField(Filters.AGENCY, BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor de Recibo");
		filtersPanel.addTypifiedListField(Filters.MEDIATOR, BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		filtersPanel.addTypifiedListField(Filters.TYPES, ModuleConstants.TypifiedListIds.RECEIPT_TYPE, "Tipos");
		filtersPanel.addDateField(Filters.EMITED_FROM, "Emitido de");
		filtersPanel.addDateField(Filters.EMITED_TO, "Até");
		filtersPanel.addDateField(Filters.MATURITY_FROM, "Vencimento de");
		filtersPanel.addDateField(Filters.MATURITY_TO, "Até");
		filtersPanel.addDateField(Filters.PAYMENT_FROM, "Pagamento de");
		filtersPanel.addDateField(Filters.PAYMENT_TO, "Até");
		filtersPanel.addTypifiedListField(Filters.CATEGORY, BigBangConstants.EntityIds.CATEGORY, "Categoria");
		filtersPanel.addTypifiedListField(Filters.LINE, BigBangConstants.EntityIds.LINE, "Ramo", Filters.CATEGORY);
		filtersPanel.addTypifiedListField(Filters.SUB_LINE, BigBangConstants.EntityIds.SUB_LINE, "Modalidade", Filters.LINE);
		filtersPanel.addCheckBoxField(Filters.IS_INTERNAL, "Só Notas de Débito");
		
		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doSearch(false);
			}
		});
		
		filtersContainer.clear();
		filtersContainer.add(filtersPanel);
		
		ReceiptDataBroker broker = (ReceiptDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT);
		broker.registerClient(this);
	}

	@Override
	public void doSearch(boolean keepState) {
		ReceiptSearchParameter parameter = new ReceiptSearchParameter();
		parameter.freeText = this.getFreeText();
		String type = (String) filtersPanel.getFilterValue(Filters.TYPES);
		parameter.typeIds = type == null ? new String[0] : new String[]{type};
		parameter.companyId = (String) filtersPanel.getFilterValue(Filters.AGENCY);
		parameter.managerId = (String) filtersPanel.getFilterValue(Filters.MANAGER);
		parameter.mediatorId = (String) filtersPanel.getFilterValue(Filters.MEDIATOR);
		Date emitedF = (Date) filtersPanel.getFilterValue(Filters.EMITED_FROM);
		parameter.emitedFrom = emitedF == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(emitedF);
		Date emitedT = (Date) filtersPanel.getFilterValue(Filters.EMITED_TO);
		parameter.emitedTo = emitedT == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(emitedT);
		Date maturityF = (Date) filtersPanel.getFilterValue(Filters.MATURITY_FROM);
		parameter.maturityFrom = maturityF == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(maturityF);
		Date maturityT = (Date) filtersPanel.getFilterValue(Filters.EMITED_TO);
		parameter.maturityTo = maturityT == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(maturityT); 
		Date paymentF =  (Date) filtersPanel.getFilterValue(Filters.PAYMENT_FROM);
		parameter.paymentFrom = paymentF == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(paymentF);
		Date paymentT = (Date) filtersPanel.getFilterValue(Filters.PAYMENT_TO);
		parameter.paymentTo = paymentT == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(paymentT);
		parameter.categoryId = (String) filtersPanel.getFilterValue(Filters.CATEGORY);
		parameter.lineId = (String) filtersPanel.getFilterValue(Filters.LINE);
		parameter.subLineId = (String) filtersPanel.getFilterValue(Filters.SUB_LINE);
		parameter.internalOnly = (Boolean) filtersPanel.getFilterValue(Filters.IS_INTERNAL);
		parameter.barcode = (String) filtersPanel.getFilterValue(Filters.BARCODE);

		SearchParameter[] parameters = new SearchParameter[] {
				parameter
		};
		
		ReceiptSortParameter sortParameter = new ReceiptSortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder());
		
		ReceiptSortParameter[] sorts = new ReceiptSortParameter[]{
				sortParameter
		};
		
		doSearch(parameters, sorts, keepState);
	}

	@Override
	public void onResults(Collection<ReceiptStub> results) {
		for(ReceiptStub s : results) {
			if(!receiptsToRemove.containsKey(s.id)){
				if(receiptsToUpdate.containsKey(s.id)){
					s = receiptsToUpdate.get(s.id);
				}
				addEntry(s);
			}
		}
	}
	
	public Entry addEntry(ReceiptStub receipt){
		Entry entry = null;
		if(receipt instanceof ReceiptStub) {
			entry = new Entry(receipt);
			add(entry);
		}
		return entry;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)) {
			this.dataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)) {
			return dataVersion;
		}
		return -1;
	}

	@Override
	public void addReceipt(Receipt receipt) {
		add(0, new Entry(receipt));
	}

	@Override
	public void updateReceipt(Receipt receipt) {
		for(ValueSelectable<ReceiptStub> s : this) {
			ReceiptStub receiptStub = s.getValue();
			if(receipt.id.equalsIgnoreCase(receiptStub.id)) {
				s.setValue(receipt);
				return;
			}
		}
		this.receiptsToUpdate.put(receipt.id, receipt);
	}

	@Override
	public void removeReceipt(String id) {
		for(ValueSelectable<ReceiptStub> s : this) {
			ReceiptStub receiptStub = s.getValue();
			if(id.equalsIgnoreCase(receiptStub.id)) {
				remove(s);
				return;
			}
		}
		this.receiptsToRemove.put(id, null);
	}

}
