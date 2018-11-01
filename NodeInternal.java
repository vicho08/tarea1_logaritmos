

public class NodeInternal extends NodeBT{

     ArrayList<NodeBT> punteros;

    public NodeInternal(int B){
        ArrayList<NodeBT> punteros = ArrayList<NodeBT>();
        super(B+1);        
    }


    public boolean isLeaf(){
        return false;
    }
}