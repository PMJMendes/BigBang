package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicy.TableSection.TableField;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.Type;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.insurancePolicyModule.client.dataAccess.PolicyTypifiedListBroker;

public class InsurancePolicySubLineTableDataSection extends FormViewSection {

	protected InsurancePolicy policy;

	protected PolicyFormTable table;
	protected TableSection currentTableSection;
	protected ExpandableListBoxFormField insuredObjectsList, exercisesList;

	public InsurancePolicySubLineTableDataSection() {
		super("Coberturas");
		insuredObjectsList = new ExpandableListBoxFormField("Unidades de Risco");
		exercisesList = new ExpandableListBoxFormField("Exercícios");

		ValueChangeHandler<String> valueHandler = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getSource() == insuredObjectsList) {
					onInsuredObjectFilterChanged(event.getValue());
				}else if(event.getSource() == exercisesList){
					onExerciseFilterChanged(event.getValue());
				}
			}
		};
		insuredObjectsList.addValueChangeHandler(valueHandler);
		exercisesList.addValueChangeHandler(valueHandler);

		addFormField(insuredObjectsList, true);
		addFormField(exercisesList, false);
		setTableData(null, null);
	}

	public void setTableData(InsurancePolicy policy, TableSection tableSection){
		if(policy == null){
			clearTableData();
		}else{
			if(this.table != null) {
				this.table.removeFromParent();
			}

			Coverage[] coverageHeaders = policy.coverages;
			this.table = new PolicyFormTable();
			addWidget(this.table);

			this.table.setHeaders(coverageHeaders, policy.columns);
			setTableData(tableSection);
		}
	}

	public void setTableData(TableSection tableSection){
		if(tableSection == null) {
			clearTableData();
		}else{
			for(TableField tableField : tableSection.data) {
				Field realTableField = new Field();
				realTableField.id = tableField.fieldId;
				realTableField.value = tableField.value;

				ColumnHeader column = policy.columns[tableField.columnIndex];
				
				switch(column.type) {
				case TEXT:
					realTableField.type = Type.TEXT;
					break;
				case BOOLEAN:
					realTableField.type = Type.BOOLEAN;
					break;
				case DATE:
					realTableField.type = Type.DATE;
					break;
				case LIST:
					realTableField.type = Type.LIST;
					break;
				case NUMERIC:
					realTableField.type = Type.NUMERIC;
					break;
				case REFERENCE:
					realTableField.type = Type.REFERENCE;
					realTableField.reference = column.refersToId;
					break;
				}

				table.setValue(tableField.coverageId, tableField.columnIndex+"", realTableField);
			}
		}
		this.currentTableSection = tableSection;
		if(this.table != null) {
			this.table.setReadOnly(this.readOnly);
			this.table.render();
		}
	}

	public TableSection getCurrentTableSection() {
		TableSection result = this.currentTableSection;

		if(result != null) {
			for(TableField tableField : this.currentTableSection.data) {
				Field realTableField = table.getValue(tableField.coverageId, tableField.columnIndex+"");
				tableField.value = realTableField.value;
			}
		}

		return result;
	}

	public void clearTableData(){
		if(this.table != null) {
			this.table.removeFromParent();
		}
		this.table = null;
	}

	public Coverage[] getCoverages(){
		onInsuredObjectFilterChanged(insuredObjectsList.getValue());
		return this.table != null ? this.table.getCoverages() : null;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);

		if(this.table != null) {
			this.table.setReadOnly(readOnly);
		}

		this.insuredObjectsList.setReadOnly(false);
		this.exercisesList.setReadOnly(false);
	}

	public void setInsurancePolicy(final InsurancePolicy policy){
		this.policy = policy;
		if(policy == null) {
			clearTableData();
			return;
		}

		InsurancePolicyBroker broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);

		if(broker.isTemp(policy.id)) {
			PolicyTypifiedListBroker insurancePolicyTypifiedListBroker = PolicyTypifiedListBroker.Util.getInstance();
			this.insuredObjectsList.setTypifiedDataBroker((TypifiedListBroker) insurancePolicyTypifiedListBroker);
			this.insuredObjectsList.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECT + "/" + broker.getEffectiveId(policy.id), new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
			this.exercisesList.setTypifiedDataBroker((TypifiedListBroker) insurancePolicyTypifiedListBroker);
			this.exercisesList.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_EXERCISES + "/" + broker.getEffectiveId(policy.id), new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}else{
			this.insuredObjectsList.setTypifiedDataBroker(BigBangTypifiedListBroker.Util.getInstance());
			this.insuredObjectsList.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECT + "/" + policy.id, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
			this.exercisesList.setTypifiedDataBroker(BigBangTypifiedListBroker.Util.getInstance());
			this.exercisesList.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_EXERCISES + "/" + policy.id, new ResponseHandler<Void>() {

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
		setTableData(policy, policy.tableData == null ? null : policy.tableData.length < 1 ? null : policy.tableData[0]);
	}

	protected void onInsuredObjectFilterChanged(String newValue){
		if(this.table != null && this.policy.tableData != null && this.policy.tableData.length > 0) {
			newValue = newValue == null ? null : newValue.isEmpty() ? null : newValue;
			InsurancePolicyBroker broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);

			if(broker.isTemp(policy.id)) {
				broker.saveCoverageDetailsPage(policy.id, insuredObjectsList.getValue(), exercisesList.getValue(), getCurrentTableSection(), new ResponseHandler<TableSection>() {

					@Override
					public void onResponse(TableSection response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}

			broker.openCoverageDetailsPage(policy.id, newValue, exercisesList.getValue(), new ResponseHandler<TableSection>() {

				@Override
				public void onResponse(TableSection response) {
					table.clearValues();
					setTableData(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}

			});
		}
	}

	protected void onExerciseFilterChanged(String newValue){
		if(this.table != null) {
			newValue = newValue == null ? null : newValue.isEmpty() ? null : newValue;
			InsurancePolicyBroker broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);

			if(broker.isTemp(policy.id)) {
				broker.saveCoverageDetailsPage(policy.id, insuredObjectsList.getValue(), exercisesList.getValue(), getCurrentTableSection(), new ResponseHandler<TableSection>() {

					@Override
					public void onResponse(TableSection response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}

			broker.openCoverageDetailsPage(policy.id, insuredObjectsList.getValue(), newValue, new ResponseHandler<TableSection>() {

				@Override
				public void onResponse(TableSection response) {
					table.clearValues();
					setTableData(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}

			});
		}
	}

}
