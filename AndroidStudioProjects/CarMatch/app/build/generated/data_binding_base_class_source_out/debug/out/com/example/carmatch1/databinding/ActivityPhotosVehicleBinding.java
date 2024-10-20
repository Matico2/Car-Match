// Generated by view binder compiler. Do not edit!
package com.example.carmatch1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.carmatch1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPhotosVehicleBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final FloatingActionButton btnAddPhoto;

  @NonNull
  public final Button btnReturn;

  @NonNull
  public final Button btnSave;

  @NonNull
  public final ToolbarAppBinding includeToolbarApp;

  @NonNull
  public final GridLayout photoContainer;

  private ActivityPhotosVehicleBinding(@NonNull FrameLayout rootView,
      @NonNull FloatingActionButton btnAddPhoto, @NonNull Button btnReturn, @NonNull Button btnSave,
      @NonNull ToolbarAppBinding includeToolbarApp, @NonNull GridLayout photoContainer) {
    this.rootView = rootView;
    this.btnAddPhoto = btnAddPhoto;
    this.btnReturn = btnReturn;
    this.btnSave = btnSave;
    this.includeToolbarApp = includeToolbarApp;
    this.photoContainer = photoContainer;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPhotosVehicleBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPhotosVehicleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_photos_vehicle, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPhotosVehicleBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_add_photo;
      FloatingActionButton btnAddPhoto = ViewBindings.findChildViewById(rootView, id);
      if (btnAddPhoto == null) {
        break missingId;
      }

      id = R.id.btnReturn;
      Button btnReturn = ViewBindings.findChildViewById(rootView, id);
      if (btnReturn == null) {
        break missingId;
      }

      id = R.id.btnSave;
      Button btnSave = ViewBindings.findChildViewById(rootView, id);
      if (btnSave == null) {
        break missingId;
      }

      id = R.id.includeToolbarApp;
      View includeToolbarApp = ViewBindings.findChildViewById(rootView, id);
      if (includeToolbarApp == null) {
        break missingId;
      }
      ToolbarAppBinding binding_includeToolbarApp = ToolbarAppBinding.bind(includeToolbarApp);

      id = R.id.photoContainer;
      GridLayout photoContainer = ViewBindings.findChildViewById(rootView, id);
      if (photoContainer == null) {
        break missingId;
      }

      return new ActivityPhotosVehicleBinding((FrameLayout) rootView, btnAddPhoto, btnReturn,
          btnSave, binding_includeToolbarApp, photoContainer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
