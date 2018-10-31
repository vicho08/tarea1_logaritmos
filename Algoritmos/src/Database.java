import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;



public class Database {
	String nombreArchivo;
	Set<String> keys;
	public Database(String nombre) {
		nombreArchivo= nombre;
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
	//TODO
	public void ordenar(String attr) {

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
