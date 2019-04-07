package com.patrickmumot.eatingdice.Fragment;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrickmumot.eatingdice.Animation.ResizeAnimation;
import com.patrickmumot.eatingdice.MainActivityRemake;
import com.patrickmumot.eatingdice.Models.Cuisine;
import com.patrickmumot.eatingdice.R;

import java.lang.reflect.Array;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiceFragment.OnDiceInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiceFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView leftImage;
    private ImageView rightImage;
    private ImageView topImage;
    private ConstraintLayout dice;

    private Handler handler = new Handler();

    private Runnable loadingRunnable;

    private OnDiceInteractionListener mListener;
    private Animation bounce = null;

    private int diceWidth;
    private int diceHeight;
    private ConstraintLayout self;

    private int leftIconWidth;
    private int leftIconHeight;
    private int rightIconWidth;
    private int rightIconHeight;
    private int topIconWidth;
    private int topIconHeight;


    public DiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiceFragment newInstance(String param1, String param2) {
        DiceFragment fragment = new DiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void resizeDice(boolean small){
        if(small){
           // ResizeAnimation.collapse(this.getView(), 200);
            ResizeAnimation.resize(dice, diceWidth, diceHeight,diceWidth/2, diceWidth/2);
            ResizeAnimation.resize(leftImage, leftIconWidth, leftIconHeight,leftIconWidth/2, leftIconHeight/2);
            ResizeAnimation.resize(rightImage, rightIconWidth, rightIconHeight,rightIconWidth/2, rightIconHeight/2);
            ResizeAnimation.resize(topImage, topIconWidth, topIconHeight,topIconWidth/3, topIconHeight/3);

        } else {
            ResizeAnimation.resize(dice, diceWidth/2, diceHeight/2, diceWidth, diceHeight);
            ResizeAnimation.resize(leftImage, leftIconWidth/2, leftIconHeight/2,leftIconWidth, leftIconHeight);
            ResizeAnimation.resize(rightImage, rightIconWidth/2, rightIconHeight/2,rightIconWidth, rightIconHeight);
            ResizeAnimation.resize(topImage, topIconWidth/3, topIconHeight/3,topIconWidth, topIconHeight);
        }
    }


    int[] imagesBL = new int[] {R.drawable.pizza_bl, R.drawable.asian_bl, R.drawable.bbq_bl };
    int[] imagesBR = new int[] {R.drawable.asian_br, R.drawable.bbq_br, R.drawable.pizza_br };
    int[] imagesTop = new int[] {R.drawable.bbq_top, R.drawable.pizza_top, R.drawable.asian_top};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        loadingRunnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                leftImage.setImageResource(imagesBL[count]);
                rightImage.setImageResource(imagesBR[count]);
                topImage.setImageResource(imagesTop[count]);
                count++;

                if(count > 2){
                    count = 0;
                }

                handler.postDelayed(loadingRunnable, 200);
            }
        };
    }

    public void startShake(){
        handler.postDelayed(loadingRunnable, 350);
    }

    public void endShake(){
        handler.removeCallbacks(loadingRunnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dice, container, false);
        leftImage = view.findViewById(R.id.leftImage);
        topImage = view.findViewById(R.id.topImage);
        rightImage = view.findViewById(R.id.rightImage);
        dice = view.findViewById(R.id.diceConstraint);
        view.post(new Runnable() {
                      @Override
                      public void run() {
                          diceHeight = view.getMeasuredHeight(); // for instance
                          diceWidth = view.getMeasuredWidth(); // for instance
                          rightIconHeight = rightImage.getHeight();
                          rightIconWidth = rightImage.getWidth();
                          topIconHeight = topImage.getHeight();
                          topIconWidth = topImage.getWidth();
                          leftIconHeight = leftImage.getHeight();
                          leftIconWidth = leftImage.getWidth();
                      }
                  });
        view.setOnClickListener(this);
        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onDiceInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDiceInteractionListener) {
            mListener = (OnDiceInteractionListener) context;
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

    public void setAnimation(Animation anim){
        bounce = anim;
    }

    @Override
    public void onClick(View v) {
        onButtonPressed();
        if(bounce != null){
            v.startAnimation(bounce);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDiceInteractionListener {
        // TODO: Update argument type and name
        void onDiceInteraction();
    }
}

