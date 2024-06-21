package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midterm.Adapter.OrderProductAdapter;
import com.example.midterm.Model.Order;
import com.example.midterm.Model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailOrderActivity extends AppCompatActivity {
    ArrayList<Product> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order); // Gắn layout activity_detail_order.xml vào activity

        // Lấy dữ liệu đơn hàng từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            Order order = (Order) intent.getSerializableExtra("ORDER_DETAIL");
            if (order != null) {
                // Hiển thị thông tin đơn hàng trên giao diện
                displayOrderDetails(order);
            }
        }

        ImageView backBtnOrder = findViewById(R.id.backBtnOrder);
        backBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btnExport = findViewById(R.id.btn_order_pdf);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin đơn hàng từ Intent
                Intent intent = getIntent();
                if (intent != null) {
                    Order order = (Order) intent.getSerializableExtra("ORDER_DETAIL");
                    if (order != null) {
                        // Xuất hoá đơn
                        generateInvoice(order);
                    }
                }
            }
        });
    }

    private void displayOrderDetails(Order order) {
        // Lấy các View từ layout activity_detail_order.xml
        TextView txtOrderId = findViewById(R.id.orderId);
        TextView txtOrderName = findViewById(R.id.order_name);
        TextView txtOrderPhone = findViewById(R.id.order_phone);
        TextView txtOrderAddress = findViewById(R.id.order_address);
        TextView txtOrderStatus = findViewById(R.id.order_status);
        TextView txtOrderTotal = findViewById(R.id.order_total);
        TextView txtOrderTime = findViewById(R.id.order_time);

        // Hiển thị thông tin đơn hàng lên giao diện
        txtOrderId.setText("ID: " + order.getOrderId());
        txtOrderName.setText("Tên khách hàng: " + order.getName());
        txtOrderPhone.setText("Số điện thoại: " + order.getPhone());
        txtOrderAddress.setText("Địa chỉ: " + order.getAddress());
        txtOrderStatus.setText("Trạng thái: " + convertStatus(order.getStatus()));
        txtOrderTotal.setText("Tổng tiền: " + order.getTotal());
        txtOrderTime.setText("Thời gian đặt hàng: " + order.getOrderDateTime());

        // Bổ sung hiển thị sản phẩm đơn hàng trong RecyclerView (sử dụng Adapter)

        // Bắt đầu thêm phần hiển thị sản phẩm đơn hàng
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tạo adapter và thiết lập dữ liệu
        OrderProductAdapter adapter = new OrderProductAdapter(order.getProduct());
        recyclerView.setAdapter(adapter);

    }

    private String convertStatus(String status) {
        switch (status) {
            case "0":
                return "Đang xử lý";
            case "1":
                return "Đang chuẩn bị";
            case "2":
                return "Đang giao hàng";
            case "3":
                return "Đã giao hàng";
            case "-1":
                return "Đã hủy";
            default:
                return "";
        }
    }

    private int calculateTotal(List<Product> products) {
        // Tính tổng tiền của đơn hàng
        int total = 0;
        for (Product product : products) {
            total += product.getProductPrice() * product.getNumberInCart();
        }
        return total;
    }

    private void generateInvoice(Order order) {
        // Tạo đối tượng PDFDocument
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        // Tạo PageInfo cho trang PDF
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 800, 1).create();

        // Bắt đầu trang PDF
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Lấy Canvas để vẽ các đối tượng lên trang PDF
        Canvas canvas = page.getCanvas();

        // Tạo Paint để vẽ văn bản và các đối tượng khác
        paint.setColor(Color.BLACK);

        // Thiết lập kích thước và vị trí của các dòng văn bản
        int x = 20;
        int y = 25;
        int lineHeight = 30;

        // Biến đếm cho số thứ tự của sản phẩm
        int itemCount = 1;

        // Vẽ các thông tin của đơn hàng lên trang PDF
        // Thiết lập kiểu chữ in đậm
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


        // Đo chiều rộng của văn bản
        float textWidth = paint.measureText("HOÁ ĐƠN THANH TOÁN");
        float center = (pageInfo.getPageWidth() - textWidth) / 2; // Tính toán vị trí để văn bản nằm ở trung tâm

        // Vẽ văn bản "HOÁ ĐƠN THANH TOÁN" ở trung tâm
        canvas.drawText("HOÁ ĐƠN THANH TOÁN", center, y, titlePaint);
        y += lineHeight;



        // Đo chiều rộng của văn bản
        textWidth = paint.measureText("Số: " + order.getOrderId());
        center = (pageInfo.getPageWidth() - textWidth) / 2; // Tính toán vị trí để văn bản nằm ở trung tâm

        // Vẽ văn bản "Số: " + order.getOrderId() ở trung tâm
        canvas.drawText("Số: " + order.getOrderId(), center, y, titlePaint);
        y += lineHeight;


        canvas.drawText("Thời gian đặt hàng: " + order.getOrderDateTime(), x, y, paint);
        y += lineHeight;
        canvas.drawText("Tên khách hàng: " + order.getName(), x, y, paint);
        y += lineHeight;

        // Vẽ bảng chi tiết sản phẩm đơn hàng
        int startX = 25;
        int startY = y; // Vị trí bắt đầu vẽ bảng
        int column1 = startX; // Cột "#"
        int column2 = column1 + 50; // Cột "Tên món"
        int column3 = column2 + 100; // Cột "Giá tiền"
        int column4 = column3 + 70; // Cột "Số lượng"
        int column5 = column4 + 80; // Cột "Tổng tiền"
        int rowHeight = 40; // Chiều cao mỗi dòng

        // Vẽ tiêu đề cho bảng
        canvas.drawText("#", column1, startY + rowHeight, paint);
        canvas.drawText("Tên món", column2, startY + rowHeight, paint);
        canvas.drawText("Giá tiền", column3, startY + rowHeight, paint);
        canvas.drawText("Số lượng", column4, startY + rowHeight, paint);
        canvas.drawText("Tổng tiền", column5, startY + rowHeight, paint);

        // Vẽ đường kẻ ngang cho bảng
        canvas.drawLine(column1 - 20, startY + 10, column5 + 50, startY + 10, paint);

        // Vẽ thông tin chi tiết sản phẩm
        for (Product product : order.getProduct()) {
            startY += rowHeight; // Di chuyển xuống dòng mới
            canvas.drawText(String.valueOf(itemCount), column1, startY + rowHeight, paint); // Cột "#"
            canvas.drawText(product.getProductTitle(), column2, startY + rowHeight, paint); // Cột "Tên món"
            canvas.drawText(String.valueOf(product.getProductPrice()), column3, startY + rowHeight, paint); // Cột "Giá tiền"
            canvas.drawText(String.valueOf(product.getNumberInCart()), column4, startY + rowHeight, paint); // Cột "Số lượng"
            canvas.drawText(String.valueOf(product.getProductPrice() * product.getNumberInCart()), column5, startY + rowHeight, paint); // Cột "Tổng tiền"
            itemCount++; // Tăng giá trị số thứ tự cho sản phẩm tiếp theo
        }

        titlePaint.setTextSize(16f);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Tổng thanh toán: " + order.getTotal(), canvas.getWidth() / 2, startY + rowHeight + 50, titlePaint);
        canvas.drawText("Cảm ơn bạn vì đã lựa chọn chúng tôi!", canvas.getWidth() / 2, startY + rowHeight + 70, titlePaint);


        // Đường dẫn của ảnh QR trong thư mục drawable
        Bitmap qrBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qr);

        // Kích thước mới cho ảnh QR (độ rộng và chiều cao mong muốn)
        int newQrWidth = 160; // Độ rộng mới của ảnh (đơn vị: pixel)
        int newQrHeight = 160; // Chiều cao mới của ảnh (đơn vị: pixel)

        // Thay đổi kích thước của ảnh QR
        Bitmap scaledQrBitmap = Bitmap.createScaledBitmap(qrBitmap, newQrWidth, newQrHeight, false);

        // Vị trí của ảnh QR trên canvas
        int qrX = (canvas.getWidth() - newQrWidth) / 2; // Đặt ảnh ở giữa theo chiều ngang
        int qrY = startY + rowHeight + 80; // Vị trí dưới các dòng văn bản

        // Vẽ ảnh QR lên canvas
        canvas.drawBitmap(scaledQrBitmap, qrX, qrY, paint);

        // Kết thúc trang PDF
        pdfDocument.finishPage(page);

        // Lưu file PDF vào bộ nhớ với tên là ID của đơn hàng
        File file = new File(getExternalFilesDir(null), "ID. " + order.getOrderId() + ".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            pdfDocument.close();
            Toast.makeText(this, "Đã lưu hoá đơn thành công", Toast.LENGTH_SHORT).show();
            // Mở trình duyệt file để người dùng có thể tải xuống
            openPdfFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu hoá đơn", Toast.LENGTH_SHORT).show();
        }


        // Đóng activity sau khi xuất hoá đơn
        finish();
    }

    private void openPdfFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }




}
