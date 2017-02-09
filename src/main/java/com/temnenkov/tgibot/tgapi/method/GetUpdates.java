package com.temnenkov.tgibot.tgapi.method;

import lombok.Data;

@Data
public class GetUpdates {
    /**
     * Optional
     * Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers
     * of previously received updates. By default, updates starting with the earliest unconfirmed update are returned.
     * An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id.
     * The negative offset can be specified to retrieve updates starting from -offset update from the end of the
     * updates queue. All previous updates will forgotten.
     */
    private Long offset;
    /**
     * Optional
     * Limits the number of updates to be retrieved. Values between 1—100 are accepted. Defaults to 100.
     */
    private Integer limit;
    /**
     * Optional
     * Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling
     */
    private Integer timeout;

}
