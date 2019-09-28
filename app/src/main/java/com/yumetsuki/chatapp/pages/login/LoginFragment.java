package com.yumetsuki.chatapp.pages.login;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.yumetsuki.chatapp.MainViewModel;
import com.yumetsuki.chatapp.R;
import com.yumetsuki.chatapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends Fragment {

    @BindView(R.id.username_edit)
    TextInputEditText mUsernameEdit;

    @BindView(R.id.password_edit)
    TextInputEditText mPasswordEdit;

    @BindView(R.id.login_btn)
    MaterialButton mLoginBtn;

    @BindView(R.id.register_btn)
    MaterialButton mRegisterBtn;

    private Unbinder unbinder;

    private LoginViewModel viewModel;

    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this)
                    .get(LoginViewModel.class);
        }
        if (mainViewModel == null) {
            mainViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                    .get(MainViewModel.class);
        }
        initViewModel();
        mainViewModel.stopTimer();
        mainViewModel.getFriends().postValue(new ArrayList<>());
        mainViewModel.getFriendsMessage().postValue(new HashMap<>());
        mainViewModel.getFriendRequests().postValue(new ArrayList<>());
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressWarnings("ConstantConditions")
    private void initViewModel() {
        viewModel.isInLoginOrRegister().removeObservers(this);
        viewModel.isLoginSuccess().removeObservers(this);
        viewModel.isRegisterSuccess().removeObservers(this);
        viewModel.getTip().removeObservers(this);

        viewModel.isLoginSuccess().setValue(false);
        viewModel.getTip().setValue(null);

        viewModel.isInLoginOrRegister().observe(this, isInLogin -> {
            mLoginBtn.setEnabled(!isInLogin && !viewModel.isLoginSuccess().getValue());
            mRegisterBtn.setEnabled(!isInLogin && !viewModel.isLoginSuccess().getValue());
        });

        viewModel.isLoginSuccess().observe(this, isLoginSuccess -> {
            mLoginBtn.setEnabled(!viewModel.isInLoginOrRegister().getValue() && !isLoginSuccess);
            mRegisterBtn.setEnabled(!viewModel.isInLoginOrRegister().getValue() && !isLoginSuccess);
            if (isLoginSuccess) {
                onLoginSuccess();
            }
        });

        viewModel.isRegisterSuccess().observe(this, isRegisterSuccess -> {
//            mLoginBtn.setEnabled(!viewModel.isInLoginOrRegister().getValue() && !isRegisterSuccess);
//            mRegisterBtn.setEnabled(!viewModel.isInLoginOrRegister().getValue() && !isRegisterSuccess);
        });

        viewModel.getTip().observe(this, tip -> {
            if (tip == null) return;
            Toast.makeText(getContext(), tip.second, Toast.LENGTH_SHORT).show();
        });
    }

    @OnClick(R.id.login_btn)
    public void onMLoginBtnClicked() {
        Editable username = mUsernameEdit.getText();
        Editable password = mPasswordEdit.getText();

        if (StringUtils.isExistNullOrEmpty(username, password)) {
            Toast.makeText(getContext(), "username and password should not empty!", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.login(username.toString(), password.toString());
        }

    }

    @OnClick(R.id.register_btn)
    public void onMRegisterBtnClicked() {
        Editable username = mUsernameEdit.getText();
        Editable password = mPasswordEdit.getText();

        if (StringUtils.isExistNullOrEmpty(username, password)) {
            Toast.makeText(getContext(), "username and password should not empty!", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.register(username.toString(), password.toString());
        }
    }

    private void onLoginSuccess() {
        mainViewModel.startTimer();
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_homeFragment);
    }
}
