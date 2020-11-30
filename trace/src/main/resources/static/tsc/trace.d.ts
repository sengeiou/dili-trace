/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.27.744 on 2020-11-30 17:15:01.

interface Market {
    id: number;
    code: string;
    name: string;
    appId: number;
    appSecret: string;
    contextUrl: string;
    platformMarketId: number;
}

declare const enum RegisterBillStateEnum {
    NEW = 0,
    WAIT_AUDIT = 1,
    WAIT_SAMPLE = 2,
    WAIT_CHECK = 4,
    CHECKING = 5,
    ALREADY_CHECK = 6,
    ALREADY_AUDIT = 7,
}

declare const enum BillDetectStateEnum {
    PASS = "PASS",
    NO_PASS = "NO_PASS",
    REVIEW_PASS = "REVIEW_PASS",
    REVIEW_NO_PASS = "REVIEW_NO_PASS",
}

declare const enum RegisterSourceEnum {
    OTHERS = 0,
    TALLY_AREA = 1,
    TRADE_AREA = 2,
}
