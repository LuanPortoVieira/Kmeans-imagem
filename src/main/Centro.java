package main;

import java.awt.Color;
import java.util.ArrayList;

public class Centro {
	double r,g,b;
	ArrayList<Pixel> pixels = new ArrayList<>();
	Color color;
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	public double getG() {
		return g;
	}
	public void setG(double g) {
		this.g = g;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public ArrayList<Pixel> getPixels() {
		return pixels;
	}
	public void setPixels(ArrayList<Pixel> pixels) {
		this.pixels = pixels;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	
	
}
