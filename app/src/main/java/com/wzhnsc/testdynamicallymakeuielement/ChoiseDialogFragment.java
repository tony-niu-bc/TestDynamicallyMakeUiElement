package com.wzhnsc.testdynamicallymakeuielement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChoiseDialogFragment extends DialogFragment {

    private int                 mRequestCode;
    private DismissListener     mDismissListener;
    private ArrayList<Category> mlistCategory;
    private int                 mPrevSelectedCategory = 0; // 默认选中的分类为全部

    private View mViewInterface;

    private ArrayList<RelativeLayout>  mlistRelativeLayout;

    private class StyleHolder {
        // 该分类按钮对应的分类信息
        Category category;

        // 对应的选中状态图标
        ImageView ivSelectedIcon;

        // 对应的未选状态图标
        ImageView ivUnSelectedIcon;

        // 对应的名称显示文本
        TextView tvName;
    }

    public static interface DismissListener {
        public void onDialogDismiss(int requestCode, int resultCode, Category category);
    }

    public static final ChoiseDialogFragment getInstance(      int                 requestCode,
                                                         final ArrayList<Category> listCategory,
                                                               int                 prevSelectedCategory,
                                                               DismissListener     listener) {
        ChoiseDialogFragment dlg = new ChoiseDialogFragment();

        dlg.mRequestCode = requestCode;

        dlg.mlistCategory = new ArrayList<Category>();
        dlg.mlistCategory.clear();
        dlg.mlistCategory.addAll(listCategory);

        dlg.mPrevSelectedCategory = prevSelectedCategory;

        dlg.mDismissListener = listener;

        return dlg;
    }

    private ChoiseDialogFragment() {
    }

    private void updateBtnState(int categoryId) {
        for (int i = 0; i < mlistRelativeLayout.size(); ++i) {
            RelativeLayout rl = mlistRelativeLayout.get(i);
            StyleHolder styleHolder = (StyleHolder)rl.getTag();
            Category category = styleHolder.category;

            if (categoryId == category.getCatalogId()) {
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

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        // 如果 setCancelable() 中参数为 true，若点击 dialog 覆盖不到的 activity 的空白或者按返回键，则进行cancel，
        // 状态检测依次 onCancel() 和 onDismiss() ，别忘了在 onStart() 中修改宽高以便让 dialog 有覆盖不到的空白。
        // 如参数为 false，则按空白处或返回键无反应。
        // 缺省为 true
        setCancelable(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mlistRelativeLayout = new ArrayList<RelativeLayout>();
        mlistRelativeLayout.clear();

        mViewInterface = inflater.inflate(R.layout.choise_dlg, null);

        LinearLayout llContainer = (LinearLayout)mViewInterface.findViewById(R.id.container);

        ImageView ivAllSelected = (ImageView)mViewInterface.findViewById(R.id.all_selected_icon);

        ImageView ivAllUnSelected = (ImageView)mViewInterface.findViewById(R.id.all_unselected_icon);

        TextView tvAll = (TextView)mViewInterface.findViewById(R.id.all_text);

        RelativeLayout rlAll = (RelativeLayout)mViewInterface.findViewById(R.id.all);
        rlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyleHolder styleHolder = (StyleHolder)v.getTag();

                dismiss();

                if (mDismissListener != null) {
                    mDismissListener.onDialogDismiss(mRequestCode, Activity.RESULT_OK, styleHolder.category);
                }
            }
        });

        StyleHolder styleHolder = new StyleHolder();
        styleHolder.category = mlistCategory.get(0);
        styleHolder.ivSelectedIcon = ivAllSelected;
        styleHolder.ivUnSelectedIcon = ivAllUnSelected;
        styleHolder.tvName = tvAll;

        rlAll.setTag(styleHolder);

        mlistRelativeLayout.add(rlAll);

        View viewCategoryBtnPair = null;
        for (int i = 1; i < mlistCategory.size(); ++i) {
            Category category = mlistCategory.get(i);

            if (1 == (i % 2)) {
                viewCategoryBtnPair = inflater.inflate(R.layout.choise_dlg_item, null);
                llContainer.addView(viewCategoryBtnPair);

                ImageView ivLeftSelected = (ImageView)viewCategoryBtnPair.findViewById(R.id.left_selected_icon);
                ImageView ivLeftUnSelected = (ImageView)viewCategoryBtnPair.findViewById(R.id.left_unselected_icon);

                TextView tvLeft = (TextView)viewCategoryBtnPair.findViewById(R.id.left_text);
                tvLeft.setText(mlistCategory.get(i).getCatalogName());

                RelativeLayout rlLeft = (RelativeLayout)viewCategoryBtnPair.findViewById(R.id.left_area);
                rlLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StyleHolder styleHolder = (StyleHolder) v.getTag();

                        dismiss();

                        if (null != mDismissListener) {
                            mDismissListener.onDialogDismiss(mRequestCode, Activity.RESULT_OK, styleHolder.category);
                        }
                    }
                });

                styleHolder = new StyleHolder();
                styleHolder.category = mlistCategory.get(i);
                styleHolder.ivSelectedIcon = ivLeftSelected;
                styleHolder.ivUnSelectedIcon = ivLeftUnSelected;
                styleHolder.tvName = tvLeft;

                rlLeft.setTag(styleHolder);

                mlistRelativeLayout.add(rlLeft);
            }
            else {
                ImageView ivRightSelected = (ImageView)viewCategoryBtnPair.findViewById(R.id.right_selected_icon);
                ImageView ivRightUnSelected = (ImageView)viewCategoryBtnPair.findViewById(R.id.right_unselected_icon);

                TextView tvRight = (TextView)viewCategoryBtnPair.findViewById(R.id.right_text);
                tvRight.setText(mlistCategory.get(i).getCatalogName());

                RelativeLayout rlRight = (RelativeLayout)viewCategoryBtnPair.findViewById(R.id.right_area);
                rlRight.setVisibility(View.VISIBLE);
                rlRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StyleHolder styleHolder = (StyleHolder)v.getTag();

                        dismiss();

                        if (null != mDismissListener) {
                            mDismissListener.onDialogDismiss(mRequestCode, Activity.RESULT_OK, styleHolder.category);
                        }
                    }
                });

                styleHolder = new StyleHolder();
                styleHolder.category = mlistCategory.get(i);
                styleHolder.ivSelectedIcon = ivRightSelected;
                styleHolder.ivUnSelectedIcon = ivRightUnSelected;
                styleHolder.tvName = tvRight;

                rlRight.setTag(styleHolder);

                mlistRelativeLayout.add(rlRight);
            }
        }

        updateBtnState(mPrevSelectedCategory);

        return mViewInterface;
    }

    @Override
    public void onStart() {
        super.onStart();

        // 修改对话框的大小要在此事件中，不然有些机型会显示异常
        getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        getDialog().getWindow().setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        // 如下方式让对话框向下偏移，不如在布局 xml 文件中对第二层布局做相对第一层布局的间隔更方便，
        // 偏移值也可以用 dp 来精确设定，让系统自动做 px 的转换。
//        MarginLayoutParams mlpView = (MarginLayoutParams)mViewInterface.getLayoutParams();;
//        mlpView.topMargin += 130;
//        mViewInterface.setLayoutParams(mlpView);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (null != mDismissListener) {
            mDismissListener.onDialogDismiss(mRequestCode, Activity.RESULT_CANCELED, null);
        }

        super.onCancel(dialog);
    }
}
