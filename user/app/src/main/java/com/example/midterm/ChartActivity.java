package com.example.midterm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.midterm.Model.Order;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private List<String> xValues = Arrays.asList("Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12");
    List<Order> orderList = new ArrayList<>();
    Button openDialog;
    private String startDay;
    private String endDay;
    TextView txtStartDay;
    TextView txtEndDay;
    Button btnStartDay;
    Button btnEndDay;
    Button btnThongKe;
    ImageView backBtnUser;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    float[] totals = new float[12];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        openDialog = findViewById(R.id.btnTime);
        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSelectTimeStatistical(Gravity.CENTER);
            }
        });

        backBtnUser = findViewById(R.id.backBtnOrder);
        backBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void openDialogSelectTimeStatistical(int gravity) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_time_statistical);

//        Ánh xạ Dialog
        txtStartDay = dialog.findViewById(R.id.txtStartDay);
        txtEndDay = dialog.findViewById(R.id.txtEndDay);
        btnStartDay = dialog.findViewById(R.id.btnStartDay);
        btnEndDay = dialog.findViewById(R.id.btnEndDay);
        btnThongKe = dialog.findViewById(R.id.btnThongKe);

        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderList.clear();
                getDataToChart();
                dialog.dismiss();
            }
        });

        btnStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thời gian hiện tại
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Hiển thị DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ChartActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Xử lý ngày được chọn
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                txtStartDay.setText(selectedDate);
                                startDay = selectedDate;
                            }
                        }, year, month, dayOfMonth);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });

        btnEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thời gian hiện tại
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Hiển thị DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ChartActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Xử lý ngày được chọn
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                txtEndDay.setText(selectedDate);
                                endDay = selectedDate;
                            }
                        }, year, month, dayOfMonth);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });

//        Hiển thị dialog
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        dialog.show();
    }


    public void getDataToChart() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ordersRef = database.getReference("Orders");
        ordersRef.orderByChild("status").equalTo("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);

                    // Tách ngày, tháng và năm từ đối tượng Date
                    SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

//                    Ngày tháng dữ liệu lay tu DB
                    Date date = null;
                    try {
                        date = dateFormat.parse(order.getOrderDateTime());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    String day = dayFormat.format(date);
                    String month = monthFormat.format(date);
                    String year = yearFormat.format(date);

                    int dayInt = Integer.parseInt(day);
                    int monthInt = Integer.parseInt(month);
                    int yearInt = Integer.parseInt(year);

//                    Ngày bắt đầu người dùng chọn
                    Date startDateSelect = null;
                    try {
                        startDateSelect = dateFormat.parse(startDay);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    String startDaySelect = dayFormat.format(startDateSelect);
                    String startMonthSelect = monthFormat.format(startDateSelect);
                    String startYearSelect = yearFormat.format(startDateSelect);

                    int startDayInt = Integer.parseInt(startDaySelect);
                    int startMonthInt = Integer.parseInt(startMonthSelect);
                    int startYearInt = Integer.parseInt(startYearSelect);

//                    Ngày kết thúc người dùng chọn
                    Date endDateSelect = null;
                    try {
                        endDateSelect = dateFormat.parse(endDay);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    String endDaySelect = dayFormat.format(endDateSelect);
                    String endMonthSelect = monthFormat.format(endDateSelect);
                    String endYearSelect = yearFormat.format(endDateSelect);

                    int endDayInt = Integer.parseInt(endDaySelect);
                    int endMonthInt = Integer.parseInt(endMonthSelect);
                    int endYearInt = Integer.parseInt(endYearSelect);

                    boolean dayBoolean = (dayInt >= startDayInt && dayInt <= endDayInt);
                    boolean monthBoolean = monthInt >= startMonthInt && monthInt <= endMonthInt;
                    boolean yearBoolean = yearInt >= startYearInt && yearInt <= endYearInt;

                    if (dayBoolean && monthBoolean && yearBoolean) {
                        orderList.add(order);
                    }
                }
                loadChartFromData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Error when take data from firebase!!!");
            }
        });


    }

    public void loadChartFromData() {

        BarChart barChart = findViewById(R.id.chart);
        barChart.getAxisRight().setDrawLabels(false);

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
           totals[i] = 0;
        }

        int size = orderList.size();
        for (int i = 0; i < size; i++) {
            Order order = orderList.get(i);
            String priceWithoutCurrency = order.getTotal().replace("đ", "");
            float priceFloat = Float.parseFloat(priceWithoutCurrency);

            String[] parts = order.getOrderDateTime().split(",");
            String datePart = parts[0];
            String[] dateParts = datePart.split("/");
            String monthString = dateParts[1];
            int month = Integer.parseInt(monthString);

            if (month == 1) {
                totals[0] += priceFloat;
            } else if (month == 2) {
                totals[1] += priceFloat;
            } else if (month == 3) {
                totals[2] += priceFloat;
            } else if (month == 4) {
                totals[3] += priceFloat;
            } else if (month == 5) {
                totals[4] += priceFloat;
            } else if (month == 6) {
                totals[5] += priceFloat;
            } else if (month == 7) {
                totals[6] += priceFloat;
            } else if (month == 8) {
                totals[7] += priceFloat;
            } else if (month == 9) {
                totals[8] += priceFloat;
            } else if (month == 10) {
                totals[9] += priceFloat;
            } else if (month == 11) {
                totals[10] += priceFloat;
            } else if (month == 12) {
                totals[11] += priceFloat;
            }
        }

        entries.add(new BarEntry(0, totals[0]));
        entries.add(new BarEntry(1, totals[1]));
        entries.add(new BarEntry(2, totals[2]));
        entries.add(new BarEntry(3, totals[3]));
        entries.add(new BarEntry(4, totals[4]));
        entries.add(new BarEntry(5, totals[5]));
        entries.add(new BarEntry(6, totals[6]));
        entries.add(new BarEntry(7, totals[7]));
        entries.add(new BarEntry(8, totals[8]));
        entries.add(new BarEntry(9, totals[9]));
        entries.add(new BarEntry(10, totals[10]));
        entries.add(new BarEntry(11, totals[11]));

        float maxTotal = findMaxNumber(totals);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(maxTotal + 10000);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);


        int total = calSum(totals);
        BarDataSet dataSet = new BarDataSet(entries, "TOTAL: " + total + " đ");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.notifyDataSetChanged();
    }

    public float findMaxNumber(float[] numbers) {
        // Khởi tạo số lớn nhất là số đầu tiên trong mảng
        float maxNumber = numbers[0];

        // Duyệt qua từng số trong mảng
        for (int i = 1; i < numbers.length; i++) {
            // So sánh số hiện tại với số lớn nhất
            if (numbers[i] > maxNumber) {
                maxNumber = numbers[i];
            }
        }
        // Trả về số lớn nhất
        return maxNumber;
    }

    public int calSum(float[] numbers) {
        int total = 0;
        for (int i = 0; i < numbers.length; i++) {
            total += numbers[i];
        }
        return total;
    }

}