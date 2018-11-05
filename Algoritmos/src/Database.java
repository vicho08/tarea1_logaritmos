import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class Database {
	String nombreArchivo;
	List<String> keys;
	public Database(String nombre, String[] db_keys) {
		nombreArchivo= nombre;
		keys = new LinkedList<String>();
		for (String key:db_keys) {
			keys.add(key);
		}
	}
	
	public void comparar(Nodo nodo, String llave) {
		Set<String> keysNodo= nodo.getKeys();
		List<String> keysThisNodo= this.keys;
		for(String key1: keysThisNodo){
			if (key1.equals(llave)){
				for (String key2: keysNodo) {
					if (key2.equals(llave)){
						key1.compareTo(key2);
					}
				}
			}
		}
	}
	
	public int getIndex(List<String> set, Object value) {
		   int result = 0;
		   Iterator<String> iter = set.iterator();
		   while (iter.hasNext()) {
			   if (iter.next().equals(value)) break;
			   result++;
		   }
		   return result;
	}
	
	//TODO
	public void ordenar(String attr) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, AlreadyBoundException {
		int ind = getIndex(this.keys, attr);
		System.out.println(ind);
		String attr_ind = Integer.toString(ind);
		String[] args = {attr_ind, nombreArchivo}; 
		ExternalMergeSort.main(args);
	}
	
	public void agregar(Nodo nodo) throws IOException {
		FileWriter fw = new FileWriter(nombreArchivo, true);
		
		//Por cada llave, añadimos su valor al archivo:ej: 1;19136938-6;1000 \n
		//(en formato csv)

		int n=0;
		for(String key: keys){
			String value= nodo.getValue(key);
			if (n==0){
				fw.append(value);
			}
			else {
				fw.append(';');
				fw.append(value);
			}
			++n;
		}
		fw.append('\n');
		fw.close();
		 
	}
	
}
