package org.ikuven.resultaggregator.results;

import java.util.Arrays;

public enum InternalStatus {

    OK("OK"),
    FINISHED("Finished"),
    MISSING_PUNCH("MissingPunch"),
    DISQUALIFIED("Disqualified"),
    DID_NOT_FINISH("DidNotFinish"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    OVER_TIME("OverTime"),
    SPORTING_WITHDRAWAL("SportingWithdrawal"),
    NOT_COMPETING("NotCompeting"),
    MOVED("Moved"),
    MOVED_UP("MovedUp"),
    DID_NOT_START("DidNotStart"),
    DID_NOT_ENTER("DidNotEnter"),
    CANCELLED("Cancelled");

    private final String value;

    InternalStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static InternalStatus fromValue(String value) {

        return Arrays.stream(InternalStatus.values())
                .filter(internalStatus -> internalStatus.value().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(value));
    }
}
