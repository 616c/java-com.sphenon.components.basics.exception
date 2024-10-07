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

    The software is not configured properly. Typical reasons include:
    missing parameters, parameters with invalid values (e.g. non existent
    files), no configuration at all.
*/
public class ExceptionConfigurationError extends ExceptionEnvironmentError
{
    protected ExceptionConfigurationError (CallContext cc, Throwable cause, Message message) {
        super(cc, cause, message);
    }
    
    static public void createAndThrow(CallContext cc, Message message) {
        throw new ExceptionConfigurationError(cc, null, message);
    }

    static public void createAndThrow(CallContext cc, Throwable cause, Message message) {
        throw new ExceptionConfigurationError(cc, cause, message);
    }

    static public ExceptionConfigurationError createExceptionConfigurationError(CallContext cc, Message message) {
        return new ExceptionConfigurationError(cc, null, message);
    }

    static public ExceptionConfigurationError createExceptionConfigurationError(CallContext cc, Throwable cause, Message message) {
        return new ExceptionConfigurationError(cc, cause, message);
    }
}
