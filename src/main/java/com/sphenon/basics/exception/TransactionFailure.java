package com.sphenon.basics.exception;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

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
import com.sphenon.basics.message.*;

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/TransactionFailure">TransactionFailures</link>
    represent failures during the execution of transactions, caused by
    conflicts or otherwise database related reasons. On the one hand side, a
    reasonable application might not only want but actually should catch them,
    on the other hand side they might be thrown in each and every modification
    related method, i.e. in many methods.

    Therefore it is extremely unhandy to declare those exceptions at all those methods -
    a classical match for the java RuntimeException. Furthermore, emphasising the special
    nature of TransactionFailures, they should even not be catched directly at the methods
    throwing them, because the invoker does not have enough information to know how to
    handle the exception, he might even not know that he's involved in a transaction.

    So, where are those exceptions to be catched? The correct answer to this question is
    "the transaction boundary", a concept not existing in programming languages like java.
    This boundary is a closure (not modeled) around all possible entry points (method
    invocations) into an object aggregate currently participating with an ongoing transaction.
    Much care must be taken to keep this closure really closed, or alternatively, tools like
    code generators might be used to guarantee it (thereby augmenting language behaviour).

    Unfortunately, as a consequence of the lack of lanugage support for such a concept, no
    warnings will be issued if TransactionFailures are not caught by the application, which
    will result in runtime failures.
*/
public class TransactionFailure extends java.lang.RuntimeException implements ExceptionWithMultipleCauses, ExceptionWithHelpMessage
{
    protected Context context;
    protected Message message;
    protected MessageText help_message_text; // end user readable
    protected Throwable[] causes;

    protected TransactionFailure (CallContext call_context, Throwable cause, Message message) {
        this.context = Context.create(call_context);
        this.message = message;
        if (cause != null) {
            this.causes = new Throwable[1];
            this.causes[0] = cause;
        } else {
            this.causes = null;
        }
    }

    protected TransactionFailure (CallContext call_context, Throwable cause, Message message, MessageText help_message_text) {
        this(call_context, cause, message);
        this.help_message_text = help_message_text;
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

    static public void createAndThrow(CallContext cc, Message message) {
        throw new TransactionFailure(cc, null, message);
    }

    static public void createAndThrow(CallContext cc, Throwable cause, Message message) {
        throw new TransactionFailure(cc, cause, message);
    }

    static public TransactionFailure createTransactionFailure(CallContext cc, Message message) {
        return new TransactionFailure(cc, null, message);
    }

    static public TransactionFailure createTransactionFailure(CallContext cc, Throwable cause, Message message) {
        return new TransactionFailure(cc, cause, message);
    }
}
