import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


public class Btree {
	ArrayList<String[]> btArray; 
	String archivoRaiz;
	int size;
	String[] hojaArray=new String[3];
	String nombreArchivo;
	BufferedReader reader;
	Set<String> attr;
	String nombreArchivoNodo;
	int numArchivos;
	String extension;
	String archivoActual;
	long startAt = 0;

	public Btree(String nombre, Set<String> keys, int n) {
		nombreArchivo= nombre;
		size=n;
		attr= keys;
		btArray= new ArrayList<String[]>(size);
        startAt=0;
        reader=null;
        nombreArchivoNodo="archivo";
        numArchivos=1;
        extension=".txt";
        archivoActual="archivo1.txt";
        archivoRaiz = "archivo1.txt";
	}
	private static int getIndex(Set<String> set, Object value) {
		   int result = 0;
		   for (String entry:set) {
		     if (entry.equals(value)) break;
		     result++;
		   }
		   return result;
	}
	private static int getIndexLine(ArrayList<String[]> array, String[]linea) {
		int indice=-1;
		for (int i = 0; i < array.size(); i++) {
		    String[] array1 = array.get(i);
		    if (Arrays.equals(linea, array1)) {
		        indice = i;
		        break;
		    }
		}
		return indice;
}
	private void  saveArrayInFile(String nombreArchivo) throws IOException {
		   FileWriter fw = new FileWriter(nombreArchivo, false);   
			   for (int i=0;i<btArray.size();i++) {
				   	   fw.append(btArray.get(i)[0]);
					   fw.append(';');
					   fw.append(btArray.get(i)[1]);
					   fw.append(';');
					   fw.append(btArray.get(i)[2]);
					   fw.append('\n');				  
			   }
			   fw.close();
		}
	private void setBtArray(String nombreArchivo) throws IOException {
		btArray.clear();
		BufferedReader reader1 = new BufferedReader(new FileReader(nombreArchivo));
		int ind=0;
		while (true){
			String line= reader1.readLine();
			if(line != null) {
				String[]split=line.split(";");
				btArray.add(ind, split);
				ind++;	
			}
			else {
				ind=0;
				break;
			}
		}
		reader1.close();
	}
	//String[] hojaArray= [atributoBuscado, tipo, dirección]
	
	public void insert(String atributo, String line) throws IOException {
			if(line != null) {
				int lenghtLine= line.length();
				String[] split= line.split( ";");
				int indice= Btree.getIndex(attr, atributo);
				String atributoBuscado= split[indice];
				String tipo= "hoja";
				String direccion=  Long.toString(startAt);
				hojaArray[0]=atributoBuscado;
				hojaArray[1]=tipo;
				hojaArray[2]=direccion;
				//Posicion de la siguiente linea
				startAt= startAt+lenghtLine+1;
			}
			insertAux(hojaArray);
	}
	 private void insertAux(String[] linea) throws IOException {
		 File f = new File(archivoRaiz);
		 if(!f.exists()){
			 saveArrayInFile(archivoRaiz);
		 }
		 setBtArray(archivoRaiz);
		 archivoActual=archivoRaiz;
		 insertInterno(linea);
	 }
	 
	 private void insertInterno(String[] linea) throws IOException{		
		   //Nodo tiene espacio 
		   if(btArray.size()<size) {
			   insertInNode(linea);
			   saveArrayInFile(archivoActual);
		   }
		   //Nodo lleno
		   else {
			   if (btArray.size()==size) {
				   insertInNode(linea);
				   split(linea);	 
				   	  
			   }
		   }
	   }	
	 private void insertInNode(String[] linea) {
		   //Primer elemento que agrego
		   if (btArray.size()==0) {
			   String[] aux = new String[3];
			   aux[0] = linea[0];
			   aux[1] = linea[1];
			   aux[2] = linea[2];			   
			   btArray.add(aux);
		   }
		   
		   //Si hay más elementos tengo que agregarlo en la posicion correcta
		   else {
			   int contador=btArray.size();
			   for(int i=0;i<contador;i++) {
				 //Si el elemento que inserto es menor al que comparo, lo inserto
				   if((linea[0].compareTo(btArray.get(i)[0]))<0) {
					   contador++;
					   String[] auxLinea = new String[3];
					   auxLinea[0] = linea[0];
					   auxLinea[1] = linea[1];
					   auxLinea[2] =linea[2];
					   btArray.add(i, auxLinea);
					   break;
				   }
				 //Si el elemento que inserto es mayor al que comparo, comparo con el siguiente 
				   else {
					 //Lo inserto al final
					   if(i==contador-1) {
						   String[] aux = new String[3];
						   aux[0] = linea[0];
						   aux[1] = linea[1];
						   aux[2] = linea[2];
						   btArray.add(aux);
					   }
					   else {
						   continue;
					   }
				   }
			   }  
		   }
	}
	//Linea=genero el split, archivo=lo sobreescribo
		private void split(String[]linea) throws IOException {
			int indice=getIndexLine(btArray,linea);
			String[] aux=new String[3];
			String[] auxLinea=new String[3];
			//guardo linea
			auxLinea[0]=linea[0];
			auxLinea[1]=linea[1];
			auxLinea[2]=linea[2];

			//Nuevo archivo=lineaHoja+siguienteHoja
			//Archivo antiguo= siguienteNodo + resto
			if(indice==0) {
				aux[0]=btArray.get(1)[0];
				aux[1]=btArray.get(1)[1];
				aux[2]=btArray.get(1)[2];
				btArray.get(1)[1]="nodo";
				if(aux[1].equals("hoja")) {
					int nextFile=numArchivos+1;
					btArray.get(1)[2]=nombreArchivoNodo+nextFile+extension;
					
				}
				btArray.remove(0);
				saveArrayInFile(archivoActual);
				btArray.clear();
				//se reinicia el array 
				if(aux[1].equals("nodo")) {
					archivoActual=aux[2];
					setBtArray(archivoActual);
					insertInterno(auxLinea);
				}
				else {
					numArchivos++;
					archivoActual=nombreArchivoNodo+numArchivos+extension;
					insertInterno(auxLinea);
					insertInterno(aux);
				}
				
			}

			//Nuevo archivo=anteriorHoja+ lineaHoja
			//Archivo antiguo= anteriores(menos anteriorHoja) +lineaNodo + resto
			else {
				//guardo anteriorHoja/nodo
				aux[0]=btArray.get(indice-1)[0];
				aux[1]=btArray.get(indice-1)[1];
				aux[2]=btArray.get(indice-1)[2];
				//lineaNodo
				btArray.get(indice)[1]="nodo";
				if(aux[1].equals("nodo")) {
					btArray.get(indice)[2]=aux[2];
				}
				else {
					int nextFile=numArchivos+1;
					btArray.get(indice)[2]=nombreArchivoNodo+nextFile+extension;	
				}
				
				btArray.remove(indice-1);
				//reescribe archivo
				saveArrayInFile(archivoActual);
				btArray.clear();
				//se reinicia el array 
				if(aux[1].equals("nodo")) {
					archivoActual=aux[2];
					setBtArray(archivoActual);
				}
				else {
					numArchivos++;
					archivoActual=nombreArchivoNodo+numArchivos+extension;
					insertInterno(aux);
				}			
				insertInterno(auxLinea);	
			}	
		}

	  public FileWriter busqueda(String attr) throws IOException {
		   boolean notEmpty=false;
		   BufferedReader readerB = new BufferedReader(new FileReader(archivoRaiz));
		   BufferedReader readerBigFile = new BufferedReader(new FileReader(nombreArchivo));
		   long c=0;
		   FileWriter f = new FileWriter("resultado.txt", false);
		   while(true) {
			   String line=readerB.readLine();
			   if(line==null) {
				   break;
			   }
			   String[]split= line.split(";");
			   
			   if (attr.compareTo(split[0])<=0) {
				   if (attr.equals(split[0]) && split[1].equals("hoja")) {
					   notEmpty=true;
					   readerBigFile.skip(Long.parseLong(split[2])-c);
					   String lineAux=readerBigFile.readLine();
					   f.append(lineAux);
					   f.append("\n");
					   c=Long.parseLong(split[2])+lineAux.length()+1;
				   }
				   else {
					   if (split[1].equals("nodo")) {
						   readerB.close();
						   readerB = new BufferedReader(new FileReader(split[2]));
					   }
					   continue;
				   }
			   }
			   else {
					continue;   
			   }
		   }
		   readerB.close();
		   readerBigFile.close();
		   f.close();
		   if(notEmpty==false) {
			   System.out.println( "not found");
		   }
		   else {
			   return f;
		   }
		return null;
	   }

}
