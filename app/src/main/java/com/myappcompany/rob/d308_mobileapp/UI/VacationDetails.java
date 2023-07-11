package com.myappcompany.rob.d308_mobileapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.myappcompany.rob.d308_mobileapp.R;
import com.myappcompany.rob.d308_mobileapp.entities.Excursion;
import com.myappcompany.rob.d308_mobileapp.entities.Vacation;

public class VacationDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            this.finish();
            return true;

        } else if (itemId == R.id.deletevacation) {
            for (Vacation vaca : repository.getAllVacations()) {
                if (vaca.getVacationID() == vacationId) {
                    currentVacation = vaca;
                }
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getVacationID() == vacationId) {
                    numExcursions++;
                }
            }
            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(VacationDetails.this, VacationList.class);
                intent.putExtra("vacationID", vacationId);
                startActivity(intent);
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(VacationDetails.this, VacationList.class);
                intent.putExtra("vacationID", vacationId);
                startActivity(intent);
            }
            return true;
}