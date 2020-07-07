package com.agenin.report.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.report.Activity.DetailActivity;
import com.agenin.report.DBQueries;
import com.agenin.report.Model.CartItemModel;
import com.agenin.report.R;

import java.util.List;

import info.hoang8f.widget.FButton;

public class CartPaymentAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;


    public CartPaymentAdapter(List<CartItemModel> cartItemModelList,Context context) {
        this.cartItemModelList = cartItemModelList;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cart_item,viewGroup,false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cart_total,viewGroup,false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String title = cartItemModelList.get(position).getProductTitle();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                int quantity = cartItemModelList.get(position).getProductQuantity();
                String satuan = cartItemModelList.get(position).getSatuan();
                String ongkir = cartItemModelList.get(position).getOngkir();
                String handphonePenjual = cartItemModelList.get(position).getHandphonePenjual();


                ((CartItemViewholder)viewHolder).setItemDetails(productID,title,quantity,productPrice,satuan,ongkir,handphonePenjual);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice =0;
                int totalDeleveryItemPrice =0;
                String deliveryPrice = "";
                int totalAmount ;
                int saveAmount = 0;


                for (int x=0;x<cartItemModelList.size();x++){

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).getInStock()){
                        int qty = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                        totalItems= totalItems + qty;
                        totalItemPrice = totalItemPrice + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())* qty);
                        totalDeleveryItemPrice = totalDeleveryItemPrice +((int)(8000*cartItemModelList.get(x).getProductWeight())*qty);

                        if (totalItemPrice >= 200000 ) {
                            saveAmount = saveAmount+30000;
                        }

                    }
                }


                int totalDeliveryDiskon;
                totalDeliveryDiskon = totalDeleveryItemPrice-saveAmount;

//                if (totalItemPrice >= 200000 ){
//                    //  PaymentActivity.isfree = true;
//                    saveAmount = 30000;
//                    totalDeliveryDiskon = totalDeleveryItemPrice-30000;
//                }else {
//                    saveAmount = 0;
//                    totalDeliveryDiskon = totalDeleveryItemPrice;
//                }

                totalAmount = totalItemPrice +totalDeliveryDiskon;
                deliveryPrice = String.valueOf(totalDeleveryItemPrice);

                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemsPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(saveAmount);




                    ((CartTotalAmountViewholder) viewHolder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, saveAmount, position);

            default:
                return;

        }

        if (lastPosition <position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder {

        private TextView productTitle;
        private TextView productPrice;
        private TextView productQuantity;
        private TextView harga;
        private TextView ongkir;
        private TextView satuan;
        private FButton phonePenjual;

        public CartItemViewholder(View cartItemView) {
            super(cartItemView);
            productTitle = cartItemView.findViewById(R.id.nama_barang);
            productPrice = cartItemView.findViewById(R.id.hrg_barang);
            productQuantity = cartItemView.findViewById(R.id.jml_barang);
            harga = cartItemView.findViewById(R.id.harga);
            ongkir = cartItemView.findViewById(R.id.ongkir);
            satuan = cartItemView.findViewById(R.id.satuan);
            phonePenjual = cartItemView.findViewById(R.id.call);
        }

        private void setItemDetails(String productID, String title, int quantity, String productPriceText,  String satuanText,String ongkirText,String handphonePenjualText) {
            productTitle.setText(title);
            phonePenjual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noWA = handphonePenjualText;

                    if (noWA.substring(0,1).equals("0")){
                        noWA="+62"+noWA.substring(1,noWA.length());
                    }

                    String url = "https://api.whatsapp.com/send?phone=" + noWA;
                    try {
                        PackageManager pm = context.getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(context, "Anda Belum menginstall WhatsApp", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
            ongkir.setText("Total Ongkir + Rp "+ DBQueries.currencyFormatter(ongkirText));
            if (satuanText.equals("")){
              satuan.setText("");
            }else {
                satuan.setText(" "+satuanText);
            }
            productQuantity.setText("x"+ Long.toString(quantity));

            harga.setText("Total Harga + Rp "+ DBQueries.currencyFormatter(String.valueOf(Integer.parseInt(productPriceText)*quantity)));
            productPrice.setText("Rp "+DBQueries.currencyFormatter(String.valueOf((Integer.parseInt(productPriceText)*quantity)+Integer.valueOf(ongkirText))));

        }
    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private  TextView totalItems;
        private  TextView totalItemPrice;
        private  TextView deliveryPrice;
        private  TextView totalAmount;
        private  TextView saveAmount;
        private TextView diskonText;
        private TextView diskon;



        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            saveAmount = itemView.findViewById(R.id.saved_amount);
            diskonText = itemView.findViewById(R.id.diskon_text);
            diskon = itemView.findViewById(R.id.diskon);

        }

        private  void setTotalAmount(int totalItemText, int totalItemPriceText,String deliveryPriceText,int totalAmountText, int saveAmountText,int position){
            totalItems.setText("Harga("+totalItemText+" barang)");
            totalItemPrice.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalItemPriceText)));
            deliveryPrice.setText("+ Rp "+DBQueries.currencyFormatter(deliveryPriceText));
            diskon.setVisibility(View.VISIBLE);
            diskonText.setVisibility(View.VISIBLE);
            diskon.setText( "- Rp "+DBQueries.currencyFormatter(String.valueOf(saveAmountText)));

            if (!(totalAmountText==0)) {
                totalAmount.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(totalAmountText)) );

                saveAmount.setText("Hemat Rp "+DBQueries.currencyFormatter(String.valueOf(saveAmountText)) +" pada pesanan ini");
            }
            if (totalItemPriceText == 0){
                cartItemModelList.remove(cartItemModelList.size()-1);
            }

        }


    }
}
