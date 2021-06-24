package com.helpinghands.toursandtravels;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FireStoreAdapterContactUs extends FirestoreRecyclerAdapter<productsModelForContactUs, FireStoreAdapterContactUs.productsviewholderForContatUs> {

    Context m;

    public FireStoreAdapterContactUs(@NonNull FirestoreRecyclerOptions<productsModelForContactUs> options, android.content.Context applicationContext) {
        super(options);
        m = applicationContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull FireStoreAdapterContactUs.productsviewholderForContatUs holder, int position, @NonNull productsModelForContactUs model) {
        holder.UserName.setText(model.getUserName());
        Log.d("abc", model.getUserName());
        holder.call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + model.getContact()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                m.startActivity(callIntent);
            }
        });

        holder.Respond_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getSnapshots().getSnapshot(position).getId();
                Log.d("ContactUs", "Clicked" + position + "-=-" + id);
                Intent intent = new Intent(m, EmailToUser.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID", id);
                m.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public productsviewholderForContatUs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_for_contactus, parent, false);
        return new FireStoreAdapterContactUs.productsviewholderForContatUs(view);
    }

    public class productsviewholderForContatUs extends RecyclerView.ViewHolder {

        private final TextView UserName;
        private final Button Respond_btn;
        private final Button call_btn;

        public productsviewholderForContatUs(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.listname);
            Respond_btn = itemView.findViewById(R.id.respond_btn);
            call_btn = itemView.findViewById(R.id.call_btn);
        }
    }
}
