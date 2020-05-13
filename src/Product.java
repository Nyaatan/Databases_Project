public class Product {
    public int id;
    public int count;
    public String name = null;

    public Product(int id, int count){
        this.id = id;
        this.count = count;
    }

    public Product(int id, int count, String name){
        this.id = id;
        this.count = count;
        this.name = name;
    }

    public String toString(){
        if(name==null) return String.format("{ Product: %d, Count: %d }", id, count);
        else return String.format("%s: %d\n", name, count);
    }
}
