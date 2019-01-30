package com.ingic.cavalliclub.retrofit;


import com.ingic.cavalliclub.entities.AddEditGuestListEntity;
import com.ingic.cavalliclub.entities.BarCategoriesEntity;
import com.ingic.cavalliclub.entities.CmsEntity;
import com.ingic.cavalliclub.entities.EntityCategoriesGuestList;
import com.ingic.cavalliclub.entities.EntityCavalliNights;
import com.ingic.cavalliclub.entities.EntityCreateGuestList;
import com.ingic.cavalliclub.entities.EntityGallery;
import com.ingic.cavalliclub.entities.EntityGetTotalList;
import com.ingic.cavalliclub.entities.EntityGuestListMember;
import com.ingic.cavalliclub.entities.EntityGuestListProfile;
import com.ingic.cavalliclub.entities.EntityHistoryProfile;
import com.ingic.cavalliclub.entities.EntityHomeSearch;
import com.ingic.cavalliclub.entities.EntityLiveMusic;
import com.ingic.cavalliclub.entities.EntityMarkUnmarkCalendar;
import com.ingic.cavalliclub.entities.EntityMemberships;
import com.ingic.cavalliclub.entities.EntityMessages;
import com.ingic.cavalliclub.entities.EntityMessagesThread;
import com.ingic.cavalliclub.entities.EntityOrderHistory;
import com.ingic.cavalliclub.entities.EntityProfileEvents;
import com.ingic.cavalliclub.entities.EntityPushCheck;
import com.ingic.cavalliclub.entities.GetCompetitionEntity;
import com.ingic.cavalliclub.entities.GetCompetitionHistoryEntity;
import com.ingic.cavalliclub.entities.GetReservationsEntity;
import com.ingic.cavalliclub.entities.LatestUpdatesEntity;
import com.ingic.cavalliclub.entities.MenuCategoryEntity;
import com.ingic.cavalliclub.entities.MenuCategoryParentEntity;
import com.ingic.cavalliclub.entities.MenuEntity;
import com.ingic.cavalliclub.entities.NewReservationEntity;
import com.ingic.cavalliclub.entities.NotificationEntity;
import com.ingic.cavalliclub.entities.PayNowEntity;
import com.ingic.cavalliclub.entities.PurchaseMembershipEntity;
import com.ingic.cavalliclub.entities.ResponseWrapper;
import com.ingic.cavalliclub.entities.SignUpEntity;
import com.ingic.cavalliclub.entities.TaxEntity;
import com.ingic.cavalliclub.entities.UpdateProfileEntity;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebService {

    @FormUrlEncoded
    @POST("register")
    Call<ResponseWrapper<SignUpEntity>> SignUpWithSocialMedia(
            @Field("user_name") String user_name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("dob") String dob,
            @Field("gender") String gender,
            @Field("socialmedia_id") String socialmedia_id,
            @Field("socialmedia_type") String socialmedia_type,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("register")
    Call<ResponseWrapper<SignUpEntity>> SimpleSignUp(
            @Field("user_name") String user_name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("dob") String dob,
            @Field("gender") String gender,
            @Field("password") String password,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("socialLogin")
    Call<ResponseWrapper<SignUpEntity>> socialMediaLogin(
            @Field("socialmedia_type") String socialmedia_type,
            @Field("socialmedia_id") String socialmedia_id,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("login")
    Call<ResponseWrapper<SignUpEntity>> Login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("codeVerification")
    Call<ResponseWrapper<SignUpEntity>> Verification(
            @Field("user_id") int user_id,
            @Field("verification_code") String verification_code
    );

    @FormUrlEncoded
    @POST("checkPhoneNo")
    Call<ResponseWrapper<SignUpEntity>> checkPhoneNumber(
            @Field("phone") String phone,
            @Field("type") String type,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("updatePhoneNo")
    Call<ResponseWrapper<SignUpEntity>> updatePhoneNumber(
            @Field("phone") String phone,
            @Header("token") String token
    );

    @GET("getCms")
    Call<ResponseWrapper<CmsEntity>> Cms(
            @Query("type") int type
    );

    @GET("getMenuCategories")
    Call<ResponseWrapper<ArrayList<MenuEntity>>> Menu(
            @Header("token") String token
    );

    @GET("getMenuProducts")
    Call<ResponseWrapper<ArrayList<MenuCategoryParentEntity>>> MenuCategories(
            @Query("category_id") int category_id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("addReservation")
    Call<ResponseWrapper> reserveNow(
            @Field("date") String date,
            @Field("time") String time,
            @Field("secondary_phone_no") String secondary_phone_no,
            @Field("no_people") int no_people,
            @Field("title") String title,
            @Field("event_id") String event_id,
            @Field("reservation_type") String reservation_type,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("addReservation")
    Call<ResponseWrapper> reserveNowNew(
            @Field("date") String date,
            @Field("time") String time,
            @Field("secondary_phone_no") String secondary_phone_no,
            @Field("no_people") int no_people,
            @Field("title") String title,
            @Field("event_id") String event_id,
            @Field("reservation_slot_id") int reservation_slot_id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("addReservation")
    Call<ResponseWrapper> reserveNowEvent(
            @Field("date") String date,
            @Field("time") String time,
            @Field("secondary_phone_no") String secondary_phone_no,
            @Field("no_people") int no_people,
            @Field("title") String title,
            @Field("event_id") String event_id,
            @Field("reservation_type") String reservation_type,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("editReservation")
    Call<ResponseWrapper> editReservation(
            @Field("id") String id,
            @Field("secondary_phone_no") String secondary_phone_no,
            @Field("time") String time,
            @Field("date") String date,
            @Field("no_people") int no_people,
            @Field("title") String title,
            @Field("reservation_type") String reservation_type,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("editReservation")
    Call<ResponseWrapper> editReservationNew(
            @Field("id") String id,
            @Field("secondary_phone_no") String secondary_phone_no,
            @Field("time") String time,
            @Field("date") String date,
            @Field("no_people") int no_people,
            @Field("title") String title,
            @Field("reservation_slot_id") int reservation_slot_id,
            @Header("token") String token
    );

    //New Reservation Slots
    @GET("getReservationSlots")
    Call<ResponseWrapper<ArrayList<NewReservationEntity>>> getReservationSlots(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("editReservation")
    Call<ResponseWrapper> editReservationDay(
            @Field("id") String id,
            @Field("secondary_phone_no") String secondary_phone_no,
            @Field("time") String time,
            @Field("day") String day,
            @Field("no_people") int no_people,
            @Field("title") String title,
            @Field("reservation_type") String reservation_type,
            @Header("token") String token
    );

    @GET("getReservations")
    Call<ResponseWrapper<ArrayList<GetReservationsEntity>>> getReservations(
            @Query("event_id") String event_id,
            @Header("token") String token
    );

    @GET("getLatestUpdate")
    Call<ResponseWrapper<ArrayList<LatestUpdatesEntity>>> LatestUpdates(
    );

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<ResponseWrapper<UpdateProfileEntity>> forgotPassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("resetPassword")
    Call<ResponseWrapper> resetPassword(
            @Field("password") String password,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("addInternationalRequest")
    Call<ResponseWrapper> internationalRequest(
            @Field("title") String title,
            @Field("message") String message,
            @Header("token") String token
    );

    @Multipart
    @POST("updateProfile")
    Call<ResponseWrapper<UpdateProfileEntity>> updateProfile(
            @Part("user_name") RequestBody full_name,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone_no,
            @Part("dob") RequestBody dob,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part profile_image
    );

    @POST("updateProfile")
    Call<ResponseWrapper<SignUpEntity>> updateProfileWithBody(
            @Header("token") String token,
            @Body RequestBody body
    );

    @FormUrlEncoded
    @POST("changePassword")
    Call<ResponseWrapper> changePassword(
            @Field("old_password") String old_password,
            @Field("password") String password
    );

    @GET("getCompetitions")
    Call<ResponseWrapper<ArrayList<GetCompetitionEntity>>> getCompetitions(
    );

    @Multipart
    @POST("participateCompetition")
    Call<ResponseWrapper> participateCompetition(
            @Part("competition_id") RequestBody competition_id,
            @Part MultipartBody.Part images
    );

    @FormUrlEncoded
    @POST("participateCompetition")
    Call<ResponseWrapper> participateCompetitionText(
            @Field("competition_id") String competition_id,
            @Field("text") String text
    );

    @GET("getCompetitionHistory")
    Call<ResponseWrapper<ArrayList<GetCompetitionHistoryEntity>>> getCompetitionsHistory(
    );

    @GET("getBarData")
    Call<ResponseWrapper<ArrayList<BarCategoriesEntity>>> getBarData(
    );

    @FormUrlEncoded
    @POST("addOrder")
    Call<ResponseWrapper<PayNowEntity>> addOrder(
            @Field("order_detail") String order_detail,
            @Field("sub_total") String sub_total,
            @Field("tax") String tax,
            @Field("total_amount") String total_amount,
            @Field("tip") String tip
    );

    @GET("getSetting")
    Call<ResponseWrapper<TaxEntity>> getTax(
    );

    @GET("getEvents")
    Call<ResponseWrapper<ArrayList<EntityCavalliNights>>> getCavalliEvents(
            @Query("event_type") String event_type
    );

    @GET("getEvents")
    Call<ResponseWrapper<ArrayList<EntityCavalliNights>>> getMusicEvents(
            @Query("event_type") String event_type
    );

    @FormUrlEncoded
    @POST("addInquiry")
    Call<ResponseWrapper> addInquiry(
            @Field("event_id") String event_id,
            @Field("message") String message
    );

    @GET("getMemberships")
    Call<ResponseWrapper<ArrayList<EntityMemberships>>> getMemberships(
    );

    @FormUrlEncoded
    @POST("purchaseMembership")
    Call<ResponseWrapper<PurchaseMembershipEntity>> purchaseMemberships(
            @Field("membership_id") int membership_id
    );

    @GET("getMyMemberships")
    Call<ResponseWrapper<EntityHistoryProfile>> membershipHistory(
    );

    @GET("getNotifications")
    Call<ResponseWrapper<ArrayList<NotificationEntity>>> getNotifications(
    );

    @GET("getMyOrders")
    Call<ResponseWrapper<EntityOrderHistory>> getOrdersHistoryPending(
    );

    @GET("getMyThreads")
    Call<ResponseWrapper<ArrayList<EntityMessages>>> getMessages(
    );

    @GET("getThreadMessages")
    Call<ResponseWrapper<ArrayList<EntityMessagesThread>>> getMessagesThread(
            @Query("thread_id") String thread_id
    );

    @FormUrlEncoded
    @POST("addMessage")
    Call<ResponseWrapper> sendMessage(
            @Field("receiver_id") String receiver_id,
            @Field("message") String message,
            @Field("request_id") String request_id
    );

    @GET("myMarkedEvent")
    Call<ResponseWrapper<ArrayList<EntityCavalliNights>>> calendarEvents(
            @Query("date") String date
    );

    @GET("markUnMarkEvent")
    Call<ResponseWrapper<EntityMarkUnmarkCalendar>> markUnmarkCalendar(
            @Query("event_id") String event_id
    );

    @GET("getLiveMusicInfo")
    Call<ResponseWrapper<EntityLiveMusic>> liveMusicData(
    );

    @GET("guestList")
    Call<ResponseWrapper<ArrayList<EntityGetTotalList>>> getGuestList(
            @Query("category_id") int category_id
    );

    @FormUrlEncoded
    @POST("createGuestList")
    Call<ResponseWrapper<EntityCreateGuestList>> createGuestList(
            @Field("title") String title,
            @Field("category_id") String category_id
    );

    @FormUrlEncoded
    @POST("editGuestListTitle")
    Call<ResponseWrapper> editListTitle(
            @Field("title") String title,
            @Field("guest_list_id") String guest_list_id
    );

    @FormUrlEncoded
    @POST("editGuestListMember")
    Call<ResponseWrapper<AddEditGuestListEntity>> editGuestList(
            @Field("name_title") String name_title,
            @Field("full_name") String full_name,
            @Field("email_address") String email_address,
            @Field("member_id") String member_id,
            @Field("mobile_no") String mobile_no,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("deleteGuestListMember")
    Call<ResponseWrapper> deleteGuestList(
            @Field("member_id") String member_id
    );

    @FormUrlEncoded
    @POST("addGuestListMember")
    Call<ResponseWrapper<AddEditGuestListEntity>> addGuestListMember(
            @Field("name_title") String name_title,
            @Field("full_name") String full_name,
            @Field("email_address") String email_address,
            @Field("mobile_no") String mobile_no,
            @Field("date") String date,
            @Field("category_id") String category_id
    );

    @GET("getGalleryCategories")
    Call<ResponseWrapper<ArrayList<EntityGalleryCategories>>> getGalleryCategories(
    );

    @GET("getGallery")
    Call<ResponseWrapper<EntityGallery>> gallery(
            @Query("category_id") String category_id
    );

    @GET("getMyEvents")
    Call<ResponseWrapper<EntityProfileEvents>> eventsProfile(
    );

    //guestList in profile
    @GET("getMyGuestList")
    Call<ResponseWrapper<EntityGuestListProfile>> guestListProfile(
    );

    @GET("cancelReservation")
    Call<ResponseWrapper> cancelReservation(
            @Query("reservation_id") String reservation_id);

    @FormUrlEncoded
    @POST("pushOnOff")
    Call<ResponseWrapper> pushNotification(
            @Field("notify_status") int notify_status
    );

    @GET("homeSearch")
    Call<ResponseWrapper<ArrayList<EntityHomeSearch>>> getHomeSearch(
            @Query("search_title") String search_title);

    @GET("homeSearchDetail")
    Call<ResponseWrapper<EntityCavalliNights>> getHomeSearchEventDetail(
            @Query("id") String id,
            @Query("type") String type);

    @GET("homeSearchDetail")
    Call<ResponseWrapper<ArrayList<EntityHomeSearch>>> getHomeSearchNewsDetail(
            @Query("id") String id,
            @Query("type") String type);

    @GET("homeSearchDetail")
    Call<ResponseWrapper<MenuCategoryEntity>> getHomeSearchProductDetail(
            @Query("id") String id,
            @Query("type") String type);

    @GET("getGuestMemberList")
    Call<ResponseWrapper<ArrayList<EntityGuestListMember>>> getOnlyGuestList(
            @Query("guest_list_id") String guest_list_id);

    @GET("checkMarkStatus")
    Call<ResponseWrapper<EntityPushCheck>> getMarkStatus(
            @Query("event_id") String event_id);

    @GET("guestListCategories")
    Call<ResponseWrapper<ArrayList<EntityCategoriesGuestList>>> getGuestListCategories(
    );

    @GET("getSocialLinks")
    Call<ResponseWrapper<ArrayList<TaxEntity>>> getCavalliSocialLinks(
    );

    @FormUrlEncoded
    @POST("logout")
    Call<ResponseWrapper> logoutToken(
            @Field("device_token") String device_token,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("deleteNotification")
    Call<ResponseWrapper> deleteNotification(
            @Field("notification_id") int notification_id
    );
}