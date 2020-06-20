package com.mytodo.supertodo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class,Todo.class}, version = 6, exportSchema = false)
public abstract class SuperTodoDatabase extends RoomDatabase {
    private static SuperTodoDatabase instance;
    private static final Object LOCK = new Object();
    public abstract UserDao getUserDao();
    public abstract TodoDao getTodoDao();

    public static synchronized SuperTodoDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        SuperTodoDatabase.class, "todo_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .allowMainThreadQueries()
                        .build();
            }
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao userDao;
        //private TodoDao todoDao;

        private PopulateDbAsyncTask(SuperTodoDatabase db){

            userDao = db.getUserDao();
            //todoDao = db.getTodoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("user1@supertodo.com","password"));
            userDao.insert(new User("user2@supertodo.com","password"));
            return null;
        }
    }
}
