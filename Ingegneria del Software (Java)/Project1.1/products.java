
/* class products that comprehends
 * name,producer,price,quantity present,technical notes
 * it also presents the removequant method that let you remove quantities from a product
 * and setquant method that let you set the quantities for a product*/
public class products {
    private String name;
    private String producer;
    private String technicalnotes;
    private int price;
    private int quant;
    products(int p,String na,String pro,String tn,int q){
        price=p;
        name=na;
        producer=pro;
        technicalnotes=tn;
        quant=q;}
    public String getname(){
         return name;}
    public String getproducer(){
        return producer;}
    public int getprice(){
         return price;}
    public int getquant(){
        return quant;}
    public String gettechn(){
         return technicalnotes;}
    public void removequant(int buy){
         quant-=buy;}
    public void setquant(int qu){
         quant=qu;
    }
}