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
import com.sphenon.basics.variatives.tplinst.*;

import java.util.Map;
import java.util.HashMap;

public class ExceptionHelpTextRegistry {

    static public interface ExceptionTranslator {
        public Variative_String_ translate(CallContext context, Throwable t);
    }

    static public class ExceptionTranslator_Cause implements ExceptionTranslator {
        public ExceptionTranslator_Cause(CallContext context) {
        }
        public Variative_String_ translate(CallContext context, Throwable t) {
            return t.getCause() == null ? null : get(context, t.getCause());
        }
    }

    static protected Map<Class,Variative_String_> registry;

    static protected Map<Class,ExceptionTranslator> translator_registry;

    static public void register(CallContext context, Class c, Variative_String_ vs) {
        if (registry == null) {
            registry = new HashMap<Class,Variative_String_>();
        }
        registry.put(c, vs);
    }

    static public void register(CallContext context, Class c, ExceptionTranslator et) {
        if (translator_registry == null) {
            translator_registry = new HashMap<Class,ExceptionTranslator>();
        }
        translator_registry.put(c, et);
    }

    static public Variative_String_ get(CallContext context, Throwable t) {
        Variative_String_ help_text = null;
        
        if (t instanceof ExceptionError) {
            help_text = ((ExceptionError) t).getHelpMessageText();
            if (help_text != null) { return help_text; }
        }

        if (t instanceof ReturnCode) {
            help_text = ((ReturnCode) t).getHelpMessageText();
            if (help_text != null) { return help_text; }
        }

        /* [Issue:TSMBackendJPA.java,ExceptionHelpTextRegistry.java]
            the exception is registered in JPA, but needs to be checked here,
            since not all IllegalStateException indicate that same problem;
            solution: improve registry in ExceptionHelpTextRegistry.java
            to be able to examine message texts and act correspondingly
        */

        if( t instanceof java.lang.IllegalStateException ){
            String m = t.getMessage();
            if( m != null && m.equals( "No transaction is currently active" )){
                if (registry != null) {
                    help_text = registry.get(t.getClass());
                    if (help_text != null) { return help_text; }
                }
            }
        }

        if (registry != null) {
            help_text = registry.get(t.getClass());
            if (help_text != null) { return help_text; }
        }

        if (translator_registry != null) {
            ExceptionTranslator et = translator_registry.get(t.getClass());
            if (et != null) {
                help_text = et.translate(context, t);
            }
            if (help_text != null) { return help_text; }
        }

        return MessageText.create(context, "Ein Fehler ist aufgetreten (%(class), %(message))", "class", t.getClass().getName().replaceFirst(".*\\.",""), "message", t.getMessage());
    }
}
