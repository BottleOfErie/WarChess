package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.game.ChessClassExtractor;

import java.lang.instrument.Instrumentation;

public class PreMain {

    public static ChessClassExtractor transformer=new ChessClassExtractor();

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(transformer, true);
    }

}
