package com.example.userloginsqlite;

import android.net.Uri;

public class Attire_DateView {

        private String date;
        private Uri imageUri;

        public Attire_DateView(String date, Uri imageUri) {
            this.date = date;
            this.imageUri = imageUri;
        }

        public String getDate() {
            return date;
        }

        public Uri getImageUri() {
            return imageUri;
        }

}
