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

    A limitation of the software was encountered. The feature, capacity
    or optimisation may be added in the future, but it might also be not.

    Typically, neither the user nor the developer made a mistake, it is
    simply a limitation. The occurence of such errors does, as matter of
    course, not imply a liability of the software producer.
*/
public class ExceptionLimitation extends ExceptionError
{
    protected ExceptionLimitation (CallContext cc, Throwable cause, Message message) {
        super(cc, cause, message);
    }
    
    static public void createAndThrow(CallContext cc, Message message) {
        throw new ExceptionLimitation(cc, null, message);
    }

    static public void createAndThrow(CallContext cc, Throwable cause, Message message) {
        throw new ExceptionLimitation(cc, cause, message);
    }

    static public ExceptionLimitation createExceptionLimitation(CallContext cc, Message message) {
        return new ExceptionLimitation(cc, null, message);
    }

    static public ExceptionLimitation createExceptionLimitation(CallContext cc, Throwable cause, Message message) {
        return new ExceptionLimitation(cc, cause, message);
    }
}
