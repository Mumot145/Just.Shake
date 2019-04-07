package com.patrickmumot.eatingdice;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.patrickmumot.eatingdice.Adapter.RecyclerAdapter;
import com.patrickmumot.eatingdice.Fragment.DiceFragment;
import com.patrickmumot.eatingdice.Fragment.newDiceFragment;

import java.util.ArrayList;


public class FinalActivity extends AppCompatActivity implements newDiceFragment.OnDiceInteractionListener {

    private ArrayList<String> stringArrayList;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private int diceHeight;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        final newDiceFragment diceFragment = (newDiceFragment) getSupportFragmentManager().findFragmentById(R.id.header);
      //  diceHeight = diceFragment.diceHeight;
        //final int originalHeight = diceImage.getHeight();
        //final int originialWidth = diceImage.getWidth();
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                float ratio = (float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange();
                float inverseRatio = 1 - ratio;
               // Log.i("scroll", Math.abs(verticalOffset)+"");
                //Log.i("max", appBarLayout.getTotalScrollRange()+"");
                Log.i("ratio", inverseRatio+"");
                //Log.i("diceFragment", diceFragment+"");

                if(inverseRatio > 0){
                    diceFragment.setParams(inverseRatio);
                }

                //diceImage.getLayoutParams().height = (int) (originalHeight*ratio);
                //diceImage.getLayoutParams().width = (int) (originialWidth*ratio);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.expand));

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setData(); //adding data to array list
        adapter = new RecyclerAdapter(this, stringArrayList);
        recyclerView.setAdapter(adapter);
        collapsingToolbar.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //int height = collapsingToolbar.getLayoutParams().height;

               // int scrollY = recyclerView.computeVerticalScrollOffset();
               // int scrollY = recyclerView.getScrollY(); // For ScrollView
                int scrollX = recyclerView.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES

            }
        });
    }

    private void setData() {
        stringArrayList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            stringArrayList.add("Item " + (i + 1));
        }
    }

    @Override
    public void onDiceInteraction() {
        Log.i("dice", "shake");
    }
}
