package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.library.client.userInterface.TwoKeyTable.Field;
import bigBang.library.client.userInterface.TwoKeyTable.Type;
import bigBang.library.client.userInterface.view.FormViewSection;

public class TableFieldsSection extends FormViewSection{

	PolicyFormTable table;
	
	public TableFieldsSection() {
		super("");
		table = new PolicyFormTable() {
			
			@Override
			public void onCoverageEnabled(String coverageId) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCoverageDisabled(String coverageId) {
				// TODO Auto-generated method stub
				
			}
		};
		addWidget(table);
				
	}
	
	public void setHeaders(Coverage[] coverages, ColumnHeader[] columns, ColumnField[] fields){
		table.setHeaders(coverages, columns);
//		setTableData(fields);
//		table.setReadOnly(false);
//		table.render();
	}

	private void setTableData(ColumnField[] fields) {
		for(int i = 0; i<fields.length; i++){
			
			Field realTableField = new Field();
			realTableField.id = fields[i].fieldId;
			realTableField.value = fields[i].value;
			
			switch(fields[i].type) {
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
				realTableField.reference = fields[i].refersToId;
				break;
			}

			table.putField(fields[i].coverageIndex, fields[i].columnIndex, realTableField);
		}
	}
	

}
