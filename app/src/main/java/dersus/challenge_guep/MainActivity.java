package dersus.challenge_guep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dersus.challenge_guep.constants.BundleConstants;
import dersus.challenge_guep.model.GasStation;
import dersus.challenge_guep.model.persistence.DAO.GasStationDao;

public class MainActivity extends AppCompatActivity {

    Activity ctx;
    ArrayList<String> gasStationNames;
    ArrayAdapter<String> adapter;

    ListView lvGasStation;
    Button btShowMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        gasStationNames = new ArrayList<String>();

        loadDatabase();

        initializeComponents();
    }

    private void loadDatabase(){
        List<GasStation> gasStations = GasStationDao.getInstance(this).query();

        for(GasStation gasStation : gasStations){
            gasStationNames.add(gasStation.getName());
        }
    }

    private void initializeComponents(){
        lvGasStation = (ListView) findViewById(R.id.lv_main_gas_station);
        btShowMap = (Button) findViewById(R.id.bt_main_show_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //Toolbar
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,NewGasStationActivity.class);
                startActivityForResult(intent, BundleConstants.NEW_GAS_STATION);
            }
        });


        //ListView
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, gasStationNames);
        lvGasStation.setAdapter(adapter);
        lvGasStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ctx,gasStationNames.get(position),Toast.LENGTH_SHORT).show();
            }
        });

        //Button
        btShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx,MapsActivity.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (requestCode == BundleConstants.NEW_GAS_STATION) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                //Adding new GasStation
                gasStationNames.add(data.getStringExtra(BundleConstants.RESULT_GAS_STATION));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
