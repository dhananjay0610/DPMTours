package com.helpinghands.toursandtravels;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreAdapter extends FirestoreRecyclerAdapter<productsmodel, FirestoreAdapter.productsviewholder> {

    Context m;

    public FirestoreAdapter(@NonNull FirestoreRecyclerOptions<productsmodel> options, android.content.Context applicationContext) {
        super(options);
        m = applicationContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final productsviewholder holder, final int position, @NonNull final productsmodel model) {
        holder.listname.setText(model.getLocation());
        holder.listprice.setText(model.getMoney());
        holder.listduration.setText(model.getDuration());

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getSnapshots().getSnapshot(position).getId();
//                Log.d("MainActivity", "Clicked" + position + "-=-" + id);
                Intent intent = new Intent(m, EditPackage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID", id);
                m.startActivity(intent);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getSnapshots().getSnapshot(position).getReference().delete();
                notifyDataSetChanged();

                String id = getSnapshots().getSnapshot(position).getId();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Package").document(id).delete().addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Log.d(m.this, "Delete");
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.d(m.this, "Delete");
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public productsviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new productsviewholder(view);
    }

    public static class productsviewholder extends RecyclerView.ViewHolder {
        private TextView listname;
        private TextView listprice;
        private TextView listduration;
        private Button edit_btn;
        private Button delete_btn;

        public productsviewholder(@NonNull final View itemView) {
            super(itemView);
            listname = itemView.findViewById(R.id.listname);
            listprice = itemView.findViewById(R.id.listprice);
            listduration = itemView.findViewById(R.id.listduration);
            edit_btn = itemView.findViewById(R.id.edit_btn);
            delete_btn = itemView.findViewById(R.id.btn_Delete);

        }
    }
}
