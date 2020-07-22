package me.flotsam.frettler.command;

import me.flotsam.frettler.engine.IntervalPattern;
import me.flotsam.frettler.engine.Note;
import picocli.CommandLine.Command;

/**
 * Handles the initial BANJO command/param
 * @author philwhiles
 *
 */
@Command(name = "COMPLETIONS", description = "Generates bsh tab completion script")
public class CompletionsCommand implements Runnable {

  
  @Override
  public void run() {
    StringBuilder sb = new StringBuilder();
    sb.append("#/usr/bin/env bash\n"); 
    sb.append("_frettler_completions()\n"); 
    sb.append("{\n"); 
    sb.append("    local instr_opts\n"); 
    sb.append("    instr_opts=\"GUITAR BASSGUITAR UKELELE MANDOLIN BANJO\"\n"); 

    sb.append("    local view_opts\n"); 
    sb.append("    view_opts=\"HORIZONTAL VERTICAL\"\n"); 
    
    sb.append("    local type_opts\n"); 
    sb.append("    type_opts=\"SCALE CHORD\"\n"); 
    
    sb.append("    local note_opts\n"); 
    sb.append("    note_opts=\"");
    for (Note note:Note.values()) {
      sb.append(note.name()).append(" ");
    }
    sb.replace(sb.length()-1, sb.length(), "");
    sb.append("\"\n"); 

    sb.append("    local pattern_opts\n"); 
    sb.append("    pattern_opts=\"");
    for (IntervalPattern pattern:IntervalPattern.values()) {
      sb.append(pattern.name()).append(" ");
    }
    sb.replace(sb.length()-1, sb.length(), "");
    sb.append("\"\n"); 
    
    
    sb.append("    case $COMP_CWORD in\n");
    sb.append("        1)\n");
    sb.append("            COMPREPLY=( $(compgen -W \"${instr_opts}\" -- \"${COMP_WORDS[COMP_CWORD]}\") )\n");
    sb.append("            ;;\n");
    sb.append("        2)\n");
    sb.append("            COMPREPLY=( $(compgen -W \"${view_opts}\" -- \"${COMP_WORDS[COMP_CWORD]}\") )\n");
    sb.append("            ;;\n");
    sb.append("        3)\n");
    sb.append("            COMPREPLY=( $(compgen -W \"${type_opts}\" -- \"${COMP_WORDS[COMP_CWORD]}\") )\n");
    sb.append("            ;;\n");
    sb.append("        4)\n");
    sb.append("            COMPREPLY=( $(compgen -W \"${note_opts}\" -- \"${COMP_WORDS[COMP_CWORD]}\") )\n");
    sb.append("            ;;\n");
    sb.append("        5)\n");
    sb.append("            COMPREPLY=( $(compgen -W \"${pattern_opts}\" -- \"${COMP_WORDS[COMP_CWORD]}\") )\n");
    sb.append("            ;;\n");
    sb.append("    esac\n");
    sb.append("    return 0\n");
    
    sb.append("}\n"); 
    sb.append("complete -F _frettler_completions frettler\n"); 
    System.out.println(sb);
  }
}