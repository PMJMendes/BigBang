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
}
