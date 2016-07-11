package info.akosarev.cianrobot;

public class Metro {

	private String name;
	private Coordinate metro;
	private Boolean isActiveForSearch;

	public Metro(String name, Coordinate metro, Boolean isActiveForSearch) {
		this.setName(name);
		this.setMetro(metro);
		this.setIsActiveForSearch(isActiveForSearch);
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Coordinate getMetro() {
		return metro;
	}

	public void setMetro(Coordinate metro) {
		this.metro = metro;
	}

	public Boolean getIsActiveForSearch() {
		return isActiveForSearch;
	}

	public void setIsActiveForSearch(Boolean isActiveForSearch) {
		this.isActiveForSearch = isActiveForSearch;
	}


}
