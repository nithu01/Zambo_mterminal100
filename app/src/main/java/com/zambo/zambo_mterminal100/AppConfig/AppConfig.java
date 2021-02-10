package com.zambo.zambo_mterminal100.AppConfig;
/**
 * Created by aftab on 4/28/2018.
 */
public class AppConfig {
    public static final String CONSTANT="https://www.zambo.in/mobileapi/";
    public static final String MAIN_CONSTANT="https://www.zambo.in/";
    private static final String TRANS_CONSTANT="https://www.zambo.in/dmtmrupay/";
    private static final String DMT_CONSTANT="https://www.zambo.in/dmtmahagram/";
    private static final String MRU_PAY_DMT="https://www.zambo.in/mrupaydmt/";
    private static final String ERROR_STATUS="https://www.zambo.in/webhook/";
    private static final String MICRO_ATM_NEW="https://www.zambo.in/egram/microatm/";
    private static final String  CONSTANT_FASTTAG="https://www.zambo.in/axisdmt/fastag/";
    private static String AEPS_CONSTANT="https://www.zambo.in/prime/aeps/";
    /*==============================================================================================*/

    static final String LOGIN=CONSTANT+"login";
    static final String REGISTER=CONSTANT+"registration";
   // public static final String PROFILE_DATA=CONSTANT+"profile";
    public static final String GET_PROFILE_DATA=CONSTANT+"getProfile";
    public static final String PAYMENT_RESPOSNE=CONSTANT+"addMoneyRpTxn";
    static final String GET_STATES=CONSTANT+"states";
    static final String UPDATE_PROFILE=CONSTANT+"profileUpdate";
    static final String GET_BALANCE = CONSTANT+"profileBalance";
    public static final String RECHARGE_PROCESS=CONSTANT+"rechargeProcess";
    public static final String BILL_PAYMENT_PROCESS=CONSTANT+"billPaymentProcess";
    public static final String AEPS_OUTLET=CONSTANT+"aepsOutlet";
    public static final String GET_SENDER_INFO=TRANS_CONSTANT+"getSenderInfo";
    public static final String CREATE_SENDER=TRANS_CONSTANT+"createSender";
    public static final String RESEND_OTP=TRANS_CONSTANT+"resendOtp";
    public static final String VERIFY_SENDER=TRANS_CONSTANT+"verifySender";
    public static final String BENEFICIARY_LIST=TRANS_CONSTANT+"getBeniList";
    public static final String ADD_BENEFICIARY=TRANS_CONSTANT+"addBeneficiary";
    public static final String VERIFY_BENEFICIARY=TRANS_CONSTANT+"verifyBeneficiary";
    public static final String DELETE_BENEFICIARY=TRANS_CONSTANT+"deleteBeneficiary";
    public static final String TRANSFER=TRANS_CONSTANT+"sendMoney";
    public static final String GET_SERVICE=CONSTANT+"activeServices";
    public static final String ACTIVE_SERVICE=CONSTANT+"activeServices1";
    public static final String PAYMENT_TRANSACTION = CONSTANT + "paymentTransaction";
    public static final String AEPS_2_SERVICE = CONSTANT+"aeps2Outlet";
    public static final String MICRO_ATM=CONSTANT+"aeps2Outlet";
    public static final String CHECK_AEPS_STATUS=CONSTANT+"redeemAepsStatus";
    public static final String ROUND_PAY_OPERATOR=CONSTANT+"getRoundpayOperatorsList";
    public static final String REEDEM_AEPS_STATUS=CONSTANT+"redeemAepsStatus";
    public static final String GET_CROWNDFINCH_OPERATOR=CONSTANT+"getCrowdfinchOperatorsList";
    public static final String RECHARGE_PROCESS3 = CONSTANT+"recharge3Process";
    public static final String RECHARGE2_PROCESS=CONSTANT+"recharge2Process";
    public static final String RECHARGE2_AIRTELPROCESS=CONSTANT+"recharge2AirtelProcess";
    public static final String ASSIGNED_SERVICE = CONSTANT+"assignedServices";

    /*==============================================================================================*/

    public static final String FORGET_PASSWORD=CONSTANT+"resetPassword";
    public static final String MICROATM=MICRO_ATM_NEW+"requestData";
    public static final String GET_ERROR_STATUS=ERROR_STATUS+"checkErrorStatus";
    public static final String GET_SENDER_LIMIT= MRU_PAY_DMT+"getSenderLimit";
    public static final String GET_BENEFICARY_DMT=MRU_PAY_DMT+"getBeneficiary" ;
    public static final String GET_SENDER_INFO_DMT=MRU_PAY_DMT+"getSenderInfo";
    public static final String CREATE_SENDER_DMT=MRU_PAY_DMT+"createSender";
    public static final String RESEND_OTP_DMT=DMT_CONSTANT+"resendOtp";
    public static final String VERIFY_SENDER_DMT=MRU_PAY_DMT+"senderVerify";
    public static final String BENIFICARY_VERIFY=MRU_PAY_DMT+"beneficiaryVerify";
    public static final String BANK_LIST_DMT2=MRU_PAY_DMT+ "bankList";
    public static final String ADD_BENEFICIARY_DMT=MRU_PAY_DMT +"addBeneficiary";
    public static final String OTP_VERIFY_BENE=DMT_CONSTANT + "otpVerifyBeneficiary";
    public static final String TRANSFER_MONEY=MRU_PAY_DMT+"sendMoney";
    public static final String TRANSFER_DMT=DMT_CONSTANT + "sendMoney";
    public static final String REDEEM_BALANCE_AEPS=CONSTANT+"redeemAeps";
    public static final String outletcheckstatus=MRU_PAY_DMT+"checkOutletStatus";

  //  public static final String BENEFICIARY_LIST_DMT = DMT_CONSTANT+"";

    /*==============================================================================================*/

    public static final String GET_OPERATOR_BBPS = CONSTANT+"getBbpsOperatorsList";
    public static final String GET_PARAMETERS_BBPS = CONSTANT+"getBbpsBillerParam";
    public static final String GET_BILLER_MODE_BBPS = CONSTANT +"getBbpsOperatorsBillerMode";
    public static final String FETCH_BILL_BBPS = CONSTANT+"billFetch";
    public static final String PAY_BILL_BBPS = CONSTANT+"bbpsBillPaymentProcess";
    public static final String PAY_BILL_AMOUNT_BBPS = CONSTANT+"bbpsMobilePaymentProcess";
    /*==============================================================================================*/
    public static final String GET_OPERATOR_LIST = CONSTANT+"getOperatorsList";
    public static final String RECHARGE_HISTORY = CONSTANT+"rechargeStatement";
    public static final String BILL_STATEMENT=CONSTANT+"billsStatement";
    public static final String WALLET_STATEMENT=CONSTANT+"accountStatement";
    public static final String WALLET_STATEMENT1=CONSTANT+"walletStatement";
    /*=================FASTAG=======================================================================*/
    public static final String FASTTAG_USERINFO = CONSTANT_FASTTAG+"getSenderInfo";
    public static final String FASTTAG_OTP_KYC = CONSTANT_FASTTAG+"remOtp";
    public static final String FASTTAG_OTP_VERIFY = CONSTANT_FASTTAG+"verifyRemitter";
    public static final String GET_BENE_FASTAG = CONSTANT_FASTTAG+"getBeneficiary";
    public static final String KYC_FASTAG = CONSTANT_FASTTAG+"createSender";
    public static final String FASTAG_BANK = CONSTANT_FASTTAG+"getBanks";
    public static final String ADD_BENEFICIARY_FASTAG = CONSTANT_FASTTAG+"addBeneficiary";
    public static final String RECHARGE_FASTAG = CONSTANT_FASTTAG+"sendMoney";
    public static final String CHARGES_FASTAG = CONSTANT_FASTTAG+"sendMoneyCharge";
    public static final String FASTAG_PAYMENT_RESPOSNE = CONSTANT_FASTTAG+"razorpayPaymentTransaction";
    /*==============================================================================================*/
    public static final String MONEY_BANK =MAIN_CONSTANT+"axisdmt/getBanks" ;
    /*==*/
    public static final String PRIME_AEPS = AEPS_CONSTANT+"getOutletData";

}
