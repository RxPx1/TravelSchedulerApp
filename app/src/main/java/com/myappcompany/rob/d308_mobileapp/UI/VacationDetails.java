package com.myappcompany.rob.d308_mobileapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myappcompany.rob.d308_mobileapp.R;
import com.myappcompany.rob.d308_mobileapp.database.Repository;
import com.myappcompany.rob.d308_mobileapp.entities.Excursion;
import com.myappcompany.rob.d308_mobileapp.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    EditText editName;
    EditText editLodging;
    TextView editStartDate;
    TextView editEndDate;
    String vacationName;
    String vacationLodging;
    int vacationId;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarEnd = Calendar.getInstance();
    Vacation vacation;
    Vacation currentVacation;
    int numExcursions;
    Repository repository;
    private List<Excursion> filteredExcursions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        editStartDate = findViewById(R.id.editstartdate);
        editEndDate = findViewById(R.id.editenddate);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editName = findViewById(R.id.vacationname);
        editLodging = findViewById(R.id.vacationlodging);
        vacationId = getIntent().getIntExtra("id", -1);
        vacationName = getIntent().getStringExtra("name");
        vacationLodging = getIntent().getStringExtra("lodging");
        editName.setText(vacationName);
        editLodging.setText(vacationLodging);

        repository = new Repository(getApplication());
        vacation = repository.getVacationById(vacationId).getValue();
        LiveData<Vacation> vacationLiveData = repository.getVacationById(vacationId);
        vacationLiveData.observe(this, new Observer<Vacation>() {
            @Override
            public void onChanged(Vacation vacation) {
                if (vacation != null) {
                    VacationDetails.this.vacation = vacation;
                    Date startDate = vacation.getStartDate();
                    Date endDate = vacation.getEndDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                    String formattedStartDate = sdf.format(startDate);
                    String formattedEndDate = sdf.format(endDate);
                    if(editStartDate.getText().toString().isEmpty() || editEndDate.getText().toString().isEmpty()){
                        myCalendarStart.setTime(startDate);
                        myCalendarEnd.setTime(endDate);
                        editStartDate.setText(formattedStartDate);
                        editEndDate.setText(formattedEndDate);
                    }
                }
            }
        });

        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion x : repository.getAllExcursions()) {
            if (x.getVacationID() == vacationId) filteredExcursions.add(x);
        }
        excursionAdapter.setExcursions(filteredExcursions);
        Button button = findViewById(R.id.savevacation);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date startDate = myCalendarStart.getTime();
                Date endDate = myCalendarEnd.getTime();
                if (vacationId == -1) {
                    vacation = new Vacation(0, editName.getText().toString(), editLodging.getText().toString(), startDate, endDate);
                    repository.insert(vacation);
                    Toast.makeText(VacationDetails.this, vacation.getVacationName() + " was SAVED!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(VacationDetails.this, VacationList.class);
                    intent.putExtra("vacationID", vacationId);
                    startActivity(intent);
                } else {
                    vacation.setStartDate(startDate);
                    vacation.setEndDate(endDate);
                    vacation = new Vacation(vacationId, editName.getText().toString(), editLodging.getText().toString(), startDate, endDate);
                    repository.update(vacation);
                    Toast.makeText(VacationDetails.this, vacation.getVacationName() + " was SAVED!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(VacationDetails.this, VacationList.class);
                    intent.putExtra("vacationID", vacationId);
                    startActivity(intent);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationId);
                intent.putExtra("vacationStartDate", myCalendarStart.getTimeInMillis());
                intent.putExtra("vacationEndDate", myCalendarEnd.getTimeInMillis());
                startActivity(intent);
            }
        });

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(VacationDetails.this, startDate,
                        myCalendarStart.get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH));
                if (myCalendarEnd.getTimeInMillis() > System.currentTimeMillis()) {
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.getDatePicker().setMaxDate(myCalendarEnd.getTimeInMillis());
                } else {
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }
                datePickerDialog.show();
            }
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(VacationDetails.this, endDate,
                        myCalendarEnd.get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendarStart.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart2();
            }
        };
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelStart2() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filteredExcursions = new ArrayList<>();
        for (Excursion x : repository.getAllExcursions()) {
            if (x.getVacationID() == vacationId) {
                filteredExcursions.add(x);
            }
        }
        excursionAdapter.setExcursions(filteredExcursions);
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

        } else if (itemId == R.id.share) {
            if (filteredExcursions == null) {
                Toast.makeText(this, "No excursions found", Toast.LENGTH_SHORT).show();
                return true;
            }
            StringBuilder shareText = new StringBuilder();
            shareText.append("Vacation Name: ").append(vacationName).append("\n");
            shareText.append("Lodging: ").append(vacationLodging).append("\n");
            shareText.append("Start Date: ").append(editStartDate.getText().toString()).append("\n");
            shareText.append("End Date: ").append(editEndDate.getText().toString()).append("\n\n");
            shareText.append("Excursions: ").append("\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd", Locale.US);
            for (Excursion excursion : filteredExcursions) {
                shareText.append(excursion.getExcursionName()).append(" - ");
                shareText.append(dateFormat.format(excursion.getExcursionDate())).append("\n");
            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Share Vacation Details");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;

        } else if (itemId == R.id.notifystart) {
            String dateFromScreen = editStartDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("isStartNotification", true);
            intent.putExtra("key", dateFromScreen + " should trigger");
            PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;

        } else if (itemId == R.id.notifyend) {
            String dateFromScreen = editEndDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("isStartNotification", false);
            intent.putExtra("key", dateFromScreen + " should trigger");
            PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}