package com.myappcompany.rob.d308_mobileapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myappcompany.rob.d308_mobileapp.R;
import com.myappcompany.rob.d308_mobileapp.database.Repository;
import com.myappcompany.rob.d308_mobileapp.entities.Excursion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    EditText editName;
    TextView editDate;
    EditText editNote;
    DatePickerDialog.OnDateSetListener excursionDate;
    final Calendar myCalendar = Calendar.getInstance();
    String excursionName;
    String excursionNote;
    int excursionId;
    int vacationId;
    Excursion excursion;
    Repository repository;
    Date vacationStartDate;
    Date vacationEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        repository = new Repository(getApplication());
        editName = findViewById(R.id.excursionname);
        editDate = findViewById(R.id.editdate);
        editNote = findViewById(R.id.editnote);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        excursionId = getIntent().getIntExtra("id", -1);
        excursionName = getIntent().getStringExtra("name");
        excursionNote = getIntent().getStringExtra("note");
        vacationId = getIntent().getIntExtra("vacationID", -1);
        editName.setText(excursionName);
        editNote.setText(excursionNote);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                vacationStartDate = repository.getVacationStartDate(vacationId);
                vacationEndDate = repository.getVacationEndDate(vacationId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myCalendar.setTime(vacationStartDate);
                        long minDate = myCalendar.getTimeInMillis();
                        myCalendar.setTime(vacationEndDate);
                        long maxDate = myCalendar.getTimeInMillis();
                        repository = new Repository(getApplication());
                        excursion = repository.getExcursionById(excursionId).getValue();
                        LiveData<Excursion> excursionLiveData = repository.getExcursionById(excursionId);
                        excursionLiveData.observe(ExcursionDetails.this, new Observer<Excursion>() {
                            @Override
                            public void onChanged(Excursion excursion) {
                                if (excursion != null) {
                                    ExcursionDetails.this.excursion = excursion;
                                    Date excursionDate = excursion.getExcursionDate();
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                                    String formattedStartDate = sdf.format(excursionDate);
                                    if (editDate.getText().toString().isEmpty()) {
                                        myCalendar.setTime(excursionDate);
                                        editDate.setText(formattedStartDate);
                                    }
                                    editNote.setText(excursion.getExcursionNote());
                                }
                            }
                        });

                        repository = new Repository(getApplication());
                        Button button = findViewById(R.id.saveexcursion);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Date excursionDate = myCalendar.getTime();
                                if (excursionId == -1) {
                                    excursion = new Excursion(0, editName.getText().toString(),editNote.getText().toString(), excursionDate, vacationId);
                                    repository.insert(excursion);
                                    Toast.makeText(ExcursionDetails.this, excursion.getExcursionName() + " was SAVED!", Toast.LENGTH_LONG).show();
                                } else {
                                    excursion = new Excursion(excursionId, editName.getText().toString(),editNote.getText().toString(), excursionDate, vacationId);
                                    repository.update(excursion);
                                    Toast.makeText(ExcursionDetails.this, excursion.getExcursionName() + " was SAVED!", Toast.LENGTH_LONG).show();
                                }
                                finish();
                            }
                        });


                        editDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog datePickerDialog = new DatePickerDialog(ExcursionDetails.this, excursionDate,
                                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                        myCalendar.get(Calendar.DAY_OF_MONTH));

                                datePickerDialog.getDatePicker().setMinDate(minDate);
                                datePickerDialog.getDatePicker().setMaxDate(maxDate);

                                datePickerDialog.show();
                            }
                        });

                        excursionDate = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                updateLabelStart();
                            }
                        };
                    }

                    private void updateLabelStart() {
                        String myFormat = "MM/dd/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        editDate.setText(sdf.format(myCalendar.getTime()));
                    }
                });
            }
        });
    }

    // Move these methods outside the runOnUiThread() block
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.share) {
            excursionNote = editNote.getText().toString();
            StringBuilder shareText = new StringBuilder();
            shareText.append("Excursion Name: ").append(excursionName).append("\n");
            shareText.append("Note: ").append(excursionNote).append("\n");
            shareText.append("Date: ").append(editDate.getText().toString()).append("\n");
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Share Excursion Details");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        if ( itemId == R.id.deleteexcursion) {
            if (excursion != null) {
                repository.delete(excursion);
                Toast.makeText(ExcursionDetails.this, excursion.getExcursionName() + " was deleted", Toast.LENGTH_LONG).show();
            }
            finish();
            return true;
        }
        if ( itemId == R.id.notifystart ) {
            String dateFromScreen = editDate.getText().toString();
            String excursionName = editName.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(ExcursionDetails.this, ExcursionReceiver.class);
            intent.putExtra("key", dateFromScreen + " " + excursionName + " Excursion is Starting");
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            Toast.makeText(ExcursionDetails.this,"Excursion Start Notification is Set", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}