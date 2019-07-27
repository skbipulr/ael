package net.aelbd.user.bottomSheet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.aelbd.user.R;
import net.aelbd.user.activity.NewOrderActivity;
import net.aelbd.user.databinding.BottomSheetRestaurantLayoutBinding;

public class RestaurantDetailsBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetRestaurantLayoutBinding binding;
    private String name,address;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_restaurant_layout,container,false);

        if (getArguments() != null) {
            name = getArguments().getString("name");
            address = getArguments().getString("address");
            binding.nameTV.setText(name);
            binding.addressTV.setText(address);
        } else {
            name = "";
            address = "";
        }

        binding.orderNowTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewOrderActivity.class));
                dismiss();
            }
        });

        return binding.getRoot();
    }
}
