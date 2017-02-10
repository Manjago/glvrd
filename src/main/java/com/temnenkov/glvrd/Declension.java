package com.temnenkov.glvrd;

import lombok.AllArgsConstructor;

public class Declension {

    public String decline(int value, DeclensionInfo info) {

        if (info == null) {
            throw new IllegalArgumentException();
        }

        return declineCore(value, info);
    }

    private String declineCore(int value, DeclensionInfo info) {
        if (value < 0) {
            return decline(-value, info);
        } else if (value == 1) {
            return info.one;
        } else if (value >= 2 && value <= 4) {
            return info.two;
        } else if (value >= 5 && value <= 20) {
            return info.five;
        } else if (value >= 21 && value <= 100) {
            return decline(value % 10, info);
        } else if (value >= 100) {
            return decline(value % 100, info);
        }

        return info.five;
    }

    @AllArgsConstructor
    public static class DeclensionInfo {
        private String one;
        private String two;
        private String five;

        public static DeclensionInfo create() {
            return new Declension.DeclensionInfo("замечание", "замечания", "замечаний");
        }
    }


}
