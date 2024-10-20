// Generated by view binder compiler. Do not edit!
package com.example.carmatch1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.carmatch1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityProfileBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FloatingActionButton btnAddPhoto;

  @NonNull
  public final Button btnEditProfile;

  @NonNull
  public final ShapeableImageView imgProfilePhoto;

  @NonNull
  public final ToolbarAppBinding includeToolbarApp;

  @NonNull
  public final TextView txtChangePassword;

  @NonNull
  public final TextView txtDeleteAccount;

  @NonNull
  public final TextView txtEmail;

  @NonNull
  public final TextView txtUsername;

  private ActivityProfileBinding(@NonNull ConstraintLayout rootView,
      @NonNull FloatingActionButton btnAddPhoto, @NonNull Button btnEditProfile,
      @NonNull ShapeableImageView imgProfilePhoto, @NonNull ToolbarAppBinding includeToolbarApp,
      @NonNull TextView txtChangePassword, @NonNull TextView txtDeleteAccount,
      @NonNull TextView txtEmail, @NonNull TextView txtUsername) {
    this.rootView = rootView;
    this.btnAddPhoto = btnAddPhoto;
    this.btnEditProfile = btnEditProfile;
    this.imgProfilePhoto = imgProfilePhoto;
    this.includeToolbarApp = includeToolbarApp;
    this.txtChangePassword = txtChangePassword;
    this.txtDeleteAccount = txtDeleteAccount;
    this.txtEmail = txtEmail;
    this.txtUsername = txtUsername;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_add_photo;
      FloatingActionButton btnAddPhoto = ViewBindings.findChildViewById(rootView, id);
      if (btnAddPhoto == null) {
        break missingId;
      }

      id = R.id.btn_edit_profile;
      Button btnEditProfile = ViewBindings.findChildViewById(rootView, id);
      if (btnEditProfile == null) {
        break missingId;
      }

      id = R.id.img_profile_photo;
      ShapeableImageView imgProfilePhoto = ViewBindings.findChildViewById(rootView, id);
      if (imgProfilePhoto == null) {
        break missingId;
      }

      id = R.id.includeToolbarApp;
      View includeToolbarApp = ViewBindings.findChildViewById(rootView, id);
      if (includeToolbarApp == null) {
        break missingId;
      }
      ToolbarAppBinding binding_includeToolbarApp = ToolbarAppBinding.bind(includeToolbarApp);

      id = R.id.txt_change_password;
      TextView txtChangePassword = ViewBindings.findChildViewById(rootView, id);
      if (txtChangePassword == null) {
        break missingId;
      }

      id = R.id.txt_delete_account;
      TextView txtDeleteAccount = ViewBindings.findChildViewById(rootView, id);
      if (txtDeleteAccount == null) {
        break missingId;
      }

      id = R.id.txt_email;
      TextView txtEmail = ViewBindings.findChildViewById(rootView, id);
      if (txtEmail == null) {
        break missingId;
      }

      id = R.id.txt_username;
      TextView txtUsername = ViewBindings.findChildViewById(rootView, id);
      if (txtUsername == null) {
        break missingId;
      }

      return new ActivityProfileBinding((ConstraintLayout) rootView, btnAddPhoto, btnEditProfile,
          imgProfilePhoto, binding_includeToolbarApp, txtChangePassword, txtDeleteAccount, txtEmail,
          txtUsername);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
