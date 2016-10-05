package com.jcas.utilerias;

import android.app.Application;

/**
 * Created by MBrandle on 22/10/2014.
 */
public class Global  extends Application{
    private int cursor;
    private int buscar;

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getBuscar() {
        return buscar;
    }

    public void setBuscar(int buscar) {
        this.buscar = buscar;
    }
}

