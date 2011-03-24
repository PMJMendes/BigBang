package bigBang.library.client.userInterface;
import bigBang.library.client.event.CheckedStateChangedEvent;
import bigBang.library.client.event.CheckedStateChangedEventHandler;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListEntry<T> extends View implements HasValue <T> {


	private T value;
	private boolean even = false;
	private boolean isSelected = false;
	
	private final String defaultBackgroundImageUrl = ""; //images/listBackground1Even.png";
	private final String selectedBackgroundImageUrl = "images/listBackground1Selected.png";
	private final String hoverBackgroundImageUrl = "images/listBackground1Hover.png";
	
	private Image backgroundImage;
	
	
	private Label titleLabel;
	private Label textLabel;
	private HasWidgets leftWidgetContainer;
	private HasWidgets widgetContainer;
	private HasWidgets rightWidgetContainer;
	private CheckBox checkBox;
	
	
	public ListEntry(T value) {
		AbsolutePanel panel = new AbsolutePanel();
		this.widgetContainer = new SimplePanel();
		((Widget)this.widgetContainer).setSize("100%", "100%");
		panel.setSize("100%", "30px");
		panel.setStylePrimaryName("listItem");
		
		backgroundImage = new Image();
		backgroundImage.setSize("100%", "100%");
		
		panel.add(backgroundImage, 0, 0);
		
		HorizontalPanel contentWrapper = new HorizontalPanel();
		contentWrapper.setSize("100%", "100%");
		contentWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		contentWrapper.setSpacing(5);
		
		this.leftWidgetContainer = new SimplePanel();
		contentWrapper.add((Widget) this.leftWidgetContainer);
		
		VerticalPanel textWrapper = new VerticalPanel();
		textWrapper.setSize("100%", "100%");
		textWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		this.titleLabel = new Label();
		this.titleLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		this.titleLabel.getElement().getStyle().setFontSize(14, Unit.PX);
		textWrapper.add(this.titleLabel);
		textWrapper.setCellHeight(this.titleLabel, "100%");
	
		this.textLabel = new Label();
		this.textLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		this.textLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		this.textLabel.setWordWrap(true);
		textWrapper.add(this.textLabel);		
		
		widgetContainer.add(textWrapper);
		
		contentWrapper.add((Widget)widgetContainer);
		contentWrapper.setCellWidth((Widget)widgetContainer, "100%");
		
		this.rightWidgetContainer = new SimplePanel();
		contentWrapper.add((Widget) this.rightWidgetContainer);
		
		panel.add(contentWrapper, 0 , 0);
		
		initWidget(panel);
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		this.setSelected(false);
		
		this.checkBox = new CheckBox();
		this.checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setCheckable(event.getValue());
			}
		});
		
		setValue(value);
	}
	
	public <I extends Object> void setInfo(I info){}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		setValue(value, true);
	}

	public void setValue(T value, boolean fireEvents) {
		this.value = value;
	}
	
	public void setSelected(boolean selected) {
		if(selected){
			backgroundImage.setUrl(selectedBackgroundImageUrl);
			backgroundImage.setVisible(true);
			this.addStyleName("listItem-selected");
			this.removeStyleName("listItemEven");
			this.isSelected = true;
		}else{
			backgroundImage.setVisible(false); //setUrl(defaultBackgroundImageUrl);
			this.removeStyleName("listItem-selected");
			this.addStyleName("listItemEven");
			this.isSelected = false;
		}
	}
	
	public boolean isSelected(){
		return this.isSelected;
	}
	
	public void setTitle(String title){
		if(title == null){
			this.titleLabel.setText("");
			this.titleLabel.setVisible(false);
			return;
		}
		
		this.titleLabel.setText(title);
		this.titleLabel.setVisible(true);
	}
	
	public String getTitle(){
		return this.titleLabel.isVisible() ? getText() : null;
	}
	
	public void setText(String text) {
		if(text == null){
			this.textLabel.setText("");
			this.textLabel.setVisible(false);
			return;
		}
		
		this.textLabel.setText(text);
		this.textLabel.setVisible(true);
	}
	
	public String getText() {
		return this.textLabel.isVisible() ? getText() : null;
	}
	
	public void setWidget(Widget w){
		widgetContainer.clear();
		widgetContainer.add(w);
	}
	
	public Widget getWidget() {
		return null;
	}
	
	public void setLeftWidget(Widget w){
		if(w == null)
			return;
		this.leftWidgetContainer.clear();
		this.leftWidgetContainer.add(w);
	}
	
	public Widget getLeftWidget(){
		return ((Widget)this.leftWidgetContainer);
	}
	
	public void setRightWidget(Widget w){
		if(w == null)
			return;
		this.rightWidgetContainer.clear();
		this.rightWidgetContainer.add(w);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public HandlerRegistration addCheckedStateChangedHandler(
			CheckedStateChangedEventHandler handler) {
		return addHandler(handler, CheckedStateChangedEvent.TYPE);
	}

	public void setCheckable(boolean checkable) {
		setDoubleClickable(checkable);
		if(checkable) {
			this.setLeftWidget(this.checkBox);
			this.checkBox.setEnabled(true);
		}else{
			if(this.isCheckable()){
				this.leftWidgetContainer.clear();
				this.checkBox.setEnabled(false);
				this.checkBox.setValue(false);
			}
		}
	}
	
	public void setChecked(boolean checked) {
		setChecked(checked, true);
	}
	
	public void setChecked(boolean checked, boolean fireEvents) {
		if(checked != this.checkBox.getValue()){
			this.checkBox.setValue(checked);
			if(fireEvents)
				fireEvent(new CheckedStateChangedEvent(checked));
		}
	}
	
	public boolean isCheckable(){
		return this.checkBox != null && this.checkBox.isEnabled();
	}

	public boolean isChecked() {
		return this.isCheckable() && this.checkBox.getValue();
	}
	
	@Override
	public void onDoubleClick(Event event){
		super.onDoubleClick(event);
		if(this.isCheckable()){
			this.setChecked(!this.isChecked());
		}
	}
	
}
