package barak.com.parkingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    // ... view holder defined above...

    // Store a member variable for the contacts
    private List<Place> mPlaces;
    public Context context;
    OnItemClickListener mItemClickListener;
    //private DropdownMenu mDropdownMenu;
    // Pass in the contact array into the constructor
    public PlaceAdapter(List<Place> places) {
        mPlaces = places;
        //Log.d("mPlaces.size(): ",String.valueOf(mPlaces.size()));
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView addressTV;
        TextView createdDateTV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            addressTV = (TextView) itemView.findViewById(R.id.purchase_name);
            createdDateTV = (TextView) itemView.findViewById(R.id.purchase_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            mItemClickListener.onItemClick(v, getAdapterPosition(), "id"); //OnItemClickListener mItemClickListener;
        }
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position, String id);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout

        View placeView = inflater.inflate(R.layout.item_post, parent, false);

        // Return a new holder instance
        PlaceAdapter.ViewHolder viewHolder = new PlaceAdapter.ViewHolder(placeView);
        return viewHolder;
    }



    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Place place = mPlaces.get(position);

        // Set item views based on your views and data model

        TextView textViewAddress = viewHolder.addressTV;
        textViewAddress.setText(place.getAddress());
        TextView textViewCreatedDate = viewHolder.createdDateTV;
        textViewCreatedDate.setText(place.getCreatedDate());


    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPlaces.size();
    }
}
