package info.akosarev.cianrobot;

public class Coordinate {
	 private double doubleLat;
	 private double doubleLon;

	 Coordinate (double lon,double lat){
		 doubleLat = lat;
		 doubleLon = lon;
	 }

	 public double getDoubleLat() {
		return doubleLat;
	 }

	 public void setDoubleLat(double doubleLat) {
		this.doubleLat = doubleLat;
	 }
	 public double getDoubleLon() {
		 return doubleLon;
	 }
	 public void setDoubleLon(double doubleLon) {
		 this.doubleLon = doubleLon;
	 }

}
