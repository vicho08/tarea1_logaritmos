import java.io.IOException;
import java.rmi.AlreadyBoundException;

public class Experimento {
	    int N;
	    Database db;

	    public Experimento(int n){
	        N=n;
	    }


	    public void Consumidor() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, AlreadyBoundException{
	        this.db = new Database("ConsumidorInsert10.txt");
	        double timeI = 0;
	        double timeF = 0;
	        double totalTime = 0;
	        timeI= System.currentTimeMillis();
	        for (int n=0; n < this.N;n++){
	            String rut = String.valueOf((int) (Math.random() * 999999)+ 19000000);
	            String numero_identificador = String.valueOf((int) (Math.random()*9));
	            String id = String.valueOf(n);
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
	        timeF= System.currentTimeMillis();
	        totalTime= timeF-timeI;
	        System.out.println("Tiempo: "+ totalTime+ "[ms]");
	        
	        //ordenar por id
	        //db.ordenar("id");
	        //ordenar por rut
	        //db.ordenar(" rut ");
	        //ordenar por puntos
	        db.ordenar(" puntos ");

	    }
		public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, AlreadyBoundException {
			Experimento ex= new Experimento(10);
			ex.Consumidor();
		}

/***
	    public Productos(){
	        this.db = new Database("Productos");
	        double time = 0;
	        time = time(); //??
	        for (N=0; N < this.N;N++){
	            int nombre = (int) (Math.random() * 999999)+ 19000000;
	            int puntosNec = (int) (Math.random() * 100000)+1;
	            int puntosRec = (int) (Math.random() *100)+1;
	            int id = N;
	            db.agregar("id " + id.toString(); +" nombre " + nombre.toString() + " puntosNec " + puntosNec.toString() + " puntosRec "+puntosRec.toString());  )
	        }

	        //ordenar por id
	        //ordenar por nombre
	        //ordenar por puntosNec
	        //ordenar por puntos Rec
	        

	    } ***/
	}