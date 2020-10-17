package com.example.androidlabs;

public class Message {
    protected String messages;
    protected boolean isSend;

    public Message(String m, boolean i) {
        this.setMessage(m);
        isSend = i;
    }

    public Message (String n){
        this(n,true);
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

}
