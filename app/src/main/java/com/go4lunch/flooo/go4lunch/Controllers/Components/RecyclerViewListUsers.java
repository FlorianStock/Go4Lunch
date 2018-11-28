package com.go4lunch.flooo.go4lunch.Controllers.Components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.go4lunch.flooo.go4lunch.Controllers.Activities.RestaurantProfileActivity;
import com.go4lunch.flooo.go4lunch.Models.User;
import com.go4lunch.flooo.go4lunch.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewListUsers  extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.textUserDescription)
    TextView textUserDescription;
    @BindView(R.id.imageIconUser)
    ImageView imageUser;

    private Context context;
    private String placeId;
    private Boolean joiningList;

    public RecyclerViewListUsers(View itemView,Context context,Boolean joining)
    {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.context = context;
        this.joiningList = joining;

        itemView.setOnClickListener(this);
    }

    public void updateList(User model)
    {
        String txt;

        if(!joiningList ) {
            if (model.gethaveChosenRestaurant().equals("")) {
                txt = model.getUsername() + " " + context.getResources().getString(R.string.no_choice_restaurant);
                textUserDescription.setText(txt);
            } else {
                txt = model.getUsername() + " " + context.getResources().getString(R.string.choice_restaurant) + " " + model.gethaveChosenRestaurant();
                textUserDescription.setText(txt);
            }
        }
        else{
            txt = model.getUsername()+" "+context.getResources().getString(R.string.joining_restaurant);
            textUserDescription.setText(txt);
        }
        //Picasso.with(context).load(model.getUrlPicture()).resize(50, 50).centerCrop().into(mImageRestaurant);
        Picasso.with(context).load(model.getUrlPicture())
                .transform(new CircleTransform())
                .into((imageUser));

        placeId = model.getPlaceID();

    }

    @Override
    public void onClick(View view)
    {
        if(placeId.equalsIgnoreCase("") || joiningList)
        {

        }
        else
        {
            Intent intent = new Intent(context, RestaurantProfileActivity.class);
            intent.putExtra("ID",placeId);
            context.startActivity(intent);
        }

    }

    private class CircleTransform implements Transformation
    {
        @Override
        public Bitmap transform(Bitmap source)
        {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
