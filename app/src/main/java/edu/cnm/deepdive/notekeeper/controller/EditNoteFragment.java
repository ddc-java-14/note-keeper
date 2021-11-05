package edu.cnm.deepdive.notekeeper.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import edu.cnm.deepdive.notekeeper.R;
import edu.cnm.deepdive.notekeeper.databinding.FragmentEditNoteBinding;
import edu.cnm.deepdive.notekeeper.model.entity.Note;
import edu.cnm.deepdive.notekeeper.viewmodel.NoteViewModel;

public class EditNoteFragment extends BottomSheetDialogFragment implements TextWatcher {

  private FragmentEditNoteBinding binding;
  private NoteViewModel viewModel;
  private long noteId;
  private Note note;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EditNoteFragmentArgs args = EditNoteFragmentArgs.fromBundle(getArguments());
    noteId = args.getNoteId();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentEditNoteBinding.inflate(inflater, container, false);
    binding.subject.addTextChangedListener(this);
    binding.text.addTextChangedListener(this);
    binding.cancel.setOnClickListener((v) -> dismiss());
    binding.save.setOnClickListener((v) -> {
      note.setSubject(binding.subject.getText().toString().trim());
      note.setText(binding.text.getText().toString().trim());
      viewModel.save(note);
      dismiss();
    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
    if (noteId != 0) {
      // TODO Set noteId in viewModel and observe viewModel.getNote().
    } else {
      note = new Note();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    // Do nothing.
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    // Do nothing.
  }

  @Override
  public void afterTextChanged(Editable s) {
    checkSubmitConditions();
  }

  private void checkSubmitConditions() {
    String subject = binding.subject
        .getText()
        .toString()
        .trim();
    String text = binding.text
        .getText()
        .toString()
        .trim();
    binding.save.setEnabled(!subject.isEmpty() && !text.isEmpty());
  }

}