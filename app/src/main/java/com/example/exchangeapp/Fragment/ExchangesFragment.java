package com.example.exchangeapp.Fragment;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.exchangeapp.Adapter.BaseRateAdapter;
import com.example.exchangeapp.Adapter.RateAdapter;
import com.example.exchangeapp.Bean.Rate;
import com.example.exchangeapp.Database.RealmHelper;
import com.example.exchangeapp.Dialog.AlertDialogFragment;
import com.example.exchangeapp.Dialog.MessageProgressDialog;
import com.example.exchangeapp.Listener.OnSearchClickListener;
import com.example.exchangeapp.Preferences.SharedPreferences;
import com.example.exchangeapp.R;
import com.example.exchangeapp.Utils.Utils;
import com.example.exchangeapp.Volley.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExchangesFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private RealmHelper realmHelper;
    private LinearLayoutManager linearLayoutManager;
    private RateAdapter rateAdapter;
    private List<Rate> rates;
    private MessageProgressDialog messageProgressDialog;


    public ExchangesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ExchangesFragment newInstance() {
        ExchangesFragment fragment = new ExchangesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        sharedPreferences = new SharedPreferences(getContext());
        realmHelper = new RealmHelper();
        messageProgressDialog = new MessageProgressDialog(getContext(), R.string.message_updating);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_exchanges, container, false);
        rates = new ArrayList<>();
        setUpRecyclerView();
        populateRecyclerView();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.action_bar_spinner_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button:
                if(Utils.hasInternet(getContext())){
                    sendRequest();
                }else{
                    sendMessage(getString(R.string.missing_connection_title),getString(R.string.missing_conection));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //setup recycler view
    private void setUpRecyclerView() {
        recyclerView = root.findViewById(R.id.item_recycler_view);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //populate recycler view
    private void populateRecyclerView() {
        rates = realmHelper.readAllSelectedAscending();
        rateAdapter = new RateAdapter(getContext(), rates);
        recyclerView.setAdapter(rateAdapter);
    }


    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    private void sendMessage(String titulo, String mensaje) {
        DialogFragment dialogFragment = new AlertDialogFragment(titulo, mensaje);
        dialogFragment.show(getFragmentManager(), "AlertDialogFragment");
    }

    private void sendRequest() {
        sharedPreferences.getBaseCountry();
        messageProgressDialog.show();

        final String url = "https://api.exchangeratesapi.io/latest?base=";

        // prepare the Request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + sharedPreferences.getBaseCountry(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            rates = new ArrayList<>();
                            Gson gson = new Gson();
                            JSONObject jsonObject = response.getJSONObject("rates");
                            Map<String, Object> ratesMap = new Gson().fromJson(jsonObject.toString(), new TypeToken<HashMap<String, Object>>() {
                            }.getType());
                            for (Map.Entry<String, Object> entry : ratesMap.entrySet()) {
                                Rate rate = realmHelper.readOne(entry.getKey());
                                if (rate == null) {
                                    rate = new Rate();
                                    rate.setValue((double) entry.getValue());
                                    rate.setCountry(entry.getKey());
                                    realmHelper.save(rate);
                                } else {
                                    realmHelper.updateValue(entry.getKey(), (double) entry.getValue());
                                }
                            }
                            messageProgressDialog.dismiss();
                            ExchangesFragment exchangesFragment = ExchangesFragment.newInstance();
                            replaceFragmentWithAnimation(exchangesFragment, "Fragment");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        VolleySingleton.getInstance(getContext()).addToRequestqueue(request);
    }

}
