package com.example.retrofitapp.odishadairy.Activity.SalesModule.SalesFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofitapp.odishadairy.Activity.Activity.Milk_collection_Report_UI;
import com.example.retrofitapp.odishadairy.Activity.Activity.Sharedpref;
import com.example.retrofitapp.odishadairy.Activity.Adapter.BannerImageAdapter;
import com.example.retrofitapp.odishadairy.Activity.Adapter.Month_Report_Adapter;
import com.example.retrofitapp.odishadairy.Activity.Model.Milk_Report_Month_MODEL;
import com.example.retrofitapp.odishadairy.Activity.Model.get_retailer_MODEL;
import com.example.retrofitapp.odishadairy.Activity.Model.own_retailer_List;
import com.example.retrofitapp.odishadairy.Activity.Model.sales_executive_List;
import com.example.retrofitapp.odishadairy.Activity.POJO.Milk_Report_Month_POJO;
import com.example.retrofitapp.odishadairy.Activity.POJO.get_retailer_POJO;
import com.example.retrofitapp.odishadairy.Activity.Retro.AppConstants;
import com.example.retrofitapp.odishadairy.Activity.Retro.NetworkHandler;
import com.example.retrofitapp.odishadairy.Activity.Retro.RestApis;
import com.example.retrofitapp.odishadairy.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    ArrayList<own_retailer_List> mOwnRetailerListId = new ArrayList<>();
    ArrayList<sales_executive_List> mSalesExecutiveList = new ArrayList<>();
    RadioButton[] mRadiobutton;
    String[] Names = {"Sunil Kara", "Amit Majhee", "Sai Dash", "Suryakanta Nath","Biswajit Behera"};
    ArrayList<String> ImageList = new ArrayList<>();
    RecyclerView BannerRecyclerview;
    BannerImageAdapter bannerImageAdapter;
    TextView mButton,viewbutton;
    RadioGroup radioGroup;
    String type="3";
    int id = 1;
    RadioButton radioButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);

        BannerRecyclerview = (RecyclerView)view.findViewById(R.id.BannerRecyclerview);
        mButton = (TextView) view.findViewById(R.id.mButton);
        viewbutton = (TextView) view.findViewById(R.id.view);
        viewbutton.setVisibility(View.GONE);
        BannerRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        BannerRecyclerview.setHasFixedSize(true);
        BannerRecyclerview.setNestedScrollingEnabled(false);
        ImageList.add("https://img.freepik.com/premium-vector/milk-product-splash-realistic-isolated-blue_1284-56497.jpg?w=2000");
        ImageList.add("https://i.pinimg.com/originals/d6/93/b8/d693b84ca034b1a00e73ea54cdb6668f.jpg");
        ImageList.add("https://theorganicmilk.com/wp-content/uploads/2015/02/banner1-1200x500.jpg");
        GetBannerData();
      // GetRadioName();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)view.findViewById(checkedRadioButtonId);
                Toast.makeText(getActivity(), radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewbutton.setVisibility(View.VISIBLE);
                viewbutton.setAlpha(0.0f);

// Start the animation
                viewbutton.animate()
                        .translationY(viewbutton.getHeight())
                        .alpha(1.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                               // viewbutton.setVisibility(View.GONE);
                            }
                        });

            }
        });
        get_retailer();
        return view;
    }
    private void get_retailer() {

        get_retailer_POJO pojo = new get_retailer_POJO( id,type);

        RestApis restApis = NetworkHandler.getRetrofit().create(RestApis.class);
        Call<get_retailer_MODEL> call = restApis.get_retailer(pojo);
        call.enqueue(new Callback<get_retailer_MODEL>() {
            @Override
            public void onResponse(Call<get_retailer_MODEL> call, Response<get_retailer_MODEL> response) {
                if (response.body() != null) {
                    if (response.body().getCode() == 200) {
                       if ((response.body().getPayload().getOwn_retailer().size()>0)&&(response.body().getPayload().getSales_executive().size()>0)){
                           mOwnRetailerListId = new ArrayList<>();
                           mSalesExecutiveList = new ArrayList<>();
                           mOwnRetailerListId.addAll(response.body().getPayload().getOwn_retailer());
                           mSalesExecutiveList.addAll(response.body().getPayload().getSales_executive());
                           Log.e("GetRetailer","-->");

                               mRadiobutton = new RadioButton[mSalesExecutiveList.size()];
                           Log.e("GetRetailerGroup","-->");
                               for (int i = 0; i < mSalesExecutiveList.size(); i++) {
                                   Log.e("GetRetailerGroup2","-->");

                                   mRadiobutton[i] = new RadioButton(getActivity());

                                   mRadiobutton[i].setText(mSalesExecutiveList.get(i).getSalesexcutive_name());
                                   mRadiobutton[i].setId(mSalesExecutiveList.get(i).getId());
                                   radioGroup.addView(mRadiobutton[i]);



                           }
                       }
                    }
                } else {

                    Toast.makeText(getActivity(), "It seems your Network is unstable . Please Try again !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<get_retailer_MODEL> call, Throwable t) {
                Log.e("erroris", String.valueOf(t));
                Toast.makeText(getActivity(), "It seems your Network is unstable . Please Try again !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetRadioName() {
        mRadiobutton = new RadioButton[Names.length];
        for (int i = 0; i < Names.length; i++) {

            mRadiobutton[i] = new RadioButton(getActivity());
            mRadiobutton[i].setText(Names[i]);
            mRadiobutton[i].setId(i);
            radioGroup.addView(mRadiobutton[i]);

        }

    }

    private void GetBannerData() {
        bannerImageAdapter = new BannerImageAdapter(getActivity(), ImageList);
        BannerRecyclerview.setAdapter(bannerImageAdapter);

        setSpeedScrollTop(ImageList.size(), BannerRecyclerview);

    }
    private void setSpeedScrollTop(final int length, RecyclerView bannerRecyclerView) {
        final int speedScroll = 6000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {
                if (count < length) {
                    if (count == length - 1) {
                        flag = false;
                    } else if (count == 0) {
                        flag = true;
                    }
                    if (flag) count++;
                    else count--;

                    bannerRecyclerView.smoothScrollToPosition(count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

}