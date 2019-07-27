package net.aelbd.user.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.aelbd.user.R;
import net.aelbd.user.databinding.ActivityMainBinding;
import net.aelbd.user.fragment.AboutUsFragment;
import net.aelbd.user.fragment.HomeFragment;
import net.aelbd.user.fragment.OrdersFragment;
import net.aelbd.user.fragment.PaymentFragment;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private int currentPage = 0;
    private boolean isForOrder = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (currentPage!=0){
                        currentPage = 0;
                        replaceFragment(new HomeFragment());
                    }
                    return true;
                case R.id.navigation_orders_history:
                    if (currentPage!=1){
                        currentPage = 1;
                        replaceFragment(new OrdersFragment());
                    }
                    return true;
                case R.id.navigation_payment:
                    if (currentPage!=2){
                        currentPage = 2;
                        replaceFragment(new PaymentFragment());
                    }
                    return true;
                case R.id.navigation_about:
                    if (currentPage!=3){
                        currentPage = 3;
                        replaceFragment(new AboutUsFragment());
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (getIntent().getExtras()!=null){
            isForOrder = getIntent().getBooleanExtra("isForOrder",false);
        }

        if (isForOrder==false){
            currentPage = 0;
            replaceFragment(new HomeFragment());
        }else {
            View view = binding.navView.findViewById(R.id.navigation_orders_history);
            view.performClick();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameL,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (currentPage==0){
            super.onBackPressed();
        }else {
            View view = binding.navView.findViewById(R.id.navigation_home);
            view.performClick();
        }
    }
}
