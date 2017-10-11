package com.firozanwar.sample.restaurantapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by firozanwar on 10/10/17.
 */

public class DataItem implements Parcelable {

    /**
     * itemName : Apple Pie
     * category : Desserts
     * description : Made with local granny smith apples to bring you the freshest classic apple pie available.
     * sort : 4
     * price : 5
     * image : apple_pie.jpg
     */

    private String itemName;
    private String category;
    private String description;
    private String sort;
    private String price;
    private String image;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.category);
        dest.writeString(this.description);
        dest.writeString(this.sort);
        dest.writeString(this.price);
        dest.writeString(this.image);
    }

    public DataItem() {
    }

    protected DataItem(Parcel in) {
        this.itemName = in.readString();
        this.category = in.readString();
        this.description = in.readString();
        this.sort = in.readString();
        this.price = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<DataItem> CREATOR = new Parcelable.Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel source) {
            return new DataItem(source);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}
