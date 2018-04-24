package zhangle.example.com.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Space;
import java.util.Calendar;
import java.util.Date;
import android.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class SpinningActivity extends Activity{

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String sid,cid,sname,cname;
    EditText mobiletx;
    ImageView bgimg,spinimg,tapimg,pinimg,titleimg,inputimg,textbgimg;
    int spinno = 0;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinning);


        Intent i = getIntent();
        sid = i.getStringExtra("SELECTED_STORE_ID");
        sname = i.getStringExtra("SELECTED_STORE");
        cid = i.getStringExtra("SELECTED_CAMPAIGN_ID");
        cname = i.getStringExtra("SELECTED_CAMPAIGN");

        mobiletx = (EditText) findViewById(R.id.editTextphone);

        bgimg = (ImageView) findViewById(R.id.imageViewbg);
        pinimg = (ImageView) findViewById(R.id.imageViewpin);
        spinimg = (ImageView) findViewById(R.id.imageViewSpin);
        titleimg =(ImageView) findViewById(R.id.imageViewTitle);
        inputimg = (ImageView) findViewById(R.id.imageViewtext);
        textbgimg = (ImageView) findViewById(R.id.imageViewtextbg);
        setimagefromfirebase("pin.png",pinimg);
        setimagefromfirebase("cnyWheelFront.png",spinimg);
        setimagefromfirebase("Title.png",titleimg);
        setimagefromfirebase("TInsertMobile.png ",inputimg);
        setimagefromfirebase("TextBg.png",textbgimg);
        setimagefromfirebase("GifBg.gif",bgimg);

        ImageButton imgbtn = (ImageButton) findViewById(R.id.imageButton);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobiletx.getText().length() ==8) {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    //currentUser.getUid()
                    int i = spinningfunc();
                    String mGroupId = mDatabase.push().getKey();
                    Date currentTime = Calendar.getInstance().getTime();
                    mDatabase.child("winner").child(mGroupId).child("mobileNumber").setValue(mobiletx.getText().toString());
                    mDatabase.child("winner").child(mGroupId).child("spinDate").setValue(currentTime.toString());
                    mDatabase.child("winner").child(mGroupId).child("storeid").setValue(sid);
                    mDatabase.child("winner").child(mGroupId).child("campadinid").setValue(cid);
                    mDatabase.child("winner").child(mGroupId).child("winningNumber").setValue(i);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SpinningActivity.this);
                    builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                    AlertDialog dialog = builder.create();
                }
            }
        });
    }

    private int spinningfunc() {
        /*
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(500);
        rotateAnimation.setRepeatCount(2);
        */
        //resetspin();
        int f = (int)Math.floor(Math.random()*100) % 8;
        spinno = spinno + f;
        float deg = findViewById(R.id.imageViewSpin).getRotation() + 3600 + ((float)(f*0.125) * 360F);
        findViewById(R.id.imageViewSpin).animate().rotationX(0).rotation(deg).setDuration(5000)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        int finalno = spinno%8;
        //findViewById(R.id.imageViewSpin).startAnimation(rotateAnimation);
        return finalno;
    }

   private void setimagefromfirebase(String imgname, final ImageView ivBasicImage){
           final long ONE_MEGABYTE = 1024 * 1024 * 10;
           String filepath = "CNY/"+imgname;
           storageRef.child(filepath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   // Got the download URL for 'users/me/profile.png'
                   Picasso.with(c).load(uri.toString()).into(ivBasicImage);
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception exception) {
                   // Handle any errors
               }
           });

   }

}
