package com.example.blooddonation.MainFragments.prevRequests.details_interested;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonation.MainFragments.prevRequests.details_interested.userProfile.FragmentProfilePublicView;
import com.example.blooddonation.MainFragments.prevRequests.importantData;
import com.example.blooddonation.R;
import com.example.blooddonation.databinding.InterestedPeopleVecyclerItemBinding;

import java.util.List;

public class InterestedDonorAdapter extends RecyclerView.Adapter<InterestedDonorAdapter.ViewHolder> {

    private final List<InterestedDonorItem> interestedDonorItems;
    private final Context context;
    private final FragmentInterestedDonor parent;

    public InterestedDonorAdapter(List<InterestedDonorItem> interestedDonorItems, Context context, FragmentInterestedDonor parent) {
        this.interestedDonorItems = interestedDonorItems;
        this.context = context;
        this.parent = parent;
    }


    @NonNull
    @Override
    public InterestedDonorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        InterestedPeopleVecyclerItemBinding binding = InterestedPeopleVecyclerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);


        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestedDonorAdapter.ViewHolder holder, int position) {
        InterestedDonorItem data = interestedDonorItems.get(position);
        holder.binding.name.setText(data.getName());

        holder.binding.btnContactBloodDonor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                importantData.USER_ID = data.getUser_id();

                Navigation.findNavController(parent.requireView()).navigate(R.id.action_fragment_requests_details_to_fragment_interested_donor);
            }
        });

    }

    @Override
    public int getItemCount() {
        return interestedDonorItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        InterestedPeopleVecyclerItemBinding binding;
        public ViewHolder(@NonNull InterestedPeopleVecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
