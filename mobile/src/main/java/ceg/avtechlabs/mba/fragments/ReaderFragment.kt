package ceg.avtechlabs.mba.fragments

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.app.Fragment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.ui.ReaderActivity
import ceg.avtechlabs.mba.util.Globals
import cn.pedant.SweetAlert.SweetAlertDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_reader.*
import javax.microedition.khronos.opengles.GL

/**
 * Created by Adhithyan V on 17-12-2017.
 */

class ReaderFragment(): Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //showProgressDialog()
        val view = inflater!!.inflate(R.layout.fragment_reader, container, false)

        val title = view!!.findViewById(R.id.textviewTitle) as TextView
        val description = view!!.findViewById(R.id.textviewDescription) as TextView
        val thumbnail = view!!.findViewById(R.id.image) as ImageView

        if(arguments == null) {
            return view
        } else {
            Log.d("hello", "bundle else")
            Log.d("hello", arguments.getString(Globals.SAVE_TITLE))
        }


            title.text = arguments.getString(Globals.SAVE_TITLE)
            description.text = arguments.getString(Globals.SAVE_CONTENT)


        val imageUrl  = arguments.getString(Globals.SAVE_IMAGE_URL)

        if(imageUrl != null) {
            description.tag = imageUrl
        } else {
            description.tag = ""
        }
        loadImage(activity, thumbnail, imageUrl)
        return view
    }

    fun loadImage(context: Context, image: ImageView, imageUrl: String?) {

        if(imageUrl == null) {
            return;
        } else if(imageUrl.equals("")){
            return;
        }

        Picasso.with(context).load(imageUrl).into(object: com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {

            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image.setImageBitmap(bitmap)
                Palette.from(bitmap).generate(object: Palette.PaletteAsyncListener {
                    override fun onGenerated(palette: Palette) {
                        val mutedColor = palette.getMutedColor(ContextCompat.getColor(context.applicationContext, R.color.colorPrimary));
                        val mutedDarkColor = palette.getDarkMutedColor(ContextCompat.getColor(context.applicationContext, R.color.colorPrimaryDark));

                        collapsing_toolbar.setContentScrimColor(mutedColor);
                        collapsing_toolbar.setStatusBarScrimColor(mutedDarkColor);

                        @TargetApi(21)
                        activity.window.statusBarColor = palette.getDarkVibrantColor(resources.getColor(R.color.colorPrimaryDark))
                    }
                });
            }
        })
    }

    /*override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString(Globals.SAVE_TITLE, textviewTitle.text.toString())
        outState!!.putString(Globals.SAVE_CONTENT, textviewDescription.text.toString())
        var url = textviewDescription.tag
        var image: String? = null
        if(url != null) {
            image = url.toString()
        }
        outState!!.putString(Globals.SAVE_IMAGE_URL, image)
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        textviewTitle.text = savedInstanceState!!.getString(Globals.SAVE_TITLE)
        textviewDescription.text = savedInstanceState!!.getString(Globals.SAVE_IMAGE_URL)
        loadImage(savedInstanceState!!.getString(Globals.SAVE_IMAGE_URL))
        hideProgressDialog()

        super.onRestoreInstanceState(savedInstanceState)
    }*/



}