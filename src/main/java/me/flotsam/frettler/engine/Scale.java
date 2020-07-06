package me.flotsam.frettler.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;

public class Scale {

  final Logger LOGGER = LoggerFactory.getLogger(Scale.class);

  public static final Scale CHROMATIC_SCALE = new Scale(Arrays.asList(Note.values()));

  private ScaleNote head = null;
  private ScaleNote tail = null;
  private ScalePattern scalePattern;
  private Note rootNote;


  public Scale(ScalePattern scalePattern, Note rootNote) {
    this.scalePattern = scalePattern;
    this.rootNote = rootNote;

    Optional<ScaleNote> westernScaleRoot = CHROMATIC_SCALE.findScaleNote(rootNote);
    for (ScaleInterval interval : scalePattern.getIntervals()) {
      ScaleNote scaleNote = Scale.getScaleNote(westernScaleRoot.get(), interval);
      addScaleNote(scaleNote.getNote(), Optional.of(interval));
    }
  }

  private Scale(List<Note> notes) {
    this.scalePattern = ScalePattern.CHROMATIC;
    this.rootNote = notes.get(0);
    for (Note note : notes) {
      addScaleNote(note, Optional.empty());
    }
  }

  // creat arpeggion in another more suitable class - the arpeggio repeats across the fretboard wheras a Sacle is limited in length
//  public Scale createArpeggio(ChordPattern chordPattern) {
//    List<Note> arpeggioNotes = new ArrayList<>();
//    ScaleNote currentNoteNode = head;
//
//    if (head == null) {
//      throw new RuntimeException("This scale contains no notes");
//    } else {
//      do {
//        if (currentNoteNode.getNote() == searchNote) {
//          return true;
//        }
//        currentNoteNode = currentNoteNode.getNextScaleNote();
//      } while (currentNoteNode != head);
//      return false;
//    }
//  }
  
  private void addScaleNote(Note note, Optional<ScaleInterval> interval) {
    ScaleNote newNoteNode = new ScaleNote(note, interval, this);

    if (head == null) {
      head = newNoteNode;
      head.setPosition(Position.HEAD);
    } else {
      tail.setNextScaleNote(newNoteNode);
      tail.setPosition(Position.MIDDLE);
    }

    tail = newNoteNode;
    tail.setPosition(Position.TAIL);
    tail.setNextScaleNote(head);
  }

  public boolean containsNote(Note note) {
    ScaleNote currentNoteNode = head;

    if (head == null) {
      return false;
    } else {
      do {
        if (currentNoteNode.getNote() == note) {
          return true;
        }
        currentNoteNode = currentNoteNode.getNextScaleNote();
      } while (currentNoteNode != head);
      return false;
    }
  }

  public Optional<ScaleNote> findScaleNote(Note note) {
    ScaleNote currentNoteNode = head;

    if (head == null) {
      return Optional.empty();
    } else {
      do {
        if (currentNoteNode.getNote() == note) {
          return Optional.of(currentNoteNode);
        }
        currentNoteNode = currentNoteNode.getNextScaleNote();
      } while (currentNoteNode != head);
      return Optional.empty();
    }
  } 
  
  public Optional<ScaleNote> findScaleNote(ScaleInterval scaleInterval) {
    ScaleNote currentNoteNode = head;

    if (head == null) {
      return Optional.empty();
    } else {
      do {
        if (currentNoteNode.getInterval().get() == scaleInterval) {
          return Optional.of(currentNoteNode);
        }
        currentNoteNode = currentNoteNode.getNextScaleNote();
      } while (currentNoteNode != head);
      return Optional.empty();
    }
  }

  public ScaleNote getHead() {
    return head;
  }

//  private void setHead(ScaleNote head) {
//    this.head = head;
//  }

  public ScaleNote getTail() {
    return tail;
  }

//  private void setTail(ScaleNote tail) {
//    this.tail = tail;
//  }

  public ScalePattern getScalePattern() {
    return scalePattern;
  }

  public Note getRootNote() {
    return rootNote;
  }

  public List<ScaleNote> getScaleNotes() {
    List<ScaleNote> scaleNotes = new ArrayList<>();
    ScaleNote note = head;

    if (note != null) {
      do {
        scaleNotes.add(note);
        note = note.getNextScaleNote();
      } while (note != tail);
    }
    return scaleNotes;
  }

  public String getTitle() {
     return rootNote.getLabel() + " " + scalePattern + " Scale";
  }

  public String toString() {
    ScaleNote currentNoteNode = head;
    StringBuilder builder = new StringBuilder();
    builder.append(getTitle()).append(" :");
    if (head != null) {
      do {
        builder.append(currentNoteNode.getNote().getLabel()).append(":")
            .append(currentNoteNode.getInterval().get().getLabel()).append(" ");
        currentNoteNode = currentNoteNode.getNextScaleNote();
      } while (currentNoteNode != head);
    }
    builder.append("\n");
    return builder.toString();
  }

  public static ScaleNote getScaleNote(ScaleNote rootScaleNote, ScaleInterval interval) {
    ScaleNote scaleNote = rootScaleNote;
    for (int i = 0; i < interval.getValue(); i++) {
      scaleNote = scaleNote.getNextScaleNote();
    }
    return scaleNote;
  }

  public static ScaleNote getThirdNote(ScaleNote rootScaleNote, int third) {
    ScaleNote scaleNote = rootScaleNote;
    for (int i = 0; i < third; i++) {
      scaleNote = scaleNote.getNextScaleNote();
      if (scaleNote.getPosition() == Position.TAIL) {
        scaleNote = scaleNote.getNextScaleNote();
      }
    }
    return scaleNote;
  }
}


enum Position {
  HEAD, MIDDLE, TAIL;
}
