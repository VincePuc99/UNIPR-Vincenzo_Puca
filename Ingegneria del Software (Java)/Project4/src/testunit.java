import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
public class testunit {
    wine objwine= new wine();
 @Test 
 public void buywinetestokquantity(){//
  assertEquals(true,objwine.buywine(3, 5)); //test passed (ok)
  }
 @Test
  public void buywinetestquantityhigher(){//
  assertEquals(false,objwine.buywine(7, 5)); //test not passed quantity> (ok)
  }
@Test
public void searchwinenotindb() throws InterruptedException {
    assertEquals(0,objwine.searchwine("Lambrusco", "2008"));//wine not found 0 (ok)
}
@Test
public void searchwine() throws InterruptedException {
    assertEquals(0,objwine.searchwine("Lambrusco", "2010"));//wine found pos (ok)
}
@Test
public void loginsigncheck(){
    assertEquals(0, App.logsignchecker(0)); // ok 0 login sign
}
@Test
public void loginsigncheck1error(){
    assertEquals(1, App.logsignchecker(1)); //ok 1 login sign error
}
}