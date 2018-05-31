package dersus.challenge_guep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import dersus.challenge_guep.constants.BundleConstants;
import dersus.challenge_guep.model.GasStation;
import dersus.challenge_guep.model.persistence.DAO.GasStationDao;

/**
 * Created by joao on 29/05/18.
 */

public class NewGasStationActivity extends Activity {

    //Interface
    Button address;
    EditText name;
    Button save;
    Activity ctx;

    //Controller
    GasStation gasStation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gas_station);

        gasStation = new GasStation();

        initializeComponents();
    }

    //Initialize interface components
    private void initializeComponents(){
        ctx = this;

        address = (Button) findViewById(R.id.gas_address);
        name = (EditText) findViewById(R.id.gas_name);
        save = (Button) findViewById(R.id.gas_save);


        //Setting Listenner
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("DEBUG","Clicou");
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(ctx);
                    startActivityForResult(intent, BundleConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    // TODO: Solucionar o erro.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Solucionar o erro.
                    e.printStackTrace();
                }
            }
        });


        //Save Button Setting Listenner
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gasStation.setName(name.getText().toString());

                //Salvando
                GasStationDao.getInstance(ctx).save(gasStation);

                //Saindo da activity
                Intent returnIntent = new Intent();
                returnIntent.putExtra(BundleConstants.RESULT_GAS_STATION,gasStation.getName());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }


    // After search
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleConstants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                gasStation.setLatLng(place.getLatLng());
                gasStation.setAddress(place.getAddress().toString());

                //Updating interface
                address.setText(place.getAddress());

                Log.i("Debug", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
