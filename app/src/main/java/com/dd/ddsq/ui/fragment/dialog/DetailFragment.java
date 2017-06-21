package com.dd.ddsq.ui.fragment.dialog;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.ddsq.R;
import com.dd.ddsq.common.BaseDialogFragment;
import com.dd.ddsq.util.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2017/2/27.
 * 活动详情
 */

public class DetailFragment extends BaseDialogFragment {

    @BindView(R.id.et_input_number)
    EditText etInputNumber;

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle(TextView tvTitle, ImageView imageView) {
        tvTitle.setText("活动详情");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_activity_detail;
    }

    @Override
    protected boolean isNeedTitle() {
        return true;
    }


    @OnClick(R.id.btn_submit)
    public void onClick() {
        String number = etInputNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!UIUtils.isMatchPhone(number)) {
            Toast.makeText(getContext(), "您输入的不是合法的手机号，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

        //// TODO: 2017/2/27 提交服务器
        Toast.makeText(getContext(), "请等待，正在提交中", Toast.LENGTH_SHORT).show();

    }
}
