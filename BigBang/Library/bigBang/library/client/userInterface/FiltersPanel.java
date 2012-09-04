package bigBang.library.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.SortOrder;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.view.View;

public class FiltersPanel extends View {

	protected Button applyFiltersButton, clearFiltersButton;
	protected ListBox sortListBox;
	protected ListBox sortOrderList;
	protected VerticalPanel filtersWrapper;
	protected Map<Enum<?>, HasValue<?>> filters;
	protected Map<Enum<?>, String> sortableOptions;

	public FiltersPanel(Map<Enum<?>, String> sorts){
		filters = new HashMap<Enum<?>, HasValue<?>>();
		sortableOptions = sorts;

		VerticalPanel wrapper = new VerticalPanel();

		ScrollPanel mainWrapper = new ScrollPanel(wrapper);
		initWidget(mainWrapper);

		mainWrapper.setSize("100%", "100%");
		mainWrapper.getElement().getStyle().setMarginBottom(10, Unit.PX);

		wrapper.setSize("100%", "100%");

		clearFiltersButton = new Button("Limpar filtros");
		applyFiltersButton = new Button("Aplicar");

		ClickHandler filtersButtonsCH = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(event.getSource() == clearFiltersButton)
					clearFilters();
				else if(event.getSource() == applyFiltersButton)
					applyFilters();
			}
		};

		clearFiltersButton.addClickHandler(filtersButtonsCH);
		applyFiltersButton.addClickHandler(filtersButtonsCH);

		wrapper.add(new Label("Ordenar por:"));

		sortListBox = new ListBox();
		sortListBox.setWidth("200px");

		for(Enum<?> key : sorts.keySet()){
			sortListBox.addItem(sorts.get(key), key.toString());
		}

		sortOrderList = new ListBox();
		sortOrderList.addItem("Descendente", "DESC");
		sortOrderList.addItem("Ascendente", "ASC");

		HorizontalPanel sortFieldsWrapper = new HorizontalPanel();
		sortFieldsWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		sortFieldsWrapper.setSpacing(5);
		sortFieldsWrapper.add(sortListBox);
		sortFieldsWrapper.add(sortOrderList);

		wrapper.add(sortFieldsWrapper);

		wrapper.add(new Label("Filtrar por:"));

		filtersWrapper = new VerticalPanel();
		filtersWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		filtersWrapper.setSpacing(5);
		wrapper.add(filtersWrapper);
		wrapper.setCellHeight(filtersWrapper, "100%");

		HorizontalPanel buttonsWrapper = new HorizontalPanel();
		buttonsWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttonsWrapper.setSpacing(5);
		buttonsWrapper.add(clearFiltersButton);
		buttonsWrapper.add(applyFiltersButton);

		wrapper.add(buttonsWrapper);
	}

	@Override
	protected void initializeView() {
		return;
	}

	public Enum<?> getSelectedSortableField(){
		String strValue = this.sortListBox.getValue(this.sortListBox.getSelectedIndex());
		for(Enum<?> key : sortableOptions.keySet()){
			if(key.toString().equals(strValue))
				return key;
		}
		return null;
	}

	public HasClickHandlers getApplyButton(){
		return this.applyFiltersButton;
	}

	public HasClickHandlers getClearButton(){
		return this.clearFiltersButton;
	}

	public void addTextField(Enum<?> id, String description){
		TextBoxFormField field = new TextBoxFormField(description);
		field.setFieldWidth("200px");
		filters.put(id, field);
		filtersWrapper.add(field);
	}

	public void addTypifiedListField(Enum<?> id, String listId, String description){
		addTypifiedListField(id, listId, description, null);
	}

	public void addTypifiedListField(Enum<?> id, final String listId, String description, Enum<?> dependencyId) {
		final ExpandableListBoxFormField field = dependencyId != null ?  new ExpandableListBoxFormField(description) : new ExpandableListBoxFormField(listId, description);
		if(dependencyId != null) {
			final ExpandableListBoxFormField dependencyField = (ExpandableListBoxFormField) this.filters.get(dependencyId);
			dependencyField.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					if(event.getValue() == null || event.getValue().isEmpty()){
						field.clearValues();
					}else{
						field.setListId(listId+"/"+event.getValue(), null);
					}
				}
			});
		}
		field.setFieldWidth("200px");
		filters.put(id, field);
		filtersWrapper.add(field);
	} 

	public void addDateField(Enum<?> id, String description) {
		DatePickerFormField field = new DatePickerFormField(description);
		field.setFieldWidth("200px");
		filters.put(id, field);
		filtersWrapper.add(field);
	}

	public void addCheckBoxField(Enum<?> id, String description){
		CheckBoxFormField field = new CheckBoxFormField(description);
		filters.put(id, field);
		filtersWrapper.add(field);
	}

	public Object getFilterValue(Enum<?> id){
		Object value = this.filters.get(id).getValue();
		if(value instanceof String && value.equals("")){
			value = null;
		}
		return value;
	}

	public void setFilterValue(Enum<?> id, Object value){
		HasValue<?> v = this.filters.get(id);
			if(v instanceof TextBoxFormField)
				((TextBoxFormField)v).setValue((String)value);
			
			else if(v instanceof DatePickerFormField)
				((DatePickerFormField)v).setValue((String)value);
			
			else if(v instanceof ExpandableListBoxFormField)
				((ExpandableListBoxFormField)v).setValue((String)value);
			
			else if(v instanceof CheckBoxFormField)
				((CheckBoxFormField)v).setValue((Boolean)value);
		
	}

	public SortOrder getSortingOrder(){
		return this.sortOrderList.getValue(this.sortOrderList.getSelectedIndex()).equals("DESC") ? SortOrder.DESC : SortOrder.ASC;
	}

	public void applyFilters(){
		//TODO
	}

	public void clearFilters(){
		for(HasValue<?> v : this.filters.values()){
			v.setValue(null, true);
		}
	}

	public void setFilterVisible(Enum<?> id, boolean b) {
		FormField<?> field =  (FormField<?>) this.filters.get(id);
		
		field.setVisible(b);
		
	}

	public boolean isFilterVisible(Enum<?> id) {
		FormField<?> field =  (FormField<?>) this.filters.get(id);
		
		return field.isVisible();
	}

}
