package net.aelbd.user.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import net.aelbd.user.R;
import net.aelbd.user.databinding.ActivityNewOrderBinding;

public class NewOrderActivity extends AppCompatActivity {

    private ActivityNewOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_order);


        binding.agreeCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    binding.confirmOrderBtn.setEnabled(true);
                    binding.confirmOrderBtn.setTextColor(getResources().getColor(R.color.white));
                    binding.confirmOrderBtn.setBackgroundResource(R.drawable.background_enabled);
                } else {
                    binding.confirmOrderBtn.setEnabled(false);
                    binding.confirmOrderBtn.setTextColor(getResources().getColor(R.color.black));
                    binding.confirmOrderBtn.setBackgroundResource(R.drawable.backgroud_disable);
                }
            }
        });

        binding.confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String product = binding.productNameET.getText().toString();
                String quantity = binding.quantityET.getText().toString();

                if (product.equals("")) {
                    Toast.makeText(NewOrderActivity.this, "Please enter product name", Toast.LENGTH_SHORT).show();
                } else if (quantity.equals("")) {
                    Toast.makeText(NewOrderActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(NewOrderActivity.this,MainActivity.class).putExtra("isForOrder",true)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                    Toast.makeText(NewOrderActivity.this, "Order confirmed", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void backBtn(View view) {
        onBackPressed();
    }
}
