package com.zambo.zambo_mterminal100.Interface;

import com.zambo.zambo_mterminal100.Activity.MainActivity;
import com.zambo.zambo_mterminal100.model.BankList;
import com.zambo.zambo_mterminal100.model.BankMode;
import com.zambo.zambo_mterminal100.model.BeneListDmt;
import com.zambo.zambo_mterminal100.model.ComplaintExample;
import com.zambo.zambo_mterminal100.model.DmtTransfer;
import com.zambo.zambo_mterminal100.model.Fingpayseps;
import com.zambo.zambo_mterminal100.model.FundExample;
import com.zambo.zambo_mterminal100.model.Notification;
import com.zambo.zambo_mterminal100.model.RechargeModal;
import com.zambo.zambo_mterminal100.model.Res;
import com.zambo.zambo_mterminal100.model.Response;
import com.zambo.zambo_mterminal100.model.Roffer;
import com.zambo.zambo_mterminal100.model.Service;
import com.zambo.zambo_mterminal100.model.States;
import com.zambo.zambo_mterminal100.model.UserData;
import com.zambo.zambo_mterminal100.model.rchage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Apiinterface {
  String val=MainActivity.userData.getToken();
  //  Call<Notification> storetoken(@Query("")String );
    @FormUrlEncoded
    @POST("deviceRegistration")
    Call<Notification> storetoken(@Field("token") String token,@Field("uUid") String userid);
//  @Headers("Content-Type: application/json")
  @FormUrlEncoded
  @POST("postdataapp")
  Call<com.zambo.zambo_mterminal100.model.Response>primeaeps(@Header("token")String token, @Field("aadhaardata") String aadhaardata, @Field("adhaarNumber") String adhaarNumber, @Field("transactionType") String transactionType, @Field("mobileNumber") String mobileNumber, @Field("latitude") Double latitude, @Field("longitude") Double longitude, @Field("amount") String amount, @Field("srno") String srno, @Field("userId") String userId, @Field("source") String source, @Field("bName")String iinno, @Field("isIris")String isIris);

  @FormUrlEncoded
  @POST("getSenderInfo")
  Call<Response> checkkyc(@Field("remMobile") String mobile,@Field("userId") String userid,@Field("source") String source);

  @FormUrlEncoded
  @POST("getOutletData")
  Call<Fingpayseps> fingpayaeps(@Field("userId") String userId, @Field("latitude") Double latitude, @Field("longitude") Double longitude, @Field("source") String source);

  @FormUrlEncoded
  @POST("usercashbackupdate")
  Call<Response> cashbackupdate(@Field("uUid") String userid, @Field("txnid") String txtnid, @Field("amount")String amount, @Field("commission")String commission);

  @FormUrlEncoded
  @POST("usercashbackupdate")
   Call<Response> scratchcard(@Field("uUid") String userid, @Field("txnid") String txtnid, @Field("amount")String amount, @Field("commission")String commission);

  @FormUrlEncoded
  @POST("getBeneficiary")
  Call<DmtTransfer> getdetails(@Field("SenderMobileNo") String mobile, @Field("uUid") String userId);

  @POST("createSender")
  Call<UserData> createSender(@Query("LastName")String name, @Query("FirstName")String lastname, @Query("SenderMobileNo")String mobile, @Query("uUid")String userid);


  @POST("bankList")
  Call<List<BeneListDmt>> getbanklist();

  @POST("getBankList")
  Call<List<BankList>> getprimebanklist();

  @POST("states")
  Call<States> getstatelist();

  @FormUrlEncoded
  @POST("redeemAepsStatus")
  Call<Res>getaepsstatus(@Field("userId") String userId);

  @FormUrlEncoded
  @POST("senddata")
  Call<com.zambo.zambo_mterminal100.model.MiniStatement>ministatement(@Header("token")String token, @Field("aadhaardata") String aadhaardata, @Field("adhaarNumber") String adhaarNumber, @Field("transactionType") String transactionType, @Field("mobileNumber") String mobileNumber, @Field("latitude") Double latitude, @Field("longitude") Double longitude, @Field("amount") String amount, @Field("srno") String srno, @Field("userId") String userId, @Field("source") String source, @Field("bName")String iinno, @Field("isIris")String isIris);

    @FormUrlEncoded
    @POST("updateTxnData")
    Call<com.zambo.zambo_mterminal100.model.MiniStatement>getministatement(@Header("token")String token, @Field("txnid") String txnid, @Field("userId") String userId, @Field("source") String source);

  @FormUrlEncoded
  @POST("payDonation")
  Call<com.zambo.zambo_mterminal100.model.Response>donation(@Header("token")String token, @Field("uUid") String uUid, @Field("amount") String amount, @Field("response") String response, @Field("source") String source);

  @FormUrlEncoded
  @POST("getSenderInfo")
  Call<com.zambo.zambo_mterminal100.model.Response>SenderInfo(@Header("token")String token, @Field("remMobile") String remMobile, @Field("userId") String userId, @Field("source") String source);

  @FormUrlEncoded
  @POST("createSender")
  Call<com.zambo.zambo_mterminal100.model.Response>createSender(@Header("token")String token, @Field("remMobile") String remMobile, @Field("remName") String remName, @Field("address") String address, @Field("city") String city, @Field("district") String district, @Field("state") String state, @Field("pincode") String pincode, @Field("userId") String userId, @Field("source") String source);


  @FormUrlEncoded
  @POST("remOtp")
  Call<com.zambo.zambo_mterminal100.model.Response>OTP(@Header("token")String token, @Field("remMobile") String remMobile, @Field("userId") String userId, @Field("source") String source);

  @FormUrlEncoded
  @POST("verifyRemitter")
  Call<com.zambo.zambo_mterminal100.model.Response>verifyOTP(@Header("token")String token, @Field("remMobile") String remMobile, @Field("remOtp") String remOtp, @Field("userId") String userId, @Field("source") String source);

  @FormUrlEncoded
  @POST("generateOtp")
  Call<Response>getOtpin(@Field("username") String username);

  @FormUrlEncoded
  @POST("changePinOtp ")
  Call<Response>getPinOtp(@Field("userId") String username,@Field("uOTP") String uOTP);

  @FormUrlEncoded
  @POST("verifyOtp")
  Call<Response>getverifyOtp(@Field("username") String username,@Field("uOTP") String uOTP);

  @FormUrlEncoded
  @POST("changeTxnPin")
  Call<Response>getpin(@Field("userId") String userId,@Field("uPin") String uPin,@Field("cPin") String cPin);

  @FormUrlEncoded
  @POST("lockedscreen")
  Call<Response>getlockedscreen(@Field("uUid") String uUid,@Field("loginpin") String loginpin);

  @POST("zamboservices")
  Call<List<Service>> getServices();

  @FormUrlEncoded
  @POST("complaint")
  Call<Response>getcomplain(@Field("userId") String userId,@Field("service") String service,@Field("txnId") String txnId,@Field("amount") String amount,@Field("date") String date,@Field("remarks") String remarks);

  @FormUrlEncoded
  @POST("changePassword")
  Call<Response>getpassword(@Field("userId") String userId,@Field("oPassword") String oPassword,@Field("uPassword") String uPassword,@Field("cpassword") String cpassword);

  @FormUrlEncoded
  @POST("complaintList")
  Call<ComplaintExample>getComplaint(@Field("userId") String userId, @Field("startdate") String uPin, @Field("enddate") String cPin);

  @FormUrlEncoded
  @POST("fundRequestList")
  Call<FundExample>getFund(@Field("userId") String userId, @Field("startdate") String uPin, @Field("enddate") String cPin);

  @POST("zamboaccounts")
  Call<List<BankMode>> getmode();

  @FormUrlEncoded
  @POST("fundRequest")
  Call<Response> getfundRequest(@Field("userId")String userid,@Field("account") String account,@Field("mode") String mode,@Field("amount") String amount,@Field("remarks") String remarks,@Field("txnId") String txnId);

  @FormUrlEncoded
  @POST("checkTxnStatusRecharge2")
  Call<rchage>checkRecharge(@Field("txnId") String txnId);

  @FormUrlEncoded
  @POST("rechargemobile")
  Call<RechargeModal> rechargemobile(@Header("token") String token,@Header("appPin") String appPin,@Field("mobile") String number,@Field("operator") String operatorId,@Field("amount") String amount,@Field("service") String serviceProvider,@Field("latitude") Double lat,@Field("longitude") Double lon,@Field("userId") String userId);

  @FormUrlEncoded
  @POST("roffer")
  Call<Roffer> getofferlist(@Header("token") String token, @Field("mobile") String number, @Field("operator") String operatorId, @Field("userId") String userId);
}
