package hust.soict.it2.sonbv.appsqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProduct extends AppCompatActivity {
    boolean isUpdate;
    int idProduct;
    EditText editName, editPrice;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
        if (isUpdate) {

            idProduct = intent.getIntExtra("idProduct", 0);

            SQLiteDatabase db = openOrCreateDatabase(MainActivity.DB_NAME, Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT id, name, price from product where id = ?",
                    new String[]{idProduct + ""});
            cursor.moveToFirst();
            int productID = cursor.getInt(0);
            String productName = cursor.getString(1);
            int productPrice = cursor.getInt(2);
            product = new Product(productID, productName, productPrice);
            cursor.close();

            findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase db = openOrCreateDatabase(MainActivity.DB_NAME, Context.MODE_PRIVATE, null);
                    db.execSQL("DELETE FROM product where id = ?", new String[]{String.valueOf(idProduct)});
                    db.close();
                    finish();
                }
            });
        } else {

            product = new Product(0,"",0);
            findViewById(R.id.delete_btn).setVisibility(View.GONE);
            ((Button) findViewById(R.id.save_btn)).setText("CREATE NEW PRODUCT");
        }

        editName = findViewById(R.id.name_product_edittext);
        editPrice = findViewById(R.id.price_product_edittext);

        editName.setText(product.getName());
        editPrice.setText(product.getPrice() + "");

        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase(MainActivity.DB_NAME, Context.MODE_PRIVATE, null);
                product.setName(editName.getText().toString());
                product.setPrice(Integer.parseInt(editPrice.getText().toString()));

                if (isUpdate) {
                    //Cập nhật
                    db.execSQL("UPDATE product SET name=?, price = ? where id = ?",
                            new String[]{product.getName(), product.getPrice() + "", product.getID() + ""});
                } else {
                    //Tạo
                    //Cập nhật
                    db.execSQL("INSERT INTO product (name, price ) VALUES (?,?)",
                            new String[]{product.getName(), product.getPrice() + ""});
                }
                db.close();
                finish();
            }
        });

    }
}