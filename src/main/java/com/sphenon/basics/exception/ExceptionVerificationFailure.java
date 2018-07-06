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

    A safety check failed. I.e., a check, added for verfication
    purposes, proved false. Obviously, in normal operation, such errors are
    not supposed to occur. Therefore the actual reason is unknown, since
    otherwise it could have been avoided.

    The occurence of these failures typically indicate a programming
    error. The various subclasses provide more information on the presumed
    cause or location of the error.

    Caution:

    Checks throwing these exceptions can typically be disabled for
    production releases after sufficient testing. In fact, you are
    encouraged to do this. Therefore: do not use these exceptions for
    user input validation or similar purposes.

    Important:

    Though it might be tempting to use this class of exceptions
    - specifically the subclass
    <link xlink:href="oorl://Artefact:JavaClass/com/sphenon/basics/exception/ExceptionAssertionProvedFalse">ExceptionAssertionProvedFalse</link> -
    for each and every purpose, to avoid the effort of choosing
    an appropriate classification, don't take the bait. Firstly, these
    checks might be disabled, as mentioned, and secondly, software
    quality depends utterly on useful problem reporting.
*/
public class ExceptionVerificationFailure extends ExceptionError
{
    protected ExceptionVerificationFailure (CallContext cc, Throwable cause, Message message) {
        super(cc, cause, message);
    }
    
    static public void createAndThrow(CallContext cc, Message message) {
        throw new ExceptionVerificationFailure(cc, null, message);
    }

    static public void createAndThrow(CallContext cc, Throwable cause, Message message) {
        throw new ExceptionVerificationFailure(cc, cause, message);
    }

    static public ExceptionVerificationFailure createExceptionVerificationFailure(CallContext cc, Message message) {
        return new ExceptionVerificationFailure(cc, null, message);
    }

    static public ExceptionVerificationFailure createExceptionVerificationFailure(CallContext cc, Throwable cause, Message message) {
        return new ExceptionVerificationFailure(cc, cause, message);
    }
}
