

public abstract class NodeBT {

    int n; //numero de llaves que permite el nodo 
    ArrayList<int> keys; //llaves key[0] < key[1] < ...
    boolean leaf; //hoja o nodo interno
    int actuales;

    public NodeBT(int B){
        n = B;
        keys = new ArrayList<int>();
        actuales = 0;
    }

    public abstract boolean isLeaf();


}