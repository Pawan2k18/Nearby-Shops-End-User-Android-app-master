package org.nearbyshops.enduserappnew.ShopDetail;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import okhttp3.ResponseBody;
import org.nearbyshops.enduserappnew.API.FavouriteShopService;
import org.nearbyshops.enduserappnew.API.ShopImageService;
import org.nearbyshops.enduserappnew.API.ShopReviewService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Interfaces.NotifyReviewUpdate;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ModelEndPoints.FavouriteShopEndpoint;
import org.nearbyshops.enduserappnew.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.enduserappnew.ModelEndPoints.ShopReviewEndPoint;
import org.nearbyshops.enduserappnew.ModelImages.ShopImage;
import org.nearbyshops.enduserappnew.ModelReviewShop.FavouriteShop;
import org.nearbyshops.enduserappnew.ModelReviewShop.ShopReview;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Utility.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopImages.ShopImageList;
import org.nearbyshops.enduserappnew.ShopReview.ShopReviews;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;


public class ShopDetailFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, Target, RatingBar.OnRatingBarChangeListener, NotifyReviewUpdate {




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final String TAG_JSON_STRING = "shop_json_string";


//    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;


    @Inject
    ShopReviewService shopReviewService;

    @Inject
    FavouriteShopService favouriteShopService;

    @Inject
    ShopImageService shopImageService;



    @BindView(R.id.shop_profile_photo)
    ImageView shopProfilePhoto;
    @BindView(R.id.image_count)
    TextView imagesCount;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    @BindView(R.id.shop_name) TextView shopName;

    @BindView(R.id.shop_rating_numeric) TextView shopRatingNumeric;
    @BindView(R.id.shop_rating) RatingBar ratingBar;
    @BindView(R.id.rating_count) TextView ratingCount;

    @BindView(R.id.phone) TextView shopPhone;

    @BindView(R.id.shop_description) TextView shopDescription;
    @BindView(R.id.read_full_button) TextView readFullDescription;


    @BindView(R.id.shop_address) TextView shopAddress;
//    @BindView(R.id.get_directions) TextView getDirections;
//    @BindView(R.id.see_on_map) TextView seeOnMap;

    @BindView(R.id.phone_delivery) TextView phoneDelivery;

    @BindView(R.id.delivery_charge_text) TextView deliveryChargeText;
    @BindView(R.id.free_delivery_info) TextView freeDeliveryInfo;

    @BindView(R.id.shop_reviews)
    RecyclerView shopReviews;




    @BindView(R.id.user_rating_review)
    LinearLayout user_review_ratings_block;
    @BindView(R.id.edit_review_text) TextView edit_review_text;
    @BindView(R.id.ratingBar_rate) RatingBar ratingBar_rate;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.delivery_block) LinearLayout deliveryBlock;


    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;



    private Shop shop;



    private ShopReview reviewForUpdate;










    public ShopDetailFragment() {
        // Required empty public constructor


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }



//    public static MarketDetailFragment newInstance(String param1, String param2) {
//        MarketDetailFragment fragment = new MarketDetailFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }






//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_detail, container, false);
        ButterKnife.bind(this,rootView);


        ratingBar_rate.setOnRatingBarChangeListener(this);

//        setupSwipeContainer();


        String shopJson = getActivity().getIntent().getStringExtra(TAG_JSON_STRING);
        shop = UtilityFunctions.provideGson().fromJson(shopJson, Shop.class);



        toolbar.setTitle(shop.getShopName());


//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//
//        if(actionBar!=null)
//        {
//            actionBar.setTitle(shop.getShopName());
//        }




        bindViews();


        getShopImageCount();




        if (shop != null) {
            checkUserReview();
        }




        checkFavourite();



        return rootView;
    }







    @OnClick(R.id.see_reviews)
    void seeAllReviews()
    {

        Intent intent = new Intent(getActivity(), ShopReviews.class);
//        intent.putExtra(ShopReviews.SHOP_INTENT_KEY, shop);

        String shopJson = UtilityFunctions.provideGson().toJson(shop);
        intent.putExtra(ShopReviews.SHOP_INTENT_KEY,shopJson);

        startActivity(intent);
    }




//    void setupSwipeContainer()
//    {
//        if(swipeContainer!=null) {
//
//            swipeContainer.setOnRefreshListener(this);
//            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                    android.R.color.holo_green_light,
//                    android.R.color.holo_orange_light,
//                    android.R.color.holo_red_light);
//        }
//
//    }


    @Override
    public void onRefresh() {
//        swipeContainer.setRefreshing(false);
    }







    private void bindViews()
    {
        shopName.setText(shop.getShopName());
        shopRatingNumeric.setText(String.format("%.2f",shop.getRt_rating_avg()));
        ratingBar.setRating(shop.getRt_rating_avg());
        ratingCount.setText("(" + shop.getRt_rating_count() + " ratings )");

        shopPhone.setText(shop.getCustomerHelplineNumber());
        shopDescription.setText(shop.getLongDescription());





        String shop_address = shop.getShopAddress()  + ", " + shop.getCity() + " - " + shop.getPincode() + "\n" + shop.getLandmark();


        shopAddress.setText(shop_address);
        phoneDelivery.setText(shop.getDeliveryHelplineNumber());


        deliveryChargeText.setText("Delivery Charge : " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + shop.getDeliveryCharges() + " Per Delivery");
        freeDeliveryInfo.setText("Free Delivery for orders above " + PrefGeneral.getCurrencySymbol(getActivity()) + " " + String.valueOf(shop.getBillAmountForFreeDelivery()));



        String imagePath = PrefGeneral.getServiceURL(getActivity()) + "/api/v1/Shop/Image/five_hundred_"
                + shop.getLogoImagePath() + ".jpg";

//            if (!shop.getBookCoverImageURL().equals("")) {

        Drawable placeholder = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());

        Picasso.get().load(imagePath)
                .placeholder(placeholder)
                .into(shopProfilePhoto);


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(this);






        if(shop.getPickFromShopAvailable())
        {
            pickFromShopIndicator.setVisibility(View.VISIBLE);
        }
        else
        {
            pickFromShopIndicator.setVisibility(View.GONE);
        }





        if(shop.getHomeDeliveryAvailable())
        {
            homeDeliveryIndicator.setVisibility(View.VISIBLE);
            deliveryBlock.setVisibility(View.VISIBLE);
        }
        else
        {
            homeDeliveryIndicator.setVisibility(View.GONE);
            deliveryBlock.setVisibility(View.GONE);
        }




    }








    private void getShopImageCount()
    {
        Call<ShopImageEndPoint> call = shopImageService.getShopImages(
                shop.getShopID(), ShopImage.IMAGE_ORDER,
                null,null,
                true,true
        );


        call.enqueue(new Callback<ShopImageEndPoint>() {
            @Override
            public void onResponse(Call<ShopImageEndPoint> call, Response<ShopImageEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!=null)
                {
                    int count = response.body().getItemCount();


                    if(count==0)
                    {
                        imagesCount.setVisibility(View.GONE);
                    }
                    else
                    {
                        imagesCount.setText(String.valueOf(count)  + " Photos");
                    }

                }
            }

            @Override
            public void onFailure(Call<ShopImageEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Loading Images Failed !");
            }
        });


    }




    private boolean isDestroyed = false;



    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }





    void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        Palette palette = Palette.from(bitmap).generate();

        int color = getResources().getColor(R.color.colorPrimaryDark);
        int colorLight = getResources().getColor(R.color.colorPrimary);
        int vibrant = palette.getVibrantColor(color);
        int vibrantLight = palette.getLightVibrantColor(color);
        int vibrantDark = palette.getDarkVibrantColor(colorLight);
        int muted = palette.getMutedColor(color);
        int mutedLight = palette.getLightMutedColor(color);
        int mutedDark = palette.getDarkMutedColor(color);

        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        //if(vibrantSwatch!=null) {
        //  originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
        //}


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(vibrantDark);

        }




        shopName.setTextColor(vibrant);


        if (fab != null && vibrantDark != 0) {

            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));

        }//fab.setBackgroundColor(vibrantDark);

        //originalTitle.setBackgroundColor(vibrantDark);


        if (collapsingToolbarLayout != null) {

            collapsingToolbarLayout.setContentScrimColor(vibrant);

        }
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }


    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }








    @OnClick(R.id.shop_profile_photo)
    void profileImageClick() {
        Intent intent = new Intent(getActivity(), ShopImageList.class);
        intent.putExtra("shop_id", shop.getShopID());
        startActivity(intent);
    }





    @OnClick(R.id.read_full_button)
    void readFullButtonClick() {
/*
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW,R.id.author_name);
        layoutParams.setMargins(0,10,0,0);
        bookDescription.setLayoutParams(layoutParams);
*/

        shopDescription.setMaxLines(Integer.MAX_VALUE);
        readFullDescription.setVisibility(View.GONE);
    }







    @OnClick(R.id.get_directions)
    void getDirectionsPickup()
    {
        getDirections(shop.getLatCenter(),shop.getLonCenter());
    }






    @OnClick(R.id.see_on_map)
    void seeOnMapDestination()
    {
        seeOnMap(shop.getLatCenter(), shop.getLonCenter(), shop.getShopAddress());
    }




    void getDirections(double lat,double lon)
    {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(lat) + "," + String.valueOf(lon));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }



    void seeOnMap(double lat,double lon,String label)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + String.valueOf(lat) + "," + String.valueOf(lon) + "(" + label + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }









    @OnClick({R.id.phone_icon, R.id.phone})
    void phoneClick()
    {
        dialPhoneNumber(shop.getCustomerHelplineNumber());
    }




    @OnClick({R.id.phone_icon_delivery, R.id.phone_delivery})
    void phoneDeliveryClick()
    {
        dialPhoneNumber(shop.getDeliveryHelplineNumber());
    }




    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }








    boolean isFavourite = false;



    @OnClick(R.id.fab)
    void fabClick() {

        if (PrefLogin.getUser(getActivity()) == null) {

//            showToastMessage("Please Login to use this Feature !");
            showLoginDialog();


        }
        else
        {
            if(isFavourite)
            {
                deleteFavourite();
            }
            else
            {
                insertFavourite();
            }
//            toggleFavourite();
        }
    }







//    void toggleFavourite() {
//
//        if (shop != null && PrefLogin.getUser(getActivity()) != null) {
//
//            Call<FavouriteShopEndpoint> call = favouriteShopService.getFavouriteShops(shop.getShopID(), PrefLogin.getUser(getActivity()).getUserID()
//                    , null, null, null, null);
//
//
//            call.enqueue(new Callback<FavouriteShopEndpoint>() {
//                @Override
//                public void onResponse(Call<FavouriteShopEndpoint> call, Response<FavouriteShopEndpoint> response) {
//
//
//                    if (response.body() != null) {
//                        if (response.body().getItemCount() >= 1) {
//                            deleteFavourite();
//
//                        } else if (response.body().getItemCount() == 0) {
//                            insertFavourite();
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<FavouriteShopEndpoint> call, Throwable t) {
//
//                    showToastMessage("Network Request failed. Check Network Connection !");
//                }
//            });
//        }
//    }






    void setFavouriteIcon(boolean isFavourite) {

        if (fab == null) {
            return;
        }


        this.isFavourite = isFavourite;


        if (isFavourite) {


//            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_favorite_white_24px, getActivity().getTheme());
//            fab.setImageDrawable(drawable);


            fab.setImageResource(R.drawable.ic_favorite_white_24px);

        } else {
//            Drawable drawable2 = VectorDrawableCompat.create(getResources(), R.drawable.ic_favorite_border_white_24px, getActivity().getTheme());
//            fab.setImageDrawable(drawable2);


            fab.setImageResource(R.drawable.ic_favorite_border_white_24px);
        }
    }





    void checkFavourite() {

        // make a network call to check the favourite

        if (shop != null && PrefLogin.getUser(getActivity()) != null) {

            Call<FavouriteShopEndpoint> call = favouriteShopService.getFavouriteShops(shop.getShopID(), PrefLogin.getUser(getActivity()).getUserID()
                    , null, null, null, null);


            call.enqueue(new Callback<FavouriteShopEndpoint>() {
                @Override
                public void onResponse(Call<FavouriteShopEndpoint> call, Response<FavouriteShopEndpoint> response) {


                    if (response.body() != null) {
                        if (response.body().getItemCount() >= 1) {

                            setFavouriteIcon(true);
//                            isFavourite = true;

                        } else if (response.body().getItemCount() == 0) {

                            setFavouriteIcon(false);
//                            isFavourite = false;
                        }
                    }

                }

                @Override
                public void onFailure(Call<FavouriteShopEndpoint> call, Throwable t) {

                    showToastMessage("Network Request failed. Check Network Connection !");
                }
            });

        }
    }





    void insertFavourite() {


        if (shop != null && PrefLogin.getUser(getActivity()) != null) {

            FavouriteShop favouriteBook = new FavouriteShop();
            favouriteBook.setShopID(shop.getShopID());
            favouriteBook.setEndUserID(PrefLogin.getUser(getActivity()).getUserID());

            Call<FavouriteShop> call = favouriteShopService.insertFavouriteShop(favouriteBook);

            call.enqueue(new Callback<FavouriteShop>() {
                @Override
                public void onResponse(Call<FavouriteShop> call, Response<FavouriteShop> response) {

                    if (response.code() == 201) {
                        // created successfully

                        setFavouriteIcon(true);
//                        isFavourite = true;
                    }
                }

                @Override
                public void onFailure(Call<FavouriteShop> call, Throwable t) {

                    showToastMessage("Network Request failed !");

                }
            });
        }


    }





    void deleteFavourite() {

        if (shop != null && PrefLogin.getUser(getActivity()) != null) {
            Call<ResponseBody> call = favouriteShopService.deleteFavouriteShop(shop.getShopID(),
                    PrefLogin.getUser(getActivity()).getUserID());


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        setFavouriteIcon(false);
//                        isFavourite = false;
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Network Request Failed !");
                }
            });
        }
    }






    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        write_review_click();
    }




    @OnClick({R.id.edit_icon, R.id.edit_review_label})
    void edit_review_Click() {

        if (reviewForUpdate != null) {
            FragmentManager fm = getChildFragmentManager();
            RateReviewDialog dialog = new RateReviewDialog();
            dialog.show(fm, "rate");
            dialog.setMode(reviewForUpdate, true, reviewForUpdate.getShopID());
        }

    }







    @OnClick({R.id.edit_review_text, R.id.ratingBar_rate})
    void write_review_click() {

        if(PrefLogin.getUser(getActivity())==null)
        {
            showToastMessage("Login to rate and review !");
            showLoginDialog();
            return;
        }


        FragmentManager fm = getChildFragmentManager();
        RateReviewDialog dialog = new RateReviewDialog();
        dialog.show(fm, "rate");

        if (shop != null) {
            dialog.setMode(null, false, shop.getShopID());
        }
    }




    @Override
    public void notifyReviewUpdated() {

        checkUserReview();
    }




    @Override
    public void notifyReviewDeleted() {

        shop.setRt_rating_count(shop.getRt_rating_count() - 1);
        checkUserReview();
    }





    @Override
    public void notifyReviewSubmitted() {
        shop.setRt_rating_count(shop.getRt_rating_count() + 1);
        checkUserReview();
    }






    @BindView(R.id.edit_review_block)
    RelativeLayout edit_review_block;

    @BindView(R.id.review_title)
    TextView review_title;

    @BindView(R.id.review_description)
    TextView review_description;

    @BindView(R.id.review_date)
    TextView review_date;


    @BindView(R.id.member_profile_image)
    ImageView member_profile_image;

    @BindView(R.id.member_name)
    TextView member_name;

    @BindView(R.id.member_rating)
    RatingBar member_rating_indicator;


//    ShopReview reviewForUpdate;





    // method to check whether the user has written the review or not if the user is currently logged in.
    private void checkUserReview() {



        if (PrefLogin.getUser(getActivity()) == null) {

            user_review_ratings_block.setVisibility(View.VISIBLE);


        } else
            {

            // Unhide review dialog







            if (shop.getRt_rating_count() == 0) {

                user_review_ratings_block.setVisibility(View.VISIBLE);
                edit_review_block.setVisibility(View.GONE);

                edit_review_text.setText(R.string.shop_review_be_the_first_to_review);
            } else if (shop.getRt_rating_count() > 0) {


                Call<ShopReviewEndPoint> call = shopReviewService.getReviews(shop.getShopID(),
                        PrefLogin.getUser(getActivity()).getUserID(), true, "REVIEW_DATE", null, null, null);

//                Log.d("review_check",String.valueOf(UtilityGeneral.getUserID(this)) + " : " + String.valueOf(shop.getBookID()));

                call.enqueue(new Callback<ShopReviewEndPoint>() {
                    @Override
                    public void onResponse(Call<ShopReviewEndPoint> call, Response<ShopReviewEndPoint> response) {


                        if (response.body() != null) {
                            if (response.body().getItemCount() > 0) {

//                                edit_review_text.setText("Edit your review and Rating !");


                                if (edit_review_block == null) {
                                    // If the views are not bound then return. This can happen in delayed response. When this call is executed
                                    // after the activity have gone out of scope.
                                    return;
                                }




                                edit_review_block.setVisibility(View.VISIBLE);
                                user_review_ratings_block.setVisibility(View.GONE);

                                reviewForUpdate = response.body().getResults().get(0);

                                review_title.setText(response.body().getResults().get(0).getReviewTitle());
                                review_description.setText(response.body().getResults().get(0).getReviewText());

                                review_date.setText(response.body().getResults().get(0).getReviewDate().toLocaleString());

                                member_rating_indicator.setRating(response.body().getResults().get(0).getRating());


//                                user_review.setText(response.body().getResults().get(0).getReviewText());
//                                ratingBar_rate.setRating(response.body().getResults().get(0).getRating());

                                User member = response.body().getResults().get(0).getRt_end_user_profile();
                                member_name.setText(member.getName());

//                                String imagePath = PrefGeneral.getImageEndpointURL(getActivity())
//                                        + member.getProfileImagePath();

                                String imagepath = PrefGeneral.getServiceURL(getContext()) + "/api/v1/User/Image/five_hundred_"
                                        + member.getProfileImagePath() + ".jpg";


                                Drawable placeholder = VectorDrawableCompat
                                        .create(getResources(),
                                                R.drawable.ic_nature_people_white_48px,null);


                                Picasso.get()
                                        .load(imagepath)
                                        .placeholder(placeholder)
                                        .into(member_profile_image);


                            } else if (response.body().getItemCount() == 0) {
                                edit_review_text.setText("Rate this shop !");
                                edit_review_block.setVisibility(View.GONE);
                                user_review_ratings_block.setVisibility(View.VISIBLE);

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ShopReviewEndPoint> call, Throwable t) {


//                        showToastMessage("Network Request Failed. Check your internet connection !");

                    }
                });


            }

            // check shop ratings count
            // If ratings count is 0 then set message : Be the first to review


            // If ratings count is >0 then
            // check if user has written the review or not
            // if Yes
            // Write messsage : Edit your review and rating
            // If NO
            // Write message : Rate and Review this shop

        }

    }






    private void showLoginDialog() {

//        Intent intent = new Intent(getActivity(), Login.class);
//        startActivity(intent);


        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,123);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123 && resultCode == RESULT_OK)
        {
            // login success
            checkFavourite();
            checkUserReview();
        }
    }







}
