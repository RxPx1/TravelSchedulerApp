package com.myappcompany.rob.d308_mobileapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

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
