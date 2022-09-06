package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.redditcloneandroid.model.JWTUtils;
import com.google.android.material.navigation.NavigationView;

public class KorisnikA extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Menu optionsMenu;
    public NavigationView navigationView;
    private String logUserRoles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnik_pocetna);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new KorPostoviF()).commit();
            navigationView.setCheckedItem(R.id.navFisrtPagePosts);
        }

        logUserRoles = JWTUtils.getCurrentUserRoles();

        System.out.println("=====================================================");
        System.out.println(JWTUtils.getCurrentUserUsername());
        System.out.println(JWTUtils.getCurrentUser());
        System.out.println(JWTUtils.getCurrentUserRoles());
        System.out.println("=====================================================");

        if(logUserRoles.equals("ROLE_USER")){
            navMenuUser();
        }else if(logUserRoles.equals("ROLE_MODERATOR")){
            navMenuModerator();
        }else if(logUserRoles.equals("ROLE_ADMIN")){
            navMenuAdmin();
        }





    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            //user
            case R.id.logOut:
                JWTUtils.logout();
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.navUserAcc:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IzmeniPodOKorF()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.navFisrtPagePosts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new KorPostoviF()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.navAddCommunityUser:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DodavanjeZajF() ).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;


                //admin
            case R.id.blocModerators:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IkiniModF()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.blockCommunity:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UkiniZajF()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

                //moderator
            case R.id.navBlocUser:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlokirajKorisnikaF()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.navReportCom:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrijavaKomF()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.navReportPost:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrijavaPostF()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.navFlairs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FlairoviF() ).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;



            case R.id.navUserPosts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AzuriranjeKorPosF() ).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.navChangePassword:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PromeniSifruF() ).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;


        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    private void navMenuUser(){
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.navAddCommunityUser).setVisible(true);
        nav_menu.findItem(R.id.navUserAcc).setVisible(true);
        nav_menu.findItem(R.id.navUserPosts).setVisible(true);
    }

    private void navMenuAdmin(){
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.blocModerators).setVisible(true);
        nav_menu.findItem(R.id.blockCommunity).setVisible(true);
    }

    private void navMenuModerator(){
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.navBlocUser).setVisible(true);
        nav_menu.findItem(R.id.navReportCom).setVisible(true);
        nav_menu.findItem(R.id.navAddCommunityUser).setVisible(true);
        nav_menu.findItem(R.id.navReportPost).setVisible(true);
        nav_menu.findItem(R.id.navUserPosts).setVisible(true);
        nav_menu.findItem(R.id.navUserAcc).setVisible(true);
    }


}