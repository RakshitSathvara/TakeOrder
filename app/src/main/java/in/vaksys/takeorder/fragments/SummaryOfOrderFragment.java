package in.vaksys.takeorder.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.vaksys.takeorder.R;

/**
 * Created by dell980 on 6/27/2016.
 */
public class SummaryOfOrderFragment extends Fragment {

    private Button btnGenerate;
    private LinearLayout ll1;
    private View clickSummaryGenerate;
    private EditText startDate, endDate;
    private DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat sdf;
    private SimpleDateFormat dateFormatter;
    private String start, end;

    public static SummaryOfOrderFragment newInstance(int index) {
        SummaryOfOrderFragment fragment = new SummaryOfOrderFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fagment_summary_of_order, container, false);

        btnGenerate = (Button) rootView.findViewById(R.id.btn_generate_report);
        ll1 = (LinearLayout) rootView.findViewById(R.id.ll1);
        clickSummaryGenerate = rootView.findViewById(R.id.click_generate_report_summary);

        startDate = (EditText) rootView.findViewById(R.id.et_startDate);
        endDate = (EditText) rootView.findViewById(R.id.et_endDate);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        clickSummaryGenerate.setVisibility(View.GONE);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSummaryGenerate.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);

            }
        });

        return rootView;
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        start = sdf.format(myCalendar.getTime());
        startDate.setText(start);
    }


    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        }

    };

    private void updateLabel1() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        end = sdf.format(myCalendar.getTime());
        endDate.setText(end);
    }
}
