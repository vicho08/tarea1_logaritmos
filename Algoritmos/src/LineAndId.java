

/**
 * A utility class to store a string and a number.
 * 
 * @author Aidan
 *
 */
public class LineAndId implements Comparable<LineAndId> {
	private final String line;
	private final int id;
	private final int attr_ind;
	
	public LineAndId(String str, int num, int ind){
		this.line = str;
		this.id = num;
		this.attr_ind = ind;
	}
	
	@Override
	public int compareTo(LineAndId swm) {
		String[] line_split = line.split(";"); 
		String[] swm_split = swm.line.split(";");
		
		String line_attr_val = line_split[attr_ind];
		String swm_attr_val = swm_split[attr_ind];
		int comp = line_attr_val.compareTo(swm_attr_val);
		
		if(comp!=0)
			return comp;
		
		//si valores son iguales, ordeno por id de archivo
		return id - swm.id;
	}
	
	public boolean equals(Object o){
		if(o==null) return false;
		
		if(o==this) return true;
		
		if(!(o instanceof LineAndId)) return false;
		
		LineAndId swn = (LineAndId)o;
		
		return line.equals(swn.line) && id == swn.id;
	}
	
	public int hashCode(){
		return line.hashCode() + id;
	}
	
	public String getString() {
		return line;
	}

	public int getNumber() {
		return id;
	}
}
