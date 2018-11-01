import java.io.FileWriter;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.Set;



public class Database {
	String nombreArchivo;
	Set<String> keys;
	public Database(String nombre) {
		nombreArchivo= nombre;
		/*keys = new HashSet<String>(); //fix this
		keys.add("id");
		keys.add(" rut ");
		keys.add("-");
		keys.add(" puntos ");*/
	}
	
	public void comparar(Nodo nodo, String llave) {
		Set<String> keysNodo= nodo.getKeys();
		Set<String> keysThisNodo= this.keys;
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
	
	public static int getIndex(Set<String> set, Object value) {
		   int result = 0;
		   for (String entry:set) {
		     if (entry.equals(value)) break;
		     result++;
		   }
		   return result;
	}
	
	//TODO
	public void ordenar(String attr) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, AlreadyBoundException {
		for (String attri:this.keys) {
			System.out.println(attri);
		}
		int ind = Database.getIndex(this.keys, attr);
		String attr_ind = Integer.toString(ind);
		String[] args = {attr_ind, nombreArchivo}; 
		ExternalMergeSort.main(args);
	}
	
	public void agregar(Nodo nodo) throws IOException {
		FileWriter fw = new FileWriter(nombreArchivo, true);
		keys= nodo.getKeys();
		
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
