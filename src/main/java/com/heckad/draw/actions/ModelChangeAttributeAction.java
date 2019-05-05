/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.actions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.heckad.draw.model.AttributeMapKey;
import com.heckad.draw.model.CanvasModel;
import com.heckad.draw.model.CanvasObject;
import com.heckad.logisim.data.Attribute;

import static com.heckad.logisim.util.LocaleString.*;

public class ModelChangeAttributeAction extends ModelAction {
    private Map<AttributeMapKey, Object> oldValues;
    private Map<AttributeMapKey, Object> newValues;
    private Attribute<?> attr;

    public ModelChangeAttributeAction(CanvasModel model,
            Map<AttributeMapKey, Object> oldValues,
            Map<AttributeMapKey, Object> newValues) {
        super(model);
        this.oldValues = oldValues;
        this.newValues = newValues;
    }

    @Override
    public Collection<CanvasObject> getObjects() {
        Set<CanvasObject> ret = new HashSet<CanvasObject>();
        for (AttributeMapKey key : newValues.keySet()) {
            ret.add(key.getObject());
        }
        return ret;
    }

    @Override
    public String getName() {
        Attribute<?> a = attr;
        if (a == null) {
            boolean found = false;
            for (AttributeMapKey key : newValues.keySet()) {
                Attribute<?> at = key.getAttribute();
                if (found) {
                    if (a == null ? at != null : !a.equals(at)) {
                        a = null;
                        break;
                   }
                } else {
                    found = true;
                    a = at;
                }
            }
            attr = a;
        }
        
        if (a == null) {
            return getFromLocale("actionChangeAttributes");
        }
        return getFromLocale("actionChangeAttribute", a.getDisplayName());
    }

    @Override
    void doSub(CanvasModel model) {
        model.setAttributeValues(newValues);
    }

    @Override
    void undoSub(CanvasModel model) {
        model.setAttributeValues(oldValues);
    }
}
