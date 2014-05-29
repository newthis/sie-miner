package sie.db.entity;

public class PackageMetric extends Metric {
	public PackageMetric(double pValue, String pAcronym, SourceContainer pSourceContainer) {
		this.value = pValue;
		this.sourceContainer = pSourceContainer;
		this.acronym = pAcronym;
	}

	/**
	 * @return the acronym
	 */
	public String getAcronym() {
		return acronym;
	}

	/**
	 * @param acronym
	 *            the acronym to set
	 */
	public void setAcronimo(String acronym) {
		this.acronym = acronym;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackageMetric other = (PackageMetric) obj;
		if (sourceContainer == null) {
			if (other.sourceContainer != null)
				return false;
		} else if (!sourceContainer.equals(other.sourceContainer))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

	private double value;
	private SourceContainer sourceContainer;
	private String acronym;
}
