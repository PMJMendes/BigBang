package bigBang.definitions.shared;

import java.io.Serializable;

public class SubCasualty extends SubCasualtyStub {
	private static final long serialVersionUID = 1L;

	public static class SubCasualtyItem implements Serializable {
		private static final long serialVersionUID = 1L;

		public String id; // Campo auxiliar para ajudar os serviços, manter
							// inalterado
		public String coverageId; // Lista tipificada das coberturas da
									// (sub)apólice
		public String damageTypeId; // Lista tipificada, campo obrigatório
		public Double damages; // Em moeda
		public Double settlement; // Em moeda
		public Double deductible; // Unidades desconhecidas
		public Double value; // Em moeda
		public boolean isManual;
		public String injuryCauseId; // Lista tipificada das causas de lesão
		public String injuryTypeId; // Lista tipificada dos tipos de lesão
		public String injuredPartId; // Lista tipificada das partes de corpo
										// atingidas
		public boolean isThirdParty; // Indica se a indemnização foi paga a
										// terceiros
		public String notes;

		public boolean deleted;

	}

	public static class SubCasualtyInsurerRequest implements Serializable {

		private static final long serialVersionUID = 1L;

		public String id; // TODO: fica por analogia com SubCasualtyItem. A
							// remover se não necessário
		public String insurerRequestTypeId; // Request type
		public String requestDate; // the date the insurer made the request
		public String acceptanceDate; // The date the request was received
		public boolean conforms; // Conformity flag
		public String resendDate; // date the request was sent to the insurer,
									// if conformity = true
		public String clarificationDate; // the date a clarification was
											// requested, if conformity = false
		public String clarificationTypeId; // Clarification type

		public boolean deleted;

	}

	public static class SubCasualtyFraming implements Serializable {

		private static final long serialVersionUID = 1L;

		public static class SubCasualtyFramingEntity implements Serializable {
			private static final long serialVersionUID = 1L;

			public String id;
			public String entityTypeId; // The type of the entity
			public String evaluationId; // The entity's evaluation
			public String evaluationNotes; // Notes on the evaluation

			public boolean deleted;
		}
		
		public static class SubCasualtyFramingHeadings implements Serializable {
			private static final long serialVersionUID = 1L;

			public String id;
			public Double baseSalary; // The type of the entity
			public Double feedAllowance; // The entity's evaluation
			public Double otherFees12; // Notes on the evaluation
			public Double otherFees14; // Notes on the evaluation
			
			public boolean deleted;
		}

		public String id;
		public String analysisDate; // the date on which the framing was created
		public boolean framingDifficulty; // was there any issue with this
											// framing?
		public boolean validPolicy; // is the policy valid?
		public String validityNotes; // notes on the policy's validity
		public boolean generalExclusions; // are there any applicable general
											// exclusions
		public String generalExclusionNotes; // notes on general exclusions
		public boolean relevantCoverages; // are there any relevant coverages
		public String coverageRelevancyNotes; // notes on coverage relevancy
		public Double coverageValue; // the value of the associated coverage
		public boolean coverageExclusions; // are there any applicable coverage
											// exclusions
		public String coverageExclusionsNotes; // notes on coverages exclusions
		public Double franchise; // the value for the franchise
		public String deductibleTypeId; // the type (unit) of the franchise
		public String franchiseNotes; // notes on the franchise
		public String insurerEvaluationId; // The id corresponding to a textual
											// grade for the insurer
		public String insurerEvaluationNotes; // notes on the insurer evaluation
		public String expertEvaluationId; // The id corresponding to a textual
											// grade for the expert
		public String expertEvaluationNotes; // notes on the expert evaluation
		public String coverageNotes; // notes about the coverage value
		
		public boolean declinedCasualty; // Indicator of whether the casualty was declined
		public String declinedCasualtyNotes; // Notes about the fact that the casualty was declined
		
		public boolean declinedWarning; // Indicator of whether the client was warned by Crédite-EGS that the casualty cound be declined
		public String declinedWarningNotes; // Why there was not the perception there could be a decline

		public SubCasualtyFramingEntity[] framingEntities; // The array with adicional
													// entities related with
													// this framing
		
		public SubCasualtyFramingHeadings headings;
		
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

	public String insuredObjectId; // Lista tipificada dos objectos de apólice
									// *ou* de sub-apólice, filtrada pelo ID da
									// respectiva
	public String insuredObjectName;
	public SubCasualtyItem[] items; // Zero ou mais items
	public boolean hasJudicial;
	public String text;
	public String internalNotes;
	public String serviceCenterId;

	public SubCasualtyInsurerRequest[] insurerRequests; // The array with the
														// insurer requests

	public SubCasualtyFraming framing; // Framing info
}
