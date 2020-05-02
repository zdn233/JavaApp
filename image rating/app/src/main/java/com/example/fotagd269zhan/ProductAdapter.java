package com.example.fotagd269zhan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import java.util.ArrayList;




public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> myImagesURL = new ArrayList<String>();
    public ArrayList<Float> myRatings = new ArrayList<>();

    private Context myContext;


    public ProductAdapter(ArrayList<String> myImages, ArrayList<Float> myratings, Context myContext) {
        this.myContext = myContext;
        this.myImagesURL = myImages;
        this.myRatings = myratings;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Apply the rating from previous user rating it there is any
        String theName = myImagesURL.get(position);
        if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/bunny.jpg")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(0));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/chinchilla.jpg")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(1));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/doggo.jpg")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(2));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/fox.jpg")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(3));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/hamster.jpg")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(4));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/husky.jpg")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(5));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/kitten.png")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(6));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/loris.jpg")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(7));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/puppy.jpg")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(8));
        } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/sleepy.png")) {
            ((ProductViewHolder) holder).rtbar.setRating(myRatings.get(9));
        }
        //load image from URL
        new downloadImageTask(((ProductViewHolder) holder).image, ((ProductViewHolder) holder).bitmap).execute(myImagesURL.get(position));
    }



    @Override
    public int getItemCount() {
        return myImagesURL.size();
    }


    // used for showing the rating bar normally
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        Bitmap bitmap;
        RatingBar rtbar;
        LinearLayout parentlayout;

        public  ProductViewHolder(View itemView) {
            super(itemView);
            bitmap = null;
            image = itemView.findViewById(R.id.Myimage);
            rtbar = itemView.findViewById(R.id.ratingBar);
            parentlayout = itemView.findViewById(R.id.parent_layout);
            rtbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    String theName = myImagesURL.get(getAdapterPosition());
                    if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/bunny.jpg")) {
                        myRatings.set(0, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/chinchilla.jpg")) {
                        myRatings.set(1, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/doggo.jpg")) {
                        myRatings.set(2, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/fox.jpg")) {
                        myRatings.set(3, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/hamster.jpg")) {
                        myRatings.set(4, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/husky.jpg")) {
                        myRatings.set(5, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/kitten.png")) {
                        myRatings.set(6, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/loris.jpg")) {
                        myRatings.set(7, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/puppy.jpg")) {
                        myRatings.set(8, rtbar.getRating());
                    } else if(theName.equals("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/sleepy.png")) {
                        myRatings.set(9, rtbar.getRating());
                    }
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(myContext, PopupActivity.class);
                    intent.putExtra("myImageURL", myImagesURL.get(getAdapterPosition()));
                    myContext.startActivity(intent);
                }
            });
        }
    }


}




