package module6;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for gun violence on a map
 * 
 * @author Qingyang Li
 *
 */

public class GunMarker extends CommonMarker
{
	// The casualties of the gun violence incident marker
	// You will want to set this in the constructor
	
	protected float casualties;
	
	// constants for distance
	protected static final float kmPerMile = 1.6f;
	
	// ADD constants for colors


	// abstract method implemented in derived classes
	public void drawGun(PGraphics pg, float x, float y)
	{
		//pg.fill(8);
		pg.ellipse(x, y, 5, 5);
		
	}
		
	
	// constructor
	public GunMarker (Feature feature) 
	{
		super(((PointFeature) feature).getLocation(), feature.getProperties());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float numKilled = Float.parseFloat(properties.get("numKilled").toString());
		float numInjured= Float.parseFloat(properties.get("numInjured").toString());
		properties.put("casualties", numKilled + numInjured);
		setProperties(properties);
		this.casualties = getCasualty();
		
	}
	
	// TODO: Add the method:
	// this will sort the object in ascending order.
	public int compareTo(GunMarker marker) {
		int diff;
		if (this.getCasualty() > marker.getCasualty()) {
			diff = 1;
			
		} else if (this.getCasualty() < marker.getCasualty()) {
			diff = -1;
			
		} else {
			diff = 0;
		}
		return diff;
	}
	
	
	// calls abstract method drawEarthquake and then checks age and draws X if needed
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		// determine color of marker from depth
		colorDetermine(pg);
		
		// call abstract method implemented in child class to draw marker shape
		drawGun(pg, x, y);
		
		// IMPLEMENT: add X over marker if within past day		
		
		// reset to previous styling
		pg.popStyle();
		
	}

	/** Show the title of the Gun Marker if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		String title = getTitle();
		pg.pushStyle();
		
		pg.rectMode(PConstants.CORNER);
		
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);
		
		
		pg.popStyle();
		
	}

	
	
	// determine color of marker from depth
	// We use: Deep = red, intermediate = blue, shallow = yellow
	private void colorDetermine(PGraphics pg) {
		float numKilled = getNumKilled();
		float numInjured = getNumInjured();
		
		if (numKilled + numInjured == 0) {
			pg.fill(0, 0, 255);// blue no casualty
			
		}
		else if (numKilled == 0) {
			pg.fill(255, 255, 0); // yellow, someone is injured, but no one is killed
		}
		else {
			pg.fill(255, 0, 0); // someone is killed
		}
	}
	
	
	/** toString
	 * Returns an earthquake marker's string representation
	 * @return the string representation of an earthquake marker.
	 */
	public String toString()
	{
		return getTitle();
	}
	/*
	 * getters for earthquake properties
	 */
	
	public float getNumKilled() {
		return Float.parseFloat(getProperty("numKilled").toString());
	}
	
	public float getNumInjured() {
		return Float.parseFloat(getProperty("numInjured").toString());	
	}
	
	public String getTitle() {
		return (String) getProperty("title");	
		
	}
	
	public float getCasualty() {
		return Float.parseFloat(getProperty("casualties").toString());
	}
	

	
	
}
