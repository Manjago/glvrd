package org.jdesktop.http.async;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.jdesktop.http.Method;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LameHttpRequest extends AsyncHttpRequest {

    public Object onload;

    public LameHttpRequest() {
        addReadyStateChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() == ReadyState.LOADED) {
                    if  (LameHttpRequest.this.onload instanceof ScriptObjectMirror){

                        if (LameHttpRequest.this.onload != null){
                            ScriptObjectMirror f = (ScriptObjectMirror) LameHttpRequest.this.onload;
                            f.call(f);
                            LameHttpRequest.this.onload = null;
                        }

                    }
                }
            }
        });
    }

    public void open(String str, String url) {
        open(Method.valueOf(str), url, true);
    }



}
