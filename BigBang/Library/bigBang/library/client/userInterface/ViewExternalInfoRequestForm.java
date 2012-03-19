package bigBang.library.client.userInterface;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.userInterface.view.FormView;

public class ViewExternalInfoRequestForm extends FormView<ExternalInfoRequest> {

	private TextBoxFormField subject;
	private TextBoxFormField from;
	private TextBoxFormField replyLimit;
	
	public ViewExternalInfoRequestForm(){
		subject = new TextBoxFormField("Assunto");
		from = new TextBoxFormField("De");
		replyLimit = new TextBoxFormField("Prazo de Resposta (dias)");
		replyLimit.setFieldWidth("72px");
		
		subject.setEditable(false);
		from.setEditable(false);
		replyLimit.setEditable(false);
		
		addSection("Informação do Pedido Externo");
		addFormField(from, true);
		addFormField(replyLimit, false);
		addFormField(subject);
		
	}
	
	@Override
	public ExternalInfoRequest getInfo() {
		ExternalInfoRequest result = getValue();
		if(result != null) {
			result.subject = subject.getValue();
			int limit = -1;
			try{
				limit = Integer.parseInt(replyLimit.getValue());
			}catch (NumberFormatException e) {
				//
			}
			result.replylimit = limit;
			result.originalFrom = from.getValue();
		}
		return result;
	}

	@Override
	public void setInfo(ExternalInfoRequest info) {
		if(info == null){
			clearInfo();
		} else {
			subject.setValue(info.subject);
			from.setValue(info.originalFrom);
			replyLimit.setValue(info.replylimit + "");
		}
	}

}
