package tikape.runko.domain;

public class Tempolio {
    private final Object o1;
    private final Object o2;
    private final Object o3;

    public Tempolio(Object o1,Object o2,Object o3) {
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }
    
    public Object getObj1() {
        return o1;
    }
    public Object getObj2() {
        return o2;
    }
    public Object getObj3() {
        return o3;
    }
}
