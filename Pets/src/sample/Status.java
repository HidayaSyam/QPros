package sample;

public class Status {

    private  int count;
    private  String status;

    public Status(int count, String status) {
        this.count = count;
        this.status = status;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getCount() {
        return count;
    }

    public String getStatus() {
        return status;
    }

}
