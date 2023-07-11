package com.myappcompany.rob.d308_mobileapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.myappcompany.rob.d308_mobileapp.dao.ExcursionDAO;
import com.myappcompany.rob.d308_mobileapp.dao.VacationDAO;
import com.myappcompany.rob.d308_mobileapp.entities.Excursion;
import com.myappcompany.rob.d308_mobileapp.entities.Vacation;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private ExcursionDAO mExcursionDAO;
    private VacationDAO mVacationDAO;
    private List<Vacation> mAllVacations;
    private List<String> mVacationNames;
    private List<Excursion> mAllExcursions;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mVacationDAO = db.vacationDAO();
        mExcursionDAO = db.excursionDAO();
    }

    public LiveData<Vacation> getVacationById(int id) {
        return mVacationDAO.getVacationById(id);
    }

    public LiveData<Excursion> getExcursionById(int id) {
        return mExcursionDAO.getExcursionById(id);
    }

    public List<Vacation> getAllVacations() {
        databaseExecutor.execute(() -> {
            mAllVacations = mVacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllVacations;
    }

    public List<String> getVacationNames() {
        databaseExecutor.execute(() -> {
            mVacationNames = mVacationDAO.getVacationNames();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mVacationNames;
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.insert(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Excursion> getAllExcursions() {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcursions;
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Date getVacationStartDate(int vacationId) {
        Long startDateTimestamp = mVacationDAO.getVacationStartDate(vacationId);
        if (startDateTimestamp != null) {
            return new Date(startDateTimestamp);
        }
        return null;
    }

    public Date getVacationEndDate(int vacationId) {
        Long endDateTimestamp = mVacationDAO.getVacationEndDate(vacationId);
        if (endDateTimestamp != null) {
            return new Date(endDateTimestamp);
        }
        return null;
    }
}
