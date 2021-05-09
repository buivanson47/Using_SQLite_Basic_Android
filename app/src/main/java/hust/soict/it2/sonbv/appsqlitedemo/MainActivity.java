package hust.soict.it2.sonbv.appsqlitedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static String DB_NAME = "myProduct.db";
    ArrayList<Product> listProduct;
    ProductListViewAdapter productListViewAdapter;
    ListView listView;
    public static final int RESULT_PRODUCT_ACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listProduct = new ArrayList<>();
        loadDBProduct();

        productListViewAdapter = new ProductListViewAdapter(listProduct);
        listView = findViewById(R.id.list_product);
        listView.setAdapter(productListViewAdapter);

        findViewById(R.id.create_DB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTableDB();
                loadDBProduct();
                productListViewAdapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.delete_DB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDBProduct();
                loadDBProduct();
                productListViewAdapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.add_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isUpdate", false);
                intent.setClass(MainActivity.this, EditProduct.class);
                startActivityForResult(intent, RESULT_PRODUCT_ACTIVITY);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) productListViewAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("isUpdate", true);
                intent.putExtra("idProduct", product.getID());
                intent.setClass(MainActivity.this, EditProduct.class);
                startActivityForResult(intent, RESULT_PRODUCT_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PRODUCT_ACTIVITY) {
            loadDBProduct();
            productListViewAdapter.notifyDataSetChanged();
        }
    }

    void createTableDB() {
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        // Kiem tra bang product da duoc tao hay chua
        if (!(isTableExist(db, "product"))) {
            String queryCreateTable = "CREATE TABLE product ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "price DECIMAL DEFAULT (0)" +
                    ")";

            db.execSQL(queryCreateTable);

            Toast.makeText(this, "Created Successful!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Table already exists. No need to create a new one !!!", Toast.LENGTH_SHORT).show();

        db.close();
    }

    private boolean isTableExist(SQLiteDatabase db, String nameTable) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{nameTable});
        boolean tableExist = (cursor.getCount() != 0);
        cursor.close();
        return tableExist;
    }

    private void loadDBProduct() {
        listProduct.clear();
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        if (!isTableExist(db, "product")) {
            Toast.makeText(this, "The database does not exist. Need to create a table first !!!", Toast.LENGTH_SHORT).show();
            ((TextView) findViewById(R.id.information)).setText("Table Data dose not exist !!!");
            findViewById(R.id.add_product).setVisibility(View.GONE);
            return;
        }

        ((TextView) findViewById(R.id.information)).setText("LIST PRODUCT");
        findViewById(R.id.add_product).setVisibility(View.VISIBLE);

        Cursor cursor = db.rawQuery("SELECT * FROM product", null);

        if (cursor.moveToFirst()) {
            do {
                int ID = cursor.getInt(0);
                String name = cursor.getString(1);
                int price = cursor.getInt(2);

                listProduct.add(new Product(ID, name, price));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    private void deleteDBProduct() {
        SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        if (!(isTableExist(db, "product"))) {
            Toast.makeText(this, "No have table to delete", Toast.LENGTH_LONG).show();
        } else {
            db.execSQL("DROP TABLE product");
            Toast.makeText(this, "Delete Successful", Toast.LENGTH_LONG).show();
        }
        db.close();
    }
}