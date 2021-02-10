package com.zambo.zambo_mterminal100.network;

import com.zambo.zambo_mterminal100.model.ActiveServiceResponse;
import com.zambo.zambo_mterminal100.model.BeneficiaryResponse;
import com.zambo.zambo_mterminal100.model.TransferCheckResponse;
import com.zambo.zambo_mterminal100.model.TransferResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestApiService {

    @FormUrlEncoded
    @POST("activeServices1")
    Call<ActiveServiceResponse> activeService(@Field("uUid") String userId);

    @FormUrlEncoded
    @POST("axisdmt/getSenderInfo")
    Call<TransferCheckResponse> checkUserTransfer(@Field("remMobile") String mobile, @Field("userId") String userId,
                                                  @Header("token") String token, @Field("source") String source);

    @FormUrlEncoded
    @POST("axisdmt/createSender")
    Call<TransferResponse> addRemitter(@Field("remMobile") String mobile,@Field("remName") String name,
                                       @Field("address") String address,@Field("city") String city,
                                       @Field("district") String district,@Field("state") String state,
                                       @Field("pincode") String pincode,@Field("userId") String userId,
                                       @Header("token") String token,@Field("source") String source);

    @FormUrlEncoded
    @POST("axisdmt/remOtp")
    Call<TransferResponse> otpRemitter(@Field("remMobile") String mobile,@Field("userId") String userId,
                                       @Header("token") String token,@Field("source") String source);

    @FormUrlEncoded
    @POST("axisdmt/verifyRemitter")
    Call<TransferResponse> verifyRemitter(@Field("remMobile") String mobile,@Field("remOtp") String otp,
                                          @Field("userId") String userId,@Header("token") String token,@Field("source") String source);

    @FormUrlEncoded
    @POST("axisdmt/getBeneficiary")
    Call<BeneficiaryResponse> getRemitterBeneficiary(@Field("remMobile") String mobile, @Field("userId") String userId,
                                                     @Header("token") String token, @Field("source") String source);

    @FormUrlEncoded
    @POST("axisdmt/addBeneficiary")
    Call<TransferResponse> addBeneficiary(@Field("userId") String userId,@Header("token") String token,@Field("source") String source,
                                          @Field("remMobile")String mobile,@Field("beneMobile")String beneMobile,@Field("beneName") String beneName,
                                          @Field("beneAccount") String bankAccount,@Field("ifsccode") String bankIfsccode,
                                          @Field("bankcode") String bankCode);

    @FormUrlEncoded
    @POST("axisdmt/deleteBeneficiary")
    Call<TransferResponse> deleteBeneficiary(@Field("userId") String userId,@Header("token") String token,@Field("source") String source,
                                             @Field("remMobile") String mobile,@Field("beneficiary_id") String beneficiary_id);

    @FormUrlEncoded
    @POST("axisdmt/sendMoney")
    Call<TransferResponse> transferMoney(@Field("userId") String userId, @Header("token") String token, @Field("source") String source,
                                         @Field("remMobile") String mobile, @Field("remitterId") String remitterId,
                                         @Field("beneAccount") String beneAccount, @Field("txntype") String txnType,
                                         @Field("ifsccode") String ifsccode, @Field("amount") String amount,
                                         @Field("beneficiary_id") String beneficiary_id,
                                         @Field("latitude") String lat, @Field("longitude") String lon,@Field("txnpin") String pin);
}
