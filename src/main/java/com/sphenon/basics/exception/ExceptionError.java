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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.message.*;

import java.util.Vector;

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    ExceptionErrors reflect <quote>real</quote> (i.e. in fact
    <emphasis>exceptional</emphasis>) exceptions, i.e. erraneous,
    unforeseeable and typically not handable situations. The name emphasises
    this excepional, erraneous character.

    The decision to derive from <classname>Error</classname> instead of
    <classname>RuntimeException</classname> was made with intent. The
    contract of <classname>Exception</classname> states explicitly
    <quote>that a reasonable application might want to catch</quote>
    them. This is simply not true for the kind of exceptions defined
    here, since they reflect actual errors. No reasonable application
    should catch them, except for robust server main loops, but these may
    want to catch every <classname>Throwable</classname> anyway.

    These exceptions do not belong to the contract, since declaring them
    in the interface of the class would be the same as saying <quote>This
    class can be faulty due to bugs or misconfiguration</quote>, which is
    of at least questionable usefullness.

    In contrast to the semi-consequent solution of the introduction of
    <classname>RuntimeException</classname>s, we make a sharp and easily
    understandable distinction here between actual errors and normal
    processing conditions. An index out of bounds can have several
    different causes: a faulty vm, a programming error, a usage error
    by another programmer (contract violation), an end user input
    error. The cases should be distinguished and each one handled
    appropriately. Just allowing to throw the respective exception in any
    circumstance is misguided.

    Classes dervied from
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ExceptionError">ExceptionError</link>
    provide a categorisation which shall help locating the effective cause
    of the problem as far as possible. Additional information, supporting
    this goal, should be provided within the message and context.

    In contrast to
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ExceptionError">ExceptionErrors</link>,
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ReturnCode">ReturnCodes</link>
    reflect normal process results which should be handled by the caller.

    In case you are concerned with transaction failure handling, please be sure
    to also read and understand the documentation of
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/TransactionFailure">TransactionFailure</link>
    and the various implications.
*/
abstract public class ExceptionError extends java.lang.Error implements Dumpable, WithContext, ExceptionWithHelpMessage {
    protected CallContext context;
    protected Message message; // complete technical information
    protected MessageText help_message_text; // end user readable

    protected ExceptionError (CallContext cc, Throwable cause, Message message) {
        this.context = cc;
        this.message = message;
        this.help_message_text = null;
        this.initCause(cause);
    }

    protected ExceptionError (CallContext cc, Throwable cause, Message message, MessageText help_message_text) {
        this.context = cc;
        this.message = message;
        this.help_message_text = help_message_text;
        this.initCause(cause);
    }

    public CallContext getContext () {
        return this.context;
    }

    public Message getMessageObject () {
        return this.message;
    }

    public MessageText getHelpMessageText () {
        return this.help_message_text;
    }

    public String getHelpMessage () {
        MessageText mt = this.getHelpMessageText();
        return mt == null ? null : mt.getText(this.getContext());
    }

    public String getMessage () {
        return this.getMessage(true);
    }

    public String getMessage (boolean detailed) {
        return this.message.toString() + (detailed == false ? "" : ((getCause() == null ? "" : ("\n[cause: " + getCause().toString() + "]")) + RuntimeStep.getStackDump(this.getContext())));
    }

    public String toString () {
        return this.toString(true);
    }

    public String toString (boolean detailed) {
        return this.getClass().getName() + " : " + this.getMessage(detailed);
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, "Exception", this.message.toString());
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
        Throwable cause = this.getCause();
        if (cause != null) {
            dump_node.dump(context, "Cause", cause);
        }
    }
}
