import java.util.Hashtable;
import java.util.Set;

public class Nodo {
	Hashtable<String, String> nodo= new Hashtable<String,  String>();
	
	public Nodo(String [] args){
		
		int count = args.length;
		int i=0;
		//mientras queden args, añade llave+valor al diccionario
		while (i<count) {
			nodo.put(args[i], args[i+1]);
			i=i+2;
		}	
	}	
	
	public Set<String> getKeys() {
		Set<String> keys = nodo.keySet();
		return keys;
	}
	
	public String getValue(String key ) {
		String value= nodo.get(key);
		return value;
	}
}
