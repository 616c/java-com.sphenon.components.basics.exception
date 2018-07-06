package com.sphenon.basics.exception;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.message.*;

import java.util.Vector;

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ReturnCode">ReturnCodes</link>,
    in contrast to
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ExceptionError">ExceptionErrors</link>,
    reflect normal processing circumstances. They have to be declared when
    thrown, and should be caught and processed by the caller. They provide
    a means of returning a processing result in addition to what is
    declared as the return value.
*/
public class ReturnCode extends java.lang.Exception implements Dumpable, ExceptionWithMultipleCauses, ExceptionWithHelpMessage {

    protected Context context;
    protected Message message;
    protected MessageText help_message_text; // end user readable
    protected Throwable[] causes;

    protected ReturnCode (CallContext call_context, Throwable cause, Message message) {
        this(call_context, cause, message, null);
    }

    protected ReturnCode (CallContext call_context, Throwable cause, Message message, MessageText help_message_text) {
        this.context = Context.create(call_context);
        this.message = message;
        this.help_message_text = help_message_text;
        if (cause != null) {
            this.causes = new Throwable[1];
            this.causes[0] = cause;
        } else {
            this.causes = null;
        }
    }

    public Context getContext () {
        return this.context;
    }

    public Message getMessageObject (CallContext call_context) {
        return this.message;
    }

    public MessageText getHelpMessageText () {
        return this.help_message_text;
    }

    public String getHelpMessage () {
        MessageText mt = this.getHelpMessageText();
        return mt == null ? null : mt.getText(this.getContext());
    }

    public Throwable[] getCauses (CallContext call_context) {
        return this.causes;
    }

    public Throwable getCause () {
        if (this.causes == null || this.causes.length == 0) {
            return null; 
        }
        if (this.causes.length > 1) {
            System.err.println("Exception " + this + " has more than one cause, cannot report only first:");
            for (Throwable cause : causes) {
                System.err.println("Cause: " + cause);
            }
        }
        return this.causes[0];
    }

    public void addCause(CallContext call_context, Throwable cause) {
        if (this.causes == null) {
            this.causes = new Throwable[1];
            this.causes[0] = cause;
        } else {
            Throwable[] new_causes = new Throwable[this.causes.length + 1];
            int i;
            for (i=0; i<this.causes.length; i++) {
                new_causes[i] = this.causes[i];
            }
            new_causes[i] = cause;
            this.causes = new_causes;
        }
    }

    public String getMessage () {
        return this.getMessage(true);
    }

    public String getMessage (boolean detailed) {
        String result_message = (this.message == null ? "" : this.message.toString());
        if (detailed && this.causes != null) {
            result_message += "\n[causes: ";
            for (int i=0; i<this.causes.length; i++) {
                result_message += (i == 0 ? "" : ",\n") + this.causes[i].toString();
            }
            result_message += "]";
        }
        return result_message;
    }

    public String toString () {
        return this.toString(true);
    }

    public String toString (boolean detailed) {
        return this.getClass().getName() + " : " + this.getMessage(detailed);
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, "ReturnCode", this.getClass().getName().replaceFirst(".*\\.",""));
        dump_node.dump(context, "Message", (this.message == null ? "" : this.message.toString()));
        Vector<String> rss = RuntimeStep.getStack(this.getContext());
        if (rss != null) {
            DumpNode stack_dump_node = dump_node.openDumpTechnicalDetails(context, "RuntimeSteps");
            if (stack_dump_node != null) {
                for (String rs : rss) {
                    stack_dump_node.dump(context, rs);
                }
            }
        }
        DumpNode stack_dump_node = dump_node.openDumpTechnicalDetails(context, "StackTrace");
        if (stack_dump_node != null) {
            for (StackTraceElement SE : this.getStackTrace()) {
                stack_dump_node.dump(context, SE.toString());
            }
        }
        DumpNode causes_dump_node = dump_node.openDump(context, "Causes");
        Throwable[] causes = this.getCauses(context);
        if (causes != null) {
            for (Throwable cause : causes) {
                causes_dump_node.dump(context, "Cause", cause);
            }
        }
    }
}
