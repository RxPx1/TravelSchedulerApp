package com.myappcompany.rob.d308_mobileapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.myappcompany.rob.d308_mobileapp.dao.ExcursionDAO;
import com.myappcompany.rob.d308_mobileapp.dao.VacationDAO;
import com.myappcompany.rob.d308_mobileapp.entities.Excursion;
import com.myappcompany.rob.d308_mobileapp.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 1, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO;

    public abstract ExcursionDAO excursionDAO;

    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (VacationDatabaseBuilder.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "VacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
            return INSTANCE;
        }
    }

}
