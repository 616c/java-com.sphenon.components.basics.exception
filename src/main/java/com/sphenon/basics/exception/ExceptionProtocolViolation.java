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
import com.sphenon.basics.message.*;

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    The object protocol was violated. I.e., a method was called while the
    object was not in a respective required state.

    A typical reason is: objects that are - temporarily, during
    construction - allowed to exist in an invalid state and are being
    used in that state.

    It is not always clear who is responsible for the mistake, strictly
    speaking it is a precondition violation ("do not call methods on
    invalid objects"), but probably the caller has no means and is
    not responsible to determine the validity. E.g. the object might be
    "living" and becomes suddenly invalid. Then, the caller
    itself cannot do anything else but what the callee does, namely
    checking validity and throwing some rather meaningless exception.
*/
public class ExceptionProtocolViolation extends ExceptionPreConditionViolation
{
    protected ExceptionProtocolViolation (CallContext cc, Throwable cause, Message message) {
        super(cc, cause, message);
    }
    
    static public void createAndThrow(CallContext cc, Message message) {
        throw new ExceptionProtocolViolation(cc, null, message);
    }

    static public void createAndThrow(CallContext cc, Throwable cause, Message message) {
        throw new ExceptionProtocolViolation(cc, cause, message);
    }

    static public ExceptionProtocolViolation createExceptionProtocolViolation(CallContext cc, Message message) {
        return new ExceptionProtocolViolation(cc, null, message);
    }

    static public ExceptionProtocolViolation createExceptionProtocolViolation(CallContext cc, Throwable cause, Message message) {
        return new ExceptionProtocolViolation(cc, cause, message);
    }
}
