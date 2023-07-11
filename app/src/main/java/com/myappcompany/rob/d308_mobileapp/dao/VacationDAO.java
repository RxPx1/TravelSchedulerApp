package com.myappcompany.rob.d308_mobileapp.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.myappcompany.rob.d308_mobileapp.entities.Vacation;

import java.util.Date;
import java.util.List;

@Dao
public interface VacationDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM VACATIONS ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT vacationName FROM VACATIONS")
    List<String> getVacationNames();

    @Query("SELECT * FROM VACATIONS WHERE vacationID = :id")
    LiveData<Vacation> getVacationById(int id);

    @Query("SELECT startDate FROM VACATIONS WHERE vacationID = :id")
    Long getVacationStartDate(int id);

    @Query("SELECT endDate FROM VACATIONS WHERE vacationID = :id")
    Long getVacationEndDate(int id);
}
