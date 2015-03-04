package io.github.apfelcreme.MbPets.Interfaces;

public interface Styleable<Type> {
	
	/**
	 * returns the style of the pet
	 * @return
	 */
	public Type getStyle();
	
	/**
	 * sets the style
	 * @param color
	 */
	public void setStyle(Type style);
	
	
	
}
