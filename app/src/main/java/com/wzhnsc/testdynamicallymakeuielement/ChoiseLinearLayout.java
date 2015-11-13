package com.wzhnsc.testdynamicallymakeuielement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChoiseLinearLayout extends LinearLayout
                                        implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mllContainer;
    private RelativeLayout mrlAll;

    private SelectedItemListener mSelectedItemListener;

    private ArrayList<Category> mlistCategory;
    private int mPrevSelectedCategory = 0; // 默认选中的分类为全部

    private ArrayList<RelativeLayout> mlistRelativeLayout;

    private boolean mbIsNeedUpdate = true; // true - 需要刷新界面

    private class StyleHolder {
        // 该分类按钮对应的分类信息
        Category discoverCatalog;

        // 对应的选中状态图标
        ImageView ivSelectedIcon;

        // 对应的未选状态图标
        ImageView ivUnSelectedIcon;

        // 对应的名称显示文本
        TextView tvName;
    }

    public static interface SelectedItemListener {
        public void onDialogDismiss(int resultCode, Category discoverCatalog);
    }

    public ChoiseLinearLayout(Context context) {
        super(context);

        initInterface(context);
    }

    public ChoiseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initInterface(context);
    }

    public ChoiseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initInterface(context);
    }

    private void updateBtnState(int categoryId) {
        for (int i = 0; i < mlistRelativeLayout.size(); ++i) {
            RelativeLayout rl = mlistRelativeLayout.get(i);
            StyleHolder styleHolder = (StyleHolder) rl.getTag();
            Category discoverCatalog = styleHolder.discoverCatalog;

            if (categoryId == discoverCatalog.getCatalogId()) {
                rl.setBackgroundColor(Color.parseColor("#00b38a"));

                styleHolder.ivUnSelectedIcon.setVisibility(View.INVISIBLE);
                styleHolder.ivSelectedIcon.setVisibility(View.VISIBLE);

                styleHolder.tvName.setTextColor(Color.parseColor("#ffffff"));

                continue;
            }

            rl.setBackgroundColor(Color.parseColor("#ffffff"));

            styleHolder.ivUnSelectedIcon.setVisibility(View.VISIBLE);
            styleHolder.ivSelectedIcon.setVisibility(View.INVISIBLE);

            styleHolder.tvName.setTextColor(Color.parseColor("#666666"));
        }
    }

    private void initInterface(Context context) {
        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.choise_dlg, this);

        mlistCategory = new ArrayList<Category>();
        mlistCategory.clear();

        Category allCatalog = new Category();
        allCatalog.setCatalogId(0);
        // 如下内容实际代码中不会用，但为方便调试均置上说明文字
        allCatalog.setCatalogName("所有");
        mlistCategory.add(allCatalog);

        mlistRelativeLayout = new ArrayList<RelativeLayout>();
        mlistRelativeLayout.clear();

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.choise_outside);
        rl.setOnClickListener(this);

        mllContainer = (LinearLayout) findViewById(R.id.container);

        ImageView ivAllSelected = (ImageView) findViewById(R.id.all_selected_icon);

        ImageView ivAllUnSelected = (ImageView) findViewById(R.id.all_unselected_icon);

        TextView tvAll = (TextView) findViewById(R.id.all_text);

        mrlAll = (RelativeLayout) findViewById(R.id.all);
        mrlAll.setOnClickListener(this);

        StyleHolder styleHolder = new StyleHolder();
        styleHolder.discoverCatalog = mlistCategory.get(0);
        styleHolder.ivSelectedIcon = ivAllSelected;
        styleHolder.ivUnSelectedIcon = ivAllUnSelected;
        styleHolder.tvName = tvAll;

        mrlAll.setTag(styleHolder);

        mlistRelativeLayout.add(mrlAll);
    }

    public Category getDefaultCatalog() {
        if ((null != mlistCategory)
         && (0 < mlistCategory.size())) {
            return mlistCategory.get(0);
        }

        return null;
    }

    public void addNewCataLogList(final ArrayList<Category> discoverCatalogList) {
        if (null != mlistCategory) {
            boolean bIsDiff = false;

            if (mlistCategory.size() != discoverCatalogList.size()) {
                bIsDiff = true;
            }
            else {
                for (int i = 0; i < discoverCatalogList.size(); ++i) {
                    // 无论顺序还是各别项还是全部都不同了都要重刷界面
                    if (discoverCatalogList.get(i).getCatalogId() != mlistCategory.get(i).getCatalogId()
                     || discoverCatalogList.get(i).getCatalogName() != mlistCategory.get(i).getCatalogName()) {
                        bIsDiff = true;
                    }
                }
            }

            if (bIsDiff) {
                mbIsNeedUpdate = true;
                mlistCategory.clear();

                Category allCatalog = new Category();
                allCatalog.setCatalogId(0);
                // 如下内容实际代码中不会用，但为方便调试均置上说明文字
                allCatalog.setCatalogName("所有");
                mlistCategory.add(allCatalog);

                mlistCategory.addAll(1, discoverCatalogList);
            }
        }
    }

    public void setSelectedItemListener(SelectedItemListener selectedItemListener) {
        mSelectedItemListener = selectedItemListener;
    }

    public void setVisibility(int visibility, int prevSelectedCategory) {
        super.setVisibility(visibility);

        mPrevSelectedCategory = prevSelectedCategory;

        if (View.VISIBLE == visibility) {
            if (mbIsNeedUpdate) {
                UpdateInterface();
            }

            updateBtnState(mPrevSelectedCategory);
        }
    }

    private void UpdateInterface() {
        if (1 < mllContainer.getChildCount()) {
            // 清除界面元素及元素对应分类信息
            mllContainer.removeViews(1, mllContainer.getChildCount());
        }

        mlistRelativeLayout.clear();
        mlistRelativeLayout.add(mrlAll);

        View viewCategoryBtnPair = null;
        for (int i = 1; i < mlistCategory.size(); ++i) {
            Category discoverCatalog = mlistCategory.get(i);

            if (1 == (i % 2)) {
                viewCategoryBtnPair = LayoutInflater.from(mContext).inflate(R.layout.choise_dlg_item, null);
                mllContainer.addView(viewCategoryBtnPair);

                ImageView ivLeftSelected = (ImageView) viewCategoryBtnPair.findViewById(R.id.left_selected_icon);
                ImageView ivLeftUnSelected = (ImageView) viewCategoryBtnPair.findViewById(R.id.left_unselected_icon);

                TextView tvLeft = (TextView) viewCategoryBtnPair.findViewById(R.id.left_text);
                tvLeft.setText(mlistCategory.get(i).getCatalogName());

                RelativeLayout rlLeft = (RelativeLayout) viewCategoryBtnPair.findViewById(R.id.left_area);
                rlLeft.setOnClickListener(this);

                StyleHolder styleHolder = new StyleHolder();
                styleHolder.discoverCatalog = mlistCategory.get(i);
                styleHolder.ivSelectedIcon = ivLeftSelected;
                styleHolder.ivUnSelectedIcon = ivLeftUnSelected;
                styleHolder.tvName = tvLeft;

                rlLeft.setTag(styleHolder);

                mlistRelativeLayout.add(rlLeft);
            }
            else {
                ImageView ivRightSelected = (ImageView) viewCategoryBtnPair.findViewById(R.id.right_selected_icon);
                ImageView ivRightUnSelected = (ImageView) viewCategoryBtnPair.findViewById(R.id.right_unselected_icon);

                TextView tvRight = (TextView) viewCategoryBtnPair.findViewById(R.id.right_text);
                tvRight.setText(mlistCategory.get(i).getCatalogName());

                RelativeLayout rlRight = (RelativeLayout) viewCategoryBtnPair.findViewById(R.id.right_area);
                rlRight.setVisibility(View.VISIBLE);
                rlRight.setOnClickListener(this);

                StyleHolder styleHolder = new StyleHolder();
                styleHolder.discoverCatalog = mlistCategory.get(i);
                styleHolder.ivSelectedIcon = ivRightSelected;
                styleHolder.ivUnSelectedIcon = ivRightUnSelected;
                styleHolder.tvName = tvRight;

                rlRight.setTag(styleHolder);

                mlistRelativeLayout.add(rlRight);
            }
        }

        mbIsNeedUpdate = false;
    }

    @Override
    public void onClick(View v) {
        if (null != mSelectedItemListener) {
            if (R.id.choise_outside == v.getId()) {
                mSelectedItemListener.onDialogDismiss(Activity.RESULT_CANCELED, null);
                return;
            }

            StyleHolder styleHolder = (StyleHolder) v.getTag();

            mPrevSelectedCategory = styleHolder.discoverCatalog.getCatalogId();
            updateBtnState(mPrevSelectedCategory);

            mSelectedItemListener.onDialogDismiss(Activity.RESULT_OK, styleHolder.discoverCatalog);
        }
    }
}
