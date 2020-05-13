public class Product {
    public int id;
    public int count;

    public Product(int id, int count){
        this.id = id;
        this.count = count;
    }

    public String toString(){
        return String.format("{ Product: %d, Count: %d }", id, count);
    }
}
