package com.myappcompany.rob.d308_mobileapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "excursions")
@TypeConverters(Excursion.DateConverter.class)
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private Date excursionDate;
    private String excursionName;
    private String excursionNote;
    private int vacationID;

    public Excursion(int excursionID, String excursionName, String excursionNote, Date excursionDate, int vacationID) {
        this.excursionID = excursionID;
        this.excursionDate = excursionDate;
        this.excursionName = excursionName;
        this.excursionNote = excursionNote;
        this.vacationID = vacationID;
    }

    public String getExcursionNote() {
        return excursionNote;
    }

    public void setExcursionNote(String excursionNote) {
        this.excursionNote = excursionNote;
    }

    public Excursion() {
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public Date getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(Date excursionDate) {
        this.excursionDate = excursionDate;
    }



    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    @Override
    public String toString() {
        return "Excursion{" +
                "excursionID=" + excursionID +
                ", excursionDate=" + excursionDate +
                ", excursionName='" + excursionName + '\'' +
                ", excursionNote='" + excursionNote + '\'' +
                ", vacationID=" + vacationID +
                '}';
    }

    public static class DateConverter {

        @TypeConverter
        public Date toDate(Long timestamp) {
            return timestamp == null ? null : new Date(timestamp);
        }

        @TypeConverter
        public Long toTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }

        @TypeConverter
        public static String fromDate(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            return date == null ? null : dateFormat.format(date);
        }
    }
}