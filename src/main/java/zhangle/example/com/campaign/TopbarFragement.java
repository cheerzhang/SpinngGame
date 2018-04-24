package zhangle.example.com.campaign;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/* select campaign */
public class TopbarFragement extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String TAG = "Firebase Testing";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    //Start add adapter
    private List<StoreCampaign> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CampaignAdapter mAdapter;
    //end add adapter
    String getArgument = "";


    public TopbarFragement() {
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
        getArgument = getArguments().getString("sid");
        View myFragmentView = inflater.inflate(R.layout.fragment_topbar_fragement, container, false);
        //Add Campaign
        final ImageView ivBasicImage = (ImageView) myFragmentView.findViewById(R.id.imageViewCampaign);

        if(getArguments().getString("cid") != null) {
            final long ONE_MEGABYTE = 1024 * 1024 * 10;
            String filepath = getArguments().getString("cname")+"/spinicon.png";
            storageRef.child(filepath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Picasso.with(getContext()).load(uri.toString()).into(ivBasicImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        //start
        addCampaignIntoAdapter(myFragmentView);
        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //prepare Data
    private void prepareCampaignData_First(final String storeid) {
        String Spath = "Store/"+storeid+"/campaign";
        DatabaseReference myRef = database.getReference(Spath);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if(value != null){
                    String[] separated = value.split("\\|");
                    for(int i=0;i<separated.length;i++) {
                        prepareCampaignData(separated[i].trim());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void prepareCampaignData(final String campaignid) {
        String Spath = "theme/"+campaignid+"/folderName";
        DatabaseReference myRef = database.getReference(Spath);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: name " + value);
                String cid = campaignid;
                //
                Downloadimg(cid, value);
                //
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    //add campaign
    private void addCampaignIntoAdapter(View view){
        //add adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_c);
        mAdapter = new CampaignAdapter(getContext(),itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                StoreCampaign campaign = itemList.get(position);
                Toast.makeText(getContext(), campaign.getCampaignname() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity().getBaseContext(), DashboardActivity.class);
                i.putExtra("SELECTED_CAMPAIGN_ID", campaign.getCampaignid());
                i.putExtra("SELECTED_CAMPAIGN", campaign.getCampaignname());
                i.putExtra("SELECTED_STORE", getArguments().getString("sname"));
                i.putExtra("SELECTED_STORE_ID", getArguments().getString("sid"));
                getActivity().startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        prepareCampaignData_First(getArgument);
    }

    private void Downloadimg(final String campaignid, final String campaignname){
        final long ONE_MEGABYTE = 1024 * 1024 * 10;
        String filepath = campaignname+"/spinicon.png";
        storageRef.child(filepath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                StoreCampaign campaigninfo = new StoreCampaign(campaignid, campaignname, uri.toString());
                mAdapter.getItemList().add(campaigninfo);
                mAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


}
