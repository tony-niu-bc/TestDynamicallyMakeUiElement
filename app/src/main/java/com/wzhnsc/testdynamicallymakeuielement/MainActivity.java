package com.wzhnsc.testdynamicallymakeuielement;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private RelativeLayout mrlCategoryChoice;
    private TextView       mtvCategoryTitle;
    private ImageView      mivCategoryIcon;

    private ChoiseLinearLayout mchoiseLinearLayout;

    private ArrayList<Category> mCatalogList;

    ChoiseLinearLayout.SelectedItemListener selectedItemListener = new ChoiseLinearLayout.SelectedItemListener() {
        @Override
        public void onDialogDismiss(int resultCode, Category discoverCatalog) {
            if (Activity.RESULT_OK == resultCode) {
                // 保存用户选择的分类
                mrlCategoryChoice.setTag(discoverCatalog);
                // 显示新选分类的名称
                mtvCategoryTitle.setText(discoverCatalog.getCatalogName());
                // 获得新先分类的列表
            }

            mchoiseLinearLayout.setVisibility(View.INVISIBLE);
            mivCategoryIcon.setImageResource(R.drawable.down);
        }
    };

    ChoiseDialogFragment.DismissListener dl = new ChoiseDialogFragment.DismissListener() {
        @Override
        public void onDialogDismiss(int requestCode, int resultCode, Category discoverCatalog) {
            if (Activity.RESULT_OK == resultCode) {
                // 保存用户选择的分类
                mrlCategoryChoice.setTag(discoverCatalog);
                // 显示新选分类的名称
                mtvCategoryTitle.setText(discoverCatalog.getCatalogName());
                // 获得新先分类的列表
            }

            mivCategoryIcon.setImageResource(R.drawable.down);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mchoiseLinearLayout = (ChoiseLinearLayout)findViewById(R.id.choise_area);
        mchoiseLinearLayout.setSelectedItemListener(selectedItemListener);

        mrlCategoryChoice = (RelativeLayout)findViewById(R.id.find_listpage_categorychoice);
        mrlCategoryChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (View.INVISIBLE == mchoiseLinearLayout.getVisibility()) {
                    mivCategoryIcon.setImageResource(R.drawable.up);

                    Category discoverCatalog = (Category) v.getTag();
                    mchoiseLinearLayout.setVisibility(View.VISIBLE, discoverCatalog.getCatalogId());
                }
                else {
                    mchoiseLinearLayout.setVisibility(View.INVISIBLE);
                    mivCategoryIcon.setImageResource(R.drawable.down);
                }
            }
        });

        // 全部分类后台不返回，需要自己固定添加为首元素
        mrlCategoryChoice.setTag(mchoiseLinearLayout.getDefaultCatalog());

        // 全部分类后台不返回，需要自己固定添加为首元素
        mCatalogList = new ArrayList<Category>();
        Category allCatalog = new Category();
        allCatalog.setCatalogId(0);
        // 如下内容实际代码中不会用，但为方便调试均置上说明文字
        allCatalog.setCatalogName("所有");

        mrlCategoryChoice.setTag(allCatalog);
        mCatalogList.add(allCatalog);

        // test - begin
        Category firstCatalog = new Category();
        firstCatalog.setCatalogId(1);
        firstCatalog.setCatalogName("分类一");
        mCatalogList.add(firstCatalog);

        Category secondCatalog = new Category();
        secondCatalog.setCatalogId(3);
        secondCatalog.setCatalogName("分类二");
        mCatalogList.add(secondCatalog);

        Category thirdCatalog = new Category();
        thirdCatalog.setCatalogId(4);
        thirdCatalog.setCatalogName("分类三");
        mCatalogList.add(thirdCatalog);

        Category fourthCatalog = new Category();
        fourthCatalog.setCatalogId(5);
        fourthCatalog.setCatalogName("分类四");
        mCatalogList.add(fourthCatalog);

        mchoiseLinearLayout.addNewCataLogList(mCatalogList);
        // test - end

        mtvCategoryTitle = (TextView)findViewById(R.id.find_listpage_categorytitle);
        mivCategoryIcon = (ImageView)findViewById(R.id.find_listpage_categoryicon);
    }

    public void showChoiseDlg(View v) {
        mivCategoryIcon.setImageResource(R.drawable.up);

        Category discoverCatalog = (Category)mrlCategoryChoice.getTag();
        ChoiseDialogFragment.getInstance(-1,
                                         mCatalogList,
                                         discoverCatalog.getCatalogId(),
                                         dl)
                            .show(getSupportFragmentManager(), "");
    }
}
