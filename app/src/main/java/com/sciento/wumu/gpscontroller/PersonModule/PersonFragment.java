package com.sciento.wumu.gpscontroller.PersonModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sciento.wumu.gpscontroller.ConfigModule.UserStateCode;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.UserModule.UserLoginActivity;
import com.sciento.wumu.gpscontroller.UserModule.UserNetworkConn;
import com.sciento.wumu.gpscontroller.Utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PersonFragment extends Fragment {


    private final int MSG_LOGOUT = 502;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    Unbinder unbinder;
    Handler personHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGOUT:

                    break;
                case UserStateCode.USER_SUCCESS:
                    Intent logoutInent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(logoutInent);
                    break;
                case UserStateCode.USER_LOGOUT_FAIL:
                    ToastUtils.makeShortText(getString(R.string.str_logout_fail), getActivity());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public PersonFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance() {
        PersonFragment fragment = new PersonFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({
            R.id.btn_logout,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                UserNetworkConn.getInstance().logoutUser(personHandler);
                break;
        }
    }
}
