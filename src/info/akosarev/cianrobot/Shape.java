package info.akosarev.cianrobot;

public class Shape {
	private String shape;

	private String name;
	private Coordinate metro;

	public Shape(String shape, String name, Coordinate metro) {
		this.setShape(shape);
		this.setName(name);
		this.setMetro(metro);
	}
	
	@Override
	public String toString() {
		return shape;
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

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

}
