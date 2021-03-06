package me.jameshunt.hierarchyexample.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.jameshunt.hierarchyexample.R;
import me.jameshunt.hierarchyview.DepthLevelFormat;
import me.jameshunt.hierarchyview.HierarchyData;
import me.jameshunt.hierarchyview.HierarchyDataHelper;
import me.jameshunt.hierarchyview.HierarchyListener;
import me.jameshunt.hierarchyview.HierarchyView;


public class CommonActivity extends AppCompatActivity implements HierarchyListener.Data {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Data data = new Data();

        HierarchyData hierarchyData = new HierarchyData(data);

        hierarchyData.addFormatter(new DepthLevelFormat(24, R.color.colorPrimary)); //depth level 0
        hierarchyData.addFormatter(new DepthLevelFormat(20,R.color.colorAccent));  //depth level 1


        HierarchyView hierarchyView = new HierarchyView(this);
        hierarchyView.setHierarchyData(hierarchyData, this);

        setContentView(hierarchyView);



    }


    @Override
    public void hierarchyDataSelected(HierarchyDataHelper.Data data) {

    }
}
