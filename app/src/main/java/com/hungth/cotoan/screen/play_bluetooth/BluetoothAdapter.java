package com.hungth.cotoan.screen.play_bluetooth;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hungth.cotoan.R;

import java.util.List;

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.BluetoothViewHolder> {
    private List<String> listBluetooths;
    private OnClickItemBluetooth onClickItemBluetooth;

    public BluetoothAdapter(List<String> listBluetooths, OnClickItemBluetooth onClickItemBluetooth) {
        this.listBluetooths = listBluetooths;
        this.onClickItemBluetooth = onClickItemBluetooth;
    }

    @NonNull
    @Override
    public BluetoothViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bluetooth, viewGroup, false);
        return new BluetoothViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothViewHolder bluetoothViewHolder, int i) {
        bluetoothViewHolder.initData(listBluetooths.get(i), onClickItemBluetooth);
    }

    @Override
    public int getItemCount() {
        return listBluetooths.size();
    }

    class BluetoothViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textBluetooth;
        private OnClickItemBluetooth onClickItemBluetooth;
        public BluetoothViewHolder(@NonNull View itemView) {
            super(itemView);
            textBluetooth = itemView.findViewById(R.id.text_bluetooth);
            this.onClickItemBluetooth = onClickItemBluetooth;
        }

        public void initData(String bluetooth, OnClickItemBluetooth onClickItemBluetooth) {
            textBluetooth.setText(bluetooth);
            this.onClickItemBluetooth = onClickItemBluetooth;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface OnClickItemBluetooth {
        void clickItemBluetooth(int position);
    }
}
