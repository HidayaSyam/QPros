package sample;

import java.math.BigInteger;
import java.util.ArrayList;

public class Pet {

    private BigInteger id;
    private String name;
    private String status;
    public  ArrayList<Pet> allpet ;

    public Pet(BigInteger _Id, String _Name, String _State) {
        this.id = _Id;
        this.name = _Name;
        this.status = _State;
        System.out.println(this);
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigInteger getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }


}
