/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.heckad.logisim.std.arith.Arithmetic;
import com.heckad.logisim.std.base.Base;
import com.heckad.logisim.std.gates.Gates;
import com.heckad.logisim.std.io.Io;
import com.heckad.logisim.std.memory.Memory;
import com.heckad.logisim.std.plexers.Plexers;
import com.heckad.logisim.std.wiring.Wiring;
import com.heckad.logisim.tools.Library;
import com.heckad.logisim.tools.Tool;
import static com.heckad.logisim.util.LocaleString.*;

public class Builtin extends Library {
    private List<Library> libraries = null;

    public Builtin() {
        libraries = Arrays.asList(new Library[] {
            new Base(),
            new Gates(),
            new Wiring(),
            new Plexers(),
            new Arithmetic(),
            new Memory(),
            new Io(),
        });
    }

    @Override
    public String getName() { return "Builtin"; }

    @Override
    public String getDisplayName() { return getFromLocale("builtinLibrary"); }

    @Override
    public List<Tool> getTools() { return Collections.emptyList(); }

    @Override
    public List<Library> getLibraries() {
        return libraries;
    }
}
