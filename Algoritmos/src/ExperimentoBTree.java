import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExperimentoBTree {
    int N;
    Btree bt;
    Database db;

    public ExperimentoBTree(int n){
        N=n;
    }
    
    public void Consumidor() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, AlreadyBoundException{
        this.db = new Database("ConsumidorInsert10.txt");
        for (int n=0; n < this.N;n++){
        		//int reverse=this.N-n-1;
        		ArrayList<Integer> list = new ArrayList<Integer>();
        		list.add(5);
        		list.add(6);
        		list.add(5);
        		list.add(0);
        		list.add(1);
        		list.add(2);
        		list.add(3);
        		list.add(5);
        		list.add(8);
        		list.add(9);
        		
            String rut = String.valueOf((int) (Math.random() * 999999)+ 19000000);
            String numero_identificador = String.valueOf((int) (Math.random()*9));
            String id = String.valueOf(list.get(n));
            String puntos= String.valueOf((int) (Math.random() * 1000));
            String [] nodo1= new String[8];
            nodo1[0] = "id";
            nodo1[1] = id;
            nodo1[2] = " rut ";
            nodo1[3] = rut;
            nodo1[4] = "-";
            nodo1[5] = numero_identificador;
            nodo1[6] = " puntos ";
            nodo1[7] = puntos;  
            Nodo nodo= new Nodo(nodo1);
            db.agregar(nodo); 
        }
         this.bt=new Btree("ConsumidorInsert10.txt", db.keys, 5);
         
         
 		BufferedReader reader = new BufferedReader(new FileReader("ConsumidorInsert10.txt"));
 		while (true){
 			String line= reader.readLine();
 			if(line != null) {
 				bt.insert("id",line);
 			}
 			else {
 				break;
 			}
 		}
 		reader.close();
        	 System.out.println(bt.busqueda("5"));
    }
    	
    
    		
    		
    
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, AlreadyBoundException {
		ExperimentoBTree ex= new ExperimentoBTree(10);
		ex.Consumidor();
	}
}
