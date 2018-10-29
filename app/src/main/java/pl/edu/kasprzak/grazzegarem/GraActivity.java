package pl.edu.kasprzak.grazzegarem;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GraActivity extends AppCompatActivity {

    // Po każdym obrocie ekranu Activity tworzone jest ponownie.
    // Czy jest jakiś sposób, aby te zmienne nie były dla każdej instancji
    // klasy GraActivity tworzone oddzielnie?
    boolean runningClock = false;
    int counter = 50;

    private Runnable worker;
    private Button action;
    private TextView clock;
    private Handler handler;
    private RecyclerView lastTries;
    private RecyclerView bestTries;
    private MyAdapter lastTriesAdapter;
    private MyAdapter bestTriesAdapter;
    private ArrayList<String> lastTriesArray;
    private ArrayList<String> bestTriesArray;
    private ArrayList<Integer> bestTriesIntArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gra);
        // Dlaczego action i clock nie za bardzo podobają się Android Studio?
        action = (Button)findViewById(R.id.action);
        clock = (TextView)findViewById(R.id.clock);
        lastTries = (RecyclerView)findViewById(R.id.lastTries);
        bestTries = (RecyclerView)findViewById(R.id.bestTries);

        lastTriesArray = new ArrayList<>();
        lastTriesArray.add("N/A");
        lastTriesArray.add("N/A");
        lastTriesArray.add("N/A");
        lastTriesArray.add("N/A");
        lastTriesArray.add("N/A");

        bestTriesArray = new ArrayList<>();
        bestTriesArray.add("999");
        bestTriesArray.add("999");
        bestTriesArray.add("999");

        bestTriesIntArray = new ArrayList<>();
        bestTriesIntArray.add(999);
        bestTriesIntArray.add(999);
        bestTriesIntArray.add(999);

        lastTries.setHasFixedSize(true);
        lastTries.setLayoutManager(new LinearLayoutManager(this));
        lastTriesAdapter = new MyAdapter(this, lastTriesArray);
        lastTries.setAdapter(lastTriesAdapter);

        bestTries.setHasFixedSize(true);
        bestTries.setLayoutManager(new LinearLayoutManager(this));
        bestTriesAdapter = new MyAdapter(this, bestTriesArray);
        bestTries.setAdapter(bestTriesAdapter);

        // Tutaj tworzymy anonimową klasę która implementuje interfejs Runnable
        // Odpowiada to definicji class MyRunnable implements Runnable.
        worker = new Runnable() {
            @Override
            public void run() {
                Log.d("LICZNIK", "DZIAŁAM " + counter);
                clock.setText("" + counter);
                --counter;
                if (runningClock) {
                    handler.postDelayed(worker, 1);
                }
            }
        };
        handler = new Handler();
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runningClock) {
                    counter = 50;
                    runningClock = true;
                    handler.postDelayed(worker, 1);
                } else {
                    runningClock = false;
                    updateLastTriesList(counter);
                    updateBestTriesList(counter);
                }
            }

            private void updateLastTriesList(Integer clockValue) {
                lastTriesArray.add(0, (""+clockValue).toString());
                lastTriesAdapter.notifyItemInserted(0);
                lastTriesArray.remove(5);
                lastTriesAdapter.notifyItemRemoved(5);
            }

            private void updateBestTriesList(Integer clockValue) {
                bestTriesIntArray.add(clockValue);
                Collections.sort(bestTriesIntArray, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer v1, Integer v2) {
                        return Math.abs(0-v1)-Math.abs(0-v2);
                    }
                });
                bestTriesIntArray.remove(3);

                bestTriesArray.clear();
                for(Integer i : bestTriesIntArray){
                    bestTriesArray.add(i.toString());
                }
                bestTriesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Wydaje się, że czegoś tutaj brakuje
        Log.d("CYKL_ZYCIA", "ONPAUSE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Podobnie jak tutaj, też czegoś brakuje
        Log.d("CYKL_ZYCIA", "ONRESUME");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CYKL_ZYCIA", "ONSTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CYKL_ZYCIA", "ONSTOP");
    }
}