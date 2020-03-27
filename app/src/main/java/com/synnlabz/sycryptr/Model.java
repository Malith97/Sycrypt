package com.synnlabz.sycryptr;

import android.content.Context;

public class Model {

    private Context context;
    long id;
    private OnDecryptedListener onDecryptedListener;
    int accounttype;
    String accountname , accountusername , accountpassword , updatetimestamp , accountlink;

    public Model(){

    }

    public Model(Context context, OnDecryptedListener onDecryptedListener) {
        this.context = context;
        this.onDecryptedListener = onDecryptedListener;
    }

    public interface OnDecryptedListener {
        void onDecrypted(String account, String passwd);
    }

    public Model(String accountname, String accountusername, String accountpassword , String accountlink , int accounttype) {
        this.accountname = accountname;
        this.accountusername = accountusername;
        this.accountpassword = accountpassword;
        this.accountlink = accountlink;
        this.accounttype = accounttype;
        this.updatetimestamp = updatetimestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountusername() {
        return accountusername;
    }

    public void setAccountusername(String accountusername) {
        this.accountusername = accountusername;
    }

    public String getAccountpassword() {
        return accountpassword;
    }

    public void setAccountpassword(String accountpassword) {
        this.accountpassword = accountpassword;
    }

    public int getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(int accounttype) {
        this.accounttype = accounttype;
    }

    public String getAccountlink() {
        return accountlink;
    }

    public void setAccountlink(String accountlink) {
        this.accountlink = accountlink;
    }

    public String getUpdatetimestamp() {
        return updatetimestamp;
    }

    public void setUpdatetimestamp(String updatetimestamp) {
        this.updatetimestamp = updatetimestamp;
    }
}
