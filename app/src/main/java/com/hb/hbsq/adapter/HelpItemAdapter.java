package com.hb.hbsq.adapter;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hb.hbsq.R;
import com.hb.hbsq.bean.QAInfo;
import com.hb.hbsq.ui.activity.VIPActivity;
import com.hb.hbsq.util.APPUtils;
import com.hb.hbsq.util.UIUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanglin  on 2017/3/6 14:59.
 */

public class HelpItemAdapter extends BaseAdapter<QAInfo> {


    /**
     * 定义style的正则表达式
     */
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REGEX_HTML = "<[^>]+>";
    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";


    public HelpItemAdapter(Context context, List<QAInfo> mList) {
        super(context, mList);
    }

    @Override
    protected int getTotalCount() {
        return mList.size();
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        QAInfo qaInfo = mList.get(position);
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        String answer = qaInfo.getAnswer();
        Matcher m_html = p_html.matcher(answer);
        answer = m_html.replaceAll("");
        if (position == 1) {

            holder.setVisible(R.id.tv_help_answer, false);

            holder.setVisible(R.id.ll, true);

            ((LinearLayout) holder.getView(R.id.ll)).removeAllViews();
            String[] split = Html.fromHtml(answer).toString().split("-");
            for (int i = 0; i < split.length; i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i == split.length - 1) {
                    layoutParams.bottomMargin = UIUtils.dip2px(mContext, 10);
                } else {
                    layoutParams.bottomMargin = 0;
                }
                layoutParams.leftMargin = UIUtils.dip2px(mContext, 10);
                layoutParams.topMargin = UIUtils.dip2px(mContext, 10);
                layoutParams.rightMargin = UIUtils.dip2px(mContext, 10);
                Button button = new Button(mContext);
                button.setText(split[i]);
                button.setBackgroundColor(mContext.getResources().getColor(R.color.ea));
                button.setPadding(UIUtils.dip2px(mContext, 5), UIUtils.dip2px(mContext, 5), UIUtils.dip2px(mContext, 5), UIUtils.dip2px(mContext, 5));
                button.setTextSize(14);
                button.setTextColor(mContext.getResources().getColor(R.color.a74));
                button.setLayoutParams(layoutParams);
                ((LinearLayout) holder.getView(R.id.ll)).addView(button);

                final int finalJ = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (finalJ) {
                            case 0:
                                try {
                                    mContext.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

                                } catch (ActivityNotFoundException e) {
                                }
                                break;
                            case 1:
                                mContext.startActivity(new Intent(mContext, VIPActivity.class));
                                break;
                            case 2:
                                if (APPUtils.isInstallAPP(mContext, "com.tencent.mm")) {
                                    Intent intent = new Intent();
                                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");// 报名该有activity
                                    intent.setAction(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setComponent(cmp);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "您还没有安装微信，请先安装", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case 3:

                                mContext.startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                                break;
                        }
                    }
                });
            }
        } else {
            holder.setVisible(R.id.ll, false);
            holder.setVisible(R.id.tv_help_answer, true);

        }
        holder.setText(R.id.tv_help_title, qaInfo.getQuestion());
        holder.setText(R.id.tv_help_answer, Html.fromHtml(answer).toString());

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.help_item;
    }

}
