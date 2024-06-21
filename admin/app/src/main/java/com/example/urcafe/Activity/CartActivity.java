    package com.example.urcafe.Activity;
    import android.Manifest;
    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.os.Bundle;
    import android.os.CountDownTimer;
    import android.os.Handler;
    import android.text.Editable;
    import android.text.TextUtils;
    import android.text.TextWatcher;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ScrollView;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.text.DecimalFormat;
    import java.text.SimpleDateFormat;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.List;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import com.example.urcafe.Adapter.CartAdapter;
    import com.example.urcafe.Common.Common;
    import com.example.urcafe.Helper.ManagmentCart;
    import com.example.urcafe.Model.Order;
    import com.example.urcafe.R;
    import com.google.android.material.floatingactionbutton.FloatingActionButton;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class CartActivity extends AppCompatActivity {
        private RecyclerView.Adapter adapter;
        private ManagmentCart managmentCart;
        private TextView total;
        private RecyclerView rcCart;
        private Button checkoutBtn, addressBtn, paymentBtn;
        private ScrollView scrollViewCart;
        private static final int MAP_REQUEST_CODE = 100;
        private boolean cancelClicked = false;
        private EditText inputPromo;
        private TextView promoDes,discount,totalPay;
        private FloatingActionButton validateBtn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cart);

            total = findViewById(R.id.tongTien);
            rcCart = findViewById(R.id.rc_cart);
            checkoutBtn = findViewById(R.id.checkoutBtn);
            scrollViewCart = findViewById(R.id.scrollViewCart);
            inputPromo=findViewById(R.id.inputPromo);
            promoDes=findViewById(R.id.promoDes);
            validateBtn=findViewById(R.id.validateBtn);
            discount=findViewById(R.id.discount);
            totalPay=findViewById(R.id.payPrice);
            managmentCart = new ManagmentCart(this);

            if(isPromoCodeApplied){
                promoDes.setVisibility(View.VISIBLE);
                inputPromo.setText(promoCode);
                promoDes.setText(promoDescript);
            }else{
                promoDes.setVisibility(View.GONE);
            }

            setVariable();
            calculatorCart();
            initList();
        }
        private void initList() {
            if (managmentCart.getListCart().isEmpty()) {
                scrollViewCart.setVisibility(View.GONE);
            } else {
                scrollViewCart.setVisibility(View.VISIBLE);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rcCart.setLayoutManager(linearLayoutManager);
            adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart());
            rcCart.setAdapter(adapter);
        }

        private void calculatorCart() {
            double itemTotal = managmentCart.getTotalFee();
            int itemTotalInt = (int) itemTotal;
            total.setText(itemTotalInt + "đ");
        }

        private void setVariable() {
            checkoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMapDialog();
                }
            });
            validateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String promotionCode = inputPromo.getText().toString().trim();
                    if(TextUtils.isEmpty(promotionCode)){
                        Toast.makeText(CartActivity.this,"Hãy nhập mã giảm giá",Toast.LENGTH_SHORT).show();
                    }else{
                        checkCodeAvailable(promotionCode);
                    }
                }
            });
            inputPromo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Không cần thực hiện hành động ở đây
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Không cần thực hiện hành động ở đây
                    removePromoCode();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)) {
                        // Nếu EditText rỗng, xóa mã giảm giá
                        removePromoCode();
                    }
                }
            });
        }

        private void removePromoCode() {
            isPromoCodeApplied = false;
            promoCode = "";
            promoDescript = "";
            promoExpDate = "";
            promoMinimumPriceOrder = "";
            promoPrice = "";

            // Cập nhật giao diện người dùng
            promoDes.setVisibility(View.GONE);
            promoDes.setText("");
            discount.setText("");
            totalPay.setText(total.getText().toString());
        }


        public boolean isPromoCodeApplied = false;
        public String promoCode,promoDescript,promoExpDate,promoMinimumPriceOrder,promoPrice;
        private void checkCodeAvailable(String promotionCode){
            //progress bar
            ProgressDialog  progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Đang kiểm tra mã giảm giá của bạn");
            progressDialog.setCanceledOnTouchOutside(false);

            //progress is not applied yet



            //check promoCode available
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Promotion");
            ref.orderByChild("promoCode").equalTo(promotionCode).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //check if promoCode exist
                    if(snapshot.exists()){
                        progressDialog.dismiss();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            promoCode=""+ds.child("promoCode").getValue();
                            promoDescript=""+ds.child("description").getValue();
                            promoExpDate=""+ds.child("expireDate").getValue();
                            promoMinimumPriceOrder=""+ds.child("minimumOrderPrice").getValue();
                            promoPrice=""+ds.child("promoPrice").getValue();

                            //check promoCode is expried or not
                            checkCodeExpireDate();

                        }
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(CartActivity.this,"Mã không hợp lệ",Toast.LENGTH_SHORT).show();
                        promoDes.setVisibility(View.GONE);
                        promoDes.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void checkCodeExpireDate() {
            //get current day
            Calendar calendar = Calendar.getInstance();
            int nam = calendar.get(Calendar.YEAR);
            int thang = calendar.get(Calendar.MONTH)+1;
            int ngay = calendar.get(Calendar.DAY_OF_MONTH);
            //dinh dang
            String todayDate=ngay+"/"+thang+"/"+nam;
            //check expire
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date currentDate = simpleDateFormat.parse(todayDate);
                Date expireDate = simpleDateFormat.parse(promoExpDate);
                //compare Date
                if(expireDate.compareTo(currentDate)>0){
                    checkMinimumOrderPrice();
                } else if (expireDate.compareTo(currentDate)< 0) {
                    Toast.makeText(this,"Mã giảm giá đã hết hạn"+ promoExpDate,Toast.LENGTH_SHORT).show();
                    promoDes.setVisibility(View.GONE);
                    promoDes.setText("");
                } else if (expireDate.compareTo(currentDate)==0) {
                    checkMinimumOrderPrice();
                }

            }
            catch (Exception e){
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }

        private void checkMinimumOrderPrice() {
                double itemTotal = managmentCart.getTotalFee();

                double minimumPrice = Double.parseDouble(promoMinimumPriceOrder);
            if(itemTotal<minimumPrice){
                Toast.makeText(this,"Không đủ điều kiện để áp dụng mã",Toast.LENGTH_SHORT).show();
                promoDes.setVisibility(View.GONE);
                promoDes.setText("");
            }else {
                promoDes.setVisibility(View.VISIBLE);
                promoDes.setText(promoDescript);
                // Calculate discount and update payPrice
                double discountValue = Double.parseDouble(promoPrice);
                double newTotal = itemTotal - discountValue;
                int newTotalInt = (int) newTotal;
                discount.setText("-" + promoPrice + "đ");
                totalPay.setText(newTotalInt +"đ");
                isPromoCodeApplied = true;
            }
        }

        private void showMapDialog() {
            Intent intent = new Intent(this, ChooseAddressActivity.class);
            startActivityForResult(intent, MAP_REQUEST_CODE);
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == MAP_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
                String address = data.getStringExtra("address");
                saveOrderToDatabase(address);
            }
        }

        private void saveOrderToDatabase(String address) {
            List cart = new ManagmentCart(this).getListCart();
            String totalStr = total.getText().toString(); // Lấy giá trị từ TextView total
            String newTotal;
            if (isPromoCodeApplied) {
                newTotal =totalPay.getText().toString();
            } else {
                newTotal = totalStr;
            }


            Order order = new Order(Common.currentUser.getPhone(), Common.currentUser.getName(), address,newTotal , cart);
            //submit on firebase
            FirebaseDatabase database;
            database = FirebaseDatabase.getInstance();
            DatabaseReference orders;
            orders = database.getReference("Orders");
            String orderId = String.valueOf(System.currentTimeMillis());
            orders.child(orderId).setValue(order);



            showSuccessDialog(address,orderId);
        }
        private void showSuccessDialog(String address,String orderId) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đặt hàng thành công");
            View view = getLayoutInflater().inflate(R.layout.dialog_success, null);
            builder.setView(view);
            TextView messageTextView = view.findViewById(R.id.messageTextView);
            TextView countDownTextView = view.findViewById(R.id.countDownTextView);
            Button cancelButton = view.findViewById(R.id.cancelButton); // Thêm nút hủy đơn hàng
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference orders = database.getReference("Orders");
            messageTextView.setText("Đã xác định được địa chỉ của đơn hàng: " + address +"\n"+ " \nQuý khách vui lòng đợi để cửa hàng tiếp nhận đơn hàng của bạn");
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            // Đặt thời gian chờ là 5 giây
            final int countDownTime = 10;
            new CountDownTimer(countDownTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    countDownTextView.setText("Thời gian còn lại: " + millisUntilFinished / 1000 + " giây");
                }
               

                public void onFinish() {
                    if (!cancelClicked) {
                        alertDialog.dismiss();
                        orders.child(orderId).child("status").setValue("1");
                        showSecondDialog();
                    }

                }
            }.start();


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    cancelOrder(orderId); // Gọi phương thức để hủy đơn hàng
                    cancelClicked = true;
                    showThirdDialog();
                }
            });
    }

        private void showThirdDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đặt hàng đã hủy")
                    .setMessage("Đơn hàng của bạn đã bị hủy")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Đóng dialog khi người dùng bấm nút OK
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        private void cancelOrder(String orderId) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference orders = database.getReference("Orders");
            orders.child(orderId).child("status").setValue("-1");
        }

        private void showSecondDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đặt hàng thành công")
                    .setMessage("Đơn hàng của bạn đã được tiếp nhận")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Đóng dialog khi người dùng bấm nút OK
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }



