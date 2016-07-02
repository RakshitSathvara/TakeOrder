package in.vaksys.takeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import in.vaksys.takeorder.R;

/**
 * Created by dell980 on 6/27/2016.
 */
public class SummaryOfOrderFragment extends Fragment {

    private Button btnGenerate;
    private LinearLayout ll1;
    private View clickSummaryGenerate;

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
}
