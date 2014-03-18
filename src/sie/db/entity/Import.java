package sie.db.entity;

public abstract class Import {
	protected int id;
	protected String name;

	public Import(){
		
	}
	public Import(String nameSType) {
		name = nameSType;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}
}
