package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedTextBroker;
import bigBang.library.client.dataAccess.TypifiedTextClient;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ExpandableListBoxFormField.ManagementPanelType;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TypifiedTextFormField extends FormField<TypifiedText> implements TypifiedTextClient{

	private TypifiedTextBroker broker;
	private ExpandableListBoxFormField labels = new ExpandableListBoxFormField(null,"Etiqueta", ManagementPanelType.TYPIFIED_TEXT);
	private TextBoxFormField subject = new TextBoxFormField("Assunto");
	private RichTextAreaFormField textBody = new RichTextAreaFormField();
	private VerticalPanel wrapper = new VerticalPanel();
	private HorizontalPanel labelPanel = new HorizontalPanel();
	private String tag;

	public TypifiedTextFormField(){

		broker = (TypifiedTextBroker)DataBrokerManager.staticGetBroker(BigBangConstants.TypifiedListIds.TYPIFIED_TEXT);
		labels.addValueChangeHandler(new ValueChangeHandler<String>() {

			public void onValueChange(ValueChangeEvent<String> event) {

				String id = event.getValue();
				if(id.length() == 0){
					clear();
					return;
				}

				broker.getText(tag, id, new ResponseHandler<TypifiedText>() {

					@Override
					public void onResponse(TypifiedText response) {
						subject.setValue(response.subject);
						textBody.setValue(response.text);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar o texto pedido."), TYPE.ALERT_NOTIFICATION));
					}
				});



			}
		});
		initWidget(wrapper);

		labelPanel.add(labels);
		labels.setWidth("100%");

		wrapper.add(labelPanel);
		subject.setWidth("100%");
		subject.setFieldWidth("100%");
		wrapper.add(subject);

		textBody.setLabelWidth("0px");
		textBody.setFieldWidth("100%");
		wrapper.add(textBody);
		setReadOnly(false);
	}

	public void setTypifiedTexts(String tag){
		if(this.tag != null){
			broker.unregisterClient(this.tag, this);
		}
		tag = tag != null && tag.isEmpty() ? null : "@" + tag;
		this.tag = tag;
		if(this.tag != null){
			broker.registerClient(tag, this);
			labels.setListId(BigBangConstants.TypifiedListIds.TYPIFIED_TEXT+"/"+tag, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}
	
	@Override
	public void setValue(TypifiedText value, boolean fireEvents) {
		this.subject.setValue(value.subject);
		this.textBody.setValue(value.text);
		if(fireEvents)
			ValueChangeEvent.fire(this, value);
	}
	
	@Override
	public TypifiedText getValue() {
		TypifiedText result = new TypifiedText();
		result.subject = this.subject.getValue();
		result.text = this.textBody.getValue();
		return result;
	}

	@Override
	public void clear() {
		subject.clear();
		textBody.clear();
	}

	@Override
	public void setReadOnly(boolean readonly) {
		subject.setReadOnly(readonly);
		textBody.setReadOnly(readonly);
		labels.setReadOnly(readonly);
		
	}

	@Override
	public boolean isReadOnly() {
		return textBody.isReadOnly() || subject.isReadOnly();
	}

	@Override
	public void setLabelWidth(String width) {
		return;
	}

	@Override
	public void setTypifiedTexts(List<TypifiedText> texts) {
		return;
	}


	@Override
	public void removeText(TypifiedText text) {
		return;
	}


	@Override
	public void addText(TypifiedText text) {
		return;
	}


	@Override
	public void updateText(TypifiedText text) {
		return;
	}

	@Override
	public int getTypifiedTextDataVersionNumber() {
		return 0;
	}


	@Override
	public void setTypifiedTextDataVersionNumber(int number) {
		return;
	}
}
