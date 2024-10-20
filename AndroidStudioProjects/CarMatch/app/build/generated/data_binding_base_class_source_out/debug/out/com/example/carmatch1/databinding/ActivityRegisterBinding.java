// Generated by view binder compiler. Do not edit!
package com.example.carmatch1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.carmatch1.R;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegisterBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button buttonRegister;

  @NonNull
  public final ConstraintLayout imgProfile;

  @NonNull
  public final ToolbarAppBinding includeToolbarApp;

  @NonNull
  public final TextInputLayout textInputCPF;

  @NonNull
  public final TextInputLayout textInputCity;

  @NonNull
  public final TextInputLayout textInputDate;

  @NonNull
  public final TextInputLayout textInputEmail;

  @NonNull
  public final TextInputLayout textInputName;

  @NonNull
  public final TextInputLayout textInputPassword;

  private ActivityRegisterBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button buttonRegister, @NonNull ConstraintLayout imgProfile,
      @NonNull ToolbarAppBinding includeToolbarApp, @NonNull TextInputLayout textInputCPF,
      @NonNull TextInputLayout textInputCity, @NonNull TextInputLayout textInputDate,
      @NonNull TextInputLayout textInputEmail, @NonNull TextInputLayout textInputName,
      @NonNull TextInputLayout textInputPassword) {
    this.rootView = rootView;
    this.buttonRegister = buttonRegister;
    this.imgProfile = imgProfile;
    this.includeToolbarApp = includeToolbarApp;
    this.textInputCPF = textInputCPF;
    this.textInputCity = textInputCity;
    this.textInputDate = textInputDate;
    this.textInputEmail = textInputEmail;
    this.textInputName = textInputName;
    this.textInputPassword = textInputPassword;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonRegister;
      Button buttonRegister = ViewBindings.findChildViewById(rootView, id);
      if (buttonRegister == null) {
        break missingId;
      }

      ConstraintLayout imgProfile = (ConstraintLayout) rootView;

      id = R.id.includeToolbarApp;
      View includeToolbarApp = ViewBindings.findChildViewById(rootView, id);
      if (includeToolbarApp == null) {
        break missingId;
      }
      ToolbarAppBinding binding_includeToolbarApp = ToolbarAppBinding.bind(includeToolbarApp);

      id = R.id.textInputCPF;
      TextInputLayout textInputCPF = ViewBindings.findChildViewById(rootView, id);
      if (textInputCPF == null) {
        break missingId;
      }

      id = R.id.textInputCity;
      TextInputLayout textInputCity = ViewBindings.findChildViewById(rootView, id);
      if (textInputCity == null) {
        break missingId;
      }

      id = R.id.textInputDate;
      TextInputLayout textInputDate = ViewBindings.findChildViewById(rootView, id);
      if (textInputDate == null) {
        break missingId;
      }

      id = R.id.textInputEmail;
      TextInputLayout textInputEmail = ViewBindings.findChildViewById(rootView, id);
      if (textInputEmail == null) {
        break missingId;
      }

      id = R.id.textInputName;
      TextInputLayout textInputName = ViewBindings.findChildViewById(rootView, id);
      if (textInputName == null) {
        break missingId;
      }

      id = R.id.textInputPassword;
      TextInputLayout textInputPassword = ViewBindings.findChildViewById(rootView, id);
      if (textInputPassword == null) {
        break missingId;
      }

      return new ActivityRegisterBinding((ConstraintLayout) rootView, buttonRegister, imgProfile,
          binding_includeToolbarApp, textInputCPF, textInputCity, textInputDate, textInputEmail,
          textInputName, textInputPassword);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}