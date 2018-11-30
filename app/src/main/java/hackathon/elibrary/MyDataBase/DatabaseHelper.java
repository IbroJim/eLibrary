package hackathon.elibrary.MyDataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import hackathon.elibrary.MyDataBase.BookSchema.BookTable;
import hackathon.elibrary.POJO.Book;


public class DatabaseHelper extends SQLiteOpenHelper {

 private static final int DATABASE_VERSION=2;
 private static final String DB_NAME="eLibraryDb";
 private static String DB_PATH = "/data/data/hackathon.elibrary/databases/";
 private final Context mContext;
 private SQLiteDatabase myDataBase;


    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,DATABASE_VERSION);
        this.mContext = context;
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist){
            //ничего не делать - база уже есть
        }else{
            //вызывая этот метод создаем пустую базу, позже она будет перезаписана
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
     * @return true если существует, false если не существует
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //база еще не существует
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
    /**
     * Копирует базу из папки assets заместо созданной локальной БД
     * Выполняется путем копирования потока байтов.
     * */
    private void copyDataBase() throws IOException {
        //Открываем локальную БД как входящий поток
        InputStream myInput = mContext.getAssets().open(DB_NAME);

        //Путь ко вновь созданной БД
        String outFileName = DB_PATH + DB_NAME;

        //Открываем пустую базу данных как исходящий поток
        OutputStream myOutput = new FileOutputStream(outFileName);

        //перемещаем байты из входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //закрываем потоки
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    public void openDataBase() throws SQLException {
        //открываем БД
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public Cursor query(String table, String[] colums, String selection, String[] selectionArgs, String group, String having, String orderBy){
        return myDataBase.query(table,colums,selection,selectionArgs,null,null,null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion<2){
            db.execSQL("create table "+BookTable.NAME_TABLE_TWO+"("+BookTable.NameOfFields.ID+" integer primary key,"
                    +BookTable.NameOfFields.ID_BOOK+" integer,"+BookTable.NameOfFields.FIRST_NAME+" text,"+BookTable.NameOfFields.LAST_NAME+" text,"+BookTable.NameOfFields.GENRE+" text,"
                    +BookTable.NameOfFields.PAGE+" integer,"+BookTable.NameOfFields.DATE_PIBLICATION+" integer,"+BookTable.NameOfFields.TITLE+" text,"+BookTable.NameOfFields.CREATED_BY+" text,"+BookTable.NameOfFields.DISCRIPTION+" text,"+
                    BookTable.NameOfFields.NAME_FILE+" text"+")");
        }
    }

    public Cursor getInformation(SQLiteDatabase db){
        String[] projection={BookTable.NameOfFields.ID,BookTable.NameOfFields.TITLE,BookTable.NameOfFields.FIRST_NAME,
        BookTable.NameOfFields.LAST_NAME,BookTable.NameOfFields.GENRE};
        Cursor cursor=db.query(BookTable.NAME_TABLE_TWO,projection,null,null,null,null,null);
        return cursor;
    }
}
