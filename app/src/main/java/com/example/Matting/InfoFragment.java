package com.example.Matting;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class InfoFragment extends Fragment {
    private TextView tvTitle, tvCategory, tvDescription, tvLink, tvRating, tvMapX, tvMapY;
    private ImageView btnClose;
    private Button btnReserve;

    public static InfoFragment newInstance(String title, String category, String description, String link, double rating, int map_x, int map_y) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("category", category);
        args.putString("description", description);
        args.putString("link", link);
        args.putDouble("rating", rating);
        args.putInt("map_x", map_x);
        args.putInt("map_y", map_y);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);

        // UI 초기화
        tvTitle = view.findViewById(R.id.tvTitle);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvLink = view.findViewById(R.id.tvLink);
        tvRating = view.findViewById(R.id.tvRating);
        tvMapX = view.findViewById(R.id.tvMapX);
        tvMapY = view.findViewById(R.id.tvMapY);
        btnClose = view.findViewById(R.id.btnClose);

        if (getArguments() != null) {
            tvTitle.setText(getArguments().getString("title"));
            tvCategory.setText(getArguments().getString("category"));
            tvDescription.setText(getArguments().getString("description"));
            tvLink.setText(getArguments().getString("link"));
            tvRating.setText(String.valueOf(getArguments().getDouble("rating")));
            tvMapX.setText(String.valueOf(getArguments().getInt("map_x")));
            tvMapY.setText(String.valueOf(getArguments().getInt("map_y")));
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // InfoFragment를 닫음
                getParentFragmentManager().popBackStack();

                // MainActivity의 메서드를 호출하여 BottomSheet 상태를 STATE_EXPANDED로 설정
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).showBottomSheetExpanded();
                } else {
                    Log.e("BottomSheetDebug", "MainActivity instance not found");
                }
            }
        });


        btnReserve = view.findViewById(R.id.btnReserve);
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CreateCommunityFragment 인스턴스 생성
                CreateCommunityFragment createCommunityFragment = new CreateCommunityFragment();

                // 데이터 전달을 위한 Bundle 생성
                Bundle bundle = new Bundle();
                bundle.putString("restaurant", getArguments().getString("title")); // InfoFragment에서 받은 title 전달
                bundle.putString("mapx", String.valueOf(getArguments().getInt("map_x")));
                bundle.putString("mapy", String.valueOf(getArguments().getInt("map_y")));

                Log.d("mapxy", "Info"+getArguments().getInt("map_x")+getArguments().getInt("map_y"));
                createCommunityFragment.setArguments(bundle);

                // Fragment 전환
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.info_fragment_container, createCommunityFragment);
                transaction.addToBackStack(null); // 뒤로 가기 스택에 추가
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // MainActivity의 BottomNavigationView 숨기기
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigation);
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // InfoFragment가 화면을 떠날 때 BottomNavigationView 다시 표시
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigation);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}
