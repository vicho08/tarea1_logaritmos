public class BTree {
    NodeBT root;

    public class BTree(int B){
        NodeLeaf r = new NodeLeaf(B);
        this.root = r;
    }

    public search(BTree BT, int k){
        int i = 1;
        while(i <= root.actuales && k > root.keys[i])
        i++;
        if(i <= root.actuales && k == root.keys[i] && root.isLeaf())
        return root.files[i]; //retorna el archivo que contiene el valor de atributo buscado
        else if(root.isLeaf()){
            return null; // no hay registros
        }
        else{
            READ_FILE(root.punteros[i]);
            return search(root.punteros[i], k);
        }

    }
}