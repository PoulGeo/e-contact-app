package com.george.econtactdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, OnBMClickListener {

    BoomMenuButton bmb;
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmb = (BoomMenuButton) findViewById(R.id.boom);
        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_8_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_8_1);

            bmb.animate().translationYBy(-900f).rotationY(1800).setDuration(3000);

        categories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.categories)));
        categories.remove(0);

        List<Integer> drawables = new ArrayList<>(Arrays.asList(
                R.drawable.beauty,
                R.drawable.freelancer,
                R.drawable.education,
                R.drawable.fun,
                R.drawable.doctors,
                R.drawable.house,
                R.drawable.tech,
                R.drawable.texnites
        ));

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(drawables.get(i))
                    .normalText(categories.get(i))
                    .listener(MainActivity.this);
            bmb.addBuilder(builder);
        }
    }

    @Override
    public void onBoomButtonClick(int index) {
        int id = getResources().getIdentifier("sp" + index, "array", getPackageName());
        final String[] subCategories = getResources().getStringArray(id);

        PopupMenu popupMenu = new PopupMenu(MainActivity.this, bmb);
        for (int i = 0; i < subCategories.length; i++) {
            String subCategory = subCategories[i];
            popupMenu.getMenu().add(index, i, 0, subCategory);
        }

        popupMenu.setOnMenuItemClickListener(MainActivity.this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        int groupId = menuItem.getGroupId();
        intent.putExtra("category", categories.get(groupId));
        intent.putExtra("subcategory", menuItem.getTitle());
        startActivity(intent);
        return true;
    }
}
