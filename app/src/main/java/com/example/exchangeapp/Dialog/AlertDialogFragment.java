package com.example.exchangeapp.Dialog;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exchangeapp.R;


public class AlertDialogFragment extends DialogFragment {
	private Button btnOk;
	private TextView tvTitle, tvMessage;
	private btnOkListener listener;
	private String titulo, mensaje;
	private Context context;

	@SuppressLint("ValidFragment")
	public AlertDialogFragment() {
	}

	@SuppressLint("ValidFragment")
	public AlertDialogFragment(String titulo, String mensaje) {
		this.titulo = titulo;
		this.mensaje = mensaje;
	}

	public interface btnOkListener {
		void okOnClick(DialogFragment dialog);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_fragment_alert, null);
		iniciarElementosVisuales(view);
		builder.setView(view);

		return builder.create();
	}

	public void iniciarElementosVisuales(View view) {
		btnOk = view.findViewById(R.id.btnOk);
		tvTitle = view.findViewById(R.id.title);
		tvMessage = view.findViewById(R.id.message);
	}

	public void setListenerAgregarBeneficiario(btnOkListener listener) {
		this.listener = listener;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		tvTitle.setText(titulo);
		tvMessage.setText(mensaje);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(listener == null)
					getDialog().dismiss();
				else
					listener.okOnClick(AlertDialogFragment.this);
			}
		});
	}
}
