// Generated by view binder compiler. Do not edit!
package com.example.carmatch1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.carmatch1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemVehicleFavBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton btnStartConversation;

  @NonNull
  public final ImageView imgPhoto;

  @NonNull
  public final TextView txtBrand;

  @NonNull
  public final TextView txtModel;

  @NonNull
  public final TextView txtPrice;

  private ItemVehicleFavBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageButton btnStartConversation, @NonNull ImageView imgPhoto,
      @NonNull TextView txtBrand, @NonNull TextView txtModel, @NonNull TextView txtPrice) {
    this.rootView = rootView;
    this.btnStartConversation = btnStartConversation;
    this.imgPhoto = imgPhoto;
    this.txtBrand = txtBrand;
    this.txtModel = txtModel;
    this.txtPrice = txtPrice;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemVehicleFavBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemVehicleFavBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_vehicle_fav, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemVehicleFavBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnStartConversation;
      ImageButton btnStartConversation = ViewBindings.findChildViewById(rootView, id);
      if (btnStartConversation == null) {
        break missingId;
      }

      id = R.id.imgPhoto;
      ImageView imgPhoto = ViewBindings.findChildViewById(rootView, id);
      if (imgPhoto == null) {
        break missingId;
      }

      id = R.id.txtBrand;
      TextView txtBrand = ViewBindings.findChildViewById(rootView, id);
      if (txtBrand == null) {
        break missingId;
      }

      id = R.id.txtModel;
      TextView txtModel = ViewBindings.findChildViewById(rootView, id);
      if (txtModel == null) {
        break missingId;
      }

      id = R.id.txtPrice;
      TextView txtPrice = ViewBindings.findChildViewById(rootView, id);
      if (txtPrice == null) {
        break missingId;
      }

      return new ItemVehicleFavBinding((ConstraintLayout) rootView, btnStartConversation, imgPhoto,
          txtBrand, txtModel, txtPrice);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
