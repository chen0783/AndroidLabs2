package com.example.androidlabs;

public class Message {
    protected String messages;
    protected boolean isSend;
    protected long id;

    //constructor
    public Message(String m, long id, boolean i) {
        this.setMessage(m);
        this.setId(id);
        this.setOwnerOfMessage(i);
    }

    public Message (String n, boolean checked){
        this(n, 1, checked);
    }

    public String getMessage(){
        return messages;
    }

    public void setMessage(String m){
        this.messages = m;
    }

    public boolean isSend(){
        return this.isSend;
    }

    public void setOwnerOfMessage(boolean isSend) {
        this.isSend = isSend;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
