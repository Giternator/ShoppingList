package scott.macewan.shoppinglist;

import java.util.List;

public class Recipe {
	
	private int id;
	private String name;
	private List<Item> ingredients;
		
	public Recipe(){
		
	}
	
	public Recipe(String name){
		this.name = name;
	}
	
	public Recipe(String name, List<Item> ingredients){
		this.name = name;
		this.ingredients = ingredients;
	}
	
	public void setName(String name){
		this.name= name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setIngredients(List<Item> ingredients){
		this.ingredients = ingredients;
	}
	
	public List<Item> getIngredients(){
		return ingredients;
	}
	
	public int getID(){
		return id;
	}
	
	public void addIngredient(Item item){
		ingredients.add(item);		
	}

}
