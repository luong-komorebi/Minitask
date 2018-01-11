package redlor.it.minitask;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import redlor.it.minitask.Adapters.MyFragmentPagerAdapter;
import redlor.it.minitask.Utils.UpdateFirebase;

public class MainActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    // images for switcher
    private static final int[] IMAGES = {R.drawable.inbox, R.drawable.today, R.drawable.seven_day};
    // Declare a variable to check if in Dual Pane mode
    public static boolean mTwoPane;
    // Declare the class with Firebase methods
    UpdateFirebase updateFirebase;
    @BindView(R.id.view_pager)
    ViewPager pager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;
    @BindView(R.id.tabImage)
    ImageView descriptImage;
    @BindView(R.id.actionButton)
    FloatingActionButton actionButton;
    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    private int mCurrentPage;

    // Prevent dialog dismiss when orientation changes.
    private static void doKeepDialog(Dialog dialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mCurrentPage = 0;
        } else {
            mCurrentPage = savedInstanceState.getInt("currentPage");
            System.out.println("restored page: " + mCurrentPage);
        }

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        onSignedOutCleanup();

        // Check if authenticated
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());
                    initializeComponents();
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))

                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        // If the layout on activity_main.xml in the folder layout-sw600dp is not null,
        // we are in Dual Pane mode and activate the fragment
        if (findViewById(R.id.details_linear_layout) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DetailFragment detailFragment = new DetailFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.details_container, detailFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }

    private void initializeComponents() {
        //     updateDatabase = new UpdateDatabase();
        ButterKnife.bind(this);


        // set the pager adapter. More info look in 3rd party library document
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabStrip.setViewPager(pager);
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // if user go to another tab change color, change image and query from database to match
                descriptImage.setImageResource(IMAGES[position]);
                changeColor(position);
                mCurrentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        pager.setCurrentItem(mCurrentPage);
        changeColor(mCurrentPage);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add new item is clicked
                Intent intent = new Intent(v.getContext(), AddTodoItem.class);
                startActivity(intent);
            }
        });

        descriptImage.setImageResource(IMAGES[mCurrentPage]); // first start render
    }

    private void changeColor(int position) {
        switch (position) {
            case 0:
                applyNewColor("#303f9f", "#757de8", "#3f51b5");
                break;
            case 1:
                applyNewColor("#a00037", "#ff5c8d", "#d81b60");
                break;
            case 2:
                applyNewColor("#4b2c20", "#a98274", "#795548");
                break;
            default:
                break;
        }
    }

    // apply new fancy color based on material color tool
    private void applyNewColor(String actionBarColor, String tabStripColor, String indicatorColor) {
        ActionBar actionBar = getSupportActionBar();
        Window window = this.getWindow();

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(actionBarColor)));
        window.setStatusBarColor(Color.parseColor(indicatorColor));
        tabStrip.setBackground(new ColorDrawable((Color.parseColor(tabStripColor))));
        tabStrip.setIndicatorColor(Color.parseColor(indicatorColor));
        actionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(actionBarColor)));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", mCurrentPage);

    }

    // initiates menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // process menu item clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_menu_item:
                // start about activity
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.privacy_menu_item:
                Intent privacyIntent = new Intent(this, PrivacyActivity.class);
                startActivity(privacyIntent);
                return true;
            case R.id.clean_all_done:
                // clean all done tasks then recreate activity. Added a confirmation dialog.

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = firebaseUser.getUid();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("toDoItems");
                Query query = databaseReference.orderByChild("done").equalTo(true);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(MainActivity.this, R.string.no_items_checked, Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.ic_menu_delete)
                                    .setTitle(R.string.delete)
                                    .setMessage(R.string.delete_all_message)
                                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            // Delete checked items from Firebase Database
                                            updateFirebase = new UpdateFirebase();
                                            updateFirebase.deleteChecked();
                                            Toast.makeText(MainActivity.this, R.string.deleted_all_task, Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(getIntent());
                                            overridePendingTransition(0, 0);
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (dialog != null) {
                                                dialog.dismiss();
                                            }
                                        }
                                    }).create();
                            alertDialog.show();
                            doKeepDialog(alertDialog);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                // / if (passedInt > 0) {

                //    }    else {
                //      Toast.makeText(MainActivity.this, R.string.no_items_checked, Toast.LENGTH_SHORT).show();
                //  }
                return true;
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
