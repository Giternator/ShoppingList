package scott.macewan.shoppinglist;

public class Category {
	private int id;
	private String name;
	
	public Category(){
		
	}
	
	public Category(String name){
		this.name = name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}

}
