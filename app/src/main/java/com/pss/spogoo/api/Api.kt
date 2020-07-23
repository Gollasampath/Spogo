package com.pss.spogoo.api

import com.pss.spogoo.models.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    //signup
    @FormUrlEncoded
    @POST("normal_user_signup")
    fun usersignup(
        @Field("login_type") logintype: String,
        @Field("full_name") full_name: String,
        @Field("email_id") email_id: String,
        @Field("mobile_number") mobile_number: String
    ): Call<UserSignupListResponse>


    //gmail signup
    @FormUrlEncoded
    @POST("normal_user_signup")
    fun usergmailsignup(
        @Field("login_type") logintype: String,
        @Field("full_name") full_name: String,
        @Field("email_id") email_id: String,
        @Field("mobile_number") mobile_number: String
    ): Call<UserSignupListResponse>

    //facebook signup
    @FormUrlEncoded
    @POST("normal_user_signup")
    fun userfbsignup(
        @Field("login_type") logintype: String,
        @Field("full_name") full_name: String,
        @Field("email_id") email_id: String,
        @Field("mobile_number") mobile_number: String,
        @Field("oauth_uid") oauth_uid: String,
        @Field("gender") gender: String
    ): Call<UserSignupListResponse>

//pofileview


    //Login
    @FormUrlEncoded
    @POST("get_user_profile_list")
    fun getuserlogin(
        @Field("login_type") logintype: String,
        @Field("user_id") user_id: String
    ): Call<UserLoginListResponse>

    //update Login
    @FormUrlEncoded
    @POST("update_profile")
    fun getuserupdatelogin(
        @Field("login_type") logintype: String,
        @Field("user_id") user_id: String,
        @Field("full_name") full_name: String,
        @Field("email_id") email_id: String,
        @Field("mobile_number") mobile_number: String,
        @Field("age") age: String
    ): Call<UserLoginListResponse>


    //update Login
    @FormUrlEncoded
    @POST("update_profile")
    fun getuserupdateimgelogin(
        @Field("login_type") logintype: String,
        @Field("user_id") user_id: String,
        @Field("profile_img") profile_img: String
    ): Call<UserLoginListResponse>

    //Login
    @FormUrlEncoded
    @POST("check_login_data_availability")
    fun userlogin(
        @Field("type") logintype: String,
        @Field("mobile_number") mobile_number: String
    ): Call<UserLoginListResponse>


    //Community List
    @GET("communities_list")
    fun getCommunityList(
    ): Call<CommunityListResponse>


    //get all user category List
    @GET("normal_user_categories_list")
    fun getuserCategoriesList(
    ): Call<ShopListResponse>


    //get all user category List
    @GET("recent_products_list")
    fun getuserRecentProdctsList(
    ): Call<RecentProductsListResponse>

    //get all user category List
    @FormUrlEncoded
    @POST("indetailed_products_list")
    fun getuserRecentinDetailsProdcts(
        @Field("products_id") products_id: String

    ): Call<RecentInDetailsProductsListResponse>

    //get popular sports data
    @GET("user_popular_sports")
    fun getPopularsportsList(
    ): Call<PopularSportsListResponse>

    //get appareals data
    @GET("apparels_categories_list")
    fun getApparealsList(
    ): Call<ApparealsListCategoryResponse>


    //get appareals product list data
    @FormUrlEncoded
    @POST("apparels_products_list")
    fun getApparealsProductList(
        @Field("apparel_cat_id") apparel_cat_id: String
    ): Call<ApparelsProductsListResponse>

    //get appareals product list data
    @FormUrlEncoded
    @POST("apparels_related_products_list")
    fun getRelatedApparealsProductList(
        @Field("apparel_cat_id") apparel_cat_id: String
    ): Call<ApparelsProductsListResponse>

    //get appareals product list data
    @FormUrlEncoded
    @POST("apparels_indetailed_products_list")
    fun getApparealsInDetailsProductList(
        @Field("apparels_id") apparels_id: String
    ): Call<ApparealsDetailsListResponse>

    //get accesories data
    @FormUrlEncoded
    @POST("accessories_products_list")
    fun getAccessoriesList(
        @Field("acces_categories_id") acces_categories_id: String

    ): Call<AccessoriesProductListResponse>

    //get accesories data
    @FormUrlEncoded
    @POST("accessories_related_products_list")
    fun getRelatedAccessoriesList(
        @Field("acces_categories_id") acces_categories_id: String

    ): Call<AccessoriesProductListResponse>

    //get accesories indetails data
    @FormUrlEncoded
    @POST("accessories_indetailed_products_list")
    fun getAccessoriesinDetailsList(
        @Field("accessories_id") accessories_id: String
    ): Call<AccessoriesinDetailsListResponse>


    //get electronics data
    @GET("electronics_categories_list")
    fun getElectronicsList(
    ): Call<ElectronicsCategoriesListResponse>


    //get appareals product list data
    @FormUrlEncoded
    @POST("electronics_products_list")
    fun getElectronicsProductsList(
        @Field("elec_categories_id") elec_categories_id: String
    ): Call<ElectronicsProductListResponse>

    //get appareals product list data
    @FormUrlEncoded
    @POST("electronics_related_products_list")
    fun getrelatedElectronicsProductsList(
        @Field("elec_categories_id") elec_categories_id: String
    ): Call<ElectronicsProductListResponse>


    //get appareals product list data
    @FormUrlEncoded
    @POST("electronics_indetailed_products_list")
    fun getElectronicsinDetails(
        @Field("electronics_id") electronics_id: String
    ): Call<ElectronicsDetailsListResponse>


    @FormUrlEncoded
    //get healthproducts data
    @POST("health_products_list")
    fun getHealthProductsList(
        @Field("health_pro_cat_id") health_pro_cat_id: String
    ): Call<HealthProductsListResponse>

    @FormUrlEncoded
    //get healthproducts data
    @POST("health_related_products_list")
    fun getRelatedHealthProductsList(
        @Field("health_pro_cat_id") health_pro_cat_id: String
    ): Call<HealthProductsListResponse>

    @FormUrlEncoded
    //get healthproducts data
    @POST("health_indetailed_products_list")
    fun getHealthProductsinDetails(
        @Field("health_products_id") health_products_id: String
    ): Call<HealthProductDetailsListResponse>

    @FormUrlEncoded
    //get all usersub category List
    @POST("sub_category_list")
    fun getusersubproductList(
        @Field("categories_id") categories_id: String

    ): Call<SubProductListResponse>

    @FormUrlEncoded
    //get all user category List
    @POST("sub_category_products_list")
    fun getSubCategoryList(
        @Field("sub_categories_id") sub_categories_id: String

    ): Call<SubCategoryListResponse>

    @FormUrlEncoded
    //get all user category List
    @POST("related_products_list")
    fun getRelatedProductList(
        @Field("sub_categories_id") sub_categories_id: String

    ): Call<RelatedproductListResponse>


    //search data
    @GET("search_products_list")
    fun getSearchlist(
    ): Call<SearchListResponse>


    //search list data
    @FormUrlEncoded
    @POST("word_related_search_products_list")
    fun getwordSearchlist(
        @Field("search_word") search_word: String

    ): Call<SearchListResponse>

    //search list data
    @FormUrlEncoded
    @POST("search_related_products_list")
    fun getwordSearchrelatedlist(
        @Field("categories_id") categories_id: String

    ): Call<SearchRelatedListResponse>


    @GET("repair_categories_list")
    fun getrepairscategory(

    ): Call<RepairsCategoryListResponse>

    @FormUrlEncoded
    @POST("repair_sub_category_list")
    fun getrepairssucategories(
        @Field("repair_categories_id") repair_categories_id: String

    ): Call<RepairsSubCategoryListResponse>

    @GET("repairs_recent_products_list")
    fun getrepairsRecentProducts(

    ): Call<RepairsRecentListResponse>


    @FormUrlEncoded
    @POST("repairs_grip_products_list")
    fun getgripproductslist(
        @Field("repair_categories_id") repair_categories_id: String

    ): Call<AddGripProdcutsListResponse>

    @FormUrlEncoded
    @POST("repairs_grip_indetailed_products_list")
    fun getgripproductsdetailslist(
        @Field("repair_grip_id") repair_grip_id: String

    ): Call<AddGripProdcutsDetailsListResponse>


    @FormUrlEncoded
    @POST("repairs_indetailed_products_list")
    fun getrepairsRecentindetailsProducts(
        @Field("repairs_id") repairs_id: String

    ): Call<RepairsRecentIndetailsListResponse>


    @FormUrlEncoded
    @POST("repairs_products_list")
    fun getrepairsproductsList(
        @Field("repair_sub_categories_id") repair_sub_categories_id: String

    ): Call<RepairsProductsListResponse>


    //sort list
    @FormUrlEncoded
    @POST("apparels_products_list")
    fun sortapparelslist(
        @Field("apparel_cat_id") apparel_cat_id: String,
        @Field("color") color: String,
        @Field("order_by") order_by: String

    ): Call<ApparelsProductsListResponse>


    //get appareals product list data
    @FormUrlEncoded
    @POST("electronics_products_list")
    fun getsortElectronicsList(
        @Field("elec_categories_id") elec_categories_id: String,
        @Field("color") color: String,
        @Field("order_by") order_by: String
    ): Call<ElectronicsProductListResponse>

    @FormUrlEncoded
    @POST("repairs_products_list")
    fun getsortrepairsproductsList(
        @Field("repair_sub_categories_id") repair_sub_categories_id: String,
        @Field("color") color: String,
        @Field("order_by") order_by: String
    ): Call<RepairsProductsListResponse>

    @FormUrlEncoded
    //get all user category List
    @POST("sub_category_products_list")
    fun getsortSubCategoryList(
        @Field("sub_categories_id") sub_categories_id: String,
        @Field("color") color: String,
        @Field("order_by") order_by: String
    ): Call<SubCategoryListResponse>


    //get all user category List
    @GET("fitness_excercises_list")
    fun getfitnessList(

    ): Call<FitnesExcerseListResponse>

    @FormUrlEncoded
    //get all user category List
    @POST("indetailed_fit_exce_products_list")
    fun getfitnessindetailsList(
        @Field("fit_exce_id") fit_exce_id: String

    ): Call<FitnessDetailsListResponse>

    @FormUrlEncoded
    //get all user category List
    @POST("brands_related_products_list")
    fun getproductsList(
        @Field("brands_id") brands_id: String

    ): Call<BrandsProductsListResponse>

    @FormUrlEncoded
    //get all user category List
    @POST("indetailed_products_list")
    fun getproductsdetailsList(
        @Field("products_id") products_id: String

    ): Call<BrandsDetailsListResponse>


    //get all user category List
    @GET("get_brands_list")
    fun getbrands_List(
    ): Call<BrandsListResponse>


}