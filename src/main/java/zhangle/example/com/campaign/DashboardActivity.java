package zhangle.example.com.campaign;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    Button firstFragment,secondFragment,launchbtn;
    String selectedstoretx,selectedcampaigntx,selectedstoreid,selectedcampaignid="";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard2);
        setContentView(R.layout.activity_sidebar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end tool bar

        // get the reference of Button's
        firstFragment = (Button) findViewById(R.id.selectstoreFragment);
        secondFragment = (Button) findViewById(R.id.selectcampaignFragment);
        launchbtn = (Button) findViewById(R.id.buttonlaunch);
        firstFragment.setText("Select Store");
        secondFragment.setText("Select Campaign");

        Intent i = getIntent();
        try {
            selectedstoretx = i.getStringExtra("SELECTED_STORE");
            selectedstoreid = i.getStringExtra("SELECTED_STORE_ID");
            if (selectedstoreid != null) {
                firstFragment.setText(selectedstoretx);
            }
        }catch (Error er){

        }
        try {
            selectedcampaigntx = i.getStringExtra("SELECTED_CAMPAIGN");
            selectedcampaignid = i.getStringExtra("SELECTED_CAMPAIGN_ID");
            if (selectedstoreid != null){
                if(selectedcampaignid !=null){
                    secondFragment.setText(selectedcampaigntx);
                    toolbar.setBackgroundColor(Color.parseColor("#E81010"));
                }
                secondFragment.setTextColor(Color.parseColor("#E81010"));
                loadFragment(new TopbarFragement());
            }else{
                firstFragment.setTextColor(Color.parseColor("#E81010"));
                loadFragment(new DetailsFragment());
            }
        }catch (Error er){

        }

        // perform setOnClickListener event on First Button
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsFragment detailsFragment = new DetailsFragment();
                firstFragment.setTextColor(Color.parseColor("#E81010"));
                //detailsFragment.setDashboardActivity(DashboardActivity.this);
                loadFragment(new DetailsFragment());
            }
        });
        // perform setOnClickListener event on Second Button
        secondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopbarFragement topbarFragement = new TopbarFragement();
                secondFragment.setTextColor(Color.parseColor("#E81010"));
                loadFragment(topbarFragement);
            }
        });
        //launch
        launchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, SpinningActivity.class);
                i.putExtra("SELECTED_STORE", selectedstoretx);
                i.putExtra("SELECTED_STORE_ID", selectedstoreid);
                i.putExtra("SELECTED_CAMPAIGN", selectedcampaigntx);
                i.putExtra("SELECTED_CAMPAIGN_ID", selectedstoreid);
                startActivity(i);
            }
        });

    }

    public void showDetail(){
        loadFragment(new DetailsFragment());
    }

    private void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("sid", selectedstoreid);
        bundle.putString("sname", selectedstoretx);
        bundle.putString("cid", selectedcampaignid);
        bundle.putString("cname", selectedcampaigntx);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    //Sidebar
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sidebar, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gift) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            // Firebase sign out
            mAuth.signOut();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
