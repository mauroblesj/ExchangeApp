package com.example.exchangeapp.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Ventana de espera para mostrar un mensaje mientras carga una tarea en segundo plano
 */
public class MessageProgressDialog extends ProgressDialog {
    private AsyncTask<?, ?, ?> tarea;

    /**
     * Constructor para establecer el mensaje y la tarea en segundo plano
     *
     * @param mensaje Mensaje a mostrar
     * @param tarea   Tarea en segundo plano
     */
    public MessageProgressDialog(Context context, String mensaje, AsyncTask<?, ?, ?> tarea) {
        super(context);
        this.tarea = tarea;
        this.setCanceledOnTouchOutside(false);
        this.setMessage(mensaje);
        this.setCancelable(true);
    }

    /**
     * Constructor para establecer el mensaje y la tarea en segundo plano
     *
     * @param mensaje Mensaje a mostrar (recurso)
     * @param tarea   Tarea en segundo plano
     */
    public MessageProgressDialog(Context context, int mensaje, AsyncTask<?, ?, ?> tarea) {
        super(context);
        this.tarea = tarea;
        this.setCanceledOnTouchOutside(false);
        this.setMessage(context.getString(mensaje));
        this.setCancelable(false);
    }

    public MessageProgressDialog(Context context, int mensaje) {
        super(context);
        this.setCanceledOnTouchOutside(false);
        this.setMessage(context.getString(mensaje));
        this.setCancelable(false);
    }

    @Override
    public void cancel() {
        super.cancel();
        tarea.cancel(true);
    }
}