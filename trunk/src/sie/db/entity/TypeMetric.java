package sie.db.entity;

public class TypeMetric extends Metric{
	public TypeMetric(double value,String acronimo,SType type){
		this.value=value;
		this.type=type;
		this.acronimo=acronimo;
	}
	
	public TypeMetric() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}

	/**
	 * @param acronimo the acronimo to set
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
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
		TypeMetric other = (TypeMetric) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}


	private int id;
	private double value;
	private SType type;
	private String acronimo;
}
