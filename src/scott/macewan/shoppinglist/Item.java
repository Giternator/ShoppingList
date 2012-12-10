package scott.macewan.shoppinglist;

public class Item {
	private int id;
	private int category_id;
	private String name;
	
	public Item(){
		
	}
	
	public Item(int id, int category_id, String name){
		this.id = id;
		this.category_id = category_id;
		this.name = name;
	}
	
	public Item(int category_id, String name){
		this.category_id = category_id;
		this.name = name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getCategoryId(){
		return this.category_id;
	}
	
	public void setCategoryId(int category_id){
		this.category_id = category_id;
	}
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	

}
