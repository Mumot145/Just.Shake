package com.patrickmumot.eatingdice;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickmumot.eatingdice.Models.Cuisine;
import com.patrickmumot.eatingdice.dummy.DummyContent;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ZomatoCuisineItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ViewGroup container;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ZomatoCuisineItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ZomatoCuisineItemFragment newInstance(int columnCount) {
        ZomatoCuisineItemFragment fragment = new ZomatoCuisineItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zomatocuisineitem_list, container, false);
        this.container = container;
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(MainActivityRemake.delivery){
                recyclerView.setAdapter(new MyZomatoCuisineItemRecyclerViewAdapter(DummyContent.ITEMSDELIVERY, mListener, container.getContext()));
            } else {
                recyclerView.setAdapter(new MyZomatoCuisineItemRecyclerViewAdapter(DummyContent.ITEMS, mListener, container.getContext()));
            }
        }
        return view;
    }

    public void setDelivery(boolean delivery){
        if(delivery){
            recyclerView.setAdapter(new MyZomatoCuisineItemRecyclerViewAdapter(DummyContent.ITEMSDELIVERY, mListener, container.getContext()));
        } else {
            recyclerView.setAdapter(new MyZomatoCuisineItemRecyclerViewAdapter(DummyContent.ITEMS, mListener, container.getContext()));
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public RecyclerView getAdapter(){
        return recyclerView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Cuisine item);
    }
}
