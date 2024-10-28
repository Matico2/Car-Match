// Generated by view binder compiler. Do not edit!
package com.example.carmatch1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.carmatch1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentAdVehiclesBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final LinearLayout additionalInfoLayout;

  @NonNull
  public final Button btnViewMore;

  @NonNull
  public final ToolbarAppBinding includeMainToolbar;

  @NonNull
  public final TextView vehicleAdvertiser;

  @NonNull
  public final CardView vehicleCardView;

  @NonNull
  public final TextView vehicleCondition;

  @NonNull
  public final TextView vehicleDescription;

  @NonNull
  public final TextView vehicleFuelType;

  @NonNull
  public final ImageView vehicleImage;

  @NonNull
  public final TextView vehicleKm;

  @NonNull
  public final TextView vehicleModel;

  @NonNull
  public final TextView vehiclePlate;

  @NonNull
  public final TextView vehiclePrice;

  @NonNull
  public final TextView vehicleYear;

  private FragmentAdVehiclesBinding(@NonNull FrameLayout rootView,
      @NonNull LinearLayout additionalInfoLayout, @NonNull Button btnViewMore,
      @NonNull ToolbarAppBinding includeMainToolbar, @NonNull TextView vehicleAdvertiser,
      @NonNull CardView vehicleCardView, @NonNull TextView vehicleCondition,
      @NonNull TextView vehicleDescription, @NonNull TextView vehicleFuelType,
      @NonNull ImageView vehicleImage, @NonNull TextView vehicleKm, @NonNull TextView vehicleModel,
      @NonNull TextView vehiclePlate, @NonNull TextView vehiclePrice,
      @NonNull TextView vehicleYear) {
    this.rootView = rootView;
    this.additionalInfoLayout = additionalInfoLayout;
    this.btnViewMore = btnViewMore;
    this.includeMainToolbar = includeMainToolbar;
    this.vehicleAdvertiser = vehicleAdvertiser;
    this.vehicleCardView = vehicleCardView;
    this.vehicleCondition = vehicleCondition;
    this.vehicleDescription = vehicleDescription;
    this.vehicleFuelType = vehicleFuelType;
    this.vehicleImage = vehicleImage;
    this.vehicleKm = vehicleKm;
    this.vehicleModel = vehicleModel;
    this.vehiclePlate = vehiclePlate;
    this.vehiclePrice = vehiclePrice;
    this.vehicleYear = vehicleYear;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentAdVehiclesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentAdVehiclesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_ad_vehicles, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentAdVehiclesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.additionalInfoLayout;
      LinearLayout additionalInfoLayout = ViewBindings.findChildViewById(rootView, id);
      if (additionalInfoLayout == null) {
        break missingId;
      }

      id = R.id.btnViewMore;
      Button btnViewMore = ViewBindings.findChildViewById(rootView, id);
      if (btnViewMore == null) {
        break missingId;
      }

      id = R.id.includeMainToolbar;
      View includeMainToolbar = ViewBindings.findChildViewById(rootView, id);
      if (includeMainToolbar == null) {
        break missingId;
      }
      ToolbarAppBinding binding_includeMainToolbar = ToolbarAppBinding.bind(includeMainToolbar);

      id = R.id.vehicleAdvertiser;
      TextView vehicleAdvertiser = ViewBindings.findChildViewById(rootView, id);
      if (vehicleAdvertiser == null) {
        break missingId;
      }

      id = R.id.vehicleCardView;
      CardView vehicleCardView = ViewBindings.findChildViewById(rootView, id);
      if (vehicleCardView == null) {
        break missingId;
      }

      id = R.id.vehicleCondition;
      TextView vehicleCondition = ViewBindings.findChildViewById(rootView, id);
      if (vehicleCondition == null) {
        break missingId;
      }

      id = R.id.vehicleDescription;
      TextView vehicleDescription = ViewBindings.findChildViewById(rootView, id);
      if (vehicleDescription == null) {
        break missingId;
      }

      id = R.id.vehicleFuelType;
      TextView vehicleFuelType = ViewBindings.findChildViewById(rootView, id);
      if (vehicleFuelType == null) {
        break missingId;
      }

      id = R.id.vehicleImage;
      ImageView vehicleImage = ViewBindings.findChildViewById(rootView, id);
      if (vehicleImage == null) {
        break missingId;
      }

      id = R.id.vehicleKm;
      TextView vehicleKm = ViewBindings.findChildViewById(rootView, id);
      if (vehicleKm == null) {
        break missingId;
      }

      id = R.id.vehicleModel;
      TextView vehicleModel = ViewBindings.findChildViewById(rootView, id);
      if (vehicleModel == null) {
        break missingId;
      }

      id = R.id.vehiclePlate;
      TextView vehiclePlate = ViewBindings.findChildViewById(rootView, id);
      if (vehiclePlate == null) {
        break missingId;
      }

      id = R.id.vehiclePrice;
      TextView vehiclePrice = ViewBindings.findChildViewById(rootView, id);
      if (vehiclePrice == null) {
        break missingId;
      }

      id = R.id.vehicleYear;
      TextView vehicleYear = ViewBindings.findChildViewById(rootView, id);
      if (vehicleYear == null) {
        break missingId;
      }

      return new FragmentAdVehiclesBinding((FrameLayout) rootView, additionalInfoLayout,
          btnViewMore, binding_includeMainToolbar, vehicleAdvertiser, vehicleCardView,
          vehicleCondition, vehicleDescription, vehicleFuelType, vehicleImage, vehicleKm,
          vehicleModel, vehiclePlate, vehiclePrice, vehicleYear);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
