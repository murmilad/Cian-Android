package info.akosarev.cianrobot;

public class Metro {

	private String name;
	private Coordinate metro;

	public Metro(String name, Coordinate metro) {
		this.setName(name);
		this.setMetro(metro);
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


}
