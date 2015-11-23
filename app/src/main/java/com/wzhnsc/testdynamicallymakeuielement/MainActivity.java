package com.wzhnsc.testdynamicallymakeuielement;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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

    private void showCategoryChoiceDlg() {
        AnimationSet animationSet = new AnimationSet(true);

        // 两个参数分别表示初始透明度和目标透明(1表示不透明，0表示完全透明)
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(400);
        alphaAnimation.setStartOffset(100);
        alphaAnimation.setFillAfter(true);

        animationSet.addAnimation(alphaAnimation);

        mchoiseLinearLayout.startAnimation(animationSet);

        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_top2bottom);
        animation.setDuration(400);
        animation.setFillAfter(true);

        //得到一个LayoutAnimationController对象；
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        //设置控件显示的顺序；
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        //为ListView设置LayoutAnimationController属性；
        mchoiseLinearLayout.setLayoutAnimation(controller);
        mchoiseLinearLayout.startLayoutAnimation();
    }

    private void hideCategoryChoiceDlg() {
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_bottom2top);
        animation.setDuration(400);
        animation.setFillAfter(true);

        //得到一个LayoutAnimationController对象；
        LayoutAnimationController controller = new LayoutAnimationController(animation);

        //设置控件显示的顺序；
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);

        //为ListView设置LayoutAnimationController属性；
        mchoiseLinearLayout.setLayoutAnimation(controller);
        mchoiseLinearLayout.startLayoutAnimation();

        AnimationSet animationSet = new AnimationSet(true);
        // 两个参数分别表示初始透明度和目标透明(1表示不透明，0表示完全透明)
        final AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(400);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mchoiseLinearLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animationSet.addAnimation(alphaAnimation);

        mchoiseLinearLayout.startAnimation(animationSet);
    }

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

            hideCategoryChoiceDlg();
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

                    showCategoryChoiceDlg();
                }
                else {
                    hideCategoryChoiceDlg();
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
