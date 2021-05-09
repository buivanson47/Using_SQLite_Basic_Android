package hust.soict.it2.sonbv.appsqlitedemo;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductListViewAdapter extends BaseAdapter {
    final ArrayList<Product> listProduct;

    public ProductListViewAdapter(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
    }

    @Override
    public int getCount() {
        // return size of listProduct
        return listProduct.size();
    }

    @Override
    public Object getItem(int position) {
        // return data at position in listProduct
        return listProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        // return ID of data
        return listProduct.get(position).getID();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewProduct;

        if (convertView == null) {
            viewProduct = View.inflate(parent.getContext(), R.layout.product_view, null);
        } else viewProduct = convertView;

        // Bind data into View
        Product product = (Product) getItem(position);
        ((TextView) viewProduct.findViewById(R.id.id_product)).setText(String.format("ID = %d", product.getID()));
        ((TextView) viewProduct.findViewById(R.id.name_product)).setText(String.format("Name: %s", product.getName()));
        ((TextView) viewProduct.findViewById(R.id.price_product)).setText(String.format("Price: %dVND", product.getPrice()));

        return viewProduct;
    }
}
