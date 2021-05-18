package engine;

import java.util.ArrayList;

import GUI.DrawableArrow;

public class Line {
	public ArrayList<Line> sidelines;
	public ArrayList<Turn> turns;
	public ArrayList<DrawableArrow> da;
	
	public Line() {
		sidelines = new  ArrayList<Line>();
		turns = new ArrayList<Turn>();
		da = new ArrayList<DrawableArrow>();
	}
	
	public Line(ArrayList<Turn> originalLine) {
		sidelines = new  ArrayList<Line>();
		turns = originalLine;
		da = new ArrayList<DrawableArrow>();
	}
}
