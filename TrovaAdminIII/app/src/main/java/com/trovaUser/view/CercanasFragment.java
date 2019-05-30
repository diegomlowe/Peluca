package com.trovaUser.view;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.trovaUser.interfaces.IComunicaFragments;
import com.trovaAdminPack.R;


public class CercanasFragment extends Fragment {

    RecyclerView rv;
    Activity activity;

    IComunicaFragments interComunicaFragments;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cercanas, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = getView().findViewById(R.id.recycler_cercanas);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lm);
        rv.setHasFixedSize(true);
        //adapter = new AdapterPeluquerias(MainActivity.pList);



        MainActivity.adapterPeluquerias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                interComunicaFragments.enviarPeluca(MainActivity.pList.get(rv.getChildAdapterPosition(view)), 0);

            }
        });
        rv.setAdapter(MainActivity.adapterPeluquerias);


    }

    private CercanasFragment.OnFragmentInteractionListener mListener;


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){

            this.activity=(Activity)context;
            interComunicaFragments = (IComunicaFragments)this.activity;
        }
        if (context instanceof CercanasFragment.OnFragmentInteractionListener) {
            mListener = (CercanasFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
