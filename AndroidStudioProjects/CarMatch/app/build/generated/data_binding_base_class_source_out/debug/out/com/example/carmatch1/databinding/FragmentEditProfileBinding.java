// Generated by view binder compiler. Do not edit!
package com.example.carmatch1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.carmatch1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentEditProfileBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnCancel;

  @NonNull
  public final Button btnSaveVehicle;

  @NonNull
  public final EditText editTextCondition;

  @NonNull
  public final EditText editTextDescription;

  @NonNull
  public final EditText editTextFuelType;

  @NonNull
  public final EditText editTextKm;

  @NonNull
  public final EditText editTextPlate;

  @NonNull
  public final EditText editTextPrice;

  @NonNull
  public final EditText editTextType;

  @NonNull
  public final EditText editTextVehicleBrand;

  @NonNull
  public final EditText editTextVehicleModel;

  @NonNull
  public final EditText editTextVehicleYear;

  @NonNull
  public final ToolbarAppBinding includeToolbarApp;

  private FragmentEditProfileBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnCancel,
      @NonNull Button btnSaveVehicle, @NonNull EditText editTextCondition,
      @NonNull EditText editTextDescription, @NonNull EditText editTextFuelType,
      @NonNull EditText editTextKm, @NonNull EditText editTextPlate,
      @NonNull EditText editTextPrice, @NonNull EditText editTextType,
      @NonNull EditText editTextVehicleBrand, @NonNull EditText editTextVehicleModel,
      @NonNull EditText editTextVehicleYear, @NonNull ToolbarAppBinding includeToolbarApp) {
    this.rootView = rootView;
    this.btnCancel = btnCancel;
    this.btnSaveVehicle = btnSaveVehicle;
    this.editTextCondition = editTextCondition;
    this.editTextDescription = editTextDescription;
    this.editTextFuelType = editTextFuelType;
    this.editTextKm = editTextKm;
    this.editTextPlate = editTextPlate;
    this.editTextPrice = editTextPrice;
    this.editTextType = editTextType;
    this.editTextVehicleBrand = editTextVehicleBrand;
    this.editTextVehicleModel = editTextVehicleModel;
    this.editTextVehicleYear = editTextVehicleYear;
    this.includeToolbarApp = includeToolbarApp;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentEditProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentEditProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_edit_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentEditProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnCancel;
      Button btnCancel = ViewBindings.findChildViewById(rootView, id);
      if (btnCancel == null) {
        break missingId;
      }

      id = R.id.btnSaveVehicle;
      Button btnSaveVehicle = ViewBindings.findChildViewById(rootView, id);
      if (btnSaveVehicle == null) {
        break missingId;
      }

      id = R.id.editTextCondition;
      EditText editTextCondition = ViewBindings.findChildViewById(rootView, id);
      if (editTextCondition == null) {
        break missingId;
      }

      id = R.id.editTextDescription;
      EditText editTextDescription = ViewBindings.findChildViewById(rootView, id);
      if (editTextDescription == null) {
        break missingId;
      }

      id = R.id.editTextFuelType;
      EditText editTextFuelType = ViewBindings.findChildViewById(rootView, id);
      if (editTextFuelType == null) {
        break missingId;
      }

      id = R.id.editTextKm;
      EditText editTextKm = ViewBindings.findChildViewById(rootView, id);
      if (editTextKm == null) {
        break missingId;
      }

      id = R.id.editTextPlate;
      EditText editTextPlate = ViewBindings.findChildViewById(rootView, id);
      if (editTextPlate == null) {
        break missingId;
      }

      id = R.id.editTextPrice;
      EditText editTextPrice = ViewBindings.findChildViewById(rootView, id);
      if (editTextPrice == null) {
        break missingId;
      }

      id = R.id.editTextType;
      EditText editTextType = ViewBindings.findChildViewById(rootView, id);
      if (editTextType == null) {
        break missingId;
      }

      id = R.id.editTextVehicleBrand;
      EditText editTextVehicleBrand = ViewBindings.findChildViewById(rootView, id);
      if (editTextVehicleBrand == null) {
        break missingId;
      }

      id = R.id.editTextVehicleModel;
      EditText editTextVehicleModel = ViewBindings.findChildViewById(rootView, id);
      if (editTextVehicleModel == null) {
        break missingId;
      }

      id = R.id.editTextVehicleYear;
      EditText editTextVehicleYear = ViewBindings.findChildViewById(rootView, id);
      if (editTextVehicleYear == null) {
        break missingId;
      }

      id = R.id.includeToolbarApp;
      View includeToolbarApp = ViewBindings.findChildViewById(rootView, id);
      if (includeToolbarApp == null) {
        break missingId;
      }
      ToolbarAppBinding binding_includeToolbarApp = ToolbarAppBinding.bind(includeToolbarApp);

      return new FragmentEditProfileBinding((ConstraintLayout) rootView, btnCancel, btnSaveVehicle,
          editTextCondition, editTextDescription, editTextFuelType, editTextKm, editTextPlate,
          editTextPrice, editTextType, editTextVehicleBrand, editTextVehicleModel,
          editTextVehicleYear, binding_includeToolbarApp);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}