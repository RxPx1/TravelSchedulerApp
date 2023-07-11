package com.myappcompany.rob.d308_mobileapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

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