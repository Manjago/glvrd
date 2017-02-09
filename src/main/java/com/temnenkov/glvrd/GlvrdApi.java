package com.temnenkov.glvrd;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.springframework.beans.factory.annotation.Required;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GlvrdApi {

    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
    private boolean inited;

    private JsScriptProvider jsScriptProvider;

    public GlvrdApi init() {

        if (inited) {
            return this;
        }

        try {

            String scriptContent = jsScriptProvider.getScript();

            engine.eval("var window = new Object()");

            engine.eval("var XMLHttpRequest = Java.type(\"org.jdesktop.http.async.LameHttpRequest\")");
            engine.eval(scriptContent);
            inited = true;
        } catch (ScriptException e) {
            throw new GlvrdException(e);
        }

        return this;
    }


    @SuppressWarnings("squid:S1172")
    public void getStatus(Consumer<Boolean> callback) {
        init();


        try {
            ScriptObjectMirror window = (ScriptObjectMirror) engine.get("window");
            ScriptObjectMirror glvrd = (ScriptObjectMirror) window.get("glvrd");

            Invocable inv = (Invocable) engine;
            inv.invokeMethod(glvrd, "getStatus", (Consumer<ScriptObjectMirror>) s -> callback.accept("ok".equalsIgnoreCase(String.valueOf(s.get("status")))));

        } catch (ScriptException | NoSuchMethodException e) {
            throw new GlvrdException(e);
        }

    }

    @SuppressWarnings("squid:S1172")
    public void proofread(String text, Consumer<ProofreadResponse> callback) {

        init();


        try {
            ScriptObjectMirror window = (ScriptObjectMirror) engine.get("window");
            ScriptObjectMirror glvrd = (ScriptObjectMirror) window.get("glvrd");

            Invocable inv = (Invocable) engine;
            inv.invokeMethod(glvrd, "proofread", text, new ScriptObjectMirrorConsumer(callback));

        } catch (ScriptException | NoSuchMethodException e) {
            throw new GlvrdException(e);
        }


    }

    @Required
    public void setJsScriptProvider(JsScriptProvider jsScriptProvider) {
        this.jsScriptProvider = jsScriptProvider;
    }


    private static class ScriptObjectMirrorConsumer implements Consumer<ScriptObjectMirror> {
        private final Consumer<ProofreadResponse> callback;

        public ScriptObjectMirrorConsumer(Consumer<ProofreadResponse> callback) {
            this.callback = callback;
        }

        @Override
        public void accept(ScriptObjectMirror s) {

            boolean ok = "ok".equalsIgnoreCase(String.valueOf(s.get("status")));

            if (!ok) {
                callback.accept(new ProofreadResponse());
            } else {
                ProofreadResponse p = new ProofreadResponse();
                p.setOk(true);
                p.setScore(String.valueOf(s.get("score")));

                p.setFragments(new ArrayList<>());

                ScriptObjectMirror fragments = (ScriptObjectMirror) s.get("fragments");

                for (int i = 0; i < fragments.size(); ++i) {
                    ScriptObjectMirror jfrag = (ScriptObjectMirror) fragments.get(Integer.toString(i));
                    Fragment frag = new Fragment();
                    frag.setStart((int) ((double) (Double) jfrag.get("start")));
                    frag.setEnd((int) ((double) (Double) jfrag.get("end")));
                    frag.setUrl(String.valueOf(jfrag.get("url")));

                    ScriptObjectMirror jhint = (ScriptObjectMirror) jfrag.get("hint");

                    Hint hint = new Hint();
                    hint.setDescription(String.valueOf(jhint.get("description")));
                    hint.setPenalty(String.valueOf(jhint.get("penalty")));
                    hint.setWeight((Integer) jhint.get("weight"));
                    hint.setName(String.valueOf(jhint.get("name")));

                    frag.setHint(hint);

                    p.getFragments().add(frag);
                }


                callback.accept(p);

            }


        }
    }
}
