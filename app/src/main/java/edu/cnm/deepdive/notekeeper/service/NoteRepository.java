package edu.cnm.deepdive.notekeeper.service;

import android.app.Application;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.notekeeper.model.dao.NoteDao;
import edu.cnm.deepdive.notekeeper.model.entity.Note;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import java.util.List;

public class NoteRepository {

  private final Application context;
  private final NoteDao noteDao;

  public NoteRepository(Application context) {
    this.context = context;
    noteDao = NoteKeeperDatabase
        .getInstance()
        .getNoteDao();
  }

  public LiveData<Note> get(long noteId) {
    return noteDao.select(noteId);
  }

  public LiveData<List<Note>> getAll() {
    return noteDao.select();
  }

  public Single<Note> save(Note note) {
    Single<Note> task;
    note.setUpdated(new Date());
    if (note.getId() == 0) {
      note.setCreated(note.getUpdated());
      task = noteDao
          .insert(note)
          .map((id) -> {
            note.setId(id);
            return note;
          });
    } else {
      task = noteDao
          .update(note)
          .map((count) -> note);
    }
    return task.subscribeOn(Schedulers.io());
  }

  public Completable delete(Note note) {
    return (note.getId() == 0)
        ? Completable.complete()
        : noteDao
            .delete(note)
            .ignoreElement()
            .subscribeOn(Schedulers.io());
  }

}
