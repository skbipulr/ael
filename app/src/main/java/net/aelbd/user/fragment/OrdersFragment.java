package net.aelbd.user.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.aelbd.user.R;
import net.aelbd.user.databinding.FragmentOrdersBinding;


public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;

    public OrdersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_orders, container, false);

        binding.swipeRefreshL.setRefreshing(true);
        getOrders();

        binding.swipeRefreshL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrders();
            }
        });

        return binding.getRoot();
    }

    private void getOrders() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshL.setRefreshing(false);
                binding.topL.setVisibility(View.VISIBLE);
            }
        },2000);
    }


}
