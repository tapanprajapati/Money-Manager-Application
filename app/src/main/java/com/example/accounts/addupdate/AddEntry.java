package com.example.accounts.addupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEntry extends AppCompatActivity
{

    TextView txtCategory, txtType;
    TextInputEditText inputDate, inputSource, inputAmount;
    MaterialButton btnAddEntry;
    ImageButton backButton;

    Category category;
    EntryType type;

    SQLiteOpenHelper dbHandler;
    IEntryService entryService;
    ICategoryService categoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtCategory = findViewById(R.id.txtCategory);
        txtType = findViewById(R.id.txtType);
        inputDate = findViewById(R.id.inputDate);
        inputSource = findViewById(R.id.inputSource);
        inputAmount = findViewById(R.id.inputAmount);
        btnAddEntry = findViewById(R.id.btnAddEntry);
        backButton = findViewById(R.id.backButton);

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();

        category = categoryService.getCategory(getIntent().getIntExtra(Constants.CATEGORY,-1));
        type = EntryType.find(getIntent().getIntExtra(Constants.TYPE,-1));

        //set Widget values
        txtType.setText(type.toString());
        txtCategory.setText(category.getName());

        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        inputDate.setText(format.format(date));

        //set click listener for input date

        inputDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Calendar c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEntry.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        String m = String.valueOf(month + 1), d = String.valueOf(dayOfMonth);
                        if (month + 1 <= 9)
                            m = "0" + m;
                        if (dayOfMonth <= 9)
                            d = "0" + d;

                        String date = year+"-"+m+"-"+d;

                        inputDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        //check all the data and insert entry into database on clicking button

        btnAddEntry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //validate data

                String date = inputDate.getText().toString();
                String source = inputSource.getText().toString();
                String amount = inputAmount.getText().toString();
                if(date.trim().equals("") || source.trim().equals("") ||amount.trim().equals(""))
                {
                    Toast.makeText(AddEntry.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                Entry entry = new Entry();
                entry.setCategory(category);
                entry.setDate(date);
                entry.setSource(source);
                entry.setAmount(Float.parseFloat(amount));

                entryService.addEntry(entry);


                Toast.makeText(AddEntry.this, "Data Entered Successfully", Toast.LENGTH_SHORT).show();

                inputSource.setText("");
                inputAmount.setText("");

            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }
}
