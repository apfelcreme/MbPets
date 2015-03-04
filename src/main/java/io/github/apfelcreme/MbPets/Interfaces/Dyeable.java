package io.github.apfelcreme.MbPets.Interfaces;

public interface Dyeable<Type> {

	/**
	 * returns the color of the pet
	 * @return
	 */
	public Type getColor();
	
	/**
	 * sets the color
	 * @param color
	 */
	public void setColor(Type color);
	
	
}
