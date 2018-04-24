package zhangle.example.com.campaign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    String TAG = "Firebase Testing";
    //Start add adapter
    private List<Store> storesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemAdapter mAdapter;
    private DashboardActivity dashboardActivity;

    public void setDashboardActivity(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
    }
    //end add adapter

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_details, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            DatabaseReference myRef = database.getReference("Users").child(currentUser.getUid()).child("storeID");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String storedata = (String)dataSnapshot.getValue(String.class);
                    String[] separated = storedata.split("\\|");
                    for(int i =0;i<separated.length;i++){
                        prepareStoreData(separated[i]);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }else{
            //jump to login page
            Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
        //Add Store
        addStoreIntoAdapter(myFragmentView);
        return myFragmentView;
    }


    //prepare Data
    private void prepareStoreData(final String storeid) {
        String Spath = "Store/"+storeid+"/name";
        DatabaseReference myRef = database.getReference(Spath);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                String sid = storeid;
                getCampaignNoFromFirebase(sid,value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    //add store
    private void addStoreIntoAdapter(View view){
        //add adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new ItemAdapter(storesList);
        //x
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //y
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Store store = storesList.get(position);
                Toast.makeText(getContext(), store.getStorename() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity().getBaseContext(), DashboardActivity.class);
                i.putExtra("SELECTED_STORE", store.getStorename());
                i.putExtra("SELECTED_STORE_ID", store.getStoreid());
                //dashboardActivity.showDetail();
                getActivity().startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void getCampaignNoFromFirebase(final String storeid, final String storename){
        String Spath = "Store/"+storeid+"/campaign";
        DatabaseReference myRef = database.getReference(Spath);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                String[] separated = value.split("\\|");
                Store storeinfo = new Store(storeid, storename,separated.length);
                mAdapter.getItemList().add(storeinfo);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
