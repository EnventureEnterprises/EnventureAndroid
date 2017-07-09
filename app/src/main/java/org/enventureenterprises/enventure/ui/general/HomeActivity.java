package org.enventureenterprises.enventure.ui.general;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.ui.addEntry.SearchItemActivity;
import org.enventureenterprises.enventure.ui.addItem.AddItemActivity;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.inventory.InventoryFragment;
import org.enventureenterprises.enventure.ui.reports.ReportsFragment;
import org.enventureenterprises.enventure.ui.sales.SalesFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * Created by mossplix on 7/6/17.
 */

public class HomeActivity extends BaseActivity implements BottomNavigation.OnMenuItemSelectionListener {
    @BindView(R.id.bottomNavigation) BottomNavigation bottomNavigation;
    private static final int SALES = 1;
    private static final int INVENTORY =0;
    private static final int REPORTS = 2;
    private static final int PROFILE = 3;
    private int navType =SALES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bottomNavigation.setOnMenuItemClickListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_container, InventoryFragment.newInstance(), InventoryFragment.TAG)
                    .commit();
        }




    }


    public void onNavigationChanged(int navType) {
        //noinspection WrongConstant
        if (bottomNavigation.getSelectedIndex() != navType) bottomNavigation.setSelectedIndex(navType, true);
        this.navType = navType;
         onModuleChanged(getSupportFragmentManager(), navType);
    }

    @Nullable
    public static Fragment getVisibleFragment(@NonNull FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }


    public void onModuleChanged(@NonNull FragmentManager fragmentManager, int type) {
        Fragment currentVisible = getVisibleFragment(fragmentManager);
        SalesFragment salesFrag = (SalesFragment)fragmentManager.findFragmentByTag(SalesFragment.TAG);
        InventoryFragment inventoryFrag = (InventoryFragment)fragmentManager.findFragmentByTag(InventoryFragment.TAG);
        ReportsFragment reportsFrag = (ReportsFragment)fragmentManager.findFragmentByTag(ReportsFragment.TAG);
        ProfileFragment profileFrag = (ProfileFragment)fragmentManager.findFragmentByTag(ProfileFragment.TAG);

        switch (type) {
            case SALES:
                if (salesFrag== null) {
                    onAddAndHide(fragmentManager, SalesFragment.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, salesFrag, currentVisible);
                }
                break;
            case INVENTORY:
                if (inventoryFrag == null) {
                    onAddAndHide(fragmentManager, InventoryFragment.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, inventoryFrag, currentVisible);
                }
                break;
            case REPORTS:
                if (reportsFrag == null) {
                    onAddAndHide(fragmentManager, ReportsFragment.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, reportsFrag, currentVisible);
                }
                break;

            case PROFILE:



                if (profileFrag == null) {
                    onAddAndHide(fragmentManager, ProfileFragment.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, profileFrag, currentVisible);
                }
                break;
        }
    }

     public void onShowHideFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment toShow, @NonNull Fragment toHide) {
        toHide.onHiddenChanged(true);
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .show(toShow)
                .commit();
        toShow.onHiddenChanged(false);
    }

    public void onAddAndHide(@NonNull FragmentManager fragmentManager, @NonNull Fragment toAdd, @NonNull Fragment toHide) {
        toHide.onHiddenChanged(true);
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .add(R.id.home_container, toAdd, toAdd.getClass().getSimpleName())
                .commit();
        toAdd.onHiddenChanged(false);
    }


    @Override public void onMenuItemSelect(@IdRes int id, int position, boolean fromUser) {

            onNavigationChanged(position);

    }

    @Override public void onMenuItemReselect(@IdRes int id, int position, boolean fromUser) {}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.add_entry:
                String tag ="";
                Fragment currentVisible = getVisibleFragment(getSupportFragmentManager());
                if(currentVisible != null) {
                    tag = currentVisible.getTag();
                }
                if (tag ==SalesFragment.TAG)
                {
                    startActivityWithTransition(new Intent(this, SearchItemActivity.class), R.anim.slide_in_right, R.anim.fade_out_slide_out_left);

                }
                else if(tag == InventoryFragment.TAG)
                {
                    startActivityWithTransition(new Intent(this, AddItemActivity.class), R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
                }


                break;
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                break;


        }

        return super.onOptionsItemSelected(item);
    }


}