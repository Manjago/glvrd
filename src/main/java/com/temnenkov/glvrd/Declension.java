package com.temnenkov.glvrd;

import lombok.AllArgsConstructor;

public class Declension {

    public String decline(int value, DeclensionInfo info) {

         if (info == null) {
             throw new IllegalArgumentException();
         }

         if (value == 1) {
             return info.one;
         } else if (value >= 2 && value <= 4) {
             return info.two;
         }

         return  info.five;

    }

    @AllArgsConstructor
    public static class DeclensionInfo {
        private String one;
        private String two;
        private String five;

        public static DeclensionInfo create(){
            return new Declension.DeclensionInfo("замечание", "замечания", "замечаний");
        }
    }


}
