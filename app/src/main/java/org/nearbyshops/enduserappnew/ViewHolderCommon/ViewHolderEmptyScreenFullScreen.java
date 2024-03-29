package org.nearbyshops.enduserappnew.ViewHolderCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.R;


public class ViewHolderEmptyScreenFullScreen extends RecyclerView.ViewHolder{




    @BindView(R.id.graphic_image) ImageView graphicImage;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.message) TextView message;




    private Context context;

    private NotifyDeliveryAddress notifyDeliveryAddress;


    private DeliveryAddress item;




    public static ViewHolderEmptyScreenFullScreen create(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_empty_screen_full_screen,parent,false);

        return new ViewHolderEmptyScreenFullScreen(view,context,null);
    }





    public ViewHolderEmptyScreenFullScreen(View itemView, Context context, NotifyDeliveryAddress fragment) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
    }








    public interface NotifyDeliveryAddress{

        void notifyEdit(DeliveryAddress deliveryAddress);
        void notifyRemove(DeliveryAddress deliveryAddress, int position);
        void notifyListItemClick(DeliveryAddress deliveryAddress);
        void selectButtonClick(DeliveryAddress deliveryAddress, int position);
    }



    public void setItem(DeliveryAddress address)
    {


    }




}

