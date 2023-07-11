package com.myappcompany.rob.d308_mobileapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "vacations")
@TypeConverters(Vacation.DateConverter.class)
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationName;
    private String vacationLodging;
    private Date startDate;
    private Date endDate;

    public Vacation(int vacationID, String vacationName, String vacationLodging, Date startDate, Date endDate) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.vacationLodging = vacationLodging;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Vacation() {
    }

    public String getVacationLodging() {
        return vacationLodging;
    }

    public void setVacationLodging(String vacationLodging) {
        this.vacationLodging = vacationLodging;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Vacation{" +
                "vacationID=" + vacationID +
                ", vacationName='" + vacationName + '\'' +
                ", vacationLodging='" + vacationLodging + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
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
