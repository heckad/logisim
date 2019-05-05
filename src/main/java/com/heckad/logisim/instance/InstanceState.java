/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.instance;

import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.AttributeSet;
import com.heckad.logisim.data.Value;
import com.heckad.logisim.proj.Project;

public interface InstanceState {
    public Instance getInstance();
    public InstanceFactory getFactory();
    public Project getProject();
    public AttributeSet getAttributeSet();
    public <E> E getAttributeValue(Attribute<E> attr);
    public Value getPort(int portIndex);
    public boolean isPortConnected(int portIndex);
    public void setPort(int portIndex, Value value, int delay);
    public InstanceData getData();
    public void setData(InstanceData value);
    public void fireInvalidated();
    public boolean isCircuitRoot();
    public long getTickCount();
}
