package com.temnenkov.glvrd;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GlvrdApi {

    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
    private boolean inited;

    public GlvrdApi init() {

        if (inited) {
            return this;
        }

        try {

            String scriptContent = getScriptContent();

            engine.eval("var window = new Object()");

            engine.eval("var XMLHttpRequest = Java.type(\"org.jdesktop.http.async.LameHttpRequest\")");
            engine.eval(scriptContent);
            inited = true;
        } catch (ScriptException e) {
            throw new GlvrdException(e);
        }

        return this;
    }


    public void getStatus(Consumer<Boolean> callback) {
        init();


        try {
            jdk.nashorn.api.scripting.ScriptObjectMirror window = (ScriptObjectMirror) engine.get("window");
            jdk.nashorn.api.scripting.ScriptObjectMirror glvrd = (ScriptObjectMirror) window.get("glvrd");

            Invocable inv = (Invocable) engine;
            inv.invokeMethod(glvrd, "getStatus", (Consumer<ScriptObjectMirror>) s -> callback.accept("ok".equalsIgnoreCase(String.valueOf(s.get("status")))));

        } catch (ScriptException | NoSuchMethodException e) {
            throw new GlvrdException(e);
        }

    }

    public void proofread(String text, Consumer<ProofreadResponse> callback) {

        init();


        try {
            jdk.nashorn.api.scripting.ScriptObjectMirror window = (ScriptObjectMirror) engine.get("window");
            jdk.nashorn.api.scripting.ScriptObjectMirror glvrd = (ScriptObjectMirror) window.get("glvrd");

            Invocable inv = (Invocable) engine;
            inv.invokeMethod(glvrd, "proofread", text, (Consumer<ScriptObjectMirror>) s -> {

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


            });

        } catch (ScriptException | NoSuchMethodException e) {
            throw new GlvrdException(e);
        }


    }

    private String getScriptContent() {

        try {
            URL url = new URL("https://api.glvrd.ru/v1/glvrd.js");
            return IOUtils.readFullyAsString(url.openStream(), "UTF-8");
        } catch (IOException e) {
            throw new GlvrdException(e);
        }
    }

}
