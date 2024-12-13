// Generated by view binder compiler. Do not edit!
package com.example.carmatch1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.carmatch1.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMessageBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FloatingActionButton btnSend;

  @NonNull
  public final TextInputLayout inputLayoutMessage;

  @NonNull
  public final RecyclerView recyclerViewChat;

  @NonNull
  public final MaterialToolbar tb;

  @NonNull
  public final TextInputEditText txtMenssage;

  @NonNull
  public final TextView txtUserName;

  private ActivityMessageBinding(@NonNull ConstraintLayout rootView,
      @NonNull FloatingActionButton btnSend, @NonNull TextInputLayout inputLayoutMessage,
      @NonNull RecyclerView recyclerViewChat, @NonNull MaterialToolbar tb,
      @NonNull TextInputEditText txtMenssage, @NonNull TextView txtUserName) {
    this.rootView = rootView;
    this.btnSend = btnSend;
    this.inputLayoutMessage = inputLayoutMessage;
    this.recyclerViewChat = recyclerViewChat;
    this.tb = tb;
    this.txtMenssage = txtMenssage;
    this.txtUserName = txtUserName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMessageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMessageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_message, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMessageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_send;
      FloatingActionButton btnSend = ViewBindings.findChildViewById(rootView, id);
      if (btnSend == null) {
        break missingId;
      }

      id = R.id.inputLayoutMessage;
      TextInputLayout inputLayoutMessage = ViewBindings.findChildViewById(rootView, id);
      if (inputLayoutMessage == null) {
        break missingId;
      }

      id = R.id.recyclerViewChat;
      RecyclerView recyclerViewChat = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewChat == null) {
        break missingId;
      }

      id = R.id.tb;
      MaterialToolbar tb = ViewBindings.findChildViewById(rootView, id);
      if (tb == null) {
        break missingId;
      }

      id = R.id.txt_menssage;
      TextInputEditText txtMenssage = ViewBindings.findChildViewById(rootView, id);
      if (txtMenssage == null) {
        break missingId;
      }

      id = R.id.txtUserName;
      TextView txtUserName = ViewBindings.findChildViewById(rootView, id);
      if (txtUserName == null) {
        break missingId;
      }

      return new ActivityMessageBinding((ConstraintLayout) rootView, btnSend, inputLayoutMessage,
          recyclerViewChat, tb, txtMenssage, txtUserName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
