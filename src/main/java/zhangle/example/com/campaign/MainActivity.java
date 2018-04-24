package zhangle.example.com.campaign;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;

    private boolean mVerificationInProgress = false;
    private static final String TAG = "PhoneAuthActivity";
    EditText phonetx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.loginbtn);
        b.setOnClickListener(this);

        //Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        //startActivity(intent);
        //see whether the user is here

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        TextView tx = (TextView) findViewById(R.id.welcomtx);
        if (currentUser != null){
            //jump to otp page
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
        }else{
            tx.setText("Welcome!");
            //jump to otp page
        }



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                System.out.print("Failed Error"+e.toString());
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                //jump to otp page
                Intent intent = new Intent(MainActivity.this, OtpActivity.class);
                intent.putExtra("vid", mVerificationId);
                intent.putExtra("mobile", phonetx.getText().toString());
                startActivity(intent);
                // [START_EXCLUDE]
                // Update UI
                //updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
    }

    @Override
    public void onClick(View view) {
        //jump to otp intent

        //Send OTP
        phonetx = (EditText) findViewById(R.id.phonenoed);
        this.startPhoneNumberVerification(phonetx.getText().toString());
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+65"+phoneNumber,60, TimeUnit.SECONDS,this,mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        mVerificationInProgress = true;
    }
}
