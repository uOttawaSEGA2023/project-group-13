package com.uottawa.eecs.SEGDeliverable3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PendingItemAdapter extends FirebaseRecyclerAdapter<DataClass, PendingItemAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PendingItemAdapter(@NonNull FirebaseRecyclerOptions<DataClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull DataClass model) {
        holder.firstName.setText(model.getFirstName()); // we are going to set it as the info from firebase
        holder.lastName.setText(model.getLastName());
        holder.email.setText(model.getEmail());
        holder.phone.setText(model.getPhone());
        holder.streetNumber.setText(model.getStreetNumber());
        holder.street.setText(model.getStreet());
        holder.city.setText(model.getCity());
        holder.proviTerri.setText(model.getProvince());
        holder.emHeNum.setText(model.getEmployeeOrHealthCardNumber());
        holder.type.setText(model.getType());
        holder.specialties.setText(model.getSpecialties());
        // NOTE: we don't display their password so it is not included

        // change status when button is clicked
        holder.accept.setOnClickListener(view -> {
            String userId = getRef(position).getKey();

            // set it in-app
            model.setRegistrationStatus(1);

            // set in firebase too, and move to accepted
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Pending").child(userId);
            userRef.setValue(null); // clear them from pending

            DatabaseReference acceptedUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Accepted").child(userId);
            acceptedUser.setValue(model);
        });

        holder.reject.setOnClickListener(view -> {
            String userId = getRef(position).getKey();

            // set it in-app
            model.setRegistrationStatus(-1);

            // set in firebase too
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Pending").child(userId);
            userRef.setValue(null); // clear them from pending

            DatabaseReference rejectedUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Rejected").child(userId);
            rejectedUser.setValue(model);
        });

    }

    // allows us to display the cards in the recycler view
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView firstName, lastName, email, password, phone, streetNumber, street, city, proviTerri, emHeNum, type, specialties;
        Button accept, reject;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            // get all of the views by their ids
            img = (ImageView) itemView.findViewById(R.id.personIcon);
            firstName = (TextView) itemView.findViewById(R.id.FirstNameText);
            lastName = (TextView) itemView.findViewById(R.id.LastNameText);
            email = (TextView) itemView.findViewById(R.id.EmailText);
            //password = (TextView) itemView.findViewById(R.id.PasswordText);
            phone = (TextView) itemView.findViewById(R.id.PhoneNumberText);
            streetNumber = (TextView) itemView.findViewById(R.id.StreetNumberText);
            street = (TextView) itemView.findViewById(R.id.StreetText);
            city = (TextView) itemView.findViewById(R.id.CityText);
            proviTerri = (TextView) itemView.findViewById(R.id.ProvinceText);
            emHeNum = (TextView) itemView.findViewById(R.id.NumberText);
            type = (TextView) itemView.findViewById(R.id.TypeText);
            accept = (Button) itemView.findViewById(R.id.AcceptButton);
            reject = (Button) itemView.findViewById(R.id.RejectButton);
            specialties = (TextView) itemView.findViewById(R.id.SpecialtiesText);

        }
    }
}
