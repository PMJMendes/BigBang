package bigBang.definitions.shared;

import java.io.Serializable;

public class SubCasualty
	extends SubCasualtyStub
{
	private static final long serialVersionUID = 1L;

	public static class SubCasualtyItem
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String id; //Campo auxiliar para ajudar os serviços, manter inalterado
		public String coverageId; //Lista tipificada das coberturas da (sub)apólice
		public String damageTypeId; //Lista tipificada, campo obrigatório
		public Double damages; //Em moeda
		public Double settlement; //Em moeda
		public Double deductible; //Unidades desconhecidas
		public Double value; //Em moeda
		public boolean isManual;
		public String injuryCauseId; //Lista tipificada das causas de lesão
		public String injuryTypeId; //Lista tipificada dos tipos de lesão
		public String injuredPartId; //Lista tipificada das partes de corpo atingidas
		public boolean isThirdParty; //Indica se a indemnização foi paga a terceiros
		public String notes;

		public boolean deleted;

	}
	
	public static class SubCasualtyInsurerRequest implements Serializable {
	
		private static final long serialVersionUID = 1L;

		public String id; //TODO: fica por analogia com SubCasualtyItem. A remover se não necessário
		public String insurerRequestTypeId; // Request type
		public String requestDate;  // the date the insurer made the request
		public String acceptanceDate; // The date the request was received
		public boolean conforms; // Conformity flag
		public String resendDate; // date the request was sent to the insurer, if conformity = true
		public String clarificationDate; // the date a clarification was requested, if conformity = false 
	
		public boolean deleted;

	}

	public String managerId;

	public String inheritCasualtyNumber;
	public String inheritInsurerId;
	public String inheritInsurerName;
	public String inheritMasterClientId;
	public String inheritMasterClientNumber;
	public String inheritMasterClientName;
	public String inheritMasterMediatorId;
	public String inheritMasterMediatorName;

	public String insuredObjectId; //Lista tipificada dos objectos de apólice *ou* de sub-apólice, filtrada pelo ID da respectiva
	public String insuredObjectName;
	public SubCasualtyItem[] items; //Zero ou mais items
	public boolean hasJudicial;
	public String text;
	public String internalNotes;
	public String serviceCenterId;
	
	public SubCasualtyInsurerRequest[] insurerRequests;  // The array with the insurer requests
}
