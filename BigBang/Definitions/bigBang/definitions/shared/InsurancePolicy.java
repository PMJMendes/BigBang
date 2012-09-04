package bigBang.definitions.shared;

import java.io.Serializable;

public class InsurancePolicy
	extends InsurancePolicyStub
{
	private static final long serialVersionUID = 1L;

	public static class CoInsurer
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
	
		public String insuranceAgencyId;
		public Double percent;
	}

	public static class Coverage
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String coverageId;
		public String coverageName;
		public boolean mandatory;
		public int order;
		public Boolean presentInPolicy;
	}

	public static class ColumnHeader
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String label;
		public FieldType type;
		public String unitsLabel;
		public String refersToId;
	}

	public String managerId;
	public String insuranceAgencyId;
	public String startDate;
	public String durationId;
	public String fractioningId;
	public Integer maturityDay;
	public Integer maturityMonth;
	public String expirationDate;
	public String notes;
	public String mediatorId;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public Double premium;
	public String operationalProfileId;
	public String docushare;

	public CoInsurer[] coInsurers;

	public Contact[] contacts;
	public Document[] documents;

	public Coverage[] coverages;
	public ColumnHeader[] columns;

	public Object2[] objects;

	public boolean hasExercises;

	public InsurancePolicy()
	{
		contacts = new Contact[0];
		documents = new Document[0];
		headerFields = new HeaderField[0];
		columnFields = new ColumnField[0];
		extraFields = new ExtraField[0];
		exerciseData = new ExerciseData[0];
		coverages = new Coverage[0];
		columns = new ColumnHeader[0];
		objects = new Object2[0];
	}
}
