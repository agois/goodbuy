package com.msag.goodbuy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
        ActionBar.OnNavigationListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final int ACTION_CAMERA_CAPTURE = 100;
    private static final int ACTION_SELECT_PHOTO = 101;
//    private Bitmap mImageBitmap;
    private ImageView mImageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView)findViewById(R.id.photo);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
        // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(getActionBarThemedContextCompat(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, new String[] {
                                getString(R.string.title_section1),
                                getString(R.string.title_section2),
                                getString(R.string.title_section3), }), this);
    }

    /**
     * Backward-compatible version of {@link ActionBar#getThemedContext()} that
     * simply returns the {@link android.app.Activity} if
     * <code>getThemedContext</code> is unavailable.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private Context getActionBarThemedContextCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getActionBar().getThemedContext();
        } else {
            return this;
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        // Restore the previously serialized current dropdown position.
//        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
//            getActionBar().setSelectedNavigationItem(
//                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        // Serialize the current dropdown position.
//        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
//                .getSelectedNavigationIndex());
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_camera:
            dispatchTakePictureIntent(ACTION_CAMERA_CAPTURE);
            break;
        case R.id.action_file:
            dispatchGetPhotoIntent(ACTION_SELECT_PHOTO);
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
    
    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }
    
    private void dispatchGetPhotoIntent(int actionCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, ACTION_SELECT_PHOTO);  
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (data == null || resultCode == RESULT_CANCELED) {
            return;
        }
        switch(requestCode) { 
        case ACTION_SELECT_PHOTO:
            if(resultCode == RESULT_OK){  
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap b = BitmapFactory.decodeStream(imageStream);
                    handlePhoto(b);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        case ACTION_CAMERA_CAPTURE:
            Bundle extras = data.getExtras();
            if (extras != null) {
                handlePhoto((Bitmap) extras.get("data"));
            }
            break;
        }
    }
    
    private void handlePhoto(Bitmap b) {
        mImageView.setImageBitmap(b);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
//        Fragment fragment = new DummySectionFragment();
//        Bundle args = new Bundle();
//        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//        fragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, fragment).commit();
        return true;
    }

//    private Bitmap getBitmap() {
//        return mImageBitmap;
//    }
    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
//    public static class DummySectionFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        public static final String ARG_SECTION_NUMBER = "section_number";
//        public static final String ARG_BITMAP = "section_bitmap";
//
//        public DummySectionFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main_dummy,
//                    container, false);
//            ImageView imageView = (ImageView)rootView.findViewById(R.id.photo);
//                imageView.setImageBitmap(((MainActivity)getActivity()).getBitmap());
//            TextView dummyTextView = (TextView) rootView
//                    .findViewById(R.id.section_label);
//            dummyTextView.setText(Integer.toString(getArguments().getInt(
//                    ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

}
